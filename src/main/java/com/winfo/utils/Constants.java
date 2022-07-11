package com.winfo.utils;

import java.io.File;

public class Constants {

	public enum TEST_SET_LINE_ID_STATUS {
		IN_QUEUE("IN-QUEUE"), STOPPED("STOPPED"), Fail("Fail"), IN_PROGRESS("IN-PROGRESS"), Pass("Pass"), NEW("NEW");

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
		SES("Script Execution Started"),SEE("Script Execution Ended"), SSU("Sent for Status Update"), SU("Status Updated"),
		SUOQ("Status Update Request Put On Queue"),ERG("Evidence Report Generated"),EIP("Error In Producer"),EISU("Error In Status Updation After Script Run");

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
		PASS("Pass"), FAIL("Fail"),  IN_PROGRESS("IN-PROGRESS");

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
	
	public static final String SUCCESS = "SUCCESS";
	public static final String ERROR = "ERROR";
	public static final String WARNING = "WARNING";
	public static final String SYS_USER_HOME_PATH = "user.home";
	public static final String SCREENSHOT = File.separator+"wats"+ File.separator+"Screenshot"+File.separator;
	public static final String PDF = File.separator+"wats"+File.separator+"PDF"+File.separator;
	public static final String EBS = "ebs";
	public static final String SAP_CONCUR = "sap_concur";
}
