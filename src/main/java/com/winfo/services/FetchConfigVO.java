package com.winfo.services;

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