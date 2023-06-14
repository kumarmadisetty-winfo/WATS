package com.winfo.vo;

import java.time.Duration;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FetchConfigVO {
	
	

	private String OIC_APPLICATION_URL;
	
	private String SF_APPLICATION_URL;
	
	private String SHAREPOINT_SITE_ID; 
	
	private String INFORMATICA_APPLICATION_URL;
	
	private String BROWSER;
	
	private Integer WAIT_TIME;

	private String APPLICATION_URL;
	
	private String EBS_URL;
	
	private String SAP_CONCUR_URL;

	private String CHROME_DRIVER_PATH;
	
	private String EDGE_DRIVER_PATH;
	
	private String FIREFOX_DRIVER_PATH;
	
	private String DOWNLOD_FILE_PATH;
	
	private String UPLOAD_FILE_PATH;
	
	private String URI_TEST_SCRIPTS;
	
	private String SHORT_WAIT;
	
	private String API_BASE_URL;
	
	private String API_PASSWORD;
	
	private String API_USERNAME;
	
	private String ENABLE_GET_MANAGER;
	
	private String MEDIUM_WAIT;
	
	private String long_WAIT;
	
	private String IMG_URL;
	
	private String SHAREPOINT_RESP;
	
	 private String SHAREPOINT_USERNAME;
	 
	 private String SHAREPOINT_PASSWORD;
	 
	private int PARALLEL_INDEPENDENT;
	
	private int PARALLEL_DEPENDENT;
	
//	@JsonProperty("BROWSER")
	private String SCREENSHOT_PATH;
	
//	@JsonProperty("BROWSER")
	private String PDF_PATH;
	
	private String JIRA_ISSUE_URL;
	
	private String DB_PASSWORD;
	
//	@JsonProperty("BROWSER")
	private String SHAREPOINT_ITEM_ID;
	
//	@JsonProperty("BROWSER")
	private String CLIENT_ID;
	
//	@JsonProperty("BROWSER")
	private String CLIENT_SECRET;
	
//	@JsonProperty("BROWSER")
	private String TENANT_ID;
	
//	@JsonProperty("BROWSER")
	private String INSTANCE_NAME;
	
//	@JsonProperty("BROWSER")
	private int MAX_NUM_SCRIPTS;
	
//	@JsonProperty("BROWSER")
	private String PASSWORD;
	
//	@JsonProperty("BROWSER")
	private String URI_CONFIG;
	
//	@JsonProperty("BROWSER")
	private String URI_RESULTS;
	
//	@JsonProperty("BROWSER")
	private String test_evidence_repository_url;
	
//	@JsonProperty("BROWSER")
	private Date starttime;
	
//	@JsonProperty("BROWSER")
	private Date Starttime1;
	
//	@JsonProperty("BROWSER")
	private Date endtime;
	
//	@JsonProperty("BROWSER")
	private int passcount;
	
//	@JsonProperty("BROWSER")
	private int failcount;
	
//	@JsonProperty("BROWSER")
	private String status1;
	
//	@JsonProperty("BROWSER")
	private String errormessage;
	
    private String PDF_LOCATION;
    
    private String OIC_JOB_SCHEDULER;
    
    @JsonProperty("SharePoint_URL")
    private String SharePoint_URL;
    
    @JsonProperty("Site Name")
    private String Site_Name;
    
    @JsonProperty("SharePoint Library Name")
    private String SharePoint_Library_Name;
    
    @JsonProperty("Directory Name")
    private String Directory_Name;
    
    private String WINDOWS_PDF_LOCATION;
    private String WINDOWS_SCREENSHOT_LOCATION;
    
    private String EBS_APPLICATION_NAME;
    
    private List<Object[]> seqNumAndStatus; 
    
    private int otherCount;
    
    private String API_AUTHENTICATION_URL;
    
    private String API_AUTHENTICATION_CODE;
 
    private String testRunIssueId;
    
    private String scriptIssueId;
    
    private String sctiptId;
    
    private String MANAGEMENT_TOOL_ENABLED;
    
    private String SMARTBEAR_ENABLED;
    
    private String SMARTBEAR_PROJECT_NAME;
    
    private String SMARTBEAR_CUSTOM_COLUMN_NAME;

    private String EXCEL_DOWNLOAD_FILE_PATH;

    private String JIRA_ISSUE_UPDATE_STATUS_URL;
    
    private String JIRA_ISSUE_UPDATE_TRANSITIONS;
   
    public Duration getWait_time() {
	    return Duration.ofSeconds(WAIT_TIME);
	}
	public void setWait_time(Duration wait_time) {
	    this.WAIT_TIME = (int) wait_time.getSeconds();
	}

}