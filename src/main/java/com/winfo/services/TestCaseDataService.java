package com.winfo.services;

import java.io.IOException;

import java.nio.charset.StandardCharsets;

import java.nio.file.Files;

import java.nio.file.Paths;

import java.util.ArrayList;

import java.util.HashMap;

import java.util.LinkedHashMap;

import java.util.List;

import java.util.Map;

import javax.script.Invocable;

import javax.script.ScriptEngine;

import javax.script.ScriptEngineManager;

import javax.script.ScriptException;

import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;

import org.springframework.http.HttpHeaders;

import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;

import org.springframework.util.LinkedMultiValueMap;

import org.springframework.util.MultiValueMap;

import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

@Service
@RefreshScope
public class TestCaseDataService {

	Logger logger = LogManager.getLogger(TestCaseDataService.class);

	@Value("${configvO.config_url1}")
	private  String config_url;

	public LinkedHashMap<String, List<FetchMetadataVO>> dependentScriptMap;

	public LinkedHashMap<String, List<FetchMetadataVO>> prepareTestcasedata(List<FetchMetadataVO> list) {

		LinkedHashMap<String, List<FetchMetadataVO>> testCaseMap = new LinkedHashMap<String, List<FetchMetadataVO>>();

		dependentScriptMap = new LinkedHashMap<String, List<FetchMetadataVO>>();

		if (list != null) {

			for (FetchMetadataVO testcase : list) {

				String test_line_id = testcase.getTest_set_line_id();

				String dependency = testcase.getDependency();

				if (test_line_id != null && "N".equalsIgnoreCase(dependency)) {

					prepareTestData(testCaseMap, testcase, test_line_id);

				} else {

					prepareTestData(dependentScriptMap, testcase, test_line_id);

				}

			}

		}

		System.out.println(testCaseMap);

		return testCaseMap;

	}

	public LinkedHashMap<String, List<FetchMetadataVO>> getDependentScriptMap() {

		return dependentScriptMap;

	}

	private void prepareTestData(LinkedHashMap<String, List<FetchMetadataVO>> testCaseMap, FetchMetadataVO testcase,
			String test_line_id) {

		if (testCaseMap.containsKey(test_line_id)) {

			List<FetchMetadataVO> testcasedata = testCaseMap.get(test_line_id);

			testcasedata.add(testcase);

			testCaseMap.put(test_line_id, testcasedata);

		} else {

			List<FetchMetadataVO> testcasedata = new ArrayList<FetchMetadataVO>();

			testcasedata.add(testcase);

			testCaseMap.put(test_line_id, testcasedata);

		}

	}

	public List<FetchMetadataVO> getFetchMetaData(String parameter, String uri) {

		System.out.println(uri);

		RestTemplate restTemplate = new RestTemplate();

		System.out.println(restTemplate);

		String result = restTemplate.getForObject(uri, String.class);

		System.out.println(result);

		// convert Java Objects into their JSON and viz

		Gson g = new Gson();

		FetchMetadataListVO MetaList = g.fromJson(result, FetchMetadataListVO.class);

		prepareTestcasedata(MetaList.getItems());

		return MetaList.getItems();

	}

	public FetchConfigVO getFetchConfigVO(String parameter) {

//		final String uri = "https://watsdev01.winfosolutions.com:4443/wats/wats_workspace_prod/taconfig/data/"
//				+ parameter;

		final String uri = config_url + parameter;

// 	                          final String uri = "http://winfoux01.winfosolutions.com:8080/apex/test_automation/taconfig/data/" + parameter;

		System.out.println(uri);

		RestTemplate restTemplate = new RestTemplate();

		System.out.println(restTemplate);

		String result = restTemplate.getForObject(uri, String.class);

		System.out.println(result);

		// convert Java Objects into their JSON and viz

		Gson g = new Gson();

		FetchConfigMetaVO MetaList = g.fromJson(result, FetchConfigMetaVO.class);

		if (MetaList != null) {

			return MetaList.getItemconfig().get(0);

		} else {

			return null;

		}

	}

	public void updateTestCaseStatus(FetchScriptVO request, String parameter, FetchConfigVO fetchConfigVO) {

		try {

			final String uri = fetchConfigVO.getUri_test_scripts() + parameter + "?p_script_id="
					+ request.getP_script_id() + "&p_status=" + request.getP_status() + "&p_test_set_id="
					+ request.getP_test_set_id() + "&p_test_set_line_id=" + request.getP_test_set_line_id()
					+ "&p_pass_path=" + request.getP_pass_path() + "&p_fail_path=" + request.getP_fail_path()
					+ "&p_exception_path=" + request.getP_exception_path() + "&p_test_set_line_path="
					+ request.getP_test_set_line_path();

			System.out.println(uri);

			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();

			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			restTemplate.postForLocation(uri, headers);

			logger.debug(uri, parameter, "Updated the Passed Status");

		} catch (Exception e) {

			System.out.println("Not Updating the values");

			logger.debug(parameter, "Updated the Failed Status");

			throw e;

		}

		// postForLocation(uri, reqString);

//                           System.out.println("If they return object" + result);

//                           return null;

	}

}