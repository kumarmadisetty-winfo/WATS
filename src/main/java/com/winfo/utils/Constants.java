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
}
