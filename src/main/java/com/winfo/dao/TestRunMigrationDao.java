package com.winfo.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.winfo.model.FetchData;
import com.winfo.model.ScriptMetaData;
import com.winfo.model.ScriptsData;
import com.winfo.model.TestSetScriptParam;
import com.winfo.model.Testrundata;
import com.winfo.vo.ExistsTestRun;
import com.winfo.vo.LookUpCodeVO;
import com.winfo.vo.LookUpVO;
import com.winfo.vo.TestRunDetails;

import reactor.core.publisher.Mono;

@Repository
public class TestRunMigrationDao {

	@Autowired
	private EntityManager entityManager;

	public String webClientService(List<JSONObject> json2, String customer_uri) throws JsonMappingException, JsonProcessingException {
		
		List<Integer> listOfValues = new ArrayList<>();
		List<JSONObject> listOfObjects = new ArrayList<>();
		
		String uri = customer_uri + "/fromCentralRepoTestRunMigration";
		WebClient webClient = WebClient.create(uri);
		Mono<String> result = webClient.post().syncBody(json2).retrieve().bodyToMono(String.class);
		String response = result.block();
		if (response.equals("[]")) {
			response = "[{\"status\":404,\"statusMessage\":\"PV_ERROR\",\"description\":\"Wrong Product Version\"}]";
		}
//		else {
//			ObjectMapper objectMapper = new ObjectMapper();
//			DomGenericResponseBean3 responseMapping = objectMapper.readValue(response, DomGenericResponseBean3.class);
//			if(!responseMapping.getResponse().isEmpty()) {
//				for(JSONObject json : json2) {
//					if(listOfValues.contains(json.get(responseMapping))) {
//						listOfObjects.add(json);
//					}
//				}
//				webClientService(listOfObjects,customer_uri);
//			}
//		}
		System.out.println(response);
		System.out.println(response);
		System.out.println(json2);
		System.out.println(json2);
		return response;
	}


