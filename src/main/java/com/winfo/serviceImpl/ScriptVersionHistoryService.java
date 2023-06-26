package com.winfo.serviceImpl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.winfo.exception.WatsEBSException;
import com.winfo.model.ScriptMaster;
import com.winfo.utils.Constants;
import com.winfo.utils.FileUtil;
import com.winfo.vo.ResponseDto;
import com.winfo.vo.ScriptMaterVO;
import com.winfo.vo.VersionHistoryDto;

@Service
public class ScriptVersionHistoryService extends AbstractSeleniumKeywords {
	public static final Logger logger = Logger.getLogger(ScriptVersionHistoryService.class);
	public static final String HISTORY = "History";
	public static final String JSON = ".json";
	public static final String TEMP = "temp";
	@Autowired
	private DataBaseEntry dataBaseEntry;

	public ResponseDto saveVersionHistory(VersionHistoryDto versionHistoryDto) throws Exception {
		try {
			versionHistoryDto.setSaveHistory(true);
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
			ScriptMaster scriptMaster = dataBaseEntry.getScriptDetailsByScriptId(versionHistoryDto.getScriptId());
			ScriptMaterVO scriptMasterVO = mapper.readValue(mapper.writeValueAsString(scriptMaster),
					ScriptMaterVO.class);
			Integer newNumber = null;
			String directoryPath = dataBaseEntry.getDirectoryPath();
			String localPath = directoryPath + FORWARD_SLASH + TEMP + FORWARD_SLASH + HISTORY + FORWARD_SLASH
					+ versionHistoryDto.getScriptId();
			String objectStorePath = HISTORY + FORWARD_SLASH + versionHistoryDto.getScriptId();
			FileUtil.createDir(localPath);
			List<String> listOfFiles = getListOfFileNamesPresentInObjectStore(objectStorePath + FORWARD_SLASH);
			if (!listOfFiles.isEmpty()) {
				listOfSortedFiles(listOfFiles);
				String[] lastValue = listOfFiles.get(0).split("_");
				newNumber = Integer.parseInt(lastValue[1].replace(JSON, "")) + 1;
				String lastIndex[]= listOfFiles.get(0).split(FORWARD_SLASH);
				String latestHistoryName=lastIndex[2].replace(".json", "");
				String decodeFileName=URLDecoder.decode(
						new String(latestHistoryName.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8),
						"UTF-8");
				versionHistoryDto.setVersionNumber(decodeFileName);
				ScriptMaterVO History = getVersionHistory(versionHistoryDto);
				if(!scriptMasterVO.equals(History)){
					saveHistoryData(newNumber,mapper,localPath,scriptMasterVO,objectStorePath);
				}
			} else {
				newNumber = 1;
				saveHistoryData(newNumber,mapper,localPath,scriptMasterVO,objectStorePath);
			}
			logger.info("Successfully Saved Version History");
			return new ResponseDto(200, Constants.SUCCESS, "Successfully saved the history!");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Failed to Save Version History " +e.getMessage());
			return new ResponseDto(500, Constants.ERROR, e.getMessage());
		}
	}
	
	public void saveHistoryData(Integer newNumber, ObjectMapper mapper,String localPath, ScriptMaterVO scriptMasterVO, String objectStorePath ) throws UnsupportedEncodingException
	{
		Timestamp instant = Timestamp.from(Instant.now());
		String fileName = instant + "_" + newNumber + JSON;
		String encodedName = URLEncoder.encode(
				new String(fileName.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8), "UTF-8");
		// Write into the file
		try (FileWriter file = new FileWriter(localPath + FORWARD_SLASH + encodedName)) {
			file.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(scriptMasterVO));
			logger.info("Successfully saved details in file...!!");
		} catch (IOException e) {
			logger.info("Failed to save details in the file " +e.getMessage());
			throw new WatsEBSException(500, "Not able to save the file!", e);
		}
		uploadObjectToObjectStore(localPath + FORWARD_SLASH + encodedName, objectStorePath, encodedName);
	
	}

	private List<String> listOfSortedFiles(List<String> listOfFiles) {
		Collections.sort(listOfFiles, new Comparator<String>() {

			public int compare(String f1, String f2) {
				String[] numberOfFile1 = f1.replace(JSON, "").split("_");
				String[] numberOfFile2 = f2.replace(JSON, "").split("_");

				return Long.valueOf(Long.parseLong(numberOfFile1[1])).compareTo(Long.parseLong(numberOfFile2[1])) * -1;

			}

		});
		return listOfFiles;
	}

	public Map<String, String> getMapOfVersionHistory(@Valid VersionHistoryDto versionHistoryDto) throws Exception {
		try {
			String objectStorePath = HISTORY + FORWARD_SLASH + versionHistoryDto.getScriptId() + FORWARD_SLASH;
			Map<String, String> map = new HashMap<>();
			List<String> listOfFiles = getListOfFileNamesPresentInObjectStore(objectStorePath);
			listOfFiles.forEach(list -> {
				String[] timeAndOrder = list.replace(JSON, "").replace(objectStorePath, "").split("_");
				String time = null;
				try {
					time = URLDecoder.decode(
							new String(timeAndOrder[0].getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8),
							"UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				String versionNumber = timeAndOrder[1];
				map.put(versionNumber, time);
			});
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WatsEBSException(500, "Not able to get the history list!", e);
		}
	}

	public ScriptMaterVO getVersionHistory(@Valid VersionHistoryDto versionHistoryDto) throws Exception {
		try {
			String directoryPath = dataBaseEntry.getDirectoryPath();
			String objectStorePath = HISTORY + FORWARD_SLASH + versionHistoryDto.getScriptId();
			String localPath = directoryPath + FORWARD_SLASH + TEMP + FORWARD_SLASH + HISTORY + FORWARD_SLASH
					+ versionHistoryDto.getScriptId();
			String fileName = URLEncoder
					.encode(new String(versionHistoryDto.getVersionNumber().getBytes(StandardCharsets.ISO_8859_1),
							StandardCharsets.UTF_8), "UTF-8");
			downloadObjectFromObjectStore(localPath + FORWARD_SLASH + fileName + JSON, objectStorePath,
					fileName + JSON);
			ObjectMapper mapper = new ObjectMapper();
			ScriptMaterVO scriptMaterVO = mapper.readValue(new File(localPath + FORWARD_SLASH + fileName + JSON),
					ScriptMaterVO.class);
			if(!versionHistoryDto.isSaveHistory()){
				scriptMaterVO.updateFieldIfNotNull(dataBaseEntry);
			}
			return scriptMaterVO;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WatsEBSException(500, "Not able to get the history!", e);
		}
	}

}
