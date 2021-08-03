package com.winfo.services;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FetchConfigVO {
	
	private String BROWSER;
	
	private Integer WAIT_TIME;
	
	private String APPLICATION_URL;
	
	private String APPLICATION_USER_NAME;

	private String CHROME_DRIVER_PATH;
	
	private String IE_DRIVER_PATH;
	
	private String FIREFOX_DRIVER_PATH;
	
	private String DOWNLOD_FILE_PATH;
	
	private String UPLOAD_FILE_PATH;
	
	private String URI_TEST_SCRIPTS;
	
	private String SHORT_WAIT;
	
	private String API_BASE_URL;
	
	private String API_PASSWORD;
	
	private String API_USERNAME;
	
	private String ENABLE_GET_MANAGER;
	
	private String METADATA_URL;
	
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
	
//	@JsonProperty("BROWSER")
	private String DB_HOST;
	
//	@JsonProperty("BROWSER")
	private String DB_USERNAME;
	
	private String DB_PASSWORD;
//	@JsonProperty("BROWSER")
	private String SHAREPOINT_DRIVE_ID;
	
//	@JsonProperty("BROWSER")
	private String SHAREPOINT_ITEM_ID;
	
//	@JsonProperty("BROWSER")
	private String CLIENT_ID;
	
//	@JsonProperty("BROWSER")
	private String CLIENT_SECRET;
	
//	@JsonProperty("BROWSER")
	private String TENANT_ID;
	
//	@JsonProperty("BROWSER")
//changed the name from Enable_video to enable_video
	private String ENABLE_VIDEO;
	
//	@JsonProperty("BROWSER")
	private String INSTANCE_NAME;
	
//	@JsonProperty("BROWSER")
	private int MAX_NUM_SCRIPTS;
	
//	@JsonProperty("BROWSER")
	private String START_DATE;
	
	private String SHAREPOINT_PATH;
	
//	@JsonProperty("BROWSER")
	private String END_DATE;
	
	private int LOOP_COUNT;
	
	private int SCRIPT_TO_WAIT;
	
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
	
	
public String getBROWSER() {
		return BROWSER;
	}

	public void setBROWSER(String bROWSER) {
		BROWSER = bROWSER;
	}

	public Integer getWAIT_TIME() {
		return WAIT_TIME;
	}

	public void setWAIT_TIME(Integer wAIT_TIME) {
		WAIT_TIME = wAIT_TIME;
	}

	public String getAPPLICATION_URL() {
		return APPLICATION_URL;
	}

	public void setAPPLICATION_URL(String aPPLICATION_URL) {
		APPLICATION_URL = aPPLICATION_URL;
	}

	public String getAPPLICATION_USER_NAME() {
		return APPLICATION_USER_NAME;
	}

	public void setAPPLICATION_USER_NAME(String aPPLICATION_USER_NAME) {
		APPLICATION_USER_NAME = aPPLICATION_USER_NAME;
	}

	public String getCHROME_DRIVER_PATH() {
		return CHROME_DRIVER_PATH;
	}

	public void setCHROME_DRIVER_PATH(String cHROME_DRIVER_PATH) {
		CHROME_DRIVER_PATH = cHROME_DRIVER_PATH;
	}

	public String getIE_DRIVER_PATH() {
		return IE_DRIVER_PATH;
	}

	public void setIE_DRIVER_PATH(String iE_DRIVER_PATH) {
		IE_DRIVER_PATH = iE_DRIVER_PATH;
	}

	public String getFIREFOX_DRIVER_PATH() {
		return FIREFOX_DRIVER_PATH;
	}

	public void setFIREFOX_DRIVER_PATH(String fIREFOX_DRIVER_PATH) {
		FIREFOX_DRIVER_PATH = fIREFOX_DRIVER_PATH;
	}

	public String getDOWNLOD_FILE_PATH() {
		return DOWNLOD_FILE_PATH;
	}

	public void setDOWNLOD_FILE_PATH(String dOWNLOD_FILE_PATH) {
		DOWNLOD_FILE_PATH = dOWNLOD_FILE_PATH;
	}

	public String getUPLOAD_FILE_PATH() {
		return UPLOAD_FILE_PATH;
	}

	public void setUPLOAD_FILE_PATH(String uPLOAD_FILE_PATH) {
		UPLOAD_FILE_PATH = uPLOAD_FILE_PATH;
	}

	public String getURI_TEST_SCRIPTS() {
		return URI_TEST_SCRIPTS;
	}

	public void setURI_TEST_SCRIPTS(String uRI_TEST_SCRIPTS) {
		URI_TEST_SCRIPTS = uRI_TEST_SCRIPTS;
	}

	public String getSHORT_WAIT() {
		return SHORT_WAIT;
	}

	public void setSHORT_WAIT(String sHORT_WAIT) {
		SHORT_WAIT = sHORT_WAIT;
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

	public String getMETADATA_URL() {
		return METADATA_URL;
	}

	public void setMETADATA_URL(String mETADATA_URL) {
		METADATA_URL = mETADATA_URL;
	}

	public String getMEDIUM_WAIT() {
		return MEDIUM_WAIT;
	}

	public void setMEDIUM_WAIT(String mEDIUM_WAIT) {
		MEDIUM_WAIT = mEDIUM_WAIT;
	}

	public String getLong_WAIT() {
		return long_WAIT;
	}

	public void setLong_WAIT(String long_WAIT) {
		this.long_WAIT = long_WAIT;
	}

	public String getIMG_URL() {
		return IMG_URL;
	}

	public void setIMG_URL(String iMG_URL) {
		IMG_URL = iMG_URL;
	}

	public String getSHAREPOINT_RESP() {
		return SHAREPOINT_RESP;
	}

	public void setSHAREPOINT_RESP(String sHAREPOINT_RESP) {
		SHAREPOINT_RESP = sHAREPOINT_RESP;
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

	public int getPARALLEL_INDEPENDENT() {
		return PARALLEL_INDEPENDENT;
	}

	public void setPARALLEL_INDEPENDENT(int pARALLEL_INDEPENDENT) {
		PARALLEL_INDEPENDENT = pARALLEL_INDEPENDENT;
	}

	public int getPARALLEL_DEPENDENT() {
		return PARALLEL_DEPENDENT;
	}

	public void setPARALLEL_DEPENDENT(int pARALLEL_DEPENDENT) {
		PARALLEL_DEPENDENT = pARALLEL_DEPENDENT;
	}

	public String getSCREENSHOT_PATH() {
		return SCREENSHOT_PATH;
	}

	public void setSCREENSHOT_PATH(String sCREENSHOT_PATH) {
		SCREENSHOT_PATH = sCREENSHOT_PATH;
	}

	public String getPDF_PATH() {
		return PDF_PATH;
	}

	public void setPDF_PATH(String pDF_PATH) {
		PDF_PATH = pDF_PATH;
	}

	public String getJIRA_ISSUE_URL() {
		return JIRA_ISSUE_URL;
	}

	public void setJIRA_ISSUE_URL(String jIRA_ISSUE_URL) {
		JIRA_ISSUE_URL = jIRA_ISSUE_URL;
	}

	public String getDB_HOST() {
		return DB_HOST;
	}

	public void setDB_HOST(String dB_HOST) {
		DB_HOST = dB_HOST;
	}

	public String getDB_USERNAME() {
		return DB_USERNAME;
	}

	public void setDB_USERNAME(String dB_USERNAME) {
		DB_USERNAME = dB_USERNAME;
	}

	public String getDB_PASSWORD() {
		return DB_PASSWORD;
	}

	public void setDB_PASSWORD(String dB_PASSWORD) {
		DB_PASSWORD = dB_PASSWORD;
	}

	public String getSHAREPOINT_DRIVE_ID() {
		return SHAREPOINT_DRIVE_ID;
	}

	public void setSHAREPOINT_DRIVE_ID(String sHAREPOINT_DRIVE_ID) {
		SHAREPOINT_DRIVE_ID = sHAREPOINT_DRIVE_ID;
	}

	public String getSHAREPOINT_ITEM_ID() {
		return SHAREPOINT_ITEM_ID;
	}

	public void setSHAREPOINT_ITEM_ID(String sHAREPOINT_ITEM_ID) {
		SHAREPOINT_ITEM_ID = sHAREPOINT_ITEM_ID;
	}

	public String getCLIENT_ID() {
		return CLIENT_ID;
	}

	public void setCLIENT_ID(String cLIENT_ID) {
		CLIENT_ID = cLIENT_ID;
	}

	public String getCLIENT_SECRET() {
		return CLIENT_SECRET;
	}

	public void setCLIENT_SECRET(String cLIENT_SECRET) {
		CLIENT_SECRET = cLIENT_SECRET;
	}

	public String getTENANT_ID() {
		return TENANT_ID;
	}

	public void setTENANT_ID(String tENANT_ID) {
		TENANT_ID = tENANT_ID;
	}

	public String getENABLE_VIDEO() {
		return ENABLE_VIDEO;
	}

	public void setENABLE_VIDEO(String eNABLE_VIDEO) {
		ENABLE_VIDEO = eNABLE_VIDEO;
	}

	public String getINSTANCE_NAME() {
		return INSTANCE_NAME;
	}

	public void setINSTANCE_NAME(String iNSTANCE_NAME) {
		INSTANCE_NAME = iNSTANCE_NAME;
	}

	public int getMAX_NUM_SCRIPTS() {
		return MAX_NUM_SCRIPTS;
	}

	public void setMAX_NUM_SCRIPTS(int mAX_NUM_SCRIPTS) {
		MAX_NUM_SCRIPTS = mAX_NUM_SCRIPTS;
	}

	public String getSTART_DATE() {
		return START_DATE;
	}

	public void setSTART_DATE(String sTART_DATE) {
		START_DATE = sTART_DATE;
	}

	public String getSHAREPOINT_PATH() {
		return SHAREPOINT_PATH;
	}

	public void setSHAREPOINT_PATH(String sHAREPOINT_PATH) {
		SHAREPOINT_PATH = sHAREPOINT_PATH;
	}

	public String getEND_DATE() {
		return END_DATE;
	}

	public void setEND_DATE(String eND_DATE) {
		END_DATE = eND_DATE;
	}

	public int getLOOP_COUNT() {
		return LOOP_COUNT;
	}

	public void setLOOP_COUNT(int lOOP_COUNT) {
		LOOP_COUNT = lOOP_COUNT;
	}

	public int getSCRIPT_TO_WAIT() {
		return SCRIPT_TO_WAIT;
	}

	public void setSCRIPT_TO_WAIT(int sCRIPT_TO_WAIT) {
		SCRIPT_TO_WAIT = sCRIPT_TO_WAIT;
	}

	public String getPASSWORD() {
		return PASSWORD;
	}

	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}

	public String getURI_CONFIG() {
		return URI_CONFIG;
	}

	public void setURI_CONFIG(String uRI_CONFIG) {
		URI_CONFIG = uRI_CONFIG;
	}

	public String getURI_RESULTS() {
		return URI_RESULTS;
	}

	public void setURI_RESULTS(String uRI_RESULTS) {
		URI_RESULTS = uRI_RESULTS;
	}

public int getMax_num_scripts() {
		return MAX_NUM_SCRIPTS;
	}

	public void setMax_num_scripts(int max_num_scripts) {
		this.MAX_NUM_SCRIPTS = max_num_scripts;
	}

	public String getStart_date() {
		return START_DATE;
	}

	public void setStart_date(String start_date) {
		this.START_DATE = start_date;
	}

	public String getEnd_date() {
		return END_DATE;
	}

	public void setEnd_date(String end_date) {
		this.END_DATE = end_date;
	}

	//New - change
	public String getEnable_video() {
		return ENABLE_VIDEO;
	}

	public String getInstance_name() {
		return INSTANCE_NAME;
	}

	public void setInstance_name(String instance_name) {
		this.INSTANCE_NAME = instance_name;
	}



	public void setEnable_video(String enable_video) {
		this.ENABLE_VIDEO = enable_video;
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
	public String getDb_host() {
		return DB_HOST;
	}
	public void setDb_host(String db_host) {
		this.DB_HOST = db_host;
	}
	public String getDb_username() {
		return DB_USERNAME;
	}
	public void setDb_username(String db_username) {
		this.DB_USERNAME = db_username;
	}
	public String getSharepoint_drive_id() {
		return SHAREPOINT_DRIVE_ID;
	}
	public void setSharepoint_drive_id(String sharepoint_drive_id) {
		this.SHAREPOINT_DRIVE_ID = sharepoint_drive_id;
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
	public Integer getWait_time() {
		return WAIT_TIME;
	}
	public void setWait_time(Integer wait_time) {
		this.WAIT_TIME = wait_time;
	}
	public String getApplication_url() {
		return APPLICATION_URL;
	}
	public void setApplication_url(String application_url) {
		this.APPLICATION_URL = application_url;
	}
	public String getApplication_user_name() {
		return APPLICATION_USER_NAME;
	}
	public void setApplication_user_name(String application_user_name) {
		this.APPLICATION_USER_NAME = application_user_name;
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
	public String getIe_driver_path() {
		return IE_DRIVER_PATH;
	}
	public void setIe_driver_path(String ie_driver_path) {
		this.IE_DRIVER_PATH = ie_driver_path;
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
	
	
}