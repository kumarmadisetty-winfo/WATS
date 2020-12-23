package com.winfo.services;

import java.util.Date;

public class FetchConfigVO {
	
	private String browser;
	private Integer wait_time;
	private String application_url;
	private String application_user_name;
	private String password;
	private String test_evidence_repository_url;
	private String chrome_driver_path;
	private String ie_driver_path;
	private String firefox_driver_path;
	private String downlod_file_path;
	private String upload_file_path;
	private String uri_config;
	private String uri_test_scripts;
	private String uri_results;
	private String short_wait;
	private String medium_wait;
	private String long_wait;
	private String img_url;
	private String sharepoint_resp;
	private int parallel_independent;
	private int parallel_dependent;
	private String screenshot_path;
	private String pdf_path;
	private String db_host;
	private String db_username;
	private String sharepoint_drive_id;
	private String sharepoint_item_id;
	private String client_id;
	private String client_secret;
	private String tenant_id;
	private Date starttime;
	private Date Starttime1;
	private Date endtime;
	private int passcount;
	private int failcount;
	private String status1;
	private String errormessage;
//changed the name from Enable_video to enable_video
	private String enable_video;
	

//New - change
	public String getEnable_video() {
		return enable_video;
	}

	public void setEnable_video(String enable_video) {
		this.enable_video = enable_video;
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
		return db_host;
	}
	public void setDb_host(String db_host) {
		this.db_host = db_host;
	}
	public String getDb_username() {
		return db_username;
	}
	public void setDb_username(String db_username) {
		this.db_username = db_username;
	}
	public String getSharepoint_drive_id() {
		return sharepoint_drive_id;
	}
	public void setSharepoint_drive_id(String sharepoint_drive_id) {
		this.sharepoint_drive_id = sharepoint_drive_id;
	}
	public String getSharepoint_item_id() {
		return sharepoint_item_id;
	}
	public void setSharepoint_item_id(String sharepoint_item_id) {
		this.sharepoint_item_id = sharepoint_item_id;
	}
	public String getClient_id() {
		return client_id;
	}
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}
	public String getClient_secret() {
		return client_secret;
	}
	public void setClient_secret(String client_secret) {
		this.client_secret = client_secret;
	}
	public String getTenant_id() {
		return tenant_id;
	}
	public void setTenant_id(String tenant_id) {
		this.tenant_id = tenant_id;
	}
	public String getScreenshot_path() {
		return screenshot_path;
	}
	public void setScreenshot_path(String screenshot_path) {
		this.screenshot_path = screenshot_path;
	}
	public String getPdf_path() {
		return pdf_path;
	}
	public void setPdf_path(String pdf_path) {
		this.pdf_path = pdf_path;
	}
	public int getParallel_independent() {
		return parallel_independent;
	}
	public void setParallel_independent(int parallel_independent) {
		this.parallel_independent = parallel_independent;
	}
	public int getParallel_dependent() {
		return parallel_dependent;
	}
	public void setParallel_dependent(int parallel_dependent) {
		this.parallel_dependent = parallel_dependent;
	}
	public String getLong_wait() {
		return long_wait;
	}
	public void setLong_wait(String long_wait) {
		this.long_wait = long_wait;
	}
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	public String getSharepoint_resp() {
		return sharepoint_resp;
	}
	public void setSharepoint_resp(String sharepoint_resp) {
		this.sharepoint_resp = sharepoint_resp;
	}
	public String getShort_wait() {
		return short_wait;
	}
	public void setShort_wait(String short_wait) {
		this.short_wait = short_wait;
	}
	public String getMedium_wait() {
		return medium_wait;
	}
	public void setMedium_wait(String medium_wait) {
		this.medium_wait = medium_wait;
	}
	public String getBrowser() {
		return browser;
	}
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	public Integer getWait_time() {
		return wait_time;
	}
	public void setWait_time(Integer wait_time) {
		this.wait_time = wait_time;
	}
	public String getApplication_url() {
		return application_url;
	}
	public void setApplication_url(String application_url) {
		this.application_url = application_url;
	}
	public String getApplication_user_name() {
		return application_user_name;
	}
	public void setApplication_user_name(String application_user_name) {
		this.application_user_name = application_user_name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTest_evidence_repository_url() {
		return test_evidence_repository_url;
	}
	public void setTest_evidence_repository_url(String test_evidence_repository_url) {
		this.test_evidence_repository_url = test_evidence_repository_url;
	}
	public String getChrome_driver_path() {
		return chrome_driver_path;
	}
	public void setChrome_driver_path(String chrome_driver_path) {
		this.chrome_driver_path = chrome_driver_path;
	}
	public String getIe_driver_path() {
		return ie_driver_path;
	}
	public void setIe_driver_path(String ie_driver_path) {
		this.ie_driver_path = ie_driver_path;
	}
	public String getFirefox_driver_path() {
		return firefox_driver_path;
	}
	public void setFirefox_driver_path(String firefox_driver_path) {
		this.firefox_driver_path = firefox_driver_path;
	}
	public String getDownlod_file_path() {
		return downlod_file_path;
	}
	public void setDownlod_file_path(String downlod_file_path) {
		this.downlod_file_path = downlod_file_path;
	}
	public String getUpload_file_path() {
		return upload_file_path;
	}
	public void setUpload_file_path(String upload_file_path) {
		this.upload_file_path = upload_file_path;
	}
	public String getUri_config() {
		return uri_config;
	}
	public void setUri_config(String uri_config) {
		this.uri_config = uri_config;
	}
	public String getUri_test_scripts() {
		return uri_test_scripts;
	}
	public void setUri_test_scripts(String uri_test_scripts) {
		this.uri_test_scripts = uri_test_scripts;
	}
	public String getUri_results() {
		return uri_results;
	}
	public void setUri_results(String uri_results) {
		this.uri_results = uri_results;
	}
	
	
}
