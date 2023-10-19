package com.winfo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.winfo.exception.WatsEBSException;
import com.winfo.model.ScriptMaster;
import com.winfo.model.TestSet;
import com.winfo.repository.ProjectRepository;
import com.winfo.repository.ScriptMasterRepository;
import com.winfo.repository.ScriptMetaDataRepository;
import com.winfo.repository.TestSetLinesRepository;
import com.winfo.repository.TestSetRepository;
import com.winfo.repository.TestSetScriptParamRepository;
import com.winfo.repository.UserRoleRepository;
import com.winfo.vo.DomGenericResponseBean;

@SuppressWarnings({ "deprecation", "unchecked" })
@Repository


public class WatsPluginDao {
	public static final Logger logger = Logger.getLogger(WatsPluginDao.class);
	private static final String USER_ID = "userId";

	private final EntityManager entityManager;
	private final ScriptMasterRepository scriptMasterRepository;
	private final UserRoleRepository userRoleRepository;
	private final TestSetRepository testSetRepository;
	private final TestSetLinesRepository testSetLinesRepository;
	private final ProjectRepository projectRepository;

	public WatsPluginDao(EntityManager entityManager, ScriptMasterRepository scriptMasterRepository,
			UserRoleRepository userRoleRepository, TestSetRepository testSetRepository,
			TestSetLinesRepository testSetLinesRepository, ProjectRepository projectRepository) {
		this.entityManager = entityManager;
		this.scriptMasterRepository = scriptMasterRepository;
		this.userRoleRepository = userRoleRepository;
		this.testSetRepository = testSetRepository;
		this.testSetLinesRepository = testSetLinesRepository;
		this.projectRepository = projectRepository;
	}
	

	public List<String> getScriptNumber(String processArea, String module) {
		 logger.info("processArea"+processArea);
		 logger.info("module"+module);
		// List<String> results = scriptMasterRepository.findByProcessAreaAndModuleOrderByScriptNumberDesc(processArea, module).get(0).getScriptNumber();
		 List<String> results = scriptMasterRepository.findByProcessAreaAndModuleOrderByScriptNumberDesc(processArea, module);
	    logger.info("results"+results);
//	    for (String scriptNumber : results) {
//	    	logger.info("results"+results);
//	        logger.info("scriptNumber: " + scriptNumber);
//	    }
	    if (!results.isEmpty()) {
			logger.info("Script No"+results);		
			return results;
		} else {
			return new ArrayList<>();
			
		} 
	}


	public String getUserIdValidation(String username) {
		String userId = username.toUpperCase();
		List<String> results=userRoleRepository.getUserId(userId);
		if (!results.isEmpty()) {
			logger.info("USER ID " + results.get(0));
			return results.get(0);
		} else {
			return null;
		}
	}

	public String verifyEndDate(String username) {
		String userId = username.toUpperCase();
		List<String> results=userRoleRepository.getEndDate(userId);
		if (!results.isEmpty()) {
			logger.info("Verify EndDate for USER " + results.get(0));
			return results.get(0);
		} else {
			return null;
		}
	}

	public String verifyPasswordExpire(String username) {
		String userId = username.toUpperCase();
		List<String> results =userRoleRepository.getPasswordExpiry(userId) ;
		if (!results.isEmpty()) {
			logger.info("Verify Password Expire for user " + results.get(0));
			return results.get(0);
		} else {
			return null;
		}
	}

	public String verifyUserActive(String username) {
		String userId = username.toUpperCase();
		List<String> results =userRoleRepository.findUserIdByUserIdAndStatus(userId,"ACTIVE");
		if (!results.isEmpty()) {
			logger.info("Verify User Active " + results.get(0));
			return results.get(0);
		} else {
			return null;
		}
	}

	public String getEncriptPassword(String username) {
		String userId = username.toUpperCase();
		Session session = entityManager.unwrap(Session.class);
		String sql = "SELECT TOOLKIT.DECRYPT(PASSWORD) FROM WIN_TA_USER_ROLE WHERE UPPER(USER_ID) =:userId ";
		SQLQuery<String> query = session.createSQLQuery(sql);
		query.setParameter(USER_ID, userId);
		List<String> results = query.list();
		if (!results.isEmpty()) {
			return results.get(0);
		} else {
			return null;
		}
	}

	public List<String> getTestrunData() {
		List<String> results =testSetRepository.getTestRun();
		if (!results.isEmpty()) {
			return results;
		} else {
			return new ArrayList<>();
		}
	}

