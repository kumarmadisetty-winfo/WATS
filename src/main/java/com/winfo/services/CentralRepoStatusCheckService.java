package com.winfo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.winfo.exception.WatsEBSCustomException;
import com.winfo.utils.Constants;
import com.winfo.vo.ResponseDto;

@Service
public class CentralRepoStatusCheckService {
	
	@Autowired
	DataBaseEntry dataBaseEntry;

	public ResponseDto centralRepoStatus() {
		try {
			RestTemplate restTemplate = new RestTemplate();
			String url = dataBaseEntry.getCentralRepoUrl();
			restTemplate.getForObject(url, String.class);
		} catch (Exception e) {
			throw new WatsEBSCustomException(500, "Central repo is not accessible");
		}
		return new ResponseDto(200, Constants.SUCCESS, "Central repo is up");
	}

}
