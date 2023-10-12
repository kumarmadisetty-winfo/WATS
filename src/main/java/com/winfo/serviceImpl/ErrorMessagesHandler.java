package com.winfo.serviceImpl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winfo.repository.TestSetScriptParamRepository;
import com.winfo.utils.Constants;
import com.winfo.vo.FetchConfigVO;
import com.winfo.vo.ScriptDetailsDto;

@Service
public class ErrorMessagesHandler {
	public final Logger logger = LogManager.getLogger(ErrorMessagesHandler.class);
	@Autowired
	private TestSetScriptParamRepository testSetScriptParamRepository;

	private String prepareErrorMessage(String param1, String param2,String error){
		String errorMessage = "";
		if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ) {
			errorMessage = errorMessage + error + param1 + ">" + param2;
		} else if (StringUtils.isNotBlank(param1) ) {
			errorMessage = errorMessage + error + param1;
		} else if (StringUtils.isNotBlank(param2) ) {
			errorMessage = errorMessage + error + param2;
		}
		else {
			logger.info("In else part");
		}
		return errorMessage;
	}

	public void getError(String actionName, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String testScriptParamId, String message, String param1, String param2, String password) {
		try {
			fetchMetadataVO.setStatus("Fail");
			param1 = StringUtils.isBlank(param1) ? "" : param1;
			param2 = StringUtils.isBlank(param2) ? "" : param2;

			String errorMessage = "Failed during " + actionName + " action";

			 if (actionName.equalsIgnoreCase("clickButton")) {
				 errorMessage =errorMessage+ prepareErrorMessage(param1,param2, "=>  Not able to click on ");

				
			} else if (actionName.equalsIgnoreCase("SendKeys") || actionName.equalsIgnoreCase("textarea")
					|| actionName.equalsIgnoreCase("Table SendKeys")
					|| actionName.equalsIgnoreCase("multiplelinestableSendKeys")) {
				 errorMessage =errorMessage+ prepareErrorMessage(param1,param2, "=>  Not able to enter the value from ");
				 
			} else if (actionName.equalsIgnoreCase("Dropdown Values")
					|| actionName.equalsIgnoreCase("Table Dropdown Values")
					|| actionName.equalsIgnoreCase("selectByText")) {
				 errorMessage =errorMessage+ prepareErrorMessage(param1,param2, "=> Not able to select \""+fetchMetadataVO.getInputValue()+"\" value from ");
				
			} else if (actionName.equalsIgnoreCase("scrollUsingElement")) {
				errorMessage =errorMessage+ prepareErrorMessage(param1,param2, "=>  Not able to Scroll the Value from ");
				 
			} else if (actionName.equalsIgnoreCase("tab")) {
				errorMessage = "Not able to press tab";

			} else if (actionName.equalsIgnoreCase("windowhandle")) {
				errorMessage =errorMessage+ prepareErrorMessage(param1,param2, "=>  Not able to select the windowhandle from ");

			} else if (actionName.equalsIgnoreCase("switchToFrame")) {
				errorMessage =errorMessage+ prepareErrorMessage(param1,param2, "=>  Not able to select the Switch To Frame  Value from ");

			} else if (actionName.equalsIgnoreCase("dragAnddrop")) {
				errorMessage = errorMessage+prepareErrorMessage(param1,param2, "=>  Not able to select the Drag and Drop  Value from ");
					  
			} else if (actionName.equalsIgnoreCase("enter")) {
				errorMessage = errorMessage+prepareErrorMessage(param1,param2, "=>  Not able to select the Key - Enter Value from "); 

			} else if (actionName.equalsIgnoreCase("doubleclick")) {
				errorMessage =errorMessage+ prepareErrorMessage(param1,param2, "=>  Not able to select the Double Click Value from "); 

			} else if (actionName.equalsIgnoreCase("clear")) {
				errorMessage = errorMessage+prepareErrorMessage(param1,param2, "=>  Not able to select the Clear Value from "); 	
						

			} else if (actionName.equalsIgnoreCase("windowclose")) {
				errorMessage =errorMessage+ prepareErrorMessage(param1,param2, "=>  Not able to select the Close Window Value from "); 

			} else if (actionName.equalsIgnoreCase("refreshPage")) {
				errorMessage =errorMessage+ prepareErrorMessage(param1,param2, "=>  Not able to select the Refresh Browser Value from "); 

			} else if (actionName.equalsIgnoreCase("switchToDefaultFrame")) {
				errorMessage =errorMessage+ prepareErrorMessage(param1,param2, "=>  Not able to select the Switch To Default Frame Value from "); 

			} else if (actionName.equalsIgnoreCase("paste")) {
				errorMessage =errorMessage+ prepareErrorMessage(param1,param2, "=>  Not able to select the Paste Value Value from "); 

			} else if (actionName.equalsIgnoreCase("clickFilter")) {
				errorMessage =errorMessage+ prepareErrorMessage(param1,param2, "=>  Not able to select the Click Filter Value from "); 	

			} else if (actionName.equalsIgnoreCase("clickNotification")) {
				errorMessage =errorMessage+ prepareErrorMessage(param1,param2, "=> Not able to select the Click Notification Value from "); 	

			} else if (actionName.equalsIgnoreCase("uploadFileAutoIT")) {
				errorMessage =errorMessage+ prepareErrorMessage(param1,param2, "=> Not able to select the Upload File Auto IT Value from "); 	

			} else if (actionName.equalsIgnoreCase("clickTableText")) {
				errorMessage =errorMessage+ prepareErrorMessage(param1,param2, "=> Not able to select the Click Table Text Value from "); 	 

			} else if (actionName.equalsIgnoreCase("Acceptalert")) {
				errorMessage =errorMessage+ prepareErrorMessage(param1,param2, "=> Not able to select the Accept Alert Text Value from "); 
				
			} else if (actionName.equalsIgnoreCase("clickLink")) {
				errorMessage =errorMessage+ prepareErrorMessage(param1,param2, "=> Not able to select the Click Link  Value from "); 	

			} else if (actionName.equalsIgnoreCase("clickImage")) {
				errorMessage =errorMessage+ prepareErrorMessage(param1,param2, "=> Not able to select the  Click Image Value from "); 
				
			} else if (actionName.equalsIgnoreCase("switchParentWindow")) {
				errorMessage =errorMessage+ prepareErrorMessage(param1,param2, "=> Not able to select the Switch Parent Window  Value from "); 

			} else if (actionName.equalsIgnoreCase("clickMenu")) {
				errorMessage =errorMessage+ prepareErrorMessage(param1,param2, "=> Not able to select the Click Menu Value from "); 

			} else if (actionName.equalsIgnoreCase("switchToParentWindow")) {
				errorMessage =errorMessage+ prepareErrorMessage(param1,param2, "=> Not able to select the Switch To Parent Window  Value from ");

			} else if (actionName.equalsIgnoreCase("switchDefaultContent")) {
				errorMessage =errorMessage+ prepareErrorMessage(param1,param2, "=> Not able to select the switchDefaultContent  Value from ");

			} else if (actionName.equalsIgnoreCase("Login into Application")) {
				errorMessage = "Failed at Login into Application =>Please provide valid username and password";

			} else if (actionName.equalsIgnoreCase("selectAValue")) {
				errorMessage =errorMessage+ prepareErrorMessage(param1,param2, "=> Not able to select the Select Value Value from ");
				
			} else if (actionName.equalsIgnoreCase("mousehover")) {
				errorMessage =errorMessage+ prepareErrorMessage(param1,param2, "=> Not able to select the Mouse Hover  Value from ");

			} else if (actionName.equalsIgnoreCase("clickExpandorcollapse")) {
				errorMessage =errorMessage+ prepareErrorMessage(param1,param2, "=> Not able to select the Click Expand or Collapse  Value from");

			} else if (actionName.equalsIgnoreCase("clickNotificationLink")) {
				errorMessage =errorMessage+ prepareErrorMessage(param1,param2, "=> Not able to select the Click Notification Link  Value from");

			} else if (actionName.equalsIgnoreCase("clickCheckbox")) {
				errorMessage =errorMessage+ prepareErrorMessage(param1,param2, "=> Not able to select the Click Checkbox  Value from");

			} else if (actionName.equalsIgnoreCase("tableRowSelect")) {
				errorMessage =errorMessage+ prepareErrorMessage(param1,param2, "=> Not able to select the Select Table Row  Value from");

			} else if (actionName.equalsIgnoreCase("clickRadiobutton")) {
				errorMessage =errorMessage+ prepareErrorMessage(param1,param2, "=> Not able to select the Click Radio Button  Value from");

			} else if (actionName.equalsIgnoreCase("Logout")) {
				errorMessage =errorMessage+ prepareErrorMessage(param1,param2, "Failed at Logout => Not able to select the Logout  Value from");
						

			} else if (actionName.equalsIgnoreCase("clickTableLink")) {
				errorMessage =errorMessage+ prepareErrorMessage(param1,param2, "=> Not able to select the Click Table Link  Value from");
			} else if (actionName.equalsIgnoreCase("compareValue")) {
				errorMessage = "Failed : Expected value is not matching with the Actual value";
				
			} else if(actionName.equalsIgnoreCase("Navigate")) {
				errorMessage =errorMessage+ prepareErrorMessage(param1,param2, "=>Not able to Navigate");
				
			} else if(actionName.equalsIgnoreCase("openTask")) {
				errorMessage =errorMessage+ prepareErrorMessage(param1,param2, "=> Not able to open");
			}
			else {
				errorMessage = errorMessage + "=> please contact with WinfoTest support team.";
			}
			fetchMetadataVO.setLineErrorMsg(errorMessage);
			testSetScriptParamRepository.updateTestSetScriptParamEndTime(Constants.FAIL,new Date(),errorMessage,Integer.parseInt(testScriptParamId));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
