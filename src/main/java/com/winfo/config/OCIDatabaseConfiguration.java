package com.winfo.config;

import java.io.IOException;
import java.util.Optional;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.identity.Identity;
import com.oracle.bmc.identity.IdentityClient;
import com.oracle.bmc.identity.model.Compartment;
import com.oracle.bmc.identity.requests.ListCompartmentsRequest;
import com.oracle.bmc.identity.responses.ListCompartmentsResponse;
import com.oracle.bmc.keymanagement.KmsVaultClient;
import com.oracle.bmc.keymanagement.model.VaultSummary;
import com.oracle.bmc.keymanagement.requests.ListVaultsRequest;
import com.oracle.bmc.keymanagement.responses.ListVaultsResponse;
import com.oracle.bmc.secrets.SecretsClient;
import com.oracle.bmc.secrets.model.Base64SecretBundleContentDetails;
import com.oracle.bmc.secrets.requests.GetSecretBundleByNameRequest;
import com.oracle.bmc.secrets.responses.GetSecretBundleByNameResponse;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class OCIDatabaseConfiguration {
	
	public final Logger log = LogManager.getLogger(OCIDatabaseConfiguration.class);

	@Value("${oci.config.path}")
	private String ociConfigPath;

	@Value("${oci.config.name}")
	private String ociConfigName;

	@Value("${keyVault.name}")
	private String vaultName;

	@Value("${keyVault.compartment.name}")
	private String compartmentName;

	@Bean
	@Primary
	public DataSource getDataSource() throws IOException {
		final String vaultId = getVaultId();
		return DataSourceBuilder.create().url(getOracleUrl("Hostname", "SID", "Port", vaultId))
				.username(getSecretFromVault("db_user", vaultId)).password(getSecretFromVault("db_password", vaultId))
				.build();
	}

	private String getOracleUrl(String hostNameKey, String sidKey, String portKey, String vaultId) throws IOException {
		String url = "jdbc:oracle:thin:@" + getSecretFromVault(hostNameKey, vaultId) + ":"
				+ getSecretFromVault(portKey, vaultId) + ":" + getSecretFromVault(sidKey, vaultId);
		return url;
	}

	private String getSecretFromVault(String secretName, String vaultId) throws IOException {
		GetSecretBundleByNameResponse getSecretBundleByNameResponse;
		SecretsClient secretsClient = null;
		try {
			secretsClient = SecretsClient.builder()
					.build(new ConfigFileAuthenticationDetailsProvider(ociConfigPath, ociConfigName));
		} catch (IOException e) {
			log.error("Authentication failed for keyvault");
			throw e;
		}
		getSecretBundleByNameResponse = secretsClient.getSecretBundleByName(
				GetSecretBundleByNameRequest.builder().secretName(secretName).vaultId(vaultId).build());
		Base64SecretBundleContentDetails base64SecretBundleContentDetails = (Base64SecretBundleContentDetails) getSecretBundleByNameResponse
				.getSecretBundle().getSecretBundleContent();
		byte[] secretValueDecoded = Base64.decodeBase64(base64SecretBundleContentDetails.getContent());
		return new String(secretValueDecoded).replace("\n", "");
	}

	private String getVaultId() throws IOException {
		AuthenticationDetailsProvider provider = null;
		try {
			provider = new ConfigFileAuthenticationDetailsProvider(ociConfigPath, ociConfigName);
		} catch (IOException e) {
			log.error("Authentication failed for keyvault");
			throw e;
		}
		Identity identityClient = IdentityClient.builder().build(provider);
		Compartment compartment = getCompartment(identityClient, provider);
		KmsVaultClient kmsValueClient = KmsVaultClient.builder().build(provider);
		ListVaultsRequest listRequest = ListVaultsRequest.builder().compartmentId(compartment.getId()).build();
		ListVaultsResponse listResponse = kmsValueClient.listVaults(listRequest);
		Optional<VaultSummary> vaultSummary = listResponse.getItems().stream().filter(vault -> vaultName.equalsIgnoreCase(vault.getDisplayName()))
				.findFirst();
		
		return vaultSummary.get().getId();
	}

	private Compartment getCompartment(Identity identityClient, AuthenticationDetailsProvider provider) {
		Optional<Compartment> compartment = null;
		String nextPageToken = null;
		ListCompartmentsResponse response = null;
		do {
			response = identityClient.listCompartments(ListCompartmentsRequest.builder().limit(5)
					.compartmentId(provider.getTenantId()).page(nextPageToken).build());

			compartment = response.getItems().stream().filter(compart -> compartmentName.equalsIgnoreCase(compart.getName()))
					.findFirst();
			nextPageToken = response.getOpcNextPage();
		} while (compartment == null && nextPageToken != null);
		return compartment.get();
	}

}
