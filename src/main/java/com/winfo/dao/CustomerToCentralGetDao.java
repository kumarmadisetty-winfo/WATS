package com.winfo.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.winfo.model.FetchData;
import com.winfo.model.FetchDataMetadata;
import com.winfo.model.ScriptMaster;
import com.winfo.model.ScriptMetaData;
import com.winfo.vo.LookUpCodeVO;
import com.winfo.vo.LookUpVO;
//import com.winfo.utils.StringUtils;
import com.winfo.vo.ScriptDtlsDto;
import com.winfo.vo.ScriptMasterDto;
import com.winfo.vo.ScriptMetaDataDto;

@Repository
public class CustomerToCentralGetDao {

	private final Logger logger = LogManager.getLogger(CustomerToCentralGetDao.class);
	private static final String API_VALIDATION = "API_VALIDATION";

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private DataBaseEntryDao dataBaseEntryDao;

	@SuppressWarnings("unchecked")
	public JSONObject scriptMetaDataList(ScriptDtlsDto scriptDtls) throws ParseException {

		Session session = entityManager.unwrap(Session.class);

		String productVersion = scriptDtls.getProductVersion();
		List<Integer> scriptIds = scriptDtls.getScriptId();

		int i = 0;
		JSONObject responseDetailsJson = new JSONObject();

		JSONArray jsonArrayMaster = new JSONArray();
		JSONObject jsonMaster = new JSONObject();
		for (i = 0; i < scriptIds.size(); i++) {
			int dependency = 0;
			int customerId = 0;

			Integer scriptId = scriptIds.get(i);
			logger.debug("Script Id " +scriptId);
			Query<?> query3 = session
					.createQuery("select product_version from ScriptMaster where script_id=" + scriptId);
			List<String> result2 = (List<String>) query3.list();

			String productVersionDb = result2.get(0);
			if (productVersion.equals(productVersionDb)) {

				Query<?> query = session.createQuery(
						"select script_id,script_number,process_area,sub_process_area,module,role,end2end_scenario,scenario_name,scenario_description,expected_result,selenium_test_script_name,selenium_test_method,dependency,product_version,standard_custom,test_script_status,author,created_by,creation_date,updated_by,update_date,customer_id,customisation_reference,attribute1,attribute2,attribute3,attribute4,attribute5,attribute6,attribute7,attribute8,attribute9,attribute10,priority,targetApplication from ScriptMaster where script_id="
								+ scriptId);
				Query<?> query1 = session.createQuery(
						"select  line_number,input_parameter,action,xpath_location,xpath_location1,created_by,creation_date,updated_by,update_date,step_desc,field_type,hint,script_number,datatypes,unique_mandatory,validation_type,validation_name   from ScriptMetaData where script_id="
								+ scriptId);
				List<Object> result = (List<Object>) query.list();
				List<FetchData> finalresult = new ArrayList<>();
				Iterator<?> itr = result.iterator();
				List<Object> result1 = (List<Object>) query1.list();
				List<FetchDataMetadata> finalresult1 = new ArrayList<>();
				Iterator<?> itr1 = result1.iterator();
				while (itr.hasNext()) {
					Object[] obj = (Object[]) itr.next();
					FetchData fetchData = new FetchData();
					if (String.valueOf(obj[1]).equals("null")) {
						fetchData.setScriptNumber(null);
					} else {
						fetchData.setScriptNumber(String.valueOf(obj[1]));
					}
					if (String.valueOf(obj[2]).equals("null")) {
						fetchData.setProcessArea(null);
					} else {
						fetchData.setProcessArea(String.valueOf(obj[2]));
					}
					if (String.valueOf(obj[3]).equals("null")) {
						fetchData.setSubProcessArea(null);
					} else {
						fetchData.setSubProcessArea(String.valueOf(obj[3]));
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
						fetchData.setEnd2endScenario(null);
					} else {
						fetchData.setEnd2endScenario(String.valueOf(obj[6]));
					}
					if (String.valueOf(obj[7]).equals("null")) {
						fetchData.setScenarioName(null);
					} else {
						fetchData.setScenarioName(String.valueOf(obj[7]));
					}
					if (String.valueOf(obj[8]).equals("null")) {
						fetchData.setScenarioDescription(null);
					} else {
						fetchData.setScenarioDescription(String.valueOf(obj[8]));
					}
					if (String.valueOf(obj[9]).equals("null")) {
						fetchData.setExpectedResult(null);
					} else {
						fetchData.setExpectedResult(String.valueOf(obj[9]));
					}
					if (String.valueOf(obj[10]).equals("null")) {
						fetchData.setSeleniumTestScriptName(null);
					} else {
						fetchData.setSeleniumTestScriptName(String.valueOf(obj[10]));
					}
					if (String.valueOf(obj[11]).equals("null")) {
						fetchData.setSeleniumTestMethod(null);
					} else {
						fetchData.setSeleniumTestMethod(String.valueOf(obj[11]));
					}
					
					if (String.valueOf(obj[12]).equals("null")) {
						dependency = 0;
					} else {
						dependency = 1;
						Integer scriptIdDependency = Integer.parseInt(String.valueOf(obj[12]));
						if (!scriptIds.contains(scriptIdDependency)) {
							scriptIds.add(scriptIdDependency);
						}
						Query<?> querydep = session
								.createQuery("select script_number from ScriptMaster where script_id=" + scriptId);
						List<String> depSname = (List<String>) querydep.list();

						String depScriptName = depSname.get(0);
						fetchData.setDependency(Integer.parseInt(String.valueOf(obj[12])));
						fetchData.setDependentScriptNum(depScriptName);

					}
					if (String.valueOf(obj[13]).equals("null")) {
						fetchData.setProductVersion(null);
					} else {
						fetchData.setProductVersion(String.valueOf(obj[13]));
					}

					if (String.valueOf(obj[14]).equals("null")) {
						fetchData.setStandardCustom(null);
					} else {
						fetchData.setStandardCustom(String.valueOf(obj[14]));
					}
					if (String.valueOf(obj[15]).equals("null")) {
						fetchData.setTestScriptStatus(null);
					} else {
						fetchData.setTestScriptStatus(String.valueOf(obj[15]));
					}
					if (String.valueOf(obj[16]).equals("null")) {
						fetchData.setAuthor(null);
					} else {
						fetchData.setAuthor(String.valueOf(obj[16]));
					}
					if (String.valueOf(obj[17]).equals("null")) {
						fetchData.setCreatedBy(null);
					} else {
						fetchData.setCreatedBy(String.valueOf(obj[17]));
					}

					fetchData.setCreationDate((Date) (obj[18]));
					if (String.valueOf(obj[19]).equals("null")) {
						fetchData.setUpdatedBy(null);
					} else {
						fetchData.setUpdatedBy(String.valueOf(obj[19]));
					}

					fetchData.setUpdateDate((Date) (obj[20]));
					if (String.valueOf(obj[21]).equals("null")) {
						customerId = 0;
					} else {
						customerId = 1;
						fetchData.setCustomerId(Integer.parseInt(String.valueOf(obj[21])));
					}
					if (String.valueOf(obj[22]).equals("null")) {
						fetchData.setCustomisationRefrence(null);
					} else {
						fetchData.setCustomisationRefrence(String.valueOf(obj[22]));
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
					if (String.valueOf(obj[34]).equals("null")) {
						fetchData.setAttribute10(null);
					} else {
						fetchData.setAttribute10(String.valueOf(obj[32]));
					}

					finalresult.add(fetchData);
				}

				while (itr1.hasNext()) {
					Object[] obj1 = (Object[]) itr1.next();
					FetchDataMetadata fetchDataMetadata = new FetchDataMetadata();
					fetchDataMetadata.setLineNumber(Integer.parseInt(String.valueOf(obj1[0])));
					if (String.valueOf(obj1[1]).equals("null")) {
						fetchDataMetadata.setInputParameter(null);
					} else {
						fetchDataMetadata.setInputParameter(String.valueOf(obj1[1]));
					}
					if (String.valueOf(obj1[2]).equals("null")) {
						fetchDataMetadata.setAction(null);
					} else {
						fetchDataMetadata.setAction(String.valueOf(obj1[2]));
					}
					if (String.valueOf(obj1[3]).equals("null")) {
						fetchDataMetadata.setXpathLocation(null);
					} else {
						fetchDataMetadata.setXpathLocation(String.valueOf(obj1[3]));
					}
					if (String.valueOf(obj1[4]).equals("null")) {
						fetchDataMetadata.setXpathLocation1(null);
					} else {
						fetchDataMetadata.setXpathLocation1(String.valueOf(obj1[4]));
					}
					if (String.valueOf(obj1[5]).equals("null")) {
						fetchDataMetadata.setCreatedBy(null);
					} else {
						fetchDataMetadata.setCreatedBy(String.valueOf(obj1[5]));
					}

					fetchDataMetadata.setCreationDate((Date) obj1[6]);
					if (String.valueOf(obj1[7]).equals("null")) {
						fetchDataMetadata.setUpdatedBy(null);
					} else {
						fetchDataMetadata.setUpdatedBy(String.valueOf(obj1[7]));
					}

					fetchDataMetadata.setUpdateDate(((Date) obj1[8]));
					if (String.valueOf(obj1[9]).equals("null")) {
						fetchDataMetadata.setStepDesc(null);
					} else {
						fetchDataMetadata.setStepDesc(String.valueOf(obj1[9]));
					}
					if (String.valueOf(obj1[10]).equals("null")) {
						fetchDataMetadata.setFieldType(null);
					} else {
						fetchDataMetadata.setFieldType(String.valueOf(obj1[10]));
					}
					if (String.valueOf(obj1[11]).equals("null")) {
						fetchDataMetadata.setHint(null);
					} else {
						fetchDataMetadata.setHint(String.valueOf(obj1[11]));
					}
					if (String.valueOf(obj1[12]).equals("null")) {
						fetchDataMetadata.setScriptNumber(null);
					} else {
						fetchDataMetadata.setScriptNumber(String.valueOf(obj1[12]));
					}
					if (String.valueOf(obj1[13]).equals("null")) {
						fetchDataMetadata.setDatatypes("NA");
					} else {
						fetchDataMetadata.setDatatypes(String.valueOf(obj1[13]));
					}
					if (String.valueOf(obj1[14]).equals("null")) {
						fetchDataMetadata.setUniqueMandatory("NA");
					} else {
						fetchDataMetadata.setUniqueMandatory(String.valueOf(obj1[14]));
					}
					if (String.valueOf(obj1[15]).equals("null")) {
						fetchDataMetadata.setValidationType("NA");
					} else {
						fetchDataMetadata.setValidationType(String.valueOf(obj1[15]));
					}
					if (String.valueOf(obj1[16]).equals("null")) {
						fetchDataMetadata.setValidationName("NA");
					} else {
						fetchDataMetadata.setValidationName(String.valueOf(obj1[16]));
					}

					finalresult1.add(fetchDataMetadata);
				}

				for (FetchData slist : finalresult) {
					jsonMaster.put("script_number", slist.getScriptNumber());
					jsonMaster.put("process_area", slist.getProcessArea());
					jsonMaster.put("sub_process_area", slist.getSubProcessArea());
					jsonMaster.put("module", slist.getModule());
					jsonMaster.put("role", slist.getRole());
					jsonMaster.put("scenario_name", slist.getScenarioName());
					jsonMaster.put("scenario_description", slist.getScenarioDescription());
					jsonMaster.put("product_version", slist.getProductVersion());
					jsonMaster.put("standard_custom", slist.getStandardCustom());
					jsonMaster.put("test_script_status", slist.getTestScriptStatus());
					jsonMaster.put("priority", slist.getPriority());
					jsonMaster.put("target_application", slist.getTargetApplication());
					if (customerId == 0) {
						jsonMaster.put("customer_id", null);
					} else {
						jsonMaster.put("customer_id", slist.getCustomerId());
					}

					if (dependency == 0) {
						jsonMaster.put("dependency", null);
						jsonMaster.put("dependent_script_num", null);
					} else {

						jsonMaster.put("dependency", slist.getDependency());
						jsonMaster.put("dependent_script_num", slist.getDependentScriptNum());

					}
					jsonMaster.put("end2end_scenario", slist.getEnd2endScenario());
					String expectedResult = slist.getExpectedResult();
					if (expectedResult == null) {
						jsonMaster.put("expected_result", null);
					} else {
						String str1 = "\"";
						String str2 = "\\\\";
						String str3 = str2 + "\"";
						String replaceQuotes = expectedResult.replace(str1, str3);
						jsonMaster.put("expected_result", replaceQuotes);
					}
					jsonMaster.put("selenium_test_script_name", slist.getSeleniumTestScriptName());
					jsonMaster.put("selenium_test_method", slist.getSeleniumTestMethod());
					jsonMaster.put("author", slist.getAuthor());
					jsonMaster.put("created_by", slist.getCreatedBy());
					Date date = slist.getCreationDate();
					DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
					String strDate = dateFormat.format(date);
					jsonMaster.put("creation_date", strDate);
					jsonMaster.put("updated_by", slist.getUpdatedBy());
					Date date1 = slist.getUpdateDate();
					String strDate1 = dateFormat.format(date1);
					jsonMaster.put("update_date", strDate1);
					jsonMaster.put("customatisation_refrence", slist.getCustomisationRefrence());
					jsonMaster.put("attribute1", slist.getAttribute1());
					jsonMaster.put("attribute2", slist.getAttribute2());
					jsonMaster.put("attribute3", slist.getAttribute3());
					jsonMaster.put("attribute4", slist.getAttribute4());
					jsonMaster.put("attribute5", slist.getAttribute5());
					jsonMaster.put("attribute6", slist.getAttribute6());
					jsonMaster.put("attribute7", slist.getAttribute7());
					jsonMaster.put("attribute8", slist.getAttribute8());
					jsonMaster.put("attribute9", slist.getAttribute9());
					jsonMaster.put("attribute10", slist.getAttribute10());
					jsonMaster.put("priority", slist.getPriority());

				}

				JSONArray jsonArrayMetaData = new JSONArray();
				JSONObject jsonMetadata = new JSONObject();
				for (FetchDataMetadata slist1 : finalresult1) {
					jsonMetadata.put("line_number", slist1.getLineNumber());
					jsonMetadata.put("input_parameter", slist1.getInputParameter());
					jsonMetadata.put("action", slist1.getAction());
					jsonMetadata.put("xpath_location", slist1.getXpathLocation());
					jsonMetadata.put("xpath_location1", slist1.getXpathLocation1());
					jsonMetadata.put("created_by", slist1.getCreatedBy());
					Date date = slist1.getCreationDate();
					DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
					String strDate = dateFormat.format(date);
					jsonMetadata.put("creation_date", strDate);
					jsonMetadata.put("updated_by", slist1.getUpdatedBy());
					Date date1 = slist1.getUpdateDate();
					String strDate1 = dateFormat.format(date1);
					jsonMetadata.put("update_date", strDate1);
					jsonMetadata.put("step_desc", slist1.getStepDesc());
					jsonMetadata.put("field_type", slist1.getFieldType());
					jsonMetadata.put("hint", slist1.getHint());
					jsonMetadata.put("script_number", slist1.getScriptNumber());
					jsonMetadata.put("datatypes", slist1.getDatatypes());
					jsonMetadata.put("unique_mandatory", slist1.getUniqueMandatory());
					jsonMetadata.put("validation_type", slist1.getValidationType());
					jsonMetadata.put("validation_name", slist1.getValidationName());

					jsonArrayMetaData.put(jsonMetadata.toString());
				}
				jsonMaster.put("MetaDataList", jsonArrayMetaData);
				jsonArrayMaster.put(jsonMaster.toString());
				responseDetailsJson.put("data", jsonArrayMaster);
			}
		}
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

		responseDetailsJson.put("data", jsonArrayMaster);
		String replaceSlash = responseDetailsJson.toString().replace("\\", "");
		String replaceQuotes = replaceSlash.replace(str1, str2);
		String finalJSONString = replaceQuotes.replace(str3, str4);
		String finalJSONString1 = finalJSONString.replace(str5, str6);
		String finalJSONString2 = finalJSONString1.replace(str7, str9);
		String finalJSONString3 = finalJSONString2.replace(str11 + " ", str12 + " ");
		String finalJSONString4 = finalJSONString3.replace(str11 + ",", str12 + ",");

		JSONParser parser = new JSONParser();

		return (JSONObject) parser.parse(finalJSONString4);
	}

	public List<ScriptMasterDto> fecthMetaDataList(ScriptDtlsDto scriptDtls) {
		List<Integer> scriptIds = scriptDtls.getScriptId();
		List<ScriptMasterDto> scriptMasterList = new ArrayList<>();
		for (int i = 0; i < scriptIds.size(); i++) {
			Integer scriptId = scriptIds.get(i);
			ScriptMaster scriptMasterDtls = dataBaseEntryDao.findScriptMasterByScriptId(scriptId);
			List<ScriptMetaData> scriptMetaDataList = dataBaseEntryDao.getScriptMetaDataList(scriptId);
			ScriptMasterDto scriptMasterDto = new ScriptMasterDto(scriptMasterDtls);
			List<ScriptMetaDataDto> scriptMetaDataListDto = new ArrayList<>();
			LookUpVO lookUpVo = null;
			String validationType = null;
			Map<String, LookUpCodeVO> lookUpCodeMap = new HashMap<>();
			for (ScriptMetaData scriptMetaData : scriptMetaDataList) {
				ScriptMetaDataDto scriptMetaDataDto = new ScriptMetaDataDto(scriptMetaData);
				scriptMetaDataListDto.add(scriptMetaDataDto);
				if(scriptMetaData.getValidationName() != null && !scriptMetaData.getValidationName().equalsIgnoreCase("NA") && API_VALIDATION.equalsIgnoreCase(scriptMetaData.getValidationType())) {
					LookUpCodeVO lookUpCodeObj = dataBaseEntryDao.getLookupCode(scriptMetaData.getValidationType(), scriptMetaData.getValidationName());
					lookUpCodeMap.put(scriptMetaData.getValidationName(), lookUpCodeObj);
					validationType = API_VALIDATION;
				}
			}
			if(API_VALIDATION.equals(validationType)) {
				lookUpVo = dataBaseEntryDao.getLookUp(validationType, lookUpCodeMap);
			}
			scriptMasterDto.setMetaDataList(scriptMetaDataListDto);
			scriptMasterDto.setLookUpVO(lookUpVo);
			scriptMasterList.add(scriptMasterDto);
		}
		return scriptMasterList;
	}
}