	public JSONObject lookupCode(String lookUpName, String lookupCode, Session session) {
		JSONObject jsonLookUpData = new JSONObject();

		List<Object> listOfObjects = session.createNativeQuery("select * from win_ta_lookup_codes where lookup_name = '"
				+ lookUpName + "' and lookup_code = '" + lookupCode + "'").list();
		Iterator itr = listOfObjects.iterator();
		LookUpCodeVO lookupVo = new LookUpCodeVO();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			if (String.valueOf(obj[0]).equals("null")) {
				lookupVo.setLOOKUP_CODES_ID(null);
			} else {
				lookupVo.setLOOKUP_CODES_ID(String.valueOf(obj[0]));
			}
			if (String.valueOf(obj[1]).equals("null")) {
				lookupVo.setLOOKUP_ID(null);
			} else {
				lookupVo.setLOOKUP_ID(String.valueOf(obj[1]));
			}
			if (String.valueOf(obj[2]).equals("null")) {
				lookupVo.setLOOKUP_NAME(null);
			} else {
				lookupVo.setLOOKUP_NAME(String.valueOf(obj[2]));
			}

			if (String.valueOf(obj[3]).equals("null")) {
				lookupVo.setLOOKUP_CODE(null);
			} else {
				lookupVo.setLOOKUP_CODE(String.valueOf(obj[3]));
			}

			if (String.valueOf(obj[4]).equals("null")) {
				lookupVo.setTARGET_CODE(null);
			} else {
				lookupVo.setTARGET_CODE(String.valueOf(obj[4]));
			}
			if (String.valueOf(obj[5]).equals("null")) {
				lookupVo.setMEANING(null);
			} else {
				lookupVo.setMEANING(String.valueOf(obj[5]));
			}
			if (String.valueOf(obj[6]).equals("null")) {
				lookupVo.setDESCRIPTION(null);
			} else {
				lookupVo.setDESCRIPTION(String.valueOf(obj[6]));
			}
			if (String.valueOf(obj[7]).equals("null")) {
				lookupVo.setEFFECTIVE_FROM(null);
			} else {
				lookupVo.setEFFECTIVE_FROM(String.valueOf(obj[7]));
			}
			if (String.valueOf(obj[8]).equals("null")) {
				lookupVo.setEFFECTIVE_TO(null);
			} else {
				lookupVo.setEFFECTIVE_TO(String.valueOf(obj[8]));
			}
			if (String.valueOf(obj[9]).equals("null")) {
				lookupVo.setCREATED_BY(null);
			} else {
				lookupVo.setCREATED_BY(String.valueOf(obj[9]));
			}

			if (String.valueOf(obj[10]).equals("null")) {
				lookupVo.setLAST_UPDATED_BY(null);
			} else {
				lookupVo.setLAST_UPDATED_BY(String.valueOf(obj[10]));
			}

			if (String.valueOf(obj[11]).equals("null")) {
				lookupVo.setCREATION_DATE(null);
			} else {
				lookupVo.setCREATION_DATE(String.valueOf(obj[11]));
			}
			if (String.valueOf(obj[12]).equals("null")) {
				lookupVo.setUPDATE_DATE(null);
			} else {
				lookupVo.setUPDATE_DATE(String.valueOf(obj[12]));
			}
			if (String.valueOf(obj[13]).equals("null")) {
				lookupVo.setPROCESS_CODE(null);
			} else {
				lookupVo.setPROCESS_CODE(String.valueOf(obj[15]));
			}
			if (String.valueOf(obj[14]).equals("null")) {
				lookupVo.setMODULE_CODE(null);
			} else {
				lookupVo.setMODULE_CODE(String.valueOf(obj[16]));
			}
		}
		jsonLookUpData.put("lookup_codes_id", lookupVo.getLOOKUP_CODES_ID());
		jsonLookUpData.put("lookup_id", lookupVo.getLOOKUP_ID());
		jsonLookUpData.put("lookup_name", lookupVo.getLOOKUP_NAME());
		jsonLookUpData.put("lookup_code", lookupVo.getLOOKUP_CODE());
		jsonLookUpData.put("target_code", "\"" + lookupVo.getTARGET_CODE() + "\"");
		jsonLookUpData.put("meaning", lookupVo.getMEANING());
		jsonLookUpData.put("description", lookupVo.getDESCRIPTION());
		jsonLookUpData.put("effective_from", lookupVo.getEFFECTIVE_FROM());
		jsonLookUpData.put("effective_to", lookupVo.getEFFECTIVE_TO());
		jsonLookUpData.put("created_by", lookupVo.getCREATED_BY());
		jsonLookUpData.put("last_updated_by", lookupVo.getLAST_UPDATED_BY());
		jsonLookUpData.put("creation_date", lookupVo.getCREATION_DATE());
		jsonLookUpData.put("update_date", lookupVo.getUPDATE_DATE());
		jsonLookUpData.put("process_code", lookupVo.getPROCESS_CODE());
		jsonLookUpData.put("module_code", lookupVo.getMODULE_CODE());
		return jsonLookUpData;
	}

	public JSONObject lookups(String lookUpName, JSONObject jsonLookUpUniqueMandatoryCodeArray, Session session) {

		JSONObject jsonLookUpData = new JSONObject();
		List<Object> listOfObjects = session
				.createNativeQuery("select * from win_ta_lookups where lookup_name = '" + lookUpName + "'").list();
		Iterator itr = listOfObjects.iterator();
		LookUpVO lookupVo = new LookUpVO();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			if (String.valueOf(obj[0]).equals("null")) {
				lookupVo.setLOOKUP_ID(null);
			} else {
				lookupVo.setLOOKUP_ID(String.valueOf(obj[0]));
			}
			if (String.valueOf(obj[1]).equals("null")) {
				lookupVo.setLOOKUP_NAME(null);
			} else {
				lookupVo.setLOOKUP_NAME(String.valueOf(obj[1]));
			}
			if (String.valueOf(obj[2]).equals("null")) {
				lookupVo.setLOOKUP_DESC(null);
			} else {
				lookupVo.setLOOKUP_DESC(String.valueOf(obj[2]));
			}
			if (String.valueOf(obj[3]).equals("null")) {
				lookupVo.setCREATED_BY(null);
			} else {
				lookupVo.setCREATED_BY(String.valueOf(obj[3]));
			}
			if (String.valueOf(obj[4]).equals("null")) {
				lookupVo.setLAST_UPDATED_BY(null);
			} else {
				lookupVo.setLAST_UPDATED_BY(String.valueOf(obj[4]));
			}
			if (String.valueOf(obj[5]).equals("null")) {
				lookupVo.setCREATION_DATE(null);
			} else {
				lookupVo.setCREATION_DATE(String.valueOf(obj[5]));
			}
			if (String.valueOf(obj[6]).equals("null")) {
				lookupVo.setUPDATE_DATE(null);
			} else {
				lookupVo.setUPDATE_DATE(String.valueOf(obj[6]));
			}

		}

		jsonLookUpData.put("lookup_id", lookupVo.getLOOKUP_ID());
		jsonLookUpData.put("lookup_name", lookupVo.getLOOKUP_NAME());
		jsonLookUpData.put("lookup_desc", lookupVo.getLOOKUP_DESC());
		jsonLookUpData.put("created_by", lookupVo.getCREATED_BY());
		jsonLookUpData.put("last_updated_by", lookupVo.getLAST_UPDATED_BY());
		jsonLookUpData.put("creation_date", lookupVo.getCREATION_DATE());
		jsonLookUpData.put("update_date", lookupVo.getUPDATE_DATE());
		jsonLookUpData.put("mapOfData", jsonLookUpUniqueMandatoryCodeArray);
		return jsonLookUpData;
	}

	@SuppressWarnings("unchecked")
	public String testRunMigration(TestRunDetails testRunDetails) throws ParseException, JsonProcessingException {
		
		List<JSONObject> listOfTestRunData = new ArrayList<>();
		
		Session session = entityManager.unwrap(Session.class);

		Query query4 = session.createQuery("select customer_uri from CustomerTable where customer_name='"
				+ testRunDetails.getCustomerName() + "'");
		List<String> result4 = (List<String>) query4.list();

		String customer_uri = result4.get(0);

		
//		List<Integer> listOfTestRunIds = testRunDetails.getId();
		

		for (ExistsTestRun id : testRunDetails.getId()) {
			int testRunId = id.getTestSetId();
			JSONObject jsonLookUpActionCodeArray = new JSONObject();
			JSONObject jsonLookUpDataTypesCodeArray = new JSONObject();
			JSONObject jsonLookUpUniqueMandatoryCodeArray = new JSONObject();
			
			JSONObject finalJSONObject = new JSONObject();
			JSONObject jsonLineData = new JSONObject();
			JSONObject jsonScriptMaster = new JSONObject();
			JSONArray jsonScriptMasterArray = new JSONArray();

			JSONObject jsonLookUpArray = new JSONObject();

			JSONArray jsonArrayScript = new JSONArray();
			JSONObject jsonTestData = new JSONObject();
			
			Testrundata testRunData = session.find(Testrundata.class, testRunId);

			String configurationName = (String) session
					.createNativeQuery("select config_name from win_ta_config where configuration_id ='"
							+ testRunData.getConfigurationid() + "'")
					.getSingleResult();

			String projectName = (String) session
					.createNativeQuery(
							"select project_name from win_ta_projects where project_id =" + testRunData.getProjectid())
					.getSingleResult();
			
			jsonTestData.put("customer", testRunDetails.getCustomerName());
			jsonTestData.put("projectName", projectName);
			jsonTestData.put("configurationName", configurationName);
			jsonTestData.put("test_set_name", testRunData.getTestsetname());
			jsonTestData.put("test_set_desc", testRunData.getTest_set_desc());
			jsonTestData.put("test_set_comments", testRunData.getTest_set_comments());
			jsonTestData.put("enabled", testRunData.getEnabled());
			jsonTestData.put("project_id", testRunData.getProjectid());
			jsonTestData.put("description", testRunData.getDescription());
			jsonTestData.put("effective_from", testRunData.getEffective_from());
			jsonTestData.put("effective_to", testRunData.getEffective_to());
			jsonTestData.put("created_by", testRunData.getCreatedby());
			jsonTestData.put("last_updated_by", null);
			jsonTestData.put("creation_date", null);
			jsonTestData.put("update_date", null);
			jsonTestData.put("configuration_id", testRunData.getConfigurationid());
			jsonTestData.put("last_executed_by", null);
			jsonTestData.put("ts_complete_flag", testRunData.getTscompleteflag());
			jsonTestData.put("pass_path", testRunData.getPasspath());
			jsonTestData.put("fail_path", testRunData.getFailpath());
			jsonTestData.put("exeception_path", testRunData.getExceptionpath());
			jsonTestData.put("tr_mode", testRunData.getTrmode());
			
			jsonTestData.put("testRunExists", id.isFlag());
			List<Integer> testSetLineIDs = session
					.createQuery("select testsetlineid from ScriptsData where Testrundata.testsetid = " + testRunId)
					.getResultList();

			JSONObject validationData = new JSONObject();
			for (int testSetLineID : testSetLineIDs) {

				ScriptsData scriptsData = session.find(ScriptsData.class, testSetLineID);
				System.out.println("scriptsData " + scriptsData.getTestsetlineid());
				jsonLineData.put("script_id", scriptsData.getScriptid());
				jsonLineData.put("scriptnumber", scriptsData.getScriptnumber());
				jsonLineData.put("scriptUpadated", null);
				jsonLineData.put("status", scriptsData.getStatus());
				jsonLineData.put("enabled", scriptsData.getEnabled());
				jsonLineData.put("seqnum", scriptsData.getSeqnum());
				jsonLineData.put("createdby", scriptsData.getCreatedby());
				jsonLineData.put("lastupdatedby", null);
				jsonLineData.put("creationdate", null);
				jsonLineData.put("updateddate", null);
				jsonLineData.put("testsstlinescriptpath", scriptsData.getTestsstlinescriptpath());
				jsonLineData.put("executedby", scriptsData.getExecutedby());
				jsonLineData.put("executionstarttime", null);
				jsonLineData.put("executionendtime", null);

				List<TestSetScriptParam> listOfTestSetScriptParam = session
						.createQuery("from TestSetScriptParam where testSetLines.test_set_line_id = "
								+ scriptsData.getTestsetlineid())
						.getResultList();

				JSONArray jsonTestSetScriptParamArray = new JSONArray();
				JSONObject jsonTestSetScriptParam = new JSONObject();
				for (TestSetScriptParam testSetScriptParam : listOfTestSetScriptParam) {
					jsonTestSetScriptParam.put("test_script_param_id", testSetScriptParam.getTestRunScriptParamId());
					jsonTestSetScriptParam.put("script_number", testSetScriptParam.getScriptNumber());
					jsonTestSetScriptParam.put("line_error_message", testSetScriptParam.getLineErrorMessage());
					jsonTestSetScriptParam.put("line_number", testSetScriptParam.getLineNumber());
					jsonTestSetScriptParam.put("input_parameter", testSetScriptParam.getInputParameter());
					jsonTestSetScriptParam.put("action", testSetScriptParam.getAction());
					jsonTestSetScriptParam.put("xpath_location", testSetScriptParam.getXpathLocation());
					jsonTestSetScriptParam.put("xpath_location1", testSetScriptParam.getXpathLocation1());
					jsonTestSetScriptParam.put("test_run_param_name", testSetScriptParam.getTestRunParamName());
					jsonTestSetScriptParam.put("test_run_param_desc", testSetScriptParam.getTestRunParamDesc());
					jsonTestSetScriptParam.put("created_by", testSetScriptParam.getCreatedBy());
					jsonTestSetScriptParam.put("last_updated_by", null);
					jsonTestSetScriptParam.put("creation_date", null);
					jsonTestSetScriptParam.put("update_date", null);
					jsonTestSetScriptParam.put("input_value", testSetScriptParam.getInputValue());
					jsonTestSetScriptParam.put("metadata_id", testSetScriptParam.getMetadataId());
					jsonTestSetScriptParam.put("hint", testSetScriptParam.getHint());
					jsonTestSetScriptParam.put("field_type", testSetScriptParam.getFieldType());
					jsonTestSetScriptParam.put("data_types", testSetScriptParam.getDataTypes());
					jsonTestSetScriptParam.put("line_execution_status", testSetScriptParam.getLineExecutionStatus());
					jsonTestSetScriptParam.put("unique_mandatory", testSetScriptParam.getUniqueMandatory());
					jsonTestSetScriptParam.put("validation_type", testSetScriptParam.getValidationType());
					jsonTestSetScriptParam.put("validation_name", testSetScriptParam.getValidationName());
					jsonTestSetScriptParamArray.put(jsonTestSetScriptParam.toJSONString());
				}
				jsonLineData.put("ScriptParam", jsonTestSetScriptParamArray);
				jsonArrayScript.put(jsonLineData.toJSONString());
				jsonTestData.put("TestSetLinesAndParaData", jsonArrayScript);

				Query queryToGetScriptDetailsByScriptId = session.createNativeQuery(
						"select * from win_ta_script_master where script_id = " + scriptsData.getScriptid());
				List<Object> listOfObjects = (List<Object>) queryToGetScriptDetailsByScriptId.list();
				Iterator itr = listOfObjects.iterator();
				FetchData fetchData = new FetchData();
				List<Integer> listOfDependentScripts = new ArrayList<>();
				while (itr.hasNext()) {
					Object[] obj = (Object[]) itr.next();
//					fetchData.setSc .setScript_id(String.valueOf(obj[0]));
					if (String.valueOf(obj[1]).equals("null")) {
						fetchData.setScript_number(null);
					} else {
						fetchData.setScript_number(String.valueOf(obj[1]));
					}
					if (String.valueOf(obj[2]).equals("null")) {
						fetchData.setProcess_area(null);
					} else {
						fetchData.setProcess_area(String.valueOf(obj[2]));
					}
					if (String.valueOf(obj[3]).equals("null")) {
						fetchData.setSub_process_area(null);
					} else {
						fetchData.setSub_process_area(String.valueOf(obj[3]));
					}
					if (String.valueOf(obj[4]).equals("null")) {
						fetchData.setModule(null);
					} else {
						fetchData.setModule(String.valueOf(obj[4]));
					}
					if (String.valueOf(obj[5]).equals("null")) {
						fetchData.setRole(null);
					} else {
						fetchData.setRole(String.valueOf(obj[5]));
					}
					if (String.valueOf(obj[6]).equals("null")) {
						fetchData.setEnd2end_scenario(null);
					} else {
						fetchData.setEnd2end_scenario(String.valueOf(obj[6]));
					}
					if (String.valueOf(obj[7]).equals("null")) {
						fetchData.setScenario_name(null);
					} else {
						fetchData.setScenario_name(String.valueOf(obj[7]));
					}
					if (String.valueOf(obj[8]).equals("null")) {
						fetchData.setScenario_description(null);
					} else {
						fetchData.setScenario_description(String.valueOf(obj[8]));
					}
					if (String.valueOf(obj[9]).equals("null")) {
						fetchData.setExpected_result(null);
					} else {
						fetchData.setExpected_result(String.valueOf(obj[9]));
					}
					if (String.valueOf(obj[10]).equals("null")) {
						fetchData.setSelenium_test_script_name(null);
					} else {
						fetchData.setSelenium_test_script_name(String.valueOf(obj[10]));
					}
					if (String.valueOf(obj[11]).equals("null")) {
						fetchData.setSelenium_test_method(null);
					} else {
						fetchData.setSelenium_test_method(String.valueOf(obj[11]));
					}
					if (String.valueOf(obj[12]).equals("null")) {
						fetchData.setDependency(null);
					} else {
						fetchData.setDependency(Integer.parseInt(String.valueOf(obj[12])));
					}

					if (String.valueOf(obj[13]).equals("null")) {
						fetchData.setProduct_version(null);
					} else {
						fetchData.setProduct_version(String.valueOf(obj[13]));
					}

					if (String.valueOf(obj[14]).equals("null")) {
						fetchData.setStandard_custom(null);
					} else {
						fetchData.setStandard_custom(String.valueOf(obj[14]));
					}
					if (String.valueOf(obj[15]).equals("null")) {
						fetchData.setTest_script_status(null);
					} else {
						fetchData.setTest_script_status(String.valueOf(obj[15]));
					}
					if (String.valueOf(obj[16]).equals("null")) {
						fetchData.setAuthor(null);
					} else {
						fetchData.setAuthor(String.valueOf(obj[16]));
					}
					if (String.valueOf(obj[17]).equals("null")) {
						fetchData.setCreated_by(null);
					} else {
						fetchData.setCreated_by(String.valueOf(obj[17]));
					}

					if (String.valueOf(obj[19]).equals("null")) {
						fetchData.setUpdated_by(null);
					} else {
						fetchData.setUpdated_by(String.valueOf(obj[19]));
					}

					if (String.valueOf(obj[21]).equals("null")) {
						fetchData.setCustomer_id(null);
					} else {
						fetchData.setCustomer_id(Integer.parseInt(String.valueOf(obj[21])));
					}
					if (String.valueOf(obj[22]).equals("null")) {
						fetchData.setCustomisation_refrence(null);
					} else {
						fetchData.setCustomisation_refrence(String.valueOf(obj[22]));
					}

					if (String.valueOf(obj[23]).equals("null")) {
						fetchData.setAttribute1(null);
					} else {
						fetchData.setAttribute1(String.valueOf(obj[23]));
					}
					if (String.valueOf(obj[24]).equals("null")) {
						fetchData.setAttribute2(null);
					} else {
						fetchData.setAttribute2(String.valueOf(obj[24]));
					}
					if (String.valueOf(obj[25]).equals("null")) {
						fetchData.setAttribute3(null);
					} else {
						fetchData.setAttribute3(String.valueOf(obj[25]));
					}
					if (String.valueOf(obj[26]).equals("null")) {
						fetchData.setAttribute4(null);
					} else {
						fetchData.setAttribute4(String.valueOf(obj[26]));
					}
					if (String.valueOf(obj[27]).equals("null")) {
						fetchData.setAttribute5(null);
					} else {
						fetchData.setAttribute5(String.valueOf(obj[27]));
					}
					if (String.valueOf(obj[28]).equals("null")) {
						fetchData.setAttribute6(null);
					} else {
						fetchData.setAttribute6(String.valueOf(obj[28]));
					}
					if (String.valueOf(obj[29]).equals("null")) {
						fetchData.setAttribute7(null);
					} else {
						fetchData.setAttribute7(String.valueOf(obj[29]));
					}
					if (String.valueOf(obj[30]).equals("null")) {
						fetchData.setAttribute8(null);
					} else {
						fetchData.setAttribute8(String.valueOf(obj[30]));
					}
					if (String.valueOf(obj[31]).equals("null")) {
						fetchData.setAttribute9(null);
					} else {
						fetchData.setAttribute9(String.valueOf(obj[31]));
					}
					if (String.valueOf(obj[32]).equals("null")) {
						fetchData.setAttribute10(null);
					} else {
						fetchData.setAttribute10(String.valueOf(obj[32]));
					}

					fetchData.setPriority(Integer.parseInt(String.valueOf(obj[33])));
				}
//				jsonScriptMaster.put("script_id", fetchData.getScript_id());
				jsonScriptMaster.put("script_number", fetchData.getScript_number());
				jsonScriptMaster.put("process_area", fetchData.getProcess_area());
				jsonScriptMaster.put("sub_process_area", fetchData.getSub_process_area());
				jsonScriptMaster.put("module", fetchData.getModule());
				jsonScriptMaster.put("role", fetchData.getRole());
				jsonScriptMaster.put("scenario_name", fetchData.getScenario_name());
				jsonScriptMaster.put("scenario_description", fetchData.getScenario_description());
				jsonScriptMaster.put("product_version", fetchData.getProduct_version());
				jsonScriptMaster.put("standard_custom", fetchData.getStandard_custom());
				jsonScriptMaster.put("test_script_status", fetchData.getTest_script_status());
				jsonScriptMaster.put("priority", fetchData.getPriority());
				jsonScriptMaster.put("dependency", fetchData.getDependency());

				jsonScriptMaster.put("end2end_scenario", fetchData.getEnd2end_scenario());
				String expected_result = fetchData.getExpected_result();
				if (expected_result == null) {
					jsonScriptMaster.put("expected_result", null);
				} else {
//					String str1 = "\"";
//					String str2 = "\\\\";
//					String str3 = str2 + "\"";
//					String replaceQuotes = expected_result.replace(str1, str3);
//					jsonMaster.put("expected_result", replaceQuotes);
				}
				jsonScriptMaster.put("selenium_test_script_name", fetchData.getSelenium_test_script_name());
				jsonScriptMaster.put("selenium_test_method", fetchData.getSelenium_test_method());
				jsonScriptMaster.put("author", fetchData.getAuthor());
				jsonScriptMaster.put("created_by", fetchData.getCreated_by());
				jsonScriptMaster.put("updated_by", fetchData.getUpdated_by());
				jsonScriptMaster.put("customatisation_refrence", fetchData.getCustomisation_refrence());
				jsonScriptMaster.put("attribute1", fetchData.getAttribute1());
				jsonScriptMaster.put("attribute2", fetchData.getAttribute2());
				jsonScriptMaster.put("attribute3", fetchData.getAttribute3());
				jsonScriptMaster.put("attribute4", fetchData.getAttribute4());
				jsonScriptMaster.put("attribute5", fetchData.getAttribute5());
				jsonScriptMaster.put("attribute6", fetchData.getAttribute6());
				jsonScriptMaster.put("attribute7", fetchData.getAttribute7());
				jsonScriptMaster.put("attribute8", fetchData.getAttribute8());
				jsonScriptMaster.put("attribute9", fetchData.getAttribute9());
				jsonScriptMaster.put("attribute10", fetchData.getAttribute10());
				jsonScriptMaster.put("priority", fetchData.getPriority());

				List<ScriptMetaData> listOfMetaData = session
						.createQuery("from ScriptMetaData where SCRIPT_ID =" + scriptsData.getScriptid())
						.getResultList();

				JSONArray jsonArrayScriptMetaData = new JSONArray();
				JSONObject jsonScriptMetadata = new JSONObject();

				for (ScriptMetaData scriptMetaData : listOfMetaData) {

					jsonScriptMetadata.put("script_meta_data_id", scriptMetaData.getScript_meta_data_id());
					jsonScriptMetadata.put("line_number", scriptMetaData.getLine_number());
					jsonScriptMetadata.put("input_parameter", scriptMetaData.getInput_parameter());
					jsonScriptMetadata.put("action", scriptMetaData.getAction());

					if (scriptMetaData.getAction() != null || !scriptMetaData.getAction().equals("NA")) {
						jsonLookUpActionCodeArray.put(scriptMetaData.getAction(),
								lookupCode("ACTION", scriptMetaData.getAction(), session).toJSONString());
					}
					jsonScriptMetadata.put("xpath_location", scriptMetaData.getXpath_location());
					jsonScriptMetadata.put("xpath_location1", scriptMetaData.getXpath_location1());
					jsonScriptMetadata.put("created_by", scriptMetaData.getCreated_by());
					jsonScriptMetadata.put("updated_by", scriptMetaData.getUpdated_by());
					jsonScriptMetadata.put("step_desc", scriptMetaData.getStep_desc());
					jsonScriptMetadata.put("field_type", scriptMetaData.getField_type());
					jsonScriptMetadata.put("hint", scriptMetaData.getHint());
					jsonScriptMetadata.put("script_number", scriptMetaData.getScript_number());
					jsonScriptMetadata.put("datatypes", scriptMetaData.getDatatypes());

					if (!scriptMetaData.getDatatypes().equals("NA")) {
						jsonLookUpDataTypesCodeArray.put(scriptMetaData.getDatatypes(),
								lookupCode("DATATYPES", scriptMetaData.getDatatypes(), session).toJSONString());
					}
					jsonScriptMetadata.put("unique_mandatory", scriptMetaData.getUnique_mandatory());
					if (!scriptMetaData.getUnique_mandatory().equals("NA")) {
						jsonLookUpUniqueMandatoryCodeArray.put(scriptMetaData.getUnique_mandatory().toUpperCase(),
								lookupCode("UNIQUE_MANDATORY", scriptMetaData.getUnique_mandatory(), session)
										.toJSONString());
					}

					if (!scriptMetaData.getValidation_name().equals("NA")) {
						validationData.put(scriptMetaData.getValidation_name(),
								lookupCode(scriptMetaData.getValidation_type().toUpperCase(),
										scriptMetaData.getValidation_name(), session));
					}

					if (!scriptMetaData.getValidation_type().equals("NA")) {
						jsonLookUpArray.put(scriptMetaData.getValidation_type(),
								lookups(scriptMetaData.getValidation_type().toUpperCase(), validationData, session));
					}

					jsonScriptMetadata.put("validation_name", scriptMetaData.getValidation_name());

					jsonScriptMetadata.put("validation_type", scriptMetaData.getValidation_type());

					jsonArrayScriptMetaData.put(jsonScriptMetadata.toString());
				}
				jsonScriptMaster.put("ScriptMetaData", jsonArrayScriptMetaData);

				jsonScriptMasterArray.put(jsonScriptMaster.toJSONString());

				jsonTestData.put("ScriptMasterData", jsonScriptMasterArray);

			}

			jsonLookUpArray.put("action", lookups("ACTION", jsonLookUpActionCodeArray, session).toJSONString());
			jsonLookUpArray.put("unique_mandatory",
					lookups("UNIQUE_MANDATORY", jsonLookUpUniqueMandatoryCodeArray, session).toJSONString());
			jsonLookUpArray.put("datatypes",
					lookups("DATATYPES", jsonLookUpDataTypesCodeArray, session).toJSONString());
			jsonTestData.put("lookUpData", jsonLookUpArray);
			String str1 = "\"" + "{" + "\"";
			String str2 = "{" + "\"";
			String str3 = "\"" + "}" + "\"";
			String str4 = "\"" + "}";
			String str5 = "}" + "\"";
			String str6 = "}";
			String str7 = "\"\"";
			String str8 = "\\";
			String str9 = "\"" + str8 + "\"";
			String str10 = "\"";
			String str11 = str10 + str8 + str10;
			String str12 = str8 + str7;

			String replaceSlash = jsonTestData.toString().replace("\\", "");
			String replaceQuotes = replaceSlash.replace(str1, str2);
			String finalJSONString = replaceQuotes.replace(str3, str4);
			String finalJSONString1 = finalJSONString.replace(str5, str6);
			String finalJSONString2 = finalJSONString1.replace(str7, str9);
			String finalJSONString3 = finalJSONString2.replace(str11 + " ", str12 + " ");
			String finalJSONString4 = finalJSONString3.replace(str11 + ",", str12 + ",");

			JSONParser parser = new JSONParser();
			finalJSONObject = (JSONObject) parser.parse(finalJSONString4);

			System.out.println("jsonTestData " + finalJSONObject);
			listOfTestRunData.add(finalJSONObject);
		}
//		return webClientService(listOfTestRunData, customer_uri);
		return webClientService(listOfTestRunData, "http://localhost:8080");
//		return listOfTestRunData;
	}
}