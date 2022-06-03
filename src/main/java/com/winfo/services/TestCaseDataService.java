package com.winfo.services;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
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
	private String config_url;

	// public LinkedHashMap<String, List<FetchMetadataVO>> dependentScriptMap;

//	public SortedMap<String, List<FetchMetadataVO>> prepareTestcasedata(List<FetchMetadataVO> list,LinkedHashMap<String, List<FetchMetadataVO>> dependentScriptMap) {
//
//		LinkedHashMap<String, List<FetchMetadataVO>> testCaseMap = new Tr<String, List<FetchMetadataVO>>();
//
//		//dependentScriptMap = new LinkedHashMap<String, List<FetchMetadataVO>>();
//
//		if (list != null) {
//
//			for (FetchMetadataVO testcase : list) {
//
//				String test_line_id = testcase.getTest_set_line_id();
//
//				String dependency = testcase.getDependency();
//
//				if (test_line_id != null && "N".equalsIgnoreCase(dependency)) {
//
//					prepareTestData(testCaseMap, testcase, test_line_id);
//
//				} else {
//
//					prepareTestData(dependentScriptMap, testcase, test_line_id);
//
//				}
//
//			}
//
//		}
//
//		System.out.println(testCaseMap);
//
//		return testCaseMap;
//
//	}
	public SortedMap<Integer, List<FetchMetadataVO>> prepareTestcasedata(List<FetchMetadataVO> list,
			SortedMap<Integer, List<FetchMetadataVO>> dependentScriptMap) {

		SortedMap<Integer, List<FetchMetadataVO>> testCaseMap = new TreeMap<Integer, List<FetchMetadataVO>>();

		if (list != null) {

			for (FetchMetadataVO testcase : list) {

				String test_line_id = testcase.getTest_set_line_id();

				Integer seq = Integer.parseInt(testcase.getSeq_num());

				String dependency = testcase.getDependency();
				if (test_line_id != null && "N".equalsIgnoreCase(dependency)) {

					prepareTestData(testCaseMap, testcase, seq);

				} else {

					prepareTestData(dependentScriptMap, testcase, seq);

				}

			}

		}

		return testCaseMap;

	}

	/*
	 * 
	 * public LinkedHashMap<String, List<FetchMetadataVO>> getDependentScriptMap() {
	 * 
	 * return dependentScriptMap;
	 * 
	 * }
	 */
// private void prepareTestData(LinkedHashMap<String, List<FetchMetadataVO>> testCaseMap, FetchMetadataVO testcase,
//			String test_line_id) {
//
//		if (testCaseMap.containsKey(test_line_id)) {
//
//			List<FetchMetadataVO> testcasedata = testCaseMap.get(test_line_id);
//
//			testcasedata.add(testcase);
//
//			testCaseMap.put(test_line_id, testcasedata);
//
//		} else {
//
//			List<FetchMetadataVO> testcasedata = new ArrayList<FetchMetadataVO>();
//
//			testcasedata.add(testcase);
//
//			testCaseMap.put(test_line_id, testcasedata);
//
//		}
//
//	}

	private void prepareTestData(SortedMap<Integer, List<FetchMetadataVO>> testCaseMap, FetchMetadataVO testcase,
			Integer seq) {

		if (testCaseMap.containsKey(seq)) {

			List<FetchMetadataVO> testcasedata = testCaseMap.get(seq);

			testcasedata.add(testcase);

			testCaseMap.put(seq, testcasedata);

		} else {

			List<FetchMetadataVO> testcasedata = new ArrayList<FetchMetadataVO>();

			testcasedata.add(testcase);

			testCaseMap.put(seq, testcasedata);

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

		// prepareTestcasedata(MetaList.getItems());

		return MetaList.getItems();

	}

	public FetchConfigVO getFetchConfigVO(String parameter) {
		JSONParser jsonParser = new JSONParser();

		final String uri = config_url + parameter;

		try {

			RestTemplate restTemplate = new RestTemplate();
			String result = restTemplate.getForObject(uri, String.class);
			JSONObject obj = (JSONObject) jsonParser.parse(result);
			JSONArray employeeList = (JSONArray) obj.get("items");
			Map<String, String> map = new TreeMap<>();
			employeeList.forEach(emp -> parseEmployeeObject((JSONObject) emp, map));
			JSONObject jsno = new JSONObject(map);
			Gson g = new Gson();
			FetchConfigVO vo = g.fromJson(jsno.toString(), FetchConfigVO.class);
			return vo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void parseEmployeeObject(JSONObject employee, Map<String, String> map) {

		String firstName = (String) employee.get("key_name");
		String lastName = (String) employee.get("value_name");
		map.put(firstName, lastName);
	}


	public void updateTestCaseStatus(FetchScriptVO request, String parameter, FetchConfigVO fetchConfigVO) {
		try {

			final String uri = fetchConfigVO.getMETADATA_URL() + parameter + "?p_script_id=" + request.getP_script_id()
					+ "&p_status=" + request.getP_status() + "&p_test_set_id=" + request.getP_test_set_id()
					+ "&p_test_set_line_id=" + request.getP_test_set_line_id() + "&p_pass_path="
					+ request.getP_pass_path() + "&p_fail_path=" + request.getP_fail_path() + "&p_exception_path="
					+ request.getP_exception_path() + "&p_test_set_line_path=" + request.getP_test_set_line_path();

			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();

			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			restTemplate.postForLocation(uri, headers);

			logger.debug(uri, parameter, "Updated the Passed Status");

		} catch (Exception e) {

			logger.debug(parameter, "Updated the Failed Status");

			throw e;

		}

	}
	public LinkedHashMap<String, List<FetchMetadataVO>> prepareMapOfTestSetLineIdAndStepsList(List<FetchMetadataVO> list,LinkedHashMap<String, List<FetchMetadataVO>> dependentScriptMap) {

		LinkedHashMap<String, List<FetchMetadataVO>> testCaseMap = new LinkedHashMap<String, List<FetchMetadataVO>>();

		//dependentScriptMap = new LinkedHashMap<String, List<FetchMetadataVO>>();

		if (list != null) {

			for (FetchMetadataVO testcase : list) {

				String test_line_id = testcase.getTest_set_line_id();

				String dependency = testcase.getDependency();

				if (test_line_id != null && "N".equalsIgnoreCase(dependency)) {

					prepareTestSetLineIdData(testCaseMap, testcase, test_line_id);

				} else {

					prepareTestSetLineIdData(dependentScriptMap, testcase, test_line_id);

				}

			}

		}

		System.out.println(testCaseMap);

		return testCaseMap;

	}

	/*
	 * 
	 * public LinkedHashMap<String, List<FetchMetadataVO>> getDependentScriptMap() {
	 * 
	 * return dependentScriptMap;
	 * 
	 * }
	 */ 
 private void prepareTestSetLineIdData(LinkedHashMap<String, List<FetchMetadataVO>> testCaseMap, FetchMetadataVO testcase,
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
}