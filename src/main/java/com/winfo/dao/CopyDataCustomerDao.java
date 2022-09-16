package com.winfo.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.winfo.model.ScriptMaster;
import com.winfo.model.ScriptMetaData;
import com.winfo.vo.CopyDataDetails;
import com.winfo.vo.DomGenericResponseBean;

@Repository
public class CopyDataCustomerDao {

	private static final String SCRIPT_DTL_QRY = "select %s from %s where product_version='%s' and standard_custom='%s'";
	private static final String SCRIPT_MASTER = "ScriptMaster";
	private static final String CUSTOM = "Custom";

	@Autowired
	private EntityManager entityManager;
	@Autowired
	private CopyDataPostCustomerDao dao;

	@SuppressWarnings("unchecked")
	public List<DomGenericResponseBean> copyData(CopyDataDetails copyDataDetails) {

		String productVersionOld = copyDataDetails.getProduct_version_old();
		String productVersionNew = copyDataDetails.getProduct_version_new();
		Session session = entityManager.unwrap(Session.class);
		int i;
		int j = 0;
		int count = 0;

		String sqlQry = String.format(SCRIPT_DTL_QRY, "script_number", SCRIPT_MASTER, productVersionOld, CUSTOM);

		Query<?> query3 = session.createQuery(sqlQry);

		List<String> scriptNumbersOld = (List<String>) query3.list();

		sqlQry = String.format(SCRIPT_DTL_QRY, "script_number", SCRIPT_MASTER, productVersionNew, CUSTOM);

		Query<?> query4 = session.createQuery(sqlQry);

		List<String> scriptNumbersNew = (List<String>) query4.list();

		sqlQry = String.format(SCRIPT_DTL_QRY, "script_id", SCRIPT_MASTER, productVersionOld, CUSTOM);

		Query<?> querySid = session.createQuery(sqlQry);

		List<Integer> scriptIdsOld = (List<Integer>) querySid.list();

		sqlQry = String.format(SCRIPT_DTL_QRY, "script_id", SCRIPT_MASTER, productVersionNew, CUSTOM);

		Query<?> querysid1 = session.createQuery(sqlQry);
		List<Integer> scriptIdsNew = (List<Integer>) querysid1.list();

		for (j = 0; j < scriptNumbersNew.size(); j++) {
			if (scriptNumbersOld.contains(scriptNumbersNew.get(j))) {
				scriptNumbersOld.remove(scriptNumbersNew.get(j));
				scriptIdsOld.remove(scriptIdsNew.get(j));
			}
		}

		List<String> wrongDependencyScripts = new ArrayList<>();
		for (i = 0; i < scriptNumbersOld.size(); i++) {
			Integer oldScriptId = scriptIdsOld.get(i);
			String scriptNum = scriptNumbersOld.get(i);

			Query<?> query = session.createQuery(
					"select script_id,script_number,process_area,sub_process_area,module,role,end2end_scenario,scenario_name,scenario_description,expected_result,selenium_test_script_name,selenium_test_method,dependency,product_version,standard_custom,test_script_status,author,created_by,creation_date,updated_by,update_date,customer_id,customisation_reference,attribute1,attribute2,attribute3,attribute4,attribute5,attribute6,attribute7,attribute8,attribute9,attribute10,priority,pluginFlag,targetApplication from ScriptMaster where script_number='"
							+ scriptNum + "' and product_version='" + productVersionOld + "'");
			List<Object> result = (List<Object>) query.list();
			Iterator<?> itr6 = result.iterator();
			Integer scriptId = null;
			while (itr6.hasNext()) {
				Object[] obj1 = (Object[]) itr6.next();
				if (!String.valueOf(obj1[0]).equals("null")) {
					scriptId = Integer.parseInt(String.valueOf(obj1[0]));
				}
				break;
			}

			Query<?> query1 = session.createQuery(
					"select  line_number,input_parameter,action,xpath_location,xpath_location1,created_by,creation_date,updated_by,update_date,step_desc,field_type,hint,script_number,datatypes,unique_mandatory,validation_type,validation_name,metadataInputValue from ScriptMetaData where script_id="
							+ scriptId);
			List<Object> result1 = (List<Object>) query1.list();
			Iterator<?> itr1 = result1.iterator();
			Iterator<?> itr = result.iterator();
			Iterator<?> itr5 = result.iterator();
			Integer dep = null;
			while (itr5.hasNext()) {
				Object[] obj1 = (Object[]) itr5.next();
				if (!String.valueOf(obj1[12]).equals("null")) {
					dep = Integer.parseInt(String.valueOf(obj1[12]));
				}
				break;
			}

			if (dep != null) {

				Query<?> querydependency = session
						.createQuery("select product_version,standard_custom from ScriptMaster where script_id=" + dep);
				List<String> productversionlist = (List<String>) querydependency.list();
				String productversion = productversionlist.get(0);
				String standardcustom = productversionlist.get(1);
				if (!productversion.equals(productVersionOld) || !standardcustom.equals(CUSTOM)) {
					wrongDependencyScripts.add(scriptNum);
					continue;
				}
			}

			while (itr.hasNext()) {
				Object[] obj = (Object[]) itr.next();

				ScriptMaster master = new ScriptMaster();
				if (String.valueOf(obj[1]).equals("null")) {
					master.setScriptNumber(null);
				} else {

					master.setScriptNumber(String.valueOf(obj[1]));
				}
				if (String.valueOf(obj[2]).equals("null")) {
					master.setProcessArea(null);
				} else {

					master.setProcessArea(String.valueOf(obj[2]));
				}
				if (String.valueOf(obj[3]).equals("null")) {
					master.setSubProcessArea(null);
				} else {

					master.setSubProcessArea(String.valueOf(obj[3]));
				}
				if (String.valueOf(obj[4]).equals("null")) {
					master.setModule(null);
				} else {

					master.setModule(String.valueOf(obj[4]));
				}
				if (String.valueOf(obj[5]).equals("null")) {
					master.setRole(null);
				} else {

					master.setRole(String.valueOf(obj[5]));
				}
				if (String.valueOf(obj[6]).equals("null")) {
					master.setEnd2endScenario(null);
				} else {

					master.setEnd2endScenario(String.valueOf(obj[6]));
				}
				if (String.valueOf(obj[7]).equals("null")) {
					master.setScenarioName(null);
				} else {

					master.setScenarioName(String.valueOf(obj[7]));
				}
				if (String.valueOf(obj[8]).equals("null")) {
					master.setScenarioDescription(null);
				} else {
					master.setScenarioDescription(String.valueOf(obj[8]));
				}
				if (String.valueOf(obj[9]).equals("null")) {
					master.setExpectedResult(null);
				} else {
					master.setExpectedResult(String.valueOf(obj[9]));
				}
				if (String.valueOf(obj[10]).equals("null")) {
					master.setSeleniumTestScriptName(null);
				} else {
					master.setSeleniumTestScriptName(String.valueOf(obj[10]));
				}
				if (String.valueOf(obj[11]).equals("null")) {
					master.setSeleniumTestMethod(null);
				} else {
					master.setSeleniumTestMethod(String.valueOf(obj[11]));
				}
				if (String.valueOf(obj[12]).equals("null")) {
					master.setDependency(null);
					master.setDependentScriptNum(null);
				} else {

					Integer scriptIdDependency = Integer.parseInt(String.valueOf(obj[12]));
					if (!scriptIdsOld.contains(scriptIdDependency)) {
						scriptIdsOld.add(scriptIdDependency);
					}
					Query<?> querydep = session
							.createQuery("select script_number from ScriptMaster where script_id=" + oldScriptId);
					List<String> depSname = (List<String>) querydep.list();

					String depScriptName = depSname.get(0);
					master.setDependency(Integer.parseInt(String.valueOf(obj[12])));
					master.setDependentScriptNum(depScriptName);

				}
				master.setProductVersion(productVersionNew);
				if (String.valueOf(obj[14]).equals("null")) {
					master.setStandardCustom(null);
				} else {
					master.setStandardCustom(String.valueOf(obj[14]));
				}
				if (String.valueOf(obj[15]).equals("null")) {
					master.setTestScriptStatus(null);
				} else {
					master.setTestScriptStatus(String.valueOf(obj[15]));
				}
				if (String.valueOf(obj[16]).equals("null")) {
					master.setAuthor(null);
				} else {
					master.setAuthor(String.valueOf(obj[16]));
				}
				if (String.valueOf(obj[17]).equals("null")) {
					master.setCreatedBy(null);
				} else {
					master.setCreatedBy(String.valueOf(obj[17]));
				}

				if (String.valueOf(obj[18]).equals("null")) {
					master.setCreationDate(null);
				} else {
					master.setCreationDate((Date) (obj[18]));
				}

				if (String.valueOf(obj[19]).equals("null")) {
					master.setUpdatedBy(null);
				} else {
					master.setUpdatedBy(String.valueOf(obj[19]));
				}

				if (String.valueOf(obj[20]).equals("null")) {
					master.setUpdateDate(null);
				} else {
					master.setUpdateDate((Date) (obj[20]));
				}

				if (String.valueOf(obj[21]).equals("null")) {

					master.setCustomerId(null);
				} else {

					master.setCustomerId(Integer.parseInt(String.valueOf(obj[21])));
				}
				if (String.valueOf(obj[22]).equals("null")) {
					master.setCustomisationReference(null);
				} else {
					master.setCustomisationReference(String.valueOf(obj[22]));
				}

				if (String.valueOf(obj[24]).equals("null")) {
					master.setAttribute2(null);
				} else {
					master.setAttribute2(String.valueOf(obj[24]));
				}
				if (String.valueOf(obj[25]).equals("null")) {
					master.setAttribute3(null);
				} else {
					master.setAttribute3(String.valueOf(obj[25]));
				}
				if (String.valueOf(obj[26]).equals("null")) {
					master.setAttribute4(null);
				} else {
					master.setAttribute4(String.valueOf(obj[26]));
				}
				if (String.valueOf(obj[27]).equals("null")) {
					master.setAttribute5(null);
				} else {
					master.setAttribute5(String.valueOf(obj[27]));
				}
				if (String.valueOf(obj[28]).equals("null")) {
					master.setAttribute6(null);
				} else {
					master.setAttribute6(String.valueOf(obj[28]));
				}
				if (String.valueOf(obj[29]).equals("null")) {
					master.setAttribute7(null);
				} else {
					master.setAttribute7(String.valueOf(obj[29]));
				}
				if (String.valueOf(obj[30]).equals("null")) {
					master.setAttribute8(null);
				} else {
					master.setAttribute8(String.valueOf(obj[30]));
				}
				if (String.valueOf(obj[31]).equals("null")) {
					master.setAttribute9(null);
				} else {
					master.setAttribute9(String.valueOf(obj[31]));
				}
				if (String.valueOf(obj[32]).equals("null")) {
					master.setAttribute10(null);
				} else {
					master.setAttribute10(String.valueOf(obj[32]));
				}

				master.setPriority(Integer.parseInt(String.valueOf(obj[33])));
				if (String.valueOf(obj[34]).equals("null")) {
					master.setPluginFlag(null);
				} else {
					master.setPluginFlag(String.valueOf(obj[34]));
				}
				if (String.valueOf(obj[35]).equals("null")) {
					master.setTargetApplication(null);
				} else {
					master.setTargetApplication(String.valueOf(obj[35]));
				}
				master.setApprForMigration(null);

				while (itr1.hasNext()) {
					Object[] obj1 = (Object[]) itr1.next();
					ScriptMetaData metadata = new ScriptMetaData();
					metadata.setLineNumber(Integer.parseInt(String.valueOf(obj1[0])));
					if (String.valueOf(obj1[1]).equals("null")) {
						metadata.setInputParameter(null);
					} else {
						metadata.setInputParameter(String.valueOf(obj1[1]));
					}
					if (String.valueOf(obj1[2]).equals("null")) {
						metadata.setAction(null);
					} else {
						metadata.setAction(String.valueOf(obj1[2]));
					}
					if (String.valueOf(obj1[3]).equals("null")) {
						metadata.setXpathLocation(null);
					} else {
						metadata.setXpathLocation(String.valueOf(obj1[3]));
					}
					if (String.valueOf(obj1[4]).equals("null")) {
						metadata.setXpathLocation1(null);
					} else {
						metadata.setXpathLocation1(String.valueOf(obj1[4]));
					}
					if (String.valueOf(obj1[5]).equals("null")) {
						metadata.setCreatedBy(null);
					} else {
						metadata.setCreatedBy(String.valueOf(obj1[5]));
					}

					metadata.setCreationDate((Date) obj1[6]);
					if (String.valueOf(obj1[7]).equals("null")) {
						metadata.setUpdatedBy(null);
					} else {
						metadata.setUpdatedBy(String.valueOf(obj1[7]));
					}

					metadata.setUpdateDate(((Date) obj1[8]));
					if (String.valueOf(obj1[9]).equals("null")) {
						metadata.setStepDesc(null);
					} else {
						metadata.setStepDesc(String.valueOf(obj1[9]));
					}
					if (String.valueOf(obj1[10]).equals("null")) {
						metadata.setFieldType(null);
					} else {
						metadata.setFieldType(String.valueOf(obj1[10]));
					}
					if (String.valueOf(obj1[11]).equals("null")) {
						metadata.setHint(null);
					} else {
						metadata.setHint(String.valueOf(obj1[11]));
					}
					if (String.valueOf(obj1[12]).equals("null")) {
						metadata.setScriptNumber(null);
					} else {
						metadata.setScriptNumber(String.valueOf(obj1[12]));
					}
					if (String.valueOf(obj1[13]).equals("null")) {
						metadata.setDatatypes("NA");
					} else {
						metadata.setDatatypes(String.valueOf(obj1[13]));
					}
					if (String.valueOf(obj1[14]).equals("null")) {
						metadata.setUniqueMandatory("NA");
					} else {
						metadata.setUniqueMandatory(String.valueOf(obj1[14]));
					}
					if (String.valueOf(obj1[15]).equals("null")) {
						metadata.setValidationType("NA");
					} else {
						metadata.setValidationType(String.valueOf(obj1[15]));
					}
					if (String.valueOf(obj1[16]).equals("null")) {
						metadata.setValidationName("NA");
					} else {
						metadata.setValidationName(String.valueOf(obj1[16]));
					}
					if (String.valueOf(obj1[17]).equals("null")) {
						metadata.setMetadataInputvalue("NA");
					} else {
						metadata.setMetadataInputvalue(String.valueOf(obj1[17]));
					}

					master.addMetadata(metadata);
				}
				dao.copyDataPost(master);
				count++;
			}

		}

		List<DomGenericResponseBean> bean = new ArrayList<>();
		DomGenericResponseBean response = new DomGenericResponseBean();
		if (count != 0) {
			response.setStatus(200);
			response.setStatusMessage("SUCCESS");
			response.setDescription(i + " Script'(s) Copied" + " to " + productVersionNew + " Successfully");

			bean.add(response);
		} else {
			response.setStatus(409);
			response.setStatusMessage("ERROR");
			response.setDescription("No New Scripts to be copied");
			bean.add(response);
		}
		return bean;

	}
}
