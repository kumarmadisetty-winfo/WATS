package com.winfo.services;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
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
		JSONParser jsonParser = new JSONParser();
//		 String uri = "https://watshubd01.winfosolutions.com:4443/wats/wats_workspace_prod/CONFIG_GET/data";

//		final String uri = config_url + parameter;

 	                          final String uri = "https://watshubd01.winfosolutions.com:4443/wats/wats_workspace_prod/taconfig/data/" + parameter;
		try {
			System.out.println(uri);

			RestTemplate restTemplate = new RestTemplate();

			System.out.println(restTemplate);

			String result = restTemplate.getForObject(uri, String.class);

			System.out.println(result);

			JSONObject obj = (JSONObject) jsonParser.parse(result);

			System.out.println(restTemplate);

			JSONArray employeeList = (JSONArray) obj.get("items");
			System.out.println(employeeList);
			Map<String, String> map = new TreeMap<>();
			// Iterate over employee array
			employeeList.forEach(emp -> parseEmployeeObject((JSONObject) emp, map));
			JSONObject jsno = new JSONObject(map);
			Gson g = new Gson();
			FetchConfigVO vo = g.fromJson(jsno.toString(), FetchConfigVO.class);
			System.out.println(jsno.toString());
			return vo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void parseEmployeeObject(JSONObject employee, Map<String, String> map) {
		// Get employee object within list
//	        JSONObject employeeObject = (JSONObject) employee.get("items");

		// Get employee first name
		String firstName = (String) employee.get("key_name");
		System.out.println(firstName);

		// Get employee last name
		String lastName = (String) employee.get("value_name");
		System.out.println(lastName);
		map.put(firstName, lastName);
	}

	public void updateTestCaseStatus(FetchScriptVO request, String parameter, FetchConfigVO fetchConfigVO) {

		try {

			final String uri = fetchConfigVO.getMETADATA_URL() + parameter + "?p_script_id="
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