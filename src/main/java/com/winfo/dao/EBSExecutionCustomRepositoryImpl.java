package com.winfo.dao;

import com.winfo.dao.EBSExecutionCustomRepository;
import com.winfo.model.CodeLines;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
@Repository
public class EBSExecutionCustomRepositoryImpl implements EBSExecutionCustomRepository {

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
	private static final Logger log = LogManager.getLogger(EBSExecutionCustomRepositoryImpl.class);
	@Autowired
	private EntityManager entityManager;
	
	public static final  String NULL_STRING="null";
	
	public Session  getSession()
	{
		return entityManager.unwrap(Session.class);
	}
	/**This method returns all the data w.r.t a test run .i.e. TestRunScript data , TEstRunScriptParam Data , TEstRun related Propject and customer Data.
	 *@param testRunId 
	 *@return listOfTestRunExecutionVo
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	
	public String findByConfigurationId(int testrunId,String column)
	{
		log.info("start getTestRunScriptStepsData method in "+this.getClass());
		String resultList ="";
//		String sql = "SELECT value_name from WIN_TA_CONFIG_LINES WHERE configuration_id="+configId+" AND key_name='"+column+"'";
		String sql = "SELECT value_name from WIN_TA_CONFIG_LINES WHERE configuration_id in (select configuration_id from win_ta_test_set where test_set_id="+testrunId+") AND key_name='"+column+"'";

		try
		{
			Query query = getSession().createSQLQuery(sql);
			 resultList =  query.getSingleResult().toString();	
		}
		catch(Exception e)
		{
			log.error("Some thing went wrong while getting script steps data for executing TestRun : "+testrunId);
		}
		log.info("exit method getTestRunScriptStepsData in "+this.getClass());
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	
	public String findByTestRunScriptId(int TestRunScriptParamId,String inputParamName)
	{
		String resultList ="";
		//String sql = "SELECT value_name from WIN_TA_CONFIG_LINES WHERE configuration_id="+configId+" AND key_name='"+column+"'";
		String sql = "SELECT input_value from win_ta_test_set_script_param WHERE TEST_SCRIPT_PARAM_ID="+TestRunScriptParamId;

		try
		{
			Query query = getSession().createSQLQuery(sql);
			 resultList =  query.getSingleResult().toString();	
		}
		catch(Exception e)
		{
			log.error("Some thing went wrong while getting script steps data for executing TestRun : "+TestRunScriptParamId);
		}
		return resultList;
	}
	@SuppressWarnings("unchecked")
	@Transactional
	
	public String findByTestRunScriptIdInputParam(int TestRunScriptParamId,String inputParamName)
	{
		String resultList ="";
		//String sql = "SELECT value_name from WIN_TA_CONFIG_LINES WHERE configuration_id="+configId+" AND key_name='"+column+"'";
		String sql = "SELECT input_parameter from win_ta_test_set_script_param WHERE TEST_SCRIPT_PARAM_ID="+TestRunScriptParamId;

		try
		{
			Query query = getSession().createSQLQuery(sql);
			 resultList =  query.getSingleResult().toString();	
		}
		catch(Exception e)
		{
			log.error("Some thing went wrong while getting script steps data for executing TestRun : "+TestRunScriptParamId);
		}
		return resultList;
	}
	
}