	public int getTestSetId() {
		Session session = entityManager.unwrap(Session.class);
		String sql = "SELECT WATS_PROD.win_ta_test_set_id_seq.nextval FROM DUAL";
		SQLQuery<?> query = session.createSQLQuery(sql);
		List<?> results = query.list();
		if (!results.isEmpty()) {
			logger.info("TestSet Id " + results.get(0));
			BigDecimal bigDecimal = (BigDecimal) results.get(0);
			return Integer.parseInt(bigDecimal.toString());
		} else {
			return 0;
		}
	}

	public int getTestSetLineId() {
		Session session = entityManager.unwrap(Session.class);
		String sql = "SELECT win_ta_test_set_line_seq.NEXTVAL FROM DUAL";
		SQLQuery<?> query = session.createSQLQuery(sql);
		List<?> results = query.list();
			if (!results.isEmpty()) {
			logger.info("TestSet Line Id " + results.get(0));
			BigDecimal bigDecimal = (BigDecimal) results.get(0);
			Integer id = Integer.parseInt(bigDecimal.toString());
			logger.info("TestSet Line Id " + id);
			return id;
		} else {
			return 0;
		}
	}

	public int getParamId() {
		Session session = entityManager.unwrap(Session.class);
		String sql = "SELECT win_ta_param_id_seq.NEXTVAL FROM DUAL";
		SQLQuery<?> query = session.createSQLQuery(sql);
		List<?> results = query.list();
		if (!results.isEmpty()) {
			logger.info("Get Param Id " + results.get(0));
			BigDecimal bigDecimal = (BigDecimal) results.get(0);
			return Integer.parseInt(bigDecimal.toString());
		} else {
			return 0;
		}
	}

	public int getMasterScriptId() {
		Session session = entityManager.unwrap(Session.class);
		String sql = "SELECT WIN_TA_SCRIPT_MASTER_SEQ.NEXTVAL FROM DUAL";
		SQLQuery<?> query = session.createSQLQuery(sql);
		List<?> results = query.list();
		if (!results.isEmpty()) {
			logger.info("Get Master Script Id " + results.get(0));
			BigDecimal bigDecimal = (BigDecimal) results.get(0);
			Integer id = Integer.parseInt(bigDecimal.toString());
			return id;
		} else {
			return 0;
		}
	}

	public int getMetaDataId() {
		Session session = entityManager.unwrap(Session.class);
		String sql = "SELECT WIN_TA_SCRIPT_METADATA_SEQ.NEXTVAL FROM DUAL";
		SQLQuery<?> query = session.createSQLQuery(sql);
		List<?> results = query.list();
		if (!results.isEmpty()) {
			logger.info("Get MetaData Id " + results.get(0));
			BigDecimal bigDecimal = (BigDecimal) results.get(0);
			return Integer.parseInt(bigDecimal.toString());
		} else {
			return 0;
		}
	}

	public TestSet getTestrunData(int testSetId) {
		return entityManager.find(TestSet.class, testSetId);
	}

	public int getTestSetId(String testsetName) {
		int results=testSetRepository.findTestRunIdByTestRunName(testsetName);
		//logger.info("ggggggggg"+testsetName);
		if (results!=0) {
			logger.info("Get TestSet Id " + results);
			return results;
		} else {
			return 0;
		}
	}

	public int getSeqNum(int testSetId) {
		List<?> results=testSetLinesRepository.getSequenceNumber(testSetId);
		if (!results.isEmpty()) {
			logger.info("Get sequence number " +results.get(0));
			BigDecimal bigDecimal = (BigDecimal) results.get(0);
			return Integer.parseInt(bigDecimal.toString());
		} else {
			return 0;
		}
	}

	public void updateTestrun(TestSet getTestrun) {
		Session session = entityManager.unwrap(Session.class);
		session.update(getTestrun);
	}

	public DomGenericResponseBean pluginData(ScriptMaster master, String scriptnumber) {
		DomGenericResponseBean response = new DomGenericResponseBean();
		Session session = entityManager.unwrap(Session.class);
		session.save(master);
		response.setStatus(HttpStatus.OK.value());
		response.setStatusMessage("NewScriptNumber:" + scriptnumber);
		return response;
	}

	public List<String> getTestRunDataProductVersion(String productVersion) {
		return projectRepository.getTestRunData(productVersion);
	}

	public String getDirectoryPath() {
		try {
			Session session = entityManager.unwrap(Session.class);
			Query query = session.createQuery("select a.allDirectoriesEmbeddedPK.directoryPath from AllDirectories a where a.allDirectoriesEmbeddedPK.directoryName = 'WATS_OBJ_DIR'");
			return query.getSingleResult().toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Directory path is not present", e);
		}
	}
}
