package com.winfo.service;

import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import com.winfo.vo.CustomerProjectDto;
import com.winfo.vo.FetchConfigVO;
import com.winfo.vo.ScriptDetailsDto;

@Service
public interface SFInterface {
	
	public void loginDLApplication(WebDriver driver, String param1, String param2, ScriptDetailsDto fetchMetadataVO,
			FetchConfigVO fetchConfigVO, CustomerProjectDto customerDetails) throws Exception;

}
