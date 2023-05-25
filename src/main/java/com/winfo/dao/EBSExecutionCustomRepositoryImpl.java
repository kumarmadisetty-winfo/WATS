package com.winfo.dao;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.winfo.controller.JobController;

@Repository
public class EBSExecutionCustomRepositoryImpl implements EBSExecutionCustomRepository {

	private static final String EXCEPTION_MSG = "Some thing went wrong while getting script steps data for executing TestRun : %s";
//	@PersistenceContext
//    private EntityManager entityManager;

//    @Override
//    public String findProfileByKeySkills(Set<String> skillSet) {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<ProfileEntity> query = cb.createQuery(ProfileEntity.class);
//        Root<ProfileEntity> profileRoot = query.from(ProfileEntity.class);
//
//        Path<String> skillPath = profileRoot.get("keyskills");
//
//        List<Predicate> predicates = new ArrayList<>();
//        for (String skill : skillSet) {
//        	String sk="%"+skill+"%";
//            predicates.add(cb.like(skillPath, sk));
//        }
//        query.select(profileRoot)
//            .where(cb.or(predicates.toArray(new Predicate[predicates.size()])));
//
//        return entityManager.createQuery(query)
//            .getResultList();
//    }
//    @Override
//    public String findByConfigurationId(int configId,String column){
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<ConfigLines> query = cb.createQuery(ConfigLines.class);
//        Root<ConfigLines> profileRoot = query.from(ConfigLines.class);
//
//        Path<String> skillPath = profileRoot.get("configurationId");
//        Path<String> skillPath1 = profileRoot.get("key");
//        List<Predicate> predicates = new ArrayList<>();
//        
//            predicates.add(cb.equal(skillPath, configId));
//       
//        query.select(profileRoot.get(column))
//            .where(cb.or(predicates.toArray(new Predicate[predicates.size()])));
//
//        return entityManager.createQuery(query)
//            .getSingleResult().toString();
//    }
	public static final Logger logger = Logger.getLogger(EBSExecutionCustomRepositoryImpl.class);
	@Autowired
	private EntityManager entityManager;

	public static final String NULL_STRING = "null";

	public Session getSession() {
		return entityManager.unwrap(Session.class);
	}

	/**
	 * This method returns all the data w.r.t a test run .i.e. TestRunScript data ,
	 * TEstRunScriptParam Data , TEstRun related Propject and customer Data.
	 * 
	 * @param testRunId
	 * @return listOfTestRunExecutionVo
	 */
	@Transactional
	public String findByConfigurationId(int testrunId, String column) {
		logger.info("start getTestRunScriptStepsData method in {}" + this.getClass());
		String resultList = "";
		String sql = "SELECT value_name from WIN_TA_CONFIG_LINES WHERE configuration_id in (select configuration_id from win_ta_test_set where test_set_id="
				+ testrunId + ") AND key_name='" + column + "'";

		try {
			Query query = getSession().createSQLQuery(sql);
			resultList = query.getSingleResult().toString();
		} catch (Exception e) {
			logger.error(String.format(EXCEPTION_MSG, testrunId));
		}
		logger.info("exit method getTestRunScriptStepsData in {}" + this.getClass());
		return resultList;
	}

	@Transactional
	public String findByTestRunScriptId(int testRunScriptParamId, String inputParamName) {
		String resultList = "";
		String sql = "SELECT input_value from win_ta_test_set_script_param WHERE TEST_SCRIPT_PARAM_ID="
				+ testRunScriptParamId;

		try {
			Query query = getSession().createSQLQuery(sql);
			resultList = query.getSingleResult().toString();
		} catch (Exception e) {
			logger.error(String.format(EXCEPTION_MSG, testRunScriptParamId));
		}
		return resultList;
	}

	@Transactional
	public String findByTestRunScriptIdInputParam(int testRunScriptParamId, String inputParamName) {
		String resultList = "";
		String sql = "SELECT input_parameter from win_ta_test_set_script_param WHERE TEST_SCRIPT_PARAM_ID="
				+ testRunScriptParamId;

		try {
			Query query = getSession().createSQLQuery(sql);
			resultList = query.getSingleResult().toString();
		} catch (Exception e) {
			logger.error(String.format(EXCEPTION_MSG, testRunScriptParamId));
		}
		return resultList;
	}

}
