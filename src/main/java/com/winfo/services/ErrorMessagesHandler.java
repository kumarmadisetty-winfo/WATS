package com.winfo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class ErrorMessagesHandler {
	@Autowired
	private DataBaseEntry dataBaseEntry;

	public void getError(String actionName, FetchMetadataVO fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String test_script_param_id, String message, String param1, String param2, String password) {
		try {
			String error_message = "Took more than 10 seconds to load the page";
			if (actionName.equalsIgnoreCase("clickButton") && message != null) {
				// String error_message="Took more than 10 seconds to load the page";
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						message);
			} else if (actionName.equalsIgnoreCase("SendKeys")) {
				error_message = "Failed at Enter Value - Text Field => Not able to enter the value in " + param1
						+ " and " + param2;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);
			} else if (actionName.equalsIgnoreCase("textarea")) {
				error_message = "Failed at Enter Value - Text Area => Not able to enter the value in " + param1
						+ " and " + param2;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);
			} else if (actionName.equalsIgnoreCase("Table SendKeys")) {
				error_message = "Failed at Table SendKeys => Not able to enter the value in " + param1 + " and "
						+ param2;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("multiplelinestableSendKeys")) {
				error_message = "Failed at multiplelinestableSendKeys => Not able to enter the value in " + param1
						+ " and " + param2;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("Dropdown Values")) {
				error_message = "Failed at Dropdown Values =>Not able to Select the Value from " + param2
						+ " dropdownlist";
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);
			} else if (actionName.equalsIgnoreCase("Table Dropdown Values")) {
				error_message = "Failed at Table Dropdown Values =>Not able to Select the Value from " + param2
						+ " dropdownlist";
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("selectByText")) {
				error_message = "Failed at Select Dropdown Value =>Not able to Select the Value from " + param2
						+ " dropdownlist";
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("scrollUsingElement")) {
				error_message = "Failed at Vertical Scroll =>Not able to Scroll the Value from " + param1 + "and"
						+ param2;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("tab")) {
				error_message = "Failed at tab =>Not able to tab the Value from " + param1;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("windowhandle")) {
				error_message = "Failed at windowhandle =>Not able to select the windowhandle  Value from " + param1;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("switchToFrame")) {
				error_message = "Failed at Switch To Frame =>Not able to select the Switch To Frame  Value from "
						+ param1;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("dragAnddrop")) {
				error_message = "Failed at Drag and Drop =>Not able to select the Drag and Drop  Value from " + param1;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("enter")) {
				error_message = "Failed at Key - Enter =>Not able to select the Key - Enter Value from " + param1;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("doubleclick")) {
				error_message = "Failed at Double Click =>Not able to select the Double Click Value from " + param1;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("clear")) {
				error_message = "Failed at Clear =>Not able to select the Clear Value from " + param1;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("windowclose")) {
				error_message = "Failed at Close Window =>Not able to select the Close Window Value from " + param1;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("refreshPage")) {
				error_message = "Failed at Refresh Browser =>Not able to select the Refresh Browser Value from "
						+ param1;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("switchToDefaultFrame")) {
				error_message = "Failed at Switch To Default Frame =>Not able to select the Switch To Default Frame Value from "
						+ param1;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("paste")) {
				error_message = "Failed at Paste Value =>Not able to select the Paste Value Value from " + param1;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("clickFilter")) {
				error_message = "Failed at Click Filter =>Not able to select the Click Filter Value from " + param1;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("clickNotification")) {
				error_message = "Failed at Click Notification =>Not able to select the Click Notification Value from "
						+ param1;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("uploadFileAutoIT")) {
				error_message = "Failed at Upload File Auto IT =>Not able to select the Upload File Auto IT Value from "
						+ param1;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("clickTableText")) {
				error_message = "Failed at Click Table Text =>Not able to select the Click Table Text Value from "
						+ param1;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("Acceptalert")) {
				error_message = "Failed at Accept Alert =>Not able to select the Accept Alert Text Value from "
						+ param1;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("clickLink")) {
				error_message = "Failed at  Click Link =>Not able to select the Click Link  Value from " + param1;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("clickImage")) {
				error_message = "Failed at  Click Image =>Not able to select the  Click Image Value from " + param1;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("switchParentWindow")) {
				error_message = "Failed at Switch Parent Window =>Not able to select the Switch Parent Window  Value from "
						+ param1;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("clickMenu")) {
				error_message = "Failed at Click Menu =>Not able to select the Click Menu Value from " + param1;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("switchToParentWindow")) {
				error_message = "Failed at Switch To Parent Window =>Not able to select the Switch To Parent Window  Value from "
						+ param1;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("switchDefaultContent")) {
				error_message = "Failed at switchDefaultContent =>Not able to select the switchDefaultContent  Value from "
						+ param1;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("Login into Application")) {
				error_message = "Failed at Login into Application =>Please provide valid username and password";
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("selectAValue")) {
				error_message = "Failed at Select Value =>Not able to select the Select Value Value from " + param1;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("mousehover")) {
				error_message = "Failed at Mouse Hover =>Not able to select the Mouse Hover  Value from " + param1;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("clickExpandorcollapse")) {
				error_message = "Failed at Click Expand or Collapse =>Not able to select the Click Expand or Collapse  Value from "
						+ param1;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("clickNotificationLink")) {
				error_message = "Failed at Click Notification Link =>Not able to select the Click Notification Link  Value from "
						+ param1;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("clickCheckbox")) {
				error_message = "Failed at Click Checkbox =>Not able to select the Click Checkbox  Value from "
						+ param1;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("tableRowSelect")) {
				error_message = "Failed at Select Table Row =>Not able to select the Select Table Row  Value from "
						+ param1;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("clickRadiobutton")) {
				error_message = "Failed at Click Radio Button =>Not able to select the Click Radio Button  Value from "
						+ param1;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("Logout")) {
				error_message = "Failed at Logout =>Not able to select the Logout  Value from " + param1;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			} else if (actionName.equalsIgnoreCase("clickTableLink")) {
				error_message = "Failed at Click Table Link =>Not able to select the Click Table Link  Value from "
						+ param1;
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);

			}else if (actionName.equalsIgnoreCase("compareValue")) {
				error_message = "Failed : Expected value is not matching with the Actual value";
				fetchConfigVO.setErrormessage(error_message);
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);
			}
			else {
				dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, test_script_param_id, "Fail",
						error_message);
				// new changes-error_message added to else block
				fetchConfigVO.setErrormessage(error_message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
