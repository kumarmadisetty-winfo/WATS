package com.winfo.utils;

import java.io.File;

public class Constants {

	public enum TEST_SET_LINE_ID_STATUS {
		IN_QUEUE("IN-QUEUE"), STOPPED("STOPPED"), FAIL("Fail"), IN_PROGRESS("IN-PROGRESS"), PASS("Pass"), NEW("NEW");

		public final String label;

		private TEST_SET_LINE_ID_STATUS(String label) {
			this.label = label;
		}

		public String getLabel() {
			return this.label;
		}
	}

	public enum AUDIT_TRAIL_STAGES {
		RR("Received Request"), SGC("Script Generation Completed"), SQ("Script Queued"), CP("Consumer Picked"),
		SES("Script Execution Started"), SEE("Script Execution Ended"), SSU("Sent for Status Update"),
		SU("Status Updated"), SUOQ("Status Update Request Put On Queue"), ERG("Evidence Report Generated"),
		EIP("Error In Producer"), EISU("Error In Status Updation After Script Run"), DSF("Dependant Script Failed"),
		DF("Driver Failed"),EES("Excel Execution Started"),SEF("Script Execution Failed");

		public final String label;

		private AUDIT_TRAIL_STAGES(String label) {
			this.label = label;
		}

		public String getLabel() {
			return this.label;
		}
	}

	public enum SCRIPT_PARAM_STATUS {
		PASS("Pass"), FAIL("Fail"), NEW("New"), IN_PROGRESS("In-Progress");

		public final String label;

		private SCRIPT_PARAM_STATUS(String label) {
			this.label = label;
		}

		public String getLabel() {
			return this.label;
		}
	}

	public enum UPDATE_STATUS {
		PASS("Pass"), FAIL("Fail"), IN_PROGRESS("IN-PROGRESS");

		public final String label;

		private UPDATE_STATUS(String label) {
			this.label = label;
		}

		public String getLabel() {
			return this.label;
		}
	}

	public enum BOOLEAN_STATUS {
		TRUE("Y"), FALSE("N");

		public final String label;

		private BOOLEAN_STATUS(String label) {
			this.label = label;
		}

		public String getLabel() {
			return this.label;
		}
	}
	
	public enum smartBear {
		WOOD("WOOD"), YES("YES");

		public final String label;

		private smartBear(String label) {
			this.label = label;
		}

		public String getLabel() {
			return this.label;
		}
	}


	public static String addQuotes(String string) {
		return "\"\"\"" + string + "\"\"\"";
	}


