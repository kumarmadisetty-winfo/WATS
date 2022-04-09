package com.winfo.utils;

public class Constants {

	public enum TEST_SET_LINE_ID_STATUS {
		 STOOPED("STOOPED"), New("New"), IN_QUEUE("IN-QUEUE"), STOPPED("STOPPED"), Fail("Fail"),
		IN_PROGRESS("IN-PROGRESS"), Pass("Pass"), NEW("NEW");

		public final String label;

		private TEST_SET_LINE_ID_STATUS(String label) {
			this.label = label;
		}
		
		 public String getLabel() {
		      return this.label;
		   }
	}

	public enum SCRIPT_PARAM_STATUS {
		PASS("Pass"), FAIL("Fail");

		public final String label;

		private SCRIPT_PARAM_STATUS(String label) {
			this.label = label;
		}
	}
	

}
