package com.winfo.serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetObjectRequest;
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;
import com.winfo.dao.WatsPluginDao;
import com.winfo.exception.WatsEBSException;
import com.winfo.model.ScriptMaster;
import com.winfo.model.ScriptMetaData;
import com.winfo.vo.DomGenericResponseBean;
import com.winfo.vo.WatsLoginVO;
import com.winfo.vo.WatsPluginMasterVO;
import com.winfo.vo.WatsPluginMetaDataVO;
import com.winfo.vo.WatsScriptAssistantVO;

@Service
public class WatsPluginService {
	
	public static final Logger logger = Logger.getLogger(WatsPluginService.class);
	
	@Value("${oci.config.name.common}")
	private String ociConfigName;
	@Value("${oci.bucket.name.common}")
	private String ociBucketName;
	@Value("${oci.namespace.common}")
	private String ociNamespace;
	@Value("${oci.config.path}")
	private String ociConfigPath;
	public static final String FORWARD_SLASH = "/";
	@Autowired
	WatsPluginDao dao;
	
	@Autowired
	DataBaseEntry dataBaseEntry;

	@Transactional
	public DomGenericResponseBean pluginData(WatsPluginMasterVO mastervo) {
		String module = mastervo.getModule();
		String processArea = mastervo.getProcessArea();
		List<String> scriptNumbers = dao.getScriptNumber(processArea, module);

		String newmodule = mastervo.getModuleSrt();

		String newScriptNumber = null;
		ArrayList<Integer> slist = new ArrayList<Integer>();
		if (scriptNumbers != null) {
			for (String snumber : scriptNumbers) {
				Integer i = Integer.parseInt(snumber.replaceAll("[\\D]", ""));

				slist.add(i);
			}
			int max = Collections.max(slist);
			int snum = max + 1;
			newScriptNumber = processArea + "." + newmodule + "." + snum;
			System.out.println(newScriptNumber);

		} else {
			newScriptNumber = processArea + "." + newmodule + "." + "1";
		}

		ScriptMaster master = new ScriptMaster();
		master.setModule(mastervo.getModule());
		master.setScenarioName(mastervo.getScenarioName());
		master.setScenarioDescription(mastervo.getScenarioDescription());
		master.setProductVersion(mastervo.getProductVersion());
		master.setPriority(mastervo.getPriority());
		master.setProcessArea(mastervo.getProcessArea());
		master.setRole(mastervo.getRole());
		master.setScriptNumber(newScriptNumber);
		master.setSubProcessArea(mastervo.getSubProcessArea());
		master.setStandardCustom(mastervo.getStandardCustom());
		master.setTestScriptStatus(mastervo.getTestScriptStatus());
		master.setCreatedBy(mastervo.getCreatedBy());
		master.setCreationDate(java.sql.Date.valueOf(mastervo.getCreationDate()));
		master.setPluginFlag("true");
		for (WatsPluginMetaDataVO metadatavo : mastervo.getMetaDataList()) {
			ScriptMetaData metadata = new ScriptMetaData();
			metadata.setAction(metadatavo.getAction());
			metadata.setLineNumber(metadatavo.getLineNumber());
			metadata.setInputParameter(metadatavo.getInputParameter());
			metadata.setStepDesc(metadatavo.getStepDesc());
			metadata.setScriptNumber(newScriptNumber);
			metadata.setValidationType("NA");
			metadata.setValidationName("NA");
			metadata.setUniqueMandatory("NA");
			metadata.setDatatypes("NA");
			metadata.setCreatedBy(mastervo.getCreatedBy());
			metadata.setCreationDate(java.sql.Date.valueOf(mastervo.getCreationDate()));
			metadata.setMetadataInputvalue(metadatavo.getInputValue());
			master.addMetadata(metadata);

		}
		String scriptnumber = master.getScriptNumber();
		return dao.pluginData(master, scriptnumber);
	}

	@Transactional
	public DomGenericResponseBean watslogin(WatsLoginVO loginvo) {
		DomGenericResponseBean response = new DomGenericResponseBean();
		String username = loginvo.getUsername();
		String password = loginvo.getPassword();
		String userId = dao.getUserIdValidation(username);
		if (userId != null) {
			String userIdEnd = dao.verifyEndDate(username);
			String userIdPwdEx = dao.verifyPasswordExpire(username);
			String userIdActive = dao.verifyUserActive(username);
			String passwordEncript = dao.getEncriptPassword(username);

			if (userIdEnd != null && userIdPwdEx != null && userIdActive != null) {
				if (password.equalsIgnoreCase(passwordEncript)) {
					response.setStatus(200);
					response.setStatusMessage("Login successfully");
				} else {
					response.setStatus(404);
					response.setStatusMessage("Password is incorrect");
				}
			}
			if (userIdEnd == null) {
				response.setStatus(404);
				response.setStatusMessage("User account expired");
			}
			if (userIdPwdEx == null) {
				response.setStatus(404);
				response.setStatusMessage("User password expired.Please concat your administrator!");
			}
			if (userIdActive == null) {
				response.setStatus(404);
				response.setStatusMessage("UserId is in-active!");
			}
		} else {
			response.setStatus(404);
			response.setStatusMessage("User name does not exists");
		}
		return response;
	}

	@Transactional
	public List<String> getTestrunData() {
		return dao.getTestrunData();
	}

	public List<String> getTestrunDataPVerson(String productverson) {
		return dao.getTestrunDataPVerson(productverson);
	}
	