	public static final String CONFLICT = "CONFLICT";
	public static final String SUCCESS = "SUCCESS";
	public static final int SUCCESS_STATUS = 200;
	public static final String ERROR = "ERROR";
	public static final String WARNING = "WARNING";
	public static final String SYS_USER_HOME_PATH = "user.home";
	public static final String SCREENSHOT = File.separator + "wats" + File.separator + "Screenshot" + File.separator;
	public static final String PDF = File.separator + "wats" + File.separator + "PDF" + File.separator;
	public static final String EBS = "ebs";
	public static final String SAP_CONCUR = "sap_concur";
	public static final String WATS_CENTRAL = "WATS_CENTRAL";
	public static final String ERR_MSG_FOR_SCRIPT_RUN = "System could not run the script. Try to re-execute. If it continues to fail, please contact WATS Support Team";
	public static final String ERR_MSG_FOR_DEP_FAILED = "Dependent Script Failed";
	public static final String SPLIT = "@";
	public static final String SCRIPT_UPDATE_MSG = "Scripts added Successfully";
	public static final String PRODUCT_VERSION_ERROR =  "%s product version is missing from the script(s) %s.";
	public static final String INVALID_PRODUCT_VERSION =  "Invalid product version for test run %s.";
	public static final String INVALID_SCRIPT_MSG = "Script could not be added. Please contact wats support team.";
	public static final String PASSED = "passed";
	public static final String GENERATING = "generating";
	public static final String VALIDATION_TYPE_REGULAR_EXPR="REGULAR_EXPR";
	public static final String VALIDATION_DATATYPE_DATE="Date";
	public static final String MM_dd_yy="MM/dd/yy";
	public static final String Look_Up_Name="TARGET CLIENT";
	public static final String CONFFIG_ERROR="Configuration not found";
	public static final String PROJECT_ERROR="Project not found";
	public static final String CUSTOMER_ERROR="Customer not found";
	public static final String CUSTOM_SCRIPT="Custom";
	public static final String SCRIPT_HEAL="Script Heal";
	public static final String PASS="Pass";
	public static final String FAIL="Fail";
	public static final String ADDEDNUM="ADDEDNUM";
	public static final String INPROGRESS="IN-PROGRESS";
	public static final String COMPLETED="COMPLETED";
	public static final String NEW="NEW";
	public static final String YET_TO_START="Yet To Start";
	public static final String TYPE="Testrun";
	public static final String PDF_PATH="PDF_PATH";
	public static final String API_VALIDATION="API_VALIDATION";
	public static final String REGULAR_EXPRESSION="REGULAR_EXPR";
	public static final String API_BASE_URL="API_BASE_URL";
	public static final String API_USERNAME="API_USERNAME";
	public static final String API_PASSWORD="API_PASSWORD";
	public static final String VALIDATION_SUCCESS="Validation Success";
	public static final String VALIDATION_FAIL="Validation Failed";
	public static final String No_VALIDATION="No Validation";
	public static final String MANDATORY="Mandatory";
	public static final String BOTH="Both";
	public static final String NA="NA";
	public static final String GET_USER_ID="Get UserId";
	public static final String INVALID_TEST_SET_ID="Invalid Test Run";
	public static final String INVALID_TEST_SET_LINE_ID="Invalid Script";
	public static final String INVALID_JOB_ID="Invalid Schedule";
	public static final String INVALID_CREDENTIALS_CONFIG="Invalid credentails in the configuration";
	public static final String INVALID_CREDENTIALS_CONFIG_MESSAGE="Not able to Validate. Please check the credentials in the configuration";
	public static final String INVALID_CREDENTIALS_AND_API_BASE_URL_CONFIG_MESSAGE="Not able to Validate. Please check the API_BASE_URL and the credentials in the configuration";
	public static final String INVALID_API_BASE_URL_CONFIG_MESSAGE="Not able to Validate. Please check the API_BASE_URL in the configuration";
	public static final String ORACLE_SERVICE_UNAVAILABLE="Not able to Validate. Oracle service is unavailable at this moment";
	public static final String INVALID_INPUT_DATA="Given Input Data is not valid";
	public static final String NO_RESPOSNE_EXTERNAL_API="No response received from the external API";
	public static final String INVALID_RESPOSNE_EXTERNAL_API="Invalid response received from the external API";
	public static final String INTERNAL_SERVER_ERROR="Internal server error. Please contact to the administrator";
	public static final String ORACLE_CLIENT_ERROR="Oracle client side error: ";
	public static final String ORACLE_SERVER_ERROR="Oracle server side error: ";
	public static final String NO_VALIDATION_MESSAGE="No Validation added to the script";
	public static final String VALIDATED_SUCCESSFULLY="Validated successfully";
	public static final String LOOKUPCODE_NOT_FOUND="LookUpCode not found of ValidationName";
	public static final String INPUT_VALUE_MANDATORY="Input Value is mandatory for this step";
	public static final String FAILED_TO_INITIATE_THE_DRIVER="Failed to initiate the driver";
	public static final String FAILED_TO_RUN_THE_SCRIPT="Failed to run the script";
	public static final String TEST_RUN_FETCH_ERROR="Error occurred while fetching Test Runs";
	public static final String COFIG_CREDENTIALS_FETCH_ERROR="Error occurred while fetching the credentials from the configuration";
	public static final String NO_TEST_RUN_IN_SCHEDULE="No Test Runs present in the schedule";
	public static final String ACTION="ACTION";
	public static final String IP_VALIDATIONS="IP_VALIDATIONS";
	public static final String UNIQUE_MANDATORY="UNIQUE_MANDATORY";
	public static final String DATATYPES="DATATYPES";
	public static final String NO_RESPOSNE_APEX_API="No response received from the APEX API";
	public static final String INVALID_RESPOSNE_APEX_API="Invalid response received from the APEX API";
	public static final String DELETE_SCHEDULE_TEST_RUN_ENDPOINT="deleteScheduleTemplate";
	public static final String FORWARD_SLASH="/";
	public static final String WATS_SERVICE="WATSservice";
	public static final String JOSN_PARSING_ERROR="Error occurred while parsing JSON";
	public static final String TEST_RUN="testRun";
	public static final String ERROR_MESSAGE="errorMessage";
	public static final String SINGLE_QUOTE="'";
	public static final String COLON=":";
	public static final String OPEN_CURLY_BRACES="{";
	public static final String CLOSE_CURLY_BRACES="}";
	public static final String QUMA=",";
	public static final String SCHEDULE_TEST_RUN_NAME_RESPONSE_STRING=OPEN_CURLY_BRACES+SINGLE_QUOTE+TEST_RUN+SINGLE_QUOTE+COLON+SINGLE_QUOTE;
	public static final String SCHEDULE_TEST_RUN_ERROR_RESPONSE_STRING=SINGLE_QUOTE+QUMA+SINGLE_QUOTE+ERROR_MESSAGE+SINGLE_QUOTE+COLON+SINGLE_QUOTE;
	
	
	}
