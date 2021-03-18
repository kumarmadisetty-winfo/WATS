package com.winfo.interface1;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import com.winfo.services.FetchConfigVO;
import com.winfo.services.FetchMetadataVO;
@Service
public interface SeleniumKeyWordsInterface {

	
	public List<String> getFailFileNameListNew(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO) throws Exception;
	public List<String> getFileNameListNew(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO) throws Exception;
	public List<String> getPassedPdfNew(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO) throws Exception;
	public List<String> getFailedPdfNew(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO) throws Exception;
	public List<String> getDetailPdfNew(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO) throws Exception;
	 public  void convertJPGtoMovie(String targetFile1,List<String> targetFileList, List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO,String name) throws Exception;
		public List<String> getFailFileNameList(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO) throws Exception;
		public void createPdf(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO, String string,
				Date Starttime, Date endtime) throws Exception;
		public void DelatedScreenshoots(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO) throws Exception;
		public String sendValue(WebDriver driver, String param1, String param2, String input_value,
				FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception;
		public String textarea(WebDriver driver, String param1, String param2, String input_value,
				FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception;
		public void dropdownValues(WebDriver driver, String param1, String param2, String param3, String input_value,
				FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception;
		public void tableSendKeys(WebDriver driver, String param1, String param2, String param3, String input_value,
				FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception;
		public void multiplelinestableSendKeys(WebDriver driver, String param1, String param2, String param3,
				String input_value, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception;
		public void tableDropdownValues(WebDriver driver, String param1, String param2, String input_value,
				FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception;
		public void clickLinkAction(WebDriver driver, String param1, String param2, String input_value,
				FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception;
		public void clickCheckbox(WebDriver driver, String param1, String input_value, FetchMetadataVO fetchMetadataVO,
				FetchConfigVO fetchConfigVO) throws Exception;
		public void clickRadiobutton(WebDriver driver, String param1, String param2, String input_value,
				FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception;
		public void selectAValue(WebDriver driver, String param1, String param2, String input_value,
				FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception;
		public void clickTableLink(WebDriver driver, String param1, String param2, FetchMetadataVO fetchMetadataVO,
				FetchConfigVO fetchConfigVO) throws Exception;
		public void clickLink(WebDriver driver, String param1, String param2, FetchMetadataVO fetchMetadataVO,
				FetchConfigVO fetchConfigVO) throws Exception;
		public void clickNotificationLink(WebDriver driver, String param1, FetchMetadataVO fetchMetadataVO,
				FetchConfigVO fetchConfigVO) throws Exception;
		public void clickMenu(WebDriver driver, String param1, String param2, FetchMetadataVO fetchMetadataVO,
				FetchConfigVO fetchConfigVO) throws Exception;
		public void clickImage(WebDriver driver, String param1, String param2, FetchMetadataVO fetchMetadataVO,
				FetchConfigVO fetchConfigVO) throws Exception;
		public String clickTableImage(WebDriver driver, String param1, String param2, String input_value,
				FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception;
		public void clickExpandorcollapse(WebDriver driver, String param1, String param2,
				FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception;
		public void clickButton(WebDriver driver, String param1, String param2, FetchMetadataVO fetchMetadataVO,
				FetchConfigVO fetchConfigVO) throws Exception;
		public String getErrorMessages(WebDriver driver) throws Exception;
		public String screenshotFail(WebDriver driver, String string, FetchMetadataVO fetchMetadataVO,
				FetchConfigVO fetchConfigVO) throws Exception;
		public void tableRowSelect(WebDriver driver, String param1, String param2, FetchMetadataVO fetchMetadataVO,
				FetchConfigVO fetchConfigVO) throws Exception;
		public String screenshot(WebDriver driver, String string, FetchMetadataVO fetchMetadataVO,
				FetchConfigVO fetchConfigVO) throws Exception;
		public void clickButtonDropdown(WebDriver driver, String param1, String param2, String input_value,
				FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception;
		public void mousehover(WebDriver driver, String param1, String param2, FetchMetadataVO fetchMetadataVO,
				FetchConfigVO fetchConfigVO) throws Exception;
		public void scrollUsingElement(WebDriver driver, String input_parameter, FetchMetadataVO fetchMetadataVO,
				FetchConfigVO fetchConfigVO) throws Exception;
		public void moveToElement(WebDriver driver, String input_parameter, FetchMetadataVO fetchMetadataVO,
				FetchConfigVO fetchConfigVO) throws Exception;
		public void switchToDefaultFrame(WebDriver driver) throws Exception;
		public void switchToFrame(WebDriver driver, String input_parameter, FetchMetadataVO fetchMetadataVO,
				FetchConfigVO fetchConfigVO) throws Exception;
		public void selectByText(WebDriver driver, String param1, String param2, String input_value,
				FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception;
		public void clickFilter(WebDriver driver, String xpath_location, String xpath_location1,
				FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception;
		public void windowhandle(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception;
		public void dragAnddrop(WebDriver driver, String xpath_location, String xpath_location1,
				FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception;
		public void copy(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception;
		public String copytext(WebDriver driver, String xpath_location, FetchMetadataVO fetchMetadataVO,
				FetchConfigVO fetchConfigVO) throws Exception;
		public String copynumber(WebDriver driver, String param1, String param2, FetchMetadataVO fetchMetadataVO,
				FetchConfigVO fetchConfigVO) throws Exception;
		public String copyy(WebDriver driver, String xpath_location, FetchMetadataVO fetchMetadataVO,
				FetchConfigVO fetchConfigVO) throws Exception;
		public void clear(WebDriver driver, String param1, String param2, FetchMetadataVO fetchMetadataVO,
				FetchConfigVO fetchConfigVO) throws Exception;
		public void enter(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception;
		public void tab(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception;
		public void uploadFileAutoIT(String field_type, FetchMetadataVO fetchMetadataVO) throws Exception;
		public void paste(WebDriver driver, String input_parameter, FetchMetadataVO fetchMetadataVO,
				FetchConfigVO fetchConfigVO, String globalValueForSteps2) throws Exception;
		public void switchToParentWindow(WebDriver driver, FetchMetadataVO fetchMetadataVO,
				FetchConfigVO fetchConfigVO) throws Exception;
		public void switchDefaultContent(WebDriver driver, FetchMetadataVO fetchMetadataVO,
				FetchConfigVO fetchConfigVO) throws Exception;
		public void windowclose(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception;
		public void createFailedPdf(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO,
				String string, Date startdate, Date enddate) throws Exception;
		public void loginApplication(WebDriver driver, FetchConfigVO fetchConfigVO, FetchMetadataVO fetchMetadataVO,
				String type1, String type2, String type3, String param1, String param2, String param3,
				String input_value, String password) throws Exception;
		public void logout(WebDriver driver, FetchConfigVO fetchConfigVO, FetchMetadataVO fetchMetadataVO, String type1,
				String type2, String type3, String param1, String param2, String param3) throws Exception;
		public void openTask(WebDriver driver, FetchConfigVO fetchConfigVO, FetchMetadataVO fetchMetadataVO,
				String type1, String type2, String param1, String param2, int count) throws Exception;
		public void navigate(WebDriver driver, FetchConfigVO fetchConfigVO, FetchMetadataVO fetchMetadataVO,
				String type1, String type2, String param1, String param2, int count) throws Exception;
		public void datePicker(WebDriver driver, String param1, String param2, String input_value,
				FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception;
		public void refreshPage(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO);
		public void navigateToBackPage(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO);
		public void openPdf(WebDriver driver, String input_value, FetchMetadataVO fetchMetadataVO,
				FetchConfigVO fetchConfigVO);
		public void openFile(WebDriver driver, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO);
		public void actionApprove(WebDriver driver, String param1, String param2, FetchMetadataVO fetchMetadataVO,
				FetchConfigVO fetchConfigVO)throws Exception;
		public void multipleSendKeys(WebDriver driver, String param1, String param2, String value1, String value2,
				FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception;














}
