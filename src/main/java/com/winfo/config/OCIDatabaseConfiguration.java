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
import org.springframework.util.StringUtils;

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
import com.oracle.bmc.secrets.SecretsClient;
import com.oracle.bmc.secrets.model.Base64SecretBundleContentDetails;
import com.oracle.bmc.secrets.requests.GetSecretBundleByNameRequest;
import com.oracle.bmc.secrets.responses.GetSecretBundleByNameResponse;
import com.winfo.exception.WatsEBSCustomException;

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

	private static final String HOSTNAME = "Hostname";
	private static final String SID = "SID";
	private static final String PORT = "Port";
	private static final String DB_USERNAME = "db_user";
	private static final String DB_PASSWORD = "db_password";

	@Bean
	@Primary
	public DataSource getDataSource() throws Exception {
		final String vaultId = getVaultId();
		return DataSourceBuilder.create().url(getOracleUrl(HOSTNAME, SID, PORT, vaultId))
				.username(getSecretFromVault(DB_USERNAME, vaultId)).password(getSecretFromVault(DB_PASSWORD, vaultId))
				.build();
	}

	private String getOracleUrl(String hostNameKey, String sidKey, String portKey, String vaultId) throws Exception {
		StringBuilder url = (new StringBuilder()).append("jdbc:oracle:thin:@")
				.append(getSecretFromVault(hostNameKey, vaultId)).append(":")
				.append(getSecretFromVault(portKey, vaultId)).append(":").append(getSecretFromVault(sidKey, vaultId));
		return url.toString();
	}

	private String getSecretFromVault(String secretName, String vaultId) throws Exception {
		GetSecretBundleByNameResponse getSecretBundleByNameResponse;
		SecretsClient secretsClient = null;
		byte[] secretValueDecoded = null;
		try {
			secretsClient = SecretsClient.builder()
					.build(new ConfigFileAuthenticationDetailsProvider(ociConfigPath, ociConfigName));
			getSecretBundleByNameResponse = secretsClient.getSecretBundleByName(
					GetSecretBundleByNameRequest.builder().secretName(secretName).vaultId(vaultId).build());
			Base64SecretBundleContentDetails base64SecretBundleContentDetails = (Base64SecretBundleContentDetails) getSecretBundleByNameResponse
					.getSecretBundle().getSecretBundleContent();
			secretValueDecoded = Base64.decodeBase64(base64SecretBundleContentDetails.getContent());
		} catch (IOException e) {
			log.error("Authentication failed for keyvault {}", e.getMessage());
			throw new WatsEBSCustomException(400, "Authentication failed for keyvault", e);
		} catch (Exception e) {
			log.error("Failed while fetching the value {}", e.getMessage());
			throw new WatsEBSCustomException(400, "Failed while fetching the value", e);
		}
		if (secretValueDecoded != null)
			return new String(secretValueDecoded).replace("\n", "");
		else {
			log.error("Value is not present in vault with secret name {}", secretName);
			throw new WatsEBSCustomException(400, String.format("Value is not present in the vault with secret name %s", secretName));
		}
	}

	private String getVaultId() throws Exception {
		Optional<VaultSummary> vaultSummary = null;
		AuthenticationDetailsProvider provider = null;
		try {
			provider = new ConfigFileAuthenticationDetailsProvider(ociConfigPath, ociConfigName);
			Identity identityClient = IdentityClient.builder().build(provider);
			Compartment compartment = getCompartment(identityClient, provider);
			vaultSummary = KmsVaultClient.builder().build(provider)
					.listVaults(ListVaultsRequest.builder().compartmentId(compartment.getId()).build()).getItems()
					.parallelStream().filter(vault -> vaultName.equalsIgnoreCase(vault.getDisplayName())).findFirst();

			if (vaultSummary.isPresent())
				return vaultSummary.get().getId();
			else {
				log.error("Vault is not present in the compartment ::" + compartment.getName());
				throw new WatsEBSCustomException(400, 
						String.format("Vault is not present in the compartment %s ", compartment.getName()));
				
			}
		} catch (IOException e) {
			log.error("Authentication failed for keyvault {}", e.getMessage());
			throw new WatsEBSCustomException(400, "Authentication failed for keyvault", e);
		} catch (Exception e) {
			log.error("Not able to get vault id {}", e.getMessage());
			throw new WatsEBSCustomException(400, "Not able to get vault id", e);
		}
	}

	private Compartment getCompartment(Identity identityClient, AuthenticationDetailsProvider provider)
			throws Exception {
		Optional<Compartment> compartment = Optional.empty();
		String nextPageToken = null;
		ListCompartmentsResponse response = null;
		do {
			response = identityClient.listCompartments(ListCompartmentsRequest.builder().limit(10)
					.compartmentId(provider.getTenantId()).page(nextPageToken).build());

			compartment = response.getItems().parallelStream()
					.filter(compart -> compartmentName.equalsIgnoreCase(compart.getName())).findFirst();
			nextPageToken = response.getOpcNextPage();
		} while (compartment.isEmpty() && StringUtils.hasLength(nextPageToken));
		if (compartment.isPresent())
			return compartment.get();
		else {
			log.error("Compartment is not present in OCI keyvault");
			throw new WatsEBSCustomException(400, "Compartment is not present in OCI keyvault");
		}
	}

}
