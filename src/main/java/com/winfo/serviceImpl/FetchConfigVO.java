package com.winfo.serviceImpl;

import java.time.Duration;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    
    
    
   	public String getSMARTBEAR_ENABLED() {
		return SMARTBEAR_ENABLED;
	}

	public void setSMARTBEAR_ENABLED(String sMARTBEAR_ENABLED) {
		SMARTBEAR_ENABLED = sMARTBEAR_ENABLED;
	}

	public String getSMARTBEAR_PROJECT_NAME() {
		return SMARTBEAR_PROJECT_NAME;
	}

	public void setSMARTBEAR_PROJECT_NAME(String sMARTBEAR_PROJECT_NAME) {
		SMARTBEAR_PROJECT_NAME = sMARTBEAR_PROJECT_NAME;
	}

	public String getSMARTBEAR_CUSTOM_COLUMN_NAME() {
		return SMARTBEAR_CUSTOM_COLUMN_NAME;
	}

	public void setSMARTBEAR_CUSTOM_COLUMN_NAME(String sMARTBEAR_CUSTOM_COLUMN_NAME) {
		SMARTBEAR_CUSTOM_COLUMN_NAME = sMARTBEAR_CUSTOM_COLUMN_NAME;
	}

	public String getMANAGEMENT_TOOL_ENABLED() {
		return MANAGEMENT_TOOL_ENABLED;
	}

	public void setMANAGEMENT_TOOL_ENABLED(String mANAGEMENT_TOOL_ENABLED) {
		MANAGEMENT_TOOL_ENABLED = mANAGEMENT_TOOL_ENABLED;
	}

	public String getSctiptId() {
		return sctiptId;
	}

	public void setSctiptId(String sctiptId) {
		this.sctiptId = sctiptId;
	}

	public String getScriptIssueId() {
		return scriptIssueId;
	}

	public void setScriptIssueId(String scriptIssueId) {
		this.scriptIssueId = scriptIssueId;
	}

	public String getTestRunIssueId() {
		return testRunIssueId;
	}

	public void setTestRunIssueId(String testRunIssueId) {
		this.testRunIssueId = testRunIssueId;
	}

    private String EXCEL_DOWNLOAD_FILE_PATH;

    private String JIRA_ISSUE_UPDATE_STATUS_URL;
    
    private String JIRA_ISSUE_UPDATE_TRANSITIONS;
    
   	public String getExcelDownloadFilePath() {
		return EXCEL_DOWNLOAD_FILE_PATH;
	}

	public void setExcelDownloadFilePath(String excelDownloadFilePath) {
		EXCEL_DOWNLOAD_FILE_PATH = excelDownloadFilePath;
	}
	
   	public String getJiraIssueUpdateStatusURL() {
		return JIRA_ISSUE_UPDATE_STATUS_URL;
	}

	public void setJiraIssueUpdateStatusURL(String jiraIssueUpdateStatusURL) {
		JIRA_ISSUE_UPDATE_STATUS_URL = jiraIssueUpdateStatusURL;
	}

	public String getJiraIssueUpdateTransitions() {
		return JIRA_ISSUE_UPDATE_TRANSITIONS;
	}

	public void setJiraIssueUpdateTransitions(String jiraIssueUpdateTransitions) {
		JIRA_ISSUE_UPDATE_TRANSITIONS = jiraIssueUpdateTransitions;
	}

	public String getAPI_AUTHENTICATION_URL() {
		return API_AUTHENTICATION_URL;
	}

	public void setAPI_AUTHENTICATION_URL(String aPI_AUTHENTICATION_URL) {
		API_AUTHENTICATION_URL = aPI_AUTHENTICATION_URL;
	}

	public String getAPI_AUTHENTICATION_CODE() {
		return API_AUTHENTICATION_CODE;
	}

	public void setAPI_AUTHENTICATION_CODE(String aPI_AUTHENTICATION_CODE) {
		API_AUTHENTICATION_CODE = aPI_AUTHENTICATION_CODE;
	}
    
   	public int getOtherCount() {
		return otherCount;
	}

	public void setOtherCount(int otherCount) {
		this.otherCount = otherCount;
	}

	public String getWINDOWS_PDF_LOCATION() {
		return WINDOWS_PDF_LOCATION;
	}

	public void setWINDOWS_PDF_LOCATION(String wINDOWS_PDF_LOCATION) {
		WINDOWS_PDF_LOCATION = wINDOWS_PDF_LOCATION;
	}

	public String getWINDOWS_SCREENSHOT_LOCATION() {
		return WINDOWS_SCREENSHOT_LOCATION;
	}

	public void setWINDOWS_SCREENSHOT_LOCATION(String wINDOWS_SCREENSHOT_LOCATION) {
		WINDOWS_SCREENSHOT_LOCATION = wINDOWS_SCREENSHOT_LOCATION;
	}

	public String getEBS_APPLICATION_NAME() {
		return EBS_APPLICATION_NAME;
	}

	public void setEBS_APPLICATION_NAME(String eBS_APPLICATION_NAME) {
		EBS_APPLICATION_NAME = eBS_APPLICATION_NAME;
	}

	public List<Object[]> getSeqNumAndStatus() {
		return seqNumAndStatus;
	}

	public void setSeqNumAndStatus(List<Object[]> seqNumAndStatus) {
		this.seqNumAndStatus = seqNumAndStatus;
	}

	public String getSharePoint_URL() {
		return SharePoint_URL;
	}

	public void setSharePoint_URL(String sharePoint_URL) {
		SharePoint_URL = sharePoint_URL;
	}

	public String getSite_Name() {
		return Site_Name;
	}

	public void setSite_Name(String site_Name) {
		Site_Name = site_Name;
	}

	public String getSharePoint_Library_Name() {
		return SharePoint_Library_Name;
	}

	public void setSharePoint_Library_Name(String sharePoint_Library_Name) {
		SharePoint_Library_Name = sharePoint_Library_Name;
	}

	public String getDirectory_Name() {
		return Directory_Name;
	}

	public void setDirectory_Name(String directory_Name) {
		Directory_Name = directory_Name;
	}
	
	public String getOIC_JOB_SCHEDULER() {
   		return OIC_JOB_SCHEDULER;
   	}

   	public void setOIC_JOB_SCHEDULER(String oIC_JOB_SCHEDULER) {
   		OIC_JOB_SCHEDULER = oIC_JOB_SCHEDULER;
   	}

    
	public String getPDF_LOCATION() {
		return PDF_LOCATION;
	}

	public void setPDF_LOCATION(String pDF_LOCATION) {
		PDF_LOCATION = pDF_LOCATION;
	}

	public String getAPI_BASE_URL() {
		return API_BASE_URL;
	}

	public void setAPI_BASE_URL(String aPI_BASE_URL) {
		API_BASE_URL = aPI_BASE_URL;
	}

	public String getAPI_PASSWORD() {
		return API_PASSWORD;
	}

	public void setAPI_PASSWORD(String aPI_PASSWORD) {
		API_PASSWORD = aPI_PASSWORD;
	}

	public String getAPI_USERNAME() {
		return API_USERNAME;
	}

	public void setAPI_USERNAME(String aPI_USERNAME) {
		API_USERNAME = aPI_USERNAME;
	}

	public String getENABLE_GET_MANAGER() {
		return ENABLE_GET_MANAGER;
	}

	public void setENABLE_GET_MANAGER(String eNABLE_GET_MANAGER) {
		ENABLE_GET_MANAGER = eNABLE_GET_MANAGER;
	}

	public String getLong_WAIT() {
		return long_WAIT;
	}

	public void setLong_WAIT(String long_WAIT) {
		this.long_WAIT = long_WAIT;
	}


	public String getSHAREPOINT_USERNAME() {
		return SHAREPOINT_USERNAME;
	}

	public void setSHAREPOINT_USERNAME(String sHAREPOINT_USERNAME) {
		SHAREPOINT_USERNAME = sHAREPOINT_USERNAME;
	}

	public String getSHAREPOINT_PASSWORD() {
		return SHAREPOINT_PASSWORD;
	}

	public void setSHAREPOINT_PASSWORD(String sHAREPOINT_PASSWORD) {
		SHAREPOINT_PASSWORD = sHAREPOINT_PASSWORD;
	}


	public String getJIRA_ISSUE_URL() {
		return JIRA_ISSUE_URL;
	}

	public void setJIRA_ISSUE_URL(String jIRA_ISSUE_URL) {
		JIRA_ISSUE_URL = jIRA_ISSUE_URL;
	}

	public String getDB_PASSWORD() {
		return DB_PASSWORD;
	}

	public void setDB_PASSWORD(String dB_PASSWORD) {
		DB_PASSWORD = dB_PASSWORD;
	}


