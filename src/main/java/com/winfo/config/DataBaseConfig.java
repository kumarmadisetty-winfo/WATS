package com.winfo.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
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
public class DataBaseConfig {
	
	@Value("${oci.config.path}")
	private String ociConfigPath;

	@Value("${oci.config.name}")
	private String ociConfigName;

	@Value("${keyVault.name}")
	private String vaultName;

	@Value("${keyVault.compartmentId}")
	private String compartmentId;

	@Bean
	@Primary
	public DataSource getDataSource() {
		final String vaultId = getVaultId(compartmentId);
		return DataSourceBuilder.create()
				.url(getOracleUrl("Hostname", "SID", "Port", vaultId))
				.username(getSecretFromVault("db_user", vaultId))
				.password(getSecretFromVault("db_password", vaultId))
				.build();
	}

	private String getOracleUrl(String hostNameKey, String sidKey, String portKey, String vaultId) {
		String url = "jdbc:oracle:thin:@" + getSecretFromVault(hostNameKey, vaultId) + ":"
				+ getSecretFromVault(portKey, vaultId) + ":" + getSecretFromVault(sidKey, vaultId);
		return url;
	}

	private String getSecretFromVault(String secretName, String vaultId) {
		GetSecretBundleByNameResponse getSecretBundleByNameResponse;
		SecretsClient secretsClient = null;
		try {
			secretsClient = SecretsClient.builder()
					.build(new ConfigFileAuthenticationDetailsProvider(ociConfigPath, ociConfigName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		{
			getSecretBundleByNameResponse = secretsClient.getSecretBundleByName(
					GetSecretBundleByNameRequest.builder().secretName(secretName).vaultId(vaultId).build());
		}
		Base64SecretBundleContentDetails base64SecretBundleContentDetails = (Base64SecretBundleContentDetails) getSecretBundleByNameResponse
				.getSecretBundle().getSecretBundleContent();
		byte[] secretValueDecoded = Base64.decodeBase64(base64SecretBundleContentDetails.getContent());
		return new String(secretValueDecoded).replace("\n", "");
	}

	private String getVaultId(String compartmentId) {
		String vaultId = null;
		AuthenticationDetailsProvider provider = null;
		try {
			provider = new ConfigFileAuthenticationDetailsProvider(ociConfigPath, ociConfigName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		KmsVaultClient kmsValueClient = KmsVaultClient.builder().build(provider);
		ListVaultsRequest listRequest = ListVaultsRequest.builder().compartmentId(compartmentId).build();
		ListVaultsResponse listResponse = kmsValueClient.listVaults(listRequest);
		for (VaultSummary vault : listResponse.getItems()) {
			if (vaultName.equals(vault.getDisplayName())) {
				vaultId = vault.getId();
				break;
			}
		}
		return vaultId;
	}
}
