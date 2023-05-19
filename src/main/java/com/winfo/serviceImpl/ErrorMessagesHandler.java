package com.winfo.serviceImpl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winfo.vo.ScriptDetailsDto;

@Service
public class ErrorMessagesHandler {
	@Autowired
	private DataBaseEntry dataBaseEntry;

	public void getError(String actionName, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String testScriptParamId, String message, String param1, String param2, String password) {
		try {
			fetchMetadataVO.setStatus("Fail");

			param1 = StringUtils.isBlank(param1) ? "" : param1;
			param2 = StringUtils.isBlank(param2) ? "" : param2;

			String errorMessage = "Failed during " + actionName + " action";

			if (actionName.equalsIgnoreCase("clickButton") && message != null) {
				errorMessage = errorMessage + "=> Not able to Click <" + param1 + " " + param2 + "> Value";
				
			} else if (actionName.equalsIgnoreCase("SendKeys") || actionName.equalsIgnoreCase("textarea")
					|| actionName.equalsIgnoreCase("Table SendKeys")
					|| actionName.equalsIgnoreCase("multiplelinestableSendKeys")) {
				errorMessage = errorMessage + " => Not able to enter the value in the <" + param1 + " " + param2 + ">";
			} else if (actionName.equalsIgnoreCase("Dropdown Values")
					|| actionName.equalsIgnoreCase("Table Dropdown Values")
					|| actionName.equalsIgnoreCase("selectByText")) {
				errorMessage = errorMessage + "=>Not able to Select the Value from  <" + param1 + " " + param2
						+ "> dropdownlist";
			} else if (actionName.equalsIgnoreCase("scrollUsingElement")) {
				errorMessage = "Failed at Vertical Scroll =>Not able to Scroll the Value from <" + param1 + " " + param2
						+ ">";

			} else if (actionName.equalsIgnoreCase("tab")) {
				errorMessage = "Failed at tab =>Not able to press tab";

			} else if (actionName.equalsIgnoreCase("windowhandle")) {
				errorMessage = errorMessage + "=>Not able to select the windowhandle from <" + param1 + " " + param2
						+ ">";

			} else if (actionName.equalsIgnoreCase("switchToFrame")) {
				errorMessage = errorMessage + " =>Not able to select the Switch To Frame  Value from <" + param1 + " "
						+ param2 + ">";

			} else if (actionName.equalsIgnoreCase("dragAnddrop")) {
				errorMessage = errorMessage + " =>Not able to select the Drag and Drop  Value from <" + param1 + " "
						+ param2 + ">";
			} else if (actionName.equalsIgnoreCase("enter")) {
				errorMessage = errorMessage + " =>Not able to select the Key - Enter Value from <" + param1 + " "
						+ param2 + ">";

			} else if (actionName.equalsIgnoreCase("doubleclick")) {
				errorMessage = errorMessage + " =>Not able to select the Double Click Value from <" + param1 + " "
						+ param2 + ">";

			} else if (actionName.equalsIgnoreCase("clear")) {
				errorMessage = errorMessage + " =>Not able to select the Clear Value from <" + param1 + " " + param2
						+ ">";

			} else if (actionName.equalsIgnoreCase("windowclose")) {
				errorMessage = errorMessage + " =>Not able to select the Close Window Value from <" + param1 + " "
						+ param2 + ">";

			} else if (actionName.equalsIgnoreCase("refreshPage")) {
				errorMessage = "Failed at Refresh Browser =>Not able to select the Refresh Browser Value from <"
						+ param1 + " " + param2 + ">";

			} else if (actionName.equalsIgnoreCase("switchToDefaultFrame")) {
				errorMessage = "Failed at Switch To Default Frame =>Not able to select the Switch To Default Frame Value from <"
						+ param1 + " " + param2 + ">";

			} else if (actionName.equalsIgnoreCase("paste")) {
				errorMessage = "Failed at Paste Value =>Not able to select the Paste Value Value from <" + param1 + " "
						+ param2 + ">";

			} else if (actionName.equalsIgnoreCase("clickFilter")) {
				errorMessage = "Failed at Click Filter =>Not able to select the Click Filter Value from <" + param1
						+ " " + param2 + ">";

			} else if (actionName.equalsIgnoreCase("clickNotification")) {
				errorMessage = "Failed at Click Notification =>Not able to select the Click Notification Value from <"
						+ param1 + " " + param2 + ">";

			} else if (actionName.equalsIgnoreCase("uploadFileAutoIT")) {
				errorMessage = "Failed at Upload File Auto IT =>Not able to select the Upload File Auto IT Value from <"
						+ param1 + " " + param2 + ">";

			} else if (actionName.equalsIgnoreCase("clickTableText")) {
				errorMessage = "Failed at Click Table Text =>Not able to select the Click Table Text Value from <"
						+ param1 + " " + param2 + ">";

			} else if (actionName.equalsIgnoreCase("Acceptalert")) {
				errorMessage = "Failed at Accept Alert =>Not able to select the Accept Alert Text Value from <" + param1
						+ " " + param2 + ">";

			} else if (actionName.equalsIgnoreCase("clickLink")) {
				errorMessage = "Failed at  Click Link =>Not able to select the Click Link  Value from <" + param1 + " "
						+ param2 + ">";

			} else if (actionName.equalsIgnoreCase("clickImage")) {
				errorMessage = "Failed at  Click Image =>Not able to select the  Click Image Value from <" + param1
						+ " " + param2 + ">";

			} else if (actionName.equalsIgnoreCase("switchParentWindow")) {
				errorMessage = "Failed at Switch Parent Window =>Not able to select the Switch Parent Window  Value from <"
						+ param1 + " " + param2 + ">";

			} else if (actionName.equalsIgnoreCase("clickMenu")) {
				errorMessage = "Failed at Click Menu =>Not able to select the Click Menu Value from <" + param1 + " "
						+ param2 + ">";

			} else if (actionName.equalsIgnoreCase("switchToParentWindow")) {
				errorMessage = "Failed at Switch To Parent Window =>Not able to select the Switch To Parent Window  Value from <"
						+ param1 + " " + param2 + ">";

			} else if (actionName.equalsIgnoreCase("switchDefaultContent")) {
				errorMessage = "Failed at switchDefaultContent =>Not able to select the switchDefaultContent  Value from <"
						+ param1 + " " + param2 + ">";

			} else if (actionName.equalsIgnoreCase("Login into Application")) {
				errorMessage = "Failed at Login into Application =>Please provide valid username and password";

			} else if (actionName.equalsIgnoreCase("selectAValue")) {
				errorMessage = "Failed at Select Value =>Not able to select the Select Value Value from <" + param1
						+ " " + param2 + ">";

			} else if (actionName.equalsIgnoreCase("mousehover")) {
				errorMessage = "Failed at Mouse Hover =>Not able to select the Mouse Hover  Value from <" + param1 + " "
						+ param2 + ">";

			} else if (actionName.equalsIgnoreCase("clickExpandorcollapse")) {
				errorMessage = "Failed at Click Expand or Collapse =>Not able to select the Click Expand or Collapse  Value from <"
						+ param1 + " " + param2 + ">";

			} else if (actionName.equalsIgnoreCase("clickNotificationLink")) {
				errorMessage = "Failed at Click Notification Link =>Not able to select the Click Notification Link  Value from <"
						+ param1 + " " + param2 + ">";

			} else if (actionName.equalsIgnoreCase("clickCheckbox")) {
				errorMessage = "Failed at Click Checkbox =>Not able to select the Click Checkbox  Value from <" + param1
						+ " " + param2 + ">";

			} else if (actionName.equalsIgnoreCase("tableRowSelect")) {
				errorMessage = "Failed at Select Table Row =>Not able to select the Select Table Row  Value from <"
						+ param1 + " " + param2 + ">";

			} else if (actionName.equalsIgnoreCase("clickRadiobutton")) {
				errorMessage = "Failed at Click Radio Button =>Not able to select the Click Radio Button  Value from <"
						+ param1 + " " + param2 + ">";

			} else if (actionName.equalsIgnoreCase("Logout")) {
				errorMessage = "Failed at Logout =>Not able to select the Logout  Value from <" + param1 + " " + param2
						+ ">";

			} else if (actionName.equalsIgnoreCase("clickTableLink")) {
				errorMessage = "Failed at Click Table Link =>Not able to select the Click Table Link  Value from <"
						+ param1 + " " + param2 + ">";
			} else if (actionName.equalsIgnoreCase("compareValue")) {
				errorMessage = "Failed : Expected value is not matching with the Actual value";
			} else {
				errorMessage = errorMessage + "=> please contact with Wats support team.";
			}
			fetchMetadataVO.setLineErrorMsg(errorMessage);
			dataBaseEntry.updateFailedScriptLineStatus(fetchMetadataVO, fetchConfigVO, testScriptParamId, "Fail",
					errorMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
