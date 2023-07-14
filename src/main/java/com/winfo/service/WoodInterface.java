package com.winfo.service;

import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import com.winfo.vo.CustomerProjectDto;
import com.winfo.vo.FetchConfigVO;
import com.winfo.vo.ScriptDetailsDto;

@Service
public interface WoodInterface {

	public void enterMultipleTransaction(WebDriver driver, FetchConfigVO fetchConfigVO,
			ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails) throws Exception;

}