	public ResponseEntity<StreamingResponseBody> getWatsScriptAssistantFile(WatsScriptAssistantVO watsScriptAssistantVO) throws IOException {
		if(watsScriptAssistantVO.getBrowser() == null || "".equalsIgnoreCase(watsScriptAssistantVO.getBrowser())) {
			watsScriptAssistantVO.setBrowser("chrome");
		}
		String customerUri = dataBaseEntry.getCentralRepoUrl("PUBLIC_URL");
		String directoryPath = dao.getDirectoryPath();
		downloadObjectFromObjectStore(directoryPath+"/temp/plugin/WinfoTest Script Assistant.zip", "WinfoTest Script Assistant/"+watsScriptAssistantVO.getBrowser(), "WinfoTest Script Assistant.zip");
		unZipFolder(directoryPath+"/temp/plugin");
		writePropertiesFile(directoryPath+"/temp/plugin",customerUri,watsScriptAssistantVO.getTargetEnvironment());
		return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=\"WinfoTest Script Assistant - "+StringUtils.capitalize(watsScriptAssistantVO.getBrowser())+".zip\"")
				.body(out -> {
					String sourceFile = directoryPath+"/temp/plugin/WinfoTest-Auto-Recording";
					ZipOutputStream zipOut = new ZipOutputStream(out);
					File fileToZip = new File(sourceFile);
					zipFile(fileToZip, fileToZip.getName(), zipOut);
					zipOut.close();
				});
	}

	private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
		if (fileToZip.isHidden()) {
			return;
		}
		if (fileToZip.isDirectory()) {
			if (fileName.endsWith("/")) {
				zipOut.putNextEntry(new ZipEntry(fileName));
				zipOut.closeEntry();
			} else {
				zipOut.putNextEntry(new ZipEntry(fileName + "/"));
				zipOut.closeEntry();
			}
			File[] children = fileToZip.listFiles();
			for (File childFile : children) {
				zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
			}
			return;
		}
		FileInputStream fis = new FileInputStream(fileToZip);
		ZipEntry zipEntry = new ZipEntry(fileName);
		zipOut.putNextEntry(zipEntry);
		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zipOut.write(bytes, 0, length);
		}
		fis.close();
	}

	public void unZipFolder(String directoryPath) throws IOException {
		String fileZip = directoryPath+"/WinfoTest Script Assistant.zip";
		File destDir = new File(directoryPath);
		byte[] buffer = new byte[1024];
		ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
		ZipEntry zipEntry = zis.getNextEntry();
		while (zipEntry != null) {
			File newFile = newFile(destDir, zipEntry);
			if (zipEntry.isDirectory()) {
				if (!newFile.isDirectory() && !newFile.mkdirs()) {
					throw new IOException("Failed to create directory " + newFile);
				}
			} else {
				
				File parent = newFile.getParentFile();
				if (!parent.isDirectory() && !parent.mkdirs()) {
					throw new IOException("Failed to create directory " + parent);
				}

				// write file content
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
			}
			zipEntry = zis.getNextEntry();
		}
		zis.closeEntry();
		zis.close();
	}

	public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
		File destFile = new File(destinationDir, zipEntry.getName());

		String destDirPath = destinationDir.getCanonicalPath();
		String destFilePath = destFile.getCanonicalPath();

		if (!destFilePath.startsWith(destDirPath + File.separator)) {
			throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
		}

		return destFile;
	}

	public void writePropertiesFile(String directoryPath, String customerUri, String customerName) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		WatsScriptAssistantVO root = new WatsScriptAssistantVO();
		List<Map<String, String>> listOfGroups = new ArrayList<>();
		Map<String, String> map = new HashMap<>();
		map.put("baseURL", customerUri+"/wats_workspace_prod/plug_in/");
		map.put("name", customerName);
		listOfGroups.add(map);
		root.setGroups(listOfGroups);

		// Write into the file
		try (FileWriter file = new FileWriter(directoryPath+"/WinfoTest-Auto-Recording/properties.json")) {
			file.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root));
			logger.info("Successfully updated json object to file...!!");
		}
	}
	
	public void downloadObjectFromObjectStore(String localFilePath, String folderName, String fileName) {
		GetObjectResponse response = null;
		try {
			/**
			 * Create a default authentication provider that uses the DEFAULT profile in the
			 * configuration file. Refer to <see
			 * href="https://docs.cloud.oracle.com/en-us/iaas/Content/API/Concepts/sdkconfig.htm#SDK_and_CLI_Configuration_File>the
			 * public documentation</see> on how to prepare a configuration file.
			 */
			final ConfigFileReader.ConfigFile configFile = ConfigFileReader
					.parse(new FileInputStream(new File(ociConfigPath)), ociConfigName);
			final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);
			final String FILE_NAME = localFilePath;
			File file = new File(FILE_NAME);
			String destinationFilePath = folderName + FORWARD_SLASH + fileName;
			/* Create a service client */
			try (ObjectStorageClient client = new ObjectStorageClient(provider);) {
				/* Create a request and dependent object(s). */
				GetObjectRequest getObjectRequest = GetObjectRequest.builder().namespaceName(ociNamespace)
						.bucketName(ociBucketName).objectName(destinationFilePath).build();

				response = client.getObject(getObjectRequest);
				try (final InputStream stream = response.getInputStream();
						final OutputStream outputStream = new FileOutputStream(file.getPath())) {

					byte[] buf = new byte[8192];
					int bytesRead;
					while ((bytesRead = stream.read(buf)) > 0) {
						outputStream.write(buf, 0, bytesRead);
					}
				}

			}
		} catch (WatsEBSException e) {
			throw e;
		} catch (Exception e) {
			throw new WatsEBSException(500, "Exception occured while downloading file from Object Store", e);
		}

	}
}