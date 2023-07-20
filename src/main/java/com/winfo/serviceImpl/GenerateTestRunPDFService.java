package com.winfo.serviceImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.winfo.constants.TestScriptExecServiceEnum;
import com.winfo.repository.TestSetLinesRepository;
import com.winfo.utils.Constants;
import com.winfo.utils.FileUtil;
import com.winfo.vo.CustomerProjectDto;
import com.winfo.vo.FetchConfigVO;
import com.winfo.vo.ScriptDetailsDto;

@Service
public class GenerateTestRunPDFService extends AbstractSeleniumKeywords {

	@Autowired
	DataBaseEntry dataBaseEntry;
	
	@Autowired
	TestSetLinesRepository testSetLinesRepo;
	
	@Async
	public void testRunPdfGeneration(String testSetId, FetchConfigVO fetchConfigVO) throws Exception {
		try {
			
			CustomerProjectDto customerDetails = dataBaseEntry.getCustomerDetails(testSetId);
			List<ScriptDetailsDto> fetchMetadataListVOFinal = dataBaseEntry.getScriptDetailsListVO(testSetId, null, true,
					false);
			
			int passCount=testSetLinesRepo.getScriptCountOfTestRun(testSetId,Constants.PASS);
			int failCount=testSetLinesRepo.getScriptCountOfTestRun(testSetId,Constants.FAIL);
			fetchConfigVO.setPasscount(passCount);
			fetchConfigVO.setFailcount(failCount);
			String screenShotFolderPath = (fetchConfigVO.getWINDOWS_SCREENSHOT_LOCATION()
					+ customerDetails.getCustomerName() + File.separator + customerDetails.getTestSetName());
			createDir(screenShotFolderPath);
			File folder = new File(screenShotFolderPath);
			Map<String, String> screenShotsMap = new HashMap<>();
			for (ScriptDetailsDto scriptDetailsData : fetchMetadataListVOFinal) {
				String seqNum = scriptDetailsData.getSeqNum();
				if (!screenShotsMap.containsKey(seqNum)) {
					String screenShot = scriptDetailsData.getSeqNum() + "_" + scriptDetailsData.getLineNumber() + "_"
							+ scriptDetailsData.getScenarioName() + "_" + scriptDetailsData.getScriptNumber() + "_"
							+ customerDetails.getTestSetName() + "_" + scriptDetailsData.getLineNumber();
					screenShotsMap.put(seqNum, screenShot);
				}
			}
			List<String> files = new ArrayList<>();
			for (File fileName : Arrays.asList(folder.listFiles())) {
				files.add(fileName.getName());
			}
			if (folder.exists()) {
				for (Map.Entry<String, String> entry : screenShotsMap.entrySet()) {
					String seqNum = entry.getKey();
					String value = entry.getValue();
					String screenShotName = null;
					if (files.contains(value + "_" + TestScriptExecServiceEnum.PASSED.getValue() + TestScriptExecServiceEnum.PNG_EXTENSION.getValue())) {
						screenShotName = value + "_" + TestScriptExecServiceEnum.PASSED.getValue() + TestScriptExecServiceEnum.PNG_EXTENSION.getValue();
					} else if (files.contains(value + "_" + TestScriptExecServiceEnum.PASSED.getValue() + TestScriptExecServiceEnum.JPG_EXTENSION.getValue())) {
						screenShotName = value + "_" + TestScriptExecServiceEnum.PASSED.getValue() + TestScriptExecServiceEnum.JPG_EXTENSION.getValue();
					} else if (files.contains(value + "_" + TestScriptExecServiceEnum.FAILED.getValue() + TestScriptExecServiceEnum.PNG_EXTENSION.getValue())) {
						screenShotName = value + "_" + TestScriptExecServiceEnum.FAILED.getValue() + TestScriptExecServiceEnum.PNG_EXTENSION.getValue();
					} else if (files.contains(value + "_" + TestScriptExecServiceEnum.FAILED.getValue() + TestScriptExecServiceEnum.JPG_EXTENSION.getValue())) {
						screenShotName = value + "_" + TestScriptExecServiceEnum.FAILED.getValue() + TestScriptExecServiceEnum.JPG_EXTENSION.getValue();
					}
					if (screenShotName == null) {
						downloadScreenshotsFromObjectStore(screenShotFolderPath, customerDetails.getCustomerName(),
								customerDetails.getTestSetName(), seqNum);
					}
				}
			}
			
			CompletableFuture<String> completableFuture1 = createPdf(fetchMetadataListVOFinal, fetchConfigVO, "Passed_Report.pdf", customerDetails);	
			CompletableFuture<String> completableFuture2 = createPdf(fetchMetadataListVOFinal, fetchConfigVO, "Failed_Report.pdf", customerDetails);
			CompletableFuture<String> completableFuture3 = createPdf(fetchMetadataListVOFinal, fetchConfigVO, "Detailed_Report.pdf", customerDetails);
			List<CompletableFuture<String>> completableFutures = Arrays.asList(completableFuture1, completableFuture2, completableFuture3);
			CompletableFuture<Void> resultantCf = CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[completableFutures.size()]));
			CompletableFuture<List<String>> allFutureResults = resultantCf.thenApply(t ->{
				FileUtil.deleteScreenshotAndPdfDirectoryFromTemp(fetchConfigVO, customerDetails);
				dataBaseEntry.updateStatusOfPdfGeneration(testSetId,Constants.PASSED);
				return completableFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
			});  		
			logger.info("Successfully generated PDFs - " + allFutureResults.get());    
		}
		catch(Exception e) {
			logger.error("Error occurred while generating PDFs");
			dataBaseEntry.updateStatusOfPdfGeneration(testSetId,Constants.PASSED);
		}
	}
	
	public void createDir(String path) {
		File folder1 = new File(path);
		if (!folder1.exists()) {
			logger.info("creating directory: " + folder1.getName());
			try {
				folder1.mkdirs();
			} catch (SecurityException se) {
				se.printStackTrace();
			}
		} else {
			logger.info("Folder exist");
		}
	}
	
}
