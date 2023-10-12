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

	public void getError(String actionName, ScriptDetailsDto fetchMetadataVO, FetchConfigVO fetchConfigVO,
			String testScriptParamId, String message, String param1, String param2, String password) {
		try {
			fetchMetadataVO.setStatus("Fail");
			param1 = StringUtils.isBlank(param1) ? "" : param1;
			param2 = StringUtils.isBlank(param2) ? "" : param2;

			String errorMessage = "Failed during " + actionName + " action";

			 if (actionName.equalsIgnoreCase("clickButton")) {
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) || StringUtils.isNotBlank(message)) {
					    errorMessage = errorMessage + "=>  Not able to click on " + param1 + ">" + param2;
					} else if (StringUtils.isNotBlank(param1) || StringUtils.isNotBlank(message)) {
					    errorMessage = errorMessage + "=>  Not able to click on " + param1;
					} else if (StringUtils.isNotBlank(param2) || StringUtils.isNotBlank(message)) {
					    errorMessage = errorMessage + "=>  Not able to click on " + param2;
					}
					  else {
						 logger.info("In else part");
					  }
				
			} else if (actionName.equalsIgnoreCase("SendKeys") || actionName.equalsIgnoreCase("textarea")
					|| actionName.equalsIgnoreCase("Table SendKeys")
					|| actionName.equalsIgnoreCase("multiplelinestableSendKeys")) {
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to enter the value from  " + param1 + ">" + param2;
					} else if (StringUtils.isNotBlank(param1) ) {
					    errorMessage = errorMessage + "=>  Not able to enter the value from  " + param1;
					} else if (StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to enter the value from " + param2;
					}
					  else {
						 logger.info("In else part");
					  }
			} else if (actionName.equalsIgnoreCase("Dropdown Values")
					|| actionName.equalsIgnoreCase("Table Dropdown Values")
					|| actionName.equalsIgnoreCase("selectByText")) {
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>Not able to select \""+fetchMetadataVO.getInputValue()+"\" value from "+ param1+ ">" +param2;
					} else if (StringUtils.isNotBlank(param1) || StringUtils.isNotBlank(message)) {
					    errorMessage = errorMessage + "=>Not able to select \""+fetchMetadataVO.getInputValue()+"\" value from "+ param1;
					} else if (StringUtils.isNotBlank(param2) || StringUtils.isNotBlank(message)) {
					    errorMessage = errorMessage + "=>Not able to select \""+fetchMetadataVO.getInputValue()+"\" value from "+ param2;
					}
					  else {
						 logger.info("In else part");
					  }
				
			} else if (actionName.equalsIgnoreCase("scrollUsingElement")) {
				
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to Scroll the Value from" + param1 + ">" + param2;
					} else if (StringUtils.isNotBlank(param1) ) {
					    errorMessage = errorMessage + "=>  Not able to Scroll the Value from " + param1;
					} else if (StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to Scroll the Value from" + param2;
					}
					  else {
						 logger.info("In else part");
					  }
			} else if (actionName.equalsIgnoreCase("tab")) {
				errorMessage = "Not able to press tab";

			} else if (actionName.equalsIgnoreCase("windowhandle")) {
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the windowhandle from " + param1 + ">" + param2;
					} else if (StringUtils.isNotBlank(param1) ) {
					    errorMessage = errorMessage + "=> Not able to select the windowhandle from " + param1;
					} else if (StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the windowhandle from " + param2;
					}
					  else {
						 logger.info("In else part");
					  }

			} else if (actionName.equalsIgnoreCase("switchToFrame")) {
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Switch To Frame  Value from " + param1 + ">" + param2;
					} else if (StringUtils.isNotBlank(param1) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Switch To Frame  Value from " + param1;
					} else if (StringUtils.isNotBlank(param2)) {
					    errorMessage = errorMessage + "=>  Not able to select the Switch To Frame  Value from " + param2;
					}
					  else {
						 logger.info("In else part");
					  }

			} else if (actionName.equalsIgnoreCase("dragAnddrop")) {
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Drag and Drop  Value from " + param1 + ">" + param2;
					} else if (StringUtils.isNotBlank(param1) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Drag and Drop  Value from " + param1;
					} else if (StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Drag and Drop  Value from " + param2;
					}
					  else {
						 logger.info("In else part");
					  }
			} else if (actionName.equalsIgnoreCase("enter")) {
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2)) {
					    errorMessage = errorMessage + "=>  Not able to select the Key - Enter Value from " + param1 + ">" + param2;
					} else if (StringUtils.isNotBlank(param1) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Key - Enter Value from " + param1;
					} else if (StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Key - Enter Value from " + param2;
					}
					  else {
						 logger.info("In else part");
					  }

			} else if (actionName.equalsIgnoreCase("doubleclick")) {
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Double Click Value from " + param1 + ">" + param2;
					} else if (StringUtils.isNotBlank(param1) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Double Click Value from " + param1;
					} else if (StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Double Click Value from " + param2;
					}
					  else {
						 logger.info("In else part");
					  }

			} else if (actionName.equalsIgnoreCase("clear")) {
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Clear Value from " + param1 + ">" + param2;
					} else if (StringUtils.isNotBlank(param1) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Clear Value from " + param1;
					} else if (StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Clear Value from" + param2;
					}
					  else {
						 logger.info("In else part");
					  }
						

			} else if (actionName.equalsIgnoreCase("windowclose")) {
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Close Window Value from " + param1 + ">" + param2;
					} else if (StringUtils.isNotBlank(param1) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Close Window Value from " + param1;
					} else if (StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Close Window Value from " + param2;
					}
					  else {
						 logger.info("In else part");
					  }

			} else if (actionName.equalsIgnoreCase("refreshPage")) {
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Refresh Browser Value from " + param1 + ">" + param2;
					} else if (StringUtils.isNotBlank(param1)) {
					    errorMessage = errorMessage + "=>  Not able to select the Refresh Browser Value from " + param1;
					} else if (StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Refresh Browser Value from " + param2;
					}
					  else {
						 logger.info("In else part");
					  }

			} else if (actionName.equalsIgnoreCase("switchToDefaultFrame")) {
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Switch To Default Frame Value from" + param1 + ">" + param2;
					} else if (StringUtils.isNotBlank(param1) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Switch To Default Frame Value from " + param1;
					} else if (StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Switch To Default Frame Value from " + param2;
					}
					  else {
						 logger.info("In else part");
					  }

			} else if (actionName.equalsIgnoreCase("paste")) {
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Paste Value Value from " + param1 + ">" + param2;
					} else if (StringUtils.isNotBlank(param1) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Paste Value Value from " + param1;
					} else if (StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Paste Value Value from " + param2;
					}
					  else {
						 logger.info("In else part");
					  }

			} else if (actionName.equalsIgnoreCase("clickFilter")) {
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Click Filter Value from " + param1 + ">" + param2;
					} else if (StringUtils.isNotBlank(param1) ) {
					    errorMessage = errorMessage + "=> Not able to select the Click Filter Value from" + param1;
					} else if (StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Click Filter Value from " + param2;
					}
					  else {
						 logger.info("In else part");
					  }

			} else if (actionName.equalsIgnoreCase("clickNotification")) {
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Click Notification Value from " + param1 + ">" + param2;
					} else if (StringUtils.isNotBlank(param1) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Click Notification Value from " + param1;
					} else if (StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=> Not able to select the Click Notification Value from" + param2;
					}
					  else {
						 logger.info("In else part");
					  }

			} else if (actionName.equalsIgnoreCase("uploadFileAutoIT")) {
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Upload File Auto IT Value from" + param1 + ">" + param2;
					} else if (StringUtils.isNotBlank(param1) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Upload File Auto IT Value from " + param1;
					} else if (StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Upload File Auto IT Value from" + param2;
					}
					  else {
						 logger.info("In else part");
					  }

			} else if (actionName.equalsIgnoreCase("clickTableText")) {
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Click Table Text Value from " + param1 + ">" + param2;
					} else if (StringUtils.isNotBlank(param1) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Click Table Text Value from " + param1;
					} else if (StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Click Table Text Value from " + param2;
					}
					  else {
						 logger.info("In else part");
					  }

			} else if (actionName.equalsIgnoreCase("Acceptalert")) {
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Accept Alert Text Value from " + param1 + ">" + param2;
					} else if (StringUtils.isNotBlank(param1) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Accept Alert Text Value from" + param1;
					} else if (StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Accept Alert Text Value from " + param2;
					}
					  else {
						 logger.info("In else part");
					  }
			} else if (actionName.equalsIgnoreCase("clickLink")) {
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=> Not able to select the Click Link  Value from " + param1 + ">" + param2;
					} else if (StringUtils.isNotBlank(param1) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Click Link  Value from " + param1;
					} else if (StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=> Not able to select the Click Link  Value from " + param2;
					}
					  else {
						 logger.info("In else part");
					  }

			} else if (actionName.equalsIgnoreCase("clickImage")) {
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the  Click Image Value from " + param1 + ">" + param2;
					} else if (StringUtils.isNotBlank(param1) ) {
					    errorMessage = errorMessage + "=>  Not able to select the  Click Image Value from " + param1;
					} else if (StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the  Click Image Value from " + param2;
					}
					  else {
						 logger.info("In else part");
					  }
			} else if (actionName.equalsIgnoreCase("switchParentWindow")) {
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Switch Parent Window  Value from " + param1 + ">" + param2;
					} else if (StringUtils.isNotBlank(param1) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Switch Parent Window  Value from " + param1;
					} else if (StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Switch Parent Window  Value from " + param2;
					}
					  else {
						 logger.info("In else part");
					  }

			} else if (actionName.equalsIgnoreCase("clickMenu")) {
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=> Not able to select the Click Menu Value from" + param1 + ">" + param2;
					} else if (StringUtils.isNotBlank(param1) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Click Menu Value from" + param1;
					} else if (StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Click Menu Value from " + param2;
					}
					  else {
						 logger.info("In else part");
					  }

			} else if (actionName.equalsIgnoreCase("switchToParentWindow")) {
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Switch To Parent Window  Value from " + param1 + ">" + param2;
					} else if (StringUtils.isNotBlank(param1) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Switch To Parent Window  Value from " + param1;
					} else if (StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Switch To Parent Window  Value from " + param2;
					}
					  else {
						 logger.info("In else part");
					  }

			} else if (actionName.equalsIgnoreCase("switchDefaultContent")) {
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the switchDefaultContent  Value from " + param1 + ">" + param2;
					} else if (StringUtils.isNotBlank(param1) ) {
					    errorMessage = errorMessage + "=> Not able to select the switchDefaultContent  Value from " + param1;
					} else if (StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the switchDefaultContent  Value from " + param2;
					}
					  else {
						 logger.info("In else part");
					  }

			} else if (actionName.equalsIgnoreCase("Login into Application")) {
				errorMessage = "Failed at Login into Application =>Please provide valid username and password";

			} else if (actionName.equalsIgnoreCase("selectAValue")) {
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=> Not able to select the Select Value Value from " + param1 + ">" + param2;
					} else if (StringUtils.isNotBlank(param1) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Select Value Value from " + param1;
					} else if (StringUtils.isNotBlank(param2)) {
					    errorMessage = errorMessage + "=>  Not able to select the Select Value Value from " + param2;
					}
					  else {
						 logger.info("In else part");
					  }
			} else if (actionName.equalsIgnoreCase("mousehover")) {
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=> Not able to select the Mouse Hover  Value from " + param1 + ">" + param2;
					} else if (StringUtils.isNotBlank(param1) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Mouse Hover  Value from " + param1;
					} else if (StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Mouse Hover  Value from " + param2;
					}
					  else {
						 logger.info("In else part");
					  }

			} else if (actionName.equalsIgnoreCase("clickExpandorcollapse")) {
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Click Expand or Collapse  Value from  " + param1 + ">" + param2;
					} else if (StringUtils.isNotBlank(param1) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Click Expand or Collapse  Value from  " + param1;
					} else if (StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Click Expand or Collapse  Value from  " + param2;
					}
					  else {
						 logger.info("In else part");
					  } 

			} else if (actionName.equalsIgnoreCase("clickNotificationLink")) {
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Click Notification Link  Value from " + param1 + ">" + param2;
					} else if (StringUtils.isNotBlank(param1) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Click Notification Link  Value from" + param1;
					} else if (StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Click Notification Link  Value from " + param2;
					}
					  else {
						 logger.info("In else part");
					  }

			} else if (actionName.equalsIgnoreCase("clickCheckbox")) {
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Click Checkbox  Value from" + param1 + ">" + param2;
					} else if (StringUtils.isNotBlank(param1) ) {
					    errorMessage = errorMessage + "=> Not able to select the Click Checkbox  Value from " + param1;
					} else if (StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Click Checkbox  Value from " + param2;
					}
					  else {
						 logger.info("In else part");
					  }

			} else if (actionName.equalsIgnoreCase("tableRowSelect")) {
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=> Not able to select the Select Table Row  Value from" + param1 + ">" + param2;
					} else if (StringUtils.isNotBlank(param1) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Select Table Row  Value from " + param1;
					} else if (StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=> Not able to select the Select Table Row  Value from " + param2;
					}
					  else {
						 logger.info("In else part");
					  }

			} else if (actionName.equalsIgnoreCase("clickRadiobutton")) {
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Click Radio Button  Value from  " + param1 + ">" + param2;
					} else if (StringUtils.isNotBlank(param1) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Click Radio Button  Value from  " + param1;
					} else if (StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Click Radio Button  Value from  " + param2;
					}
					  else {
						 logger.info("In else part");
					  }

			} else if (actionName.equalsIgnoreCase("Logout")) {
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=> Not able to select the Logout  Value from " + param1 + ">" + param2;
					} else if (StringUtils.isNotBlank(param1) ) {
					    errorMessage = errorMessage + "=> Not able to select the Logout  Value from " + param1;
					} else if (StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=> Not able to select the Logout  Value from  " + param2;
					}
					  else {
						 logger.info("In else part");
					  }
						

			} else if (actionName.equalsIgnoreCase("clickTableLink")) {
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=> Not able to select the Click Table Link  Value from " + param1 + ">" + param2;
					} else if (StringUtils.isNotBlank(param1) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Click Table Link  Value from " + param1;
					} else if (StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to select the Click Table Link  Value from " + param2;
					}
					  else {
						 logger.info("In else part");
					  }
			} else if (actionName.equalsIgnoreCase("compareValue")) {
				errorMessage = "Failed : Expected value is not matching with the Actual value";
				
			} else if(actionName.equalsIgnoreCase("Navigate")) {
				 if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to Navigate " + param1 + ">" + param2;
					} else if (StringUtils.isNotBlank(param1) ) {
					    errorMessage = errorMessage + "=> Not able to Navigate " + param1;
					} else if (StringUtils.isNotBlank(param2) ) {
					    errorMessage = errorMessage + "=>  Not able to Navigate " + param2;
					}
					  else {
						 logger.info("In else part");
					  }
			} else if(actionName.equalsIgnoreCase("openTask")) {
				
				 if (StringUtils.isNotBlank(param1) ) {
					    errorMessage = errorMessage + "=>  Not able to open " + param1;
				 }
					  else {
						 logger.info("In else part");
					  }
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
