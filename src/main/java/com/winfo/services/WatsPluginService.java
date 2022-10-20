package com.winfo.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.transaction.Transactional;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.winfo.dao.WatsPluginDao;
import com.winfo.model.ScriptMaster;
import com.winfo.model.ScriptMetaData;
import com.winfo.vo.DomGenericResponseBean;
import com.winfo.vo.PlugInVO;
import com.winfo.vo.WatsLoginVO;
import com.winfo.vo.WatsPluginMasterVO;
import com.winfo.vo.WatsPluginMetaDataVO;

@Service
public class WatsPluginService {

	@Autowired
	WatsPluginDao dao;

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
	
	public ResponseEntity<StreamingResponseBody> getPluginZipFile(PlugInVO plugInVO) throws IOException {
		unZipFolder();
		writePropertiesFile();
		zipFolder();
		    return ResponseEntity
		            .ok()
		            .header("Content-Disposition", "attachment; filename=\"WATS Script Assistant.zip\"")
		            .body(out -> {
		            	ZipOutputStream zipOutputStream = new ZipOutputStream(out);

		                // create a list to add files to be zipped
//		                ArrayList<File> files = new ArrayList<>();
		                File file = new File("\\objstore\\tst\\WATS Script Assistant.zip");
//		                File[] listOfFiles = folder.listFiles();
//		                if (listOfFiles != null)
//		                {
//		                  for (int i = 0; i < listOfFiles.length; i++)
//		                  {
//		                    if (listOfFiles[i].isFile())
//		                    {
//		                      files.add(new File(listOfFiles[i].getAbsolutePath()));
//		                    }
//		                  }
//		                }
//		                files.add(folder.getAbsoluteFile());

		                // package files
//		                for (File file : files) {
		                    //new zip entry and copying inputstream with file to zipOutputStream, after all closing streams
		                    zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
		                    FileInputStream fileInputStream = new FileInputStream(file);

		                    IOUtils.copy(fileInputStream, zipOutputStream);

		                    fileInputStream.close();
		                    zipOutputStream.closeEntry();
//		                }

		                zipOutputStream.close();
		            });
		}
	
	public void zipFolder() throws IOException {
        String sourceFile = "\\objstore\\tst\\WATS-Auto-Recording";
        FileOutputStream fos = new FileOutputStream("\\objstore\\tst\\WATS Script Assistant.zip");
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        File fileToZip = new File(sourceFile);

        zipFile(fileToZip, fileToZip.getName(), zipOut);
        zipOut.close();
        fos.close();
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
	
	public void unZipFolder() throws IOException {
		String fileZip = "\\objstore\\tst\\WATS Script Assistant.zip";
        File destDir = new File("\\objstore\\tst");
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
                // fix for Windows-created archives
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
	
	public void writePropertiesFile() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		PlugInVO root = new PlugInVO();
		List<Map<String, String>> listOfGroups = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<>();
		map.put("baseURL", "newLink");
		map.put("name", "DEV");
		listOfGroups.add(map);
		root.setGroups(listOfGroups);

		// Write into the file
		try (FileWriter file = new FileWriter(
				"\\objstore\\tst\\WATS-Auto-Recording\\properties.json")) {
			file.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root));
			System.out.println("Successfully updated json object to file...!!");
		}
	}

}