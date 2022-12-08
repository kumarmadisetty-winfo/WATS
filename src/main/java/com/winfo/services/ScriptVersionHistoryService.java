package com.winfo.services;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.winfo.exception.WatsEBSCustomException;
import com.winfo.interface1.AbstractSeleniumKeywords;
import com.winfo.model.ScriptMaster;
import com.winfo.utils.Constants;
import com.winfo.vo.ResponseDto;
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
			ObjectMapper mapper = new ObjectMapper();
			ScriptMaster scriptMaster = dataBaseEntry.getScriptDetailsByScriptId(versionHistoryDto.getScriptId());

			Timestamp instant = Timestamp.from(Instant.now());

			Integer newNumber = null;
			String directoryPath = dataBaseEntry.getDirectoryPath();
			String localPath = directoryPath + FORWARD_SLASH + TEMP + FORWARD_SLASH + HISTORY + FORWARD_SLASH
					+ versionHistoryDto.getScriptId();
			String objectStorePath = HISTORY + FORWARD_SLASH + versionHistoryDto.getScriptId();
			createDir(localPath);
			List<String> listOfFiles = getListOfFileNamesPresentInObjectStore(objectStorePath + FORWARD_SLASH);
			if (!listOfFiles.isEmpty()) {
				listOfSortedFiles(listOfFiles);
				String[] lastValue = listOfFiles.get(0).split("_");
				newNumber = Integer.parseInt(lastValue[1].replace(JSON, "")) + 1;
			} else {
				newNumber = 1;
			}
			String fileName = instant + "_" + newNumber + JSON;

			String encodedName = URLEncoder.encode(
					new String(fileName.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8), "UTF-8");
			// Write into the file
			try (FileWriter file = new FileWriter(localPath + FORWARD_SLASH + encodedName)) {
				file.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(scriptMaster));
				logger.info("Successfully saved details in file...!!");
			} catch (IOException e) {
				throw new WatsEBSCustomException(500, "Not able to save the file!", e);
			}
			uploadObjectToObjectStore(localPath + FORWARD_SLASH + encodedName, objectStorePath, encodedName);
			return new ResponseDto(200, Constants.SUCCESS, "Successfully saved the history!");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseDto(500, Constants.ERROR, e.getMessage());
		}
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
			throw new WatsEBSCustomException(500, "Not able to get the history list!", e);
		}
	}

	public ScriptMaster getVersionHistory(@Valid VersionHistoryDto versionHistoryDto) throws Exception {
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
			return mapper.readValue(new File(localPath + FORWARD_SLASH + fileName + JSON), ScriptMaster.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WatsEBSCustomException(500, "Not able to get the history!", e);
		}
	}

}