public int getMax_num_scripts() {
		return MAX_NUM_SCRIPTS;
	}

	public void setMax_num_scripts(int max_num_scripts) {
		this.MAX_NUM_SCRIPTS = max_num_scripts;
	}

	public String getInstance_name() {
		return INSTANCE_NAME;
	}

	public void setInstance_name(String instance_name) {
		this.INSTANCE_NAME = instance_name;
	}

	public String getErrormessage() {
		return errormessage;
	}
	public void setErrormessage(String errormessage) {
		this.errormessage = errormessage;
	}
	public Date getStarttime1() {
		return Starttime1;
	}
	public void setStarttime1(Date starttime1) {
		Starttime1 = starttime1;
	}
	public Date getStarttime() {
		return starttime;
	}
	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	public Date getEndtime() {
		return endtime;
	}
	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}
	public int getPasscount() {
		return passcount;
	}
	public void setPasscount(int passcount) {
		this.passcount = passcount;
	}
	public int getFailcount() {
		return failcount;
	}
	public void setFailcount(int failcount) {
		this.failcount = failcount;
	}
	public String getStatus1() {
		return status1;
	}
	public void setStatus1(String status1) {
		this.status1 = status1;
	}

	public String getSharepoint_item_id() {
		return SHAREPOINT_ITEM_ID;
	}
	public void setSharepoint_item_id(String sharepoint_item_id) {
		this.SHAREPOINT_ITEM_ID = sharepoint_item_id;
	}
	public String getClient_id() {
		return CLIENT_ID;
	}
	public void setClient_id(String client_id) {
		this.CLIENT_ID = client_id;
	}
	public String getClient_secret() {
		return CLIENT_SECRET;
	}
	public void setClient_secret(String client_secret) {
		this.CLIENT_SECRET = client_secret;
	}
	public String getTenant_id() {
		return TENANT_ID;
	}
	public void setTenant_id(String tenant_id) {
		this.TENANT_ID = tenant_id;
	}
	public String getScreenshot_path() {
		return SCREENSHOT_PATH;
	}
	public void setScreenshot_path(String screenshot_path) {
		this.SCREENSHOT_PATH = screenshot_path;
	}
	public String getPdf_path() {
		return PDF_PATH;
	}
	public void setPdf_path(String pdf_path) {
		this.PDF_PATH = pdf_path;
	}
	public int getParallel_independent() {
		return PARALLEL_INDEPENDENT;
	}
	public void setParallel_independent(int parallel_independent) {
		this.PARALLEL_INDEPENDENT = parallel_independent;
	}
	public int getParallel_dependent() {
		return PARALLEL_DEPENDENT;
	}
	public void setParallel_dependent(int parallel_dependent) {
		this.PARALLEL_DEPENDENT = parallel_dependent;
	}
	public String getLong_wait() {
		return long_WAIT;
	}
	public void setLong_wait(String long_wait) {
		this.long_WAIT = long_wait;
	}
	public String getImg_url() {
		return IMG_URL;
	}
	public void setImg_url(String img_url) {
		this.IMG_URL = img_url;
	}
	public String getSharepoint_resp() {
		return SHAREPOINT_RESP;
	}
	public void setSharepoint_resp(String sharepoint_resp) {
		this.SHAREPOINT_RESP = sharepoint_resp;
	}
	public String getShort_wait() {
		return SHORT_WAIT;
	}
	public void setShort_wait(String short_wait) {
		this.SHORT_WAIT = short_wait;
	}
	public String getMedium_wait() {
		return MEDIUM_WAIT;
	}
	public void setMedium_wait(String medium_wait) {
		this.MEDIUM_WAIT = medium_wait;
	}
	public String getBrowser() {
		return BROWSER;
	}
	public void setBrowser(String browser) {
		this.BROWSER = browser;
	}
	public Duration getWait_time() {
	    return Duration.ofSeconds(WAIT_TIME);
	}
	public void setWait_time(Duration wait_time) {
	    this.WAIT_TIME = (int) wait_time.getSeconds();
	}
	public String getApplication_url() {
		return APPLICATION_URL;
	}
	public void setApplication_url(String application_url) {
		this.APPLICATION_URL = application_url;
	}
	
	public String getEBS_URL() {
		return EBS_URL;
	}

	public void setEBS_URL(String eBS_URL) {
		EBS_URL = eBS_URL;
	}

	public String getSAP_CONCUR_URL() {
		return SAP_CONCUR_URL;
	}

	public void setSAP_CONCUR_URL(String sAP_CONCUR_URL) {
		SAP_CONCUR_URL = sAP_CONCUR_URL;
	}

	public String getPassword() {
		return PASSWORD;
	}
	public void setPassword(String password) {
		this.PASSWORD = password;
	}
	public String getTest_evidence_repository_url() {
		return test_evidence_repository_url;
	}
	public void setTest_evidence_repository_url(String test_evidence_repository_url) {
		this.test_evidence_repository_url = test_evidence_repository_url;
	}
	public String getChrome_driver_path() {
		return CHROME_DRIVER_PATH;
	}
	public void setChrome_driver_path(String chrome_driver_path) {
		this.CHROME_DRIVER_PATH = chrome_driver_path;
	}
	public String getEdge_driver_path() {
		return EDGE_DRIVER_PATH;
	}
	public void setEdge_driver_path(String edge_driver_path) {
		this.EDGE_DRIVER_PATH = edge_driver_path;
	}
	public String getFirefox_driver_path() {
		return FIREFOX_DRIVER_PATH;
	}
	public void setFirefox_driver_path(String firefox_driver_path) {
		this.FIREFOX_DRIVER_PATH = firefox_driver_path;
	}
	public String getDownlod_file_path() {
		return DOWNLOD_FILE_PATH;
	}
	public void setDownlod_file_path(String downlod_file_path) {
		this.DOWNLOD_FILE_PATH = downlod_file_path;
	}
	public String getUpload_file_path() {
		return UPLOAD_FILE_PATH;
	}
	public void setUpload_file_path(String upload_file_path) {
		this.UPLOAD_FILE_PATH = upload_file_path;
	}
	public String getUri_config() {
		return URI_CONFIG;
	}
	public void setUri_config(String uri_config) {
		this.URI_CONFIG = uri_config;
	}
	public String getUri_test_scripts() {
		return URI_TEST_SCRIPTS;
	}
	public void setUri_test_scripts(String uri_test_scripts) {
		this.URI_TEST_SCRIPTS = uri_test_scripts;
	}
	public String getUri_results() {
		return URI_RESULTS;
	}
	public void setUri_results(String uri_results) {
		this.URI_RESULTS = uri_results;
	}

	public String getSHAREPOINT_SITE_ID() {
		return SHAREPOINT_SITE_ID;
	}

	public void setSHAREPOINT_SITE_ID(String sHAREPOINT_SITE_ID) {
		SHAREPOINT_SITE_ID = sHAREPOINT_SITE_ID;
	}

	public String getOIC_APPLICATION_URL() {
		return OIC_APPLICATION_URL;
	}

	public void setOIC_APPLICATION_URL(String oIC_APPLICATION_URL) {
		OIC_APPLICATION_URL = oIC_APPLICATION_URL;
	}

	public String getINFORMATICA_APPLICATION_URL() {
		return INFORMATICA_APPLICATION_URL;
	}

	public void setINFORMATICA_APPLICATION_URL(String iNFORMATICA_APPLICATION_URL) {
		INFORMATICA_APPLICATION_URL = iNFORMATICA_APPLICATION_URL;
	}
	
	public String getSF_APPLICATION_URL() {
		return SF_APPLICATION_URL;
	}

	public void setSF_APPLICATION_URL(String sF_APPLICATION_URL) {
		SF_APPLICATION_URL = sF_APPLICATION_URL;
	}
	
}