package com.winfo.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.winfo.exception.WatsEBSException;
import com.winfo.utils.Constants;
import com.winfo.vo.ResponseDto;

@Service
public class CentralRepoStatusCheckService {
	
	@Autowired
	DataBaseEntry dataBaseEntry;
	
	@Autowired
	private RestTemplate restTemplate;

	public ResponseDto centralRepoStatus() {
		try {
			String url = dataBaseEntry.getCentralRepoUrl(Constants.WATS_CENTRAL);
			restTemplate.getForObject(url, String.class);
		} catch (Exception e) {
			throw new WatsEBSException(500, "Central repo is not accessible");
		}
		return new ResponseDto(200, Constants.SUCCESS, "Central repo is up");
	}

}
