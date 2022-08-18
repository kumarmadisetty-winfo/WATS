package com.winfo.services;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ErrorMessagesHandler {
	@Autowired
	private DataBaseEntry dataBaseEntry;

	public void getError(String actionName, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String test_script_param_id, String message, String param1, String param2, String password) {
		try {
			fetchMetadataVO.setStatus("Fail");

			param1 = StringUtils.isBlank(param1) ? "" : "=> Not able to enter the value in " + param1;
			param2 = StringUtils.isBlank(param2) ? "" : " and " + param2;

			String errorMessage = "Failed during " + actionName + " action";

			errorMessage = errorMessage + param1 + param2;

			if (actionName.equalsIgnoreCase("clickButton") && message != null) {
				// String errorMessage="Took more than 10 seconds to load the page";
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);
			} else if (actionName.equalsIgnoreCase("SendKeys")) {
				errorMessage = "Failed at Enter Value - Text Field => Not able to enter the value in " + param1
						+ " and " + param2;

				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);
			} else if (actionName.equalsIgnoreCase("textarea")) {
				errorMessage = "Failed at Enter Value - Text Area => Not able to enter the value in " + param1
						+ " and " + param2;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);
			} else if (actionName.equalsIgnoreCase("Table SendKeys")) {
				errorMessage = "Failed at Table SendKeys => Not able to enter the value in " + param1 + " and "
						+ param2;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("Table SendKeys")) {
				errorMessage = "Failed at Table SendKeys => Not able to enter the value in " + param1 + " and "
						+ param2;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("multiplelinestableSendKeys")) {
				errorMessage = "Failed at multiplelinestableSendKeys => Not able to enter the value in " + param1
						+ " and " + param2;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("Dropdown Values")) {
				errorMessage = "Failed at Dropdown Values =>Not able to Select the Value from " + param2
						+ " dropdownlist";
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);
			} else if (actionName.equalsIgnoreCase("Table Dropdown Values")) {
				errorMessage = "Failed at Table Dropdown Values =>Not able to Select the Value from " + param2
						+ " dropdownlist";
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("selectByText")) {
				errorMessage = "Failed at Select Dropdown Value =>Not able to Select the Value from " + param2
						+ " dropdownlist";
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("scrollUsingElement")) {
				errorMessage = "Failed at Vertical Scroll =>Not able to Scroll the Value from " + param1 + "and"
						+ param2;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("tab")) {
				errorMessage = "Failed at tab =>Not able to tab the Value from " + param1;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("windowhandle")) {
				errorMessage = "Failed at windowhandle =>Not able to select the windowhandle  Value from " + param1;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("switchToFrame")) {
				errorMessage = "Failed at Switch To Frame =>Not able to select the Switch To Frame  Value from "
						+ param1;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("dragAnddrop")) {
				errorMessage = "Failed at Drag and Drop =>Not able to select the Drag and Drop  Value from " + param1;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("enter")) {
				errorMessage = "Failed at Key - Enter =>Not able to select the Key - Enter Value from " + param1;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("doubleclick")) {
				errorMessage = "Failed at Double Click =>Not able to select the Double Click Value from " + param1;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("clear")) {
				errorMessage = "Failed at Clear =>Not able to select the Clear Value from " + param1;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("windowclose")) {
				errorMessage = "Failed at Close Window =>Not able to select the Close Window Value from " + param1;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("refreshPage")) {
				errorMessage = "Failed at Refresh Browser =>Not able to select the Refresh Browser Value from "
						+ param1;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("switchToDefaultFrame")) {
				errorMessage = "Failed at Switch To Default Frame =>Not able to select the Switch To Default Frame Value from "
						+ param1;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("paste")) {
				errorMessage = "Failed at Paste Value =>Not able to select the Paste Value Value from " + param1;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("clickFilter")) {
				errorMessage = "Failed at Click Filter =>Not able to select the Click Filter Value from " + param1;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("clickNotification")) {
				errorMessage = "Failed at Click Notification =>Not able to select the Click Notification Value from "
						+ param1;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("uploadFileAutoIT")) {
				errorMessage = "Failed at Upload File Auto IT =>Not able to select the Upload File Auto IT Value from "
						+ param1;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("clickTableText")) {
				errorMessage = "Failed at Click Table Text =>Not able to select the Click Table Text Value from "
						+ param1;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("Acceptalert")) {
				errorMessage = "Failed at Accept Alert =>Not able to select the Accept Alert Text Value from "
						+ param1;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("clickLink")) {
				errorMessage = "Failed at  Click Link =>Not able to select the Click Link  Value from " + param1;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("clickImage")) {
				errorMessage = "Failed at  Click Image =>Not able to select the  Click Image Value from " + param1;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("switchParentWindow")) {
				errorMessage = "Failed at Switch Parent Window =>Not able to select the Switch Parent Window  Value from "
						+ param1;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("clickMenu")) {
				errorMessage = "Failed at Click Menu =>Not able to select the Click Menu Value from " + param1;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("switchToParentWindow")) {
				errorMessage = "Failed at Switch To Parent Window =>Not able to select the Switch To Parent Window  Value from "
						+ param1;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("switchDefaultContent")) {
				errorMessage = "Failed at switchDefaultContent =>Not able to select the switchDefaultContent  Value from "
						+ param1;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("Login into Application")) {
				errorMessage = "Failed at Login into Application =>Please provide valid username and password";
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("selectAValue")) {
				errorMessage = "Failed at Select Value =>Not able to select the Select Value Value from " + param1;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("mousehover")) {
				errorMessage = "Failed at Mouse Hover =>Not able to select the Mouse Hover  Value from " + param1;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("clickExpandorcollapse")) {
				errorMessage = "Failed at Click Expand or Collapse =>Not able to select the Click Expand or Collapse  Value from "
						+ param1;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("clickNotificationLink")) {
				errorMessage = "Failed at Click Notification Link =>Not able to select the Click Notification Link  Value from "
						+ param1;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("clickCheckbox")) {
				errorMessage = "Failed at Click Checkbox =>Not able to select the Click Checkbox  Value from "
						+ param1;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("tableRowSelect")) {
				errorMessage = "Failed at Select Table Row =>Not able to select the Select Table Row  Value from "
						+ param1;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("clickRadiobutton")) {
				errorMessage = "Failed at Click Radio Button =>Not able to select the Click Radio Button  Value from "
						+ param1;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("Logout")) {
				errorMessage = "Failed at Logout =>Not able to select the Logout  Value from " + param1;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);

			} else if (actionName.equalsIgnoreCase("clickTableLink")) {
				errorMessage = "Failed at Click Table Link =>Not able to select the Click Table Link  Value from "
						+ param1;
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);
			}else if (actionName.equalsIgnoreCase("compareValue")) {
				errorMessage = "Failed : Expected value is not matching with the Actual value";
				fetchConfigVO.setErrormessage(errorMessage);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);
			}
			else {
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						errorMessage);
				// new changes-errorMessage added to else block
				fetchConfigVO.setErrormessage(errorMessage);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
