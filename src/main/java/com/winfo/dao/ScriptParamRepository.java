package com.winfo.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Repository;

import com.winfo.model.TestSetLines;
import com.winfo.model.TestSetScriptParam;

@Repository
@RefreshScope
public class ScriptParamRepository {
	@PersistenceContext
	EntityManager em;
	
	@SuppressWarnings("unchecked")
	public List<String> getStepsStatusByScriptId(int testSetLineId) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TestSetScriptParam> from = cq.from(TestSetScriptParam.class);

		Predicate condition = cb.equal(from.get("testSetLines").get("testRunScriptId"), testSetLineId);
		cq.where(condition);
		Query query = em.createQuery(cq.select(from.get("lineExecutionStatus")));
		
		return query.getResultList();
	}
	
	public Integer findFirstStepIdInScript(String testSetLineId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
		Root<TestSetScriptParam> from = cq.from(TestSetScriptParam.class);
		Predicate condition = cb.equal(from.get("testSetLines").get("test_set_line_id"), testSetLineId);
		cq.select(from.get("test_script_param_id")).where(condition);
		cq.orderBy(cb.asc(from.get("line_number")));
		return em.createQuery(cq).setMaxResults(1).getSingleResult();

	}
	
	public void updatePassedScriptLineStatus(String testScriptParamId, String status, String message) {
		try {
			Query query = em.createQuery(
					"Update TestSetScriptParam set line_execution_status='" + status + "',line_error_message='"
							+ message + "' where test_script_param_id=" + "'" + testScriptParamId + "'");
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Map<String, TestSetScriptParam> getTestScriptMap(TestSetLines test_set_line) {
		String sql = "from TestSetScriptParam where testSetLines=:testSetLines";
		Query query = em.createQuery(sql);
		query.setParameter("testSetLines", test_set_line);
		List<TestSetScriptParam> testScriptParamList = query.getResultList();
		Map<String, TestSetScriptParam> map2 = new HashMap<String, TestSetScriptParam>();
		for (TestSetScriptParam scriptParam : testScriptParamList) {
			map2.put(String.valueOf(scriptParam.getLineNumber()), scriptParam);
		}
		// map.put(String.valueOf(test_set_line.getSeq_num()),map2);
		return map2;
	}

}
