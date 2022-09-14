package com.winfo.interface1;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;
import com.winfo.services.FetchConfigVO;
import com.winfo.services.FetchMetadataVO;
import com.winfo.vo.ApiValidationVO;
import com.winfo.vo.CustomerProjectDto;
import com.winfo.vo.ScriptDetailsDto;

@Service
public interface SeleniumKeyWordsInterface {

	public void convertJPGtoMovie(String targetFile1, List<String> targetFileList,
			List<ScriptDetailsDto> fetchMetadataListVO, FetchConfigVO fetchConfigVO, String name,
			CustomerProjectDto customerDetails) throws Exception;


	public String sendValue(WebDriver driver, String param1, String param2, String input_value,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public String textarea(WebDriver driver, String param1, String param2, String input_value,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void dropdownValues(WebDriver driver, String param1, String param2, String param3, String input_value,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void tableSendKeys(WebDriver driver, String param1, String param2, String param3, String input_value,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void multiplelinestableSendKeys(WebDriver driver, String param1, String param2, String param3,
			String input_value, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void tableDropdownValues(WebDriver driver, String param1, String param2, String input_value,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void clickLinkAction(WebDriver driver, String param1, String param2, String input_value,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void clickCheckbox(WebDriver driver, String param1, String input_value, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void clickRadiobutton(WebDriver driver, String param1, String param2, String input_value,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void selectAValue(WebDriver driver, String param1, String param2, String input_value,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void clickTableLink(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void clickLink(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void clickNotificationLink(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void clickMenu(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void clickImage(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public String clickTableImage(WebDriver driver, String param1, String param2, String input_value,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void clickExpandorcollapse(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void clickButton(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public String getErrorMessages(WebDriver driver) throws Exception;

	public String screenshotFail(WebDriver driver, String string, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void tableRowSelect(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public String screenshot(WebDriver driver, String string, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void clickButtonDropdown(WebDriver driver, String param1, String param2, String input_value,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void mousehover(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void scrollUsingElement(WebDriver driver, String input_parameter, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void moveToElement(WebDriver driver, String input_parameter, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO) throws Exception;

	public void switchToDefaultFrame(WebDriver driver) throws Exception;

	public void switchToFrame(WebDriver driver, String input_parameter, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void selectByText(WebDriver driver, String param1, String param2, String input_value,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void clickFilter(WebDriver driver, String xpath_location, String xpath_location1,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void windowhandle(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception;

	public void dragAnddrop(WebDriver driver, String xpath_location, String xpath_location1,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void copy(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public String copytext(WebDriver driver, String xpath_location, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public String copynumber(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public String copyy(WebDriver driver, String xpath_location, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void clear(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void enter(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void tab(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void uploadFileAutoIT(String field_type, ScriptDetailsDto fetchMetadataVO) throws Exception;

	public void paste(WebDriver driver, String input_parameter, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, String globalValueForSteps2, CustomerProjectDto customerDetails) throws Exception;

	public void switchToParentWindow(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception;

	public void switchDefaultContent(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception;

	public void windowclose(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception;

	public void loginApplication(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String type1, String type2, String type3, String param1, String param2, String param3, String input_value,
			String password, CustomerProjectDto customerDetails) throws Exception;
	
	public void loginSFApplication(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String type1, String type2, String type3, String param1, String param2, String param3,
			String input_value, String password,CustomerProjectDto customerDetails) throws Exception;

	public void logout(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO, String type1,
			String type2, String type3, String param1, String param2, String param3, CustomerProjectDto customerDetails) throws Exception;

	public void openTask(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO, String type1,
			String type2, String param1, String param2, int count, CustomerProjectDto customerDetails) throws Exception;

	public void navigate(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO, String type1,
			String type2, String param1, String param2, int count, CustomerProjectDto customerDetails) throws Exception;

	public void datePicker(WebDriver driver, String param1, String param2, String input_value,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void refreshPage(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails);

	public void navigateToBackPage(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails);

	public void openPdf(WebDriver driver, String input_value, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails);

	public void openFile(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,CustomerProjectDto customerDetails);

	public void actionApprove(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void multipleSendKeys(WebDriver driver, String param1, String param2, String value1, String value2,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void uploadPDF(List<ScriptDetailsDto> fetchMetadataListVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails);

	public void switchParentWindow(WebDriver driver, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails)
			throws Exception;

	public void clickButtonCheckPopup(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void oicLogout(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO, String type1,
			String type2, String type3, String param1, String param2, String param3, CustomerProjectDto customerDetails) throws Exception;

	public String oicLoginPage(WebDriver driver, String param1, String keysToSend, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails);

	public void oicNavigate(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String type1, String type2, String param1, String param2, int count, CustomerProjectDto customerDetails) throws Exception;

	public String oicNavigator(WebDriver driver, String param1, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public String oicMenuNavigation(WebDriver driver, String param1, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public String oicMenuNavigationButton(WebDriver driver, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, String type1, String type2, String param1, String param2, int count, CustomerProjectDto customerDetails)
			throws Exception;

	public void oicClickButton(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public String oicSendValue(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void oicMouseHover(WebDriver driver, String param1, String param2,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public List<String> openExcelFileWithSheet(WebDriver driver, String param1, String param2, String fileName,
			String sheetName, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public String closeExcel() throws Exception;

	public String typeIntoCell(WebDriver driver, String param1, String value1, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, Integer addrowCounter) throws Exception;

	public Integer addRow(Integer addrow) throws Exception;

	public String menuItemOfExcel(WebDriver driver, String param1, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO) throws Exception;

	public String loginToExcel(WebDriver driver, String param1, String param2, String username, String password,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO) throws Exception;

	public void navigateOICUrl(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails)
			throws Exception;

	public void loginOicApplication(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String type1, String type2, String type3, String param1, String param2, String param3, String keysToSend,
			String value, CustomerProjectDto customerDetails) throws Exception;

	public void oicClickMenu(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void loginOicJob(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String type1, String type2, String type3, String param1, String param2, String param3, String keysToSend,
			String value, CustomerProjectDto customerDetails) throws Exception;

	public void loginInformaticaApplication(WebDriver driver, FetchConfigVO fetchConfigVO,
			ScriptDetailsDto fetchMetadataVO, String type1, String type2, String type3, String param1, String param2,
			String param3, String keysToSend, String value, CustomerProjectDto customerDetails) throws Exception;

	public void navigateInformaticaUrl(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails);

	public String InformaticaLoginPage(WebDriver driver, String param1, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,CustomerProjectDto customerDetails);

	public void InformaticaClickButton(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public String InformaticaSendValue(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void InformaticaclickLink(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void InformaticaSelectAValue(WebDriver driver, String param1, String param2, String keysToSend,
			ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void InformaticaClickImage(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

	public void InformaticaLogout(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String type1, String type2, String type3, String param1, String param2, String param3, CustomerProjectDto customerDetails) throws Exception;

	public void loginSSOApplication(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			String type1, String type2, String type3, String param1, String param2, String param3, String input_value,
			String password, CustomerProjectDto customerDetails) throws Exception;

	public void waitTillLoad(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO);

	public String uploadObjectToObjectStore(String sourceFilePath, String destinationFilePath);

	public void compareValue(WebDriver driver, String input_parameter, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, String globalValueForSteps2, CustomerProjectDto customerDetails) throws Exception;

	public void apiAccessToken(ScriptDetailsDto fetchMetadataVO, Map<String, String> accessTokenStorage, CustomerProjectDto customerDetails)
			throws Exception;

	public void apiValidationResponse(ScriptDetailsDto fetchMetadataVO, Map<String, String> accessTokenStorage,
			ApiValidationVO api,CustomerProjectDto customerDetails,FetchConfigVO fetchConfigVO) throws Exception;

	public void createDriverFailedPdf(List<ScriptDetailsDto> fetchMetadataListVO, FetchConfigVO fetchConfigVO,
			String pdffileName, ApiValidationVO api, boolean validationFlag, CustomerProjectDto customerDetails)
			throws IOException, DocumentException, com.lowagie.text.DocumentException;

	public boolean validation(ScriptDetailsDto fetchMetadataVO, ApiValidationVO api) throws Exception;

}
