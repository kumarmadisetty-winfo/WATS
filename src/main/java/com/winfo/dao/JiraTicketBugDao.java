package com.winfo.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.winfo.vo.TestRunVO;

@Repository
public class JiraTicketBugDao {

	@Autowired
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<Object> createJiraTicket(Integer testsetid, List<Integer> scriptIds, int testSetLineId)

	{
		Session session = entityManager.unwrap(Session.class);
		Query<?> fetchsummary = null;
		if (!scriptIds.isEmpty()) {
			fetchsummary = session.createQuery(
					"select ts.test_set_id,tsl.script_id ,tsl.seq_num,tsl.issue_key,ts.test_set_name,tsl.test_set_line_id,tsl.status,ts.configuration_id,tsl.script_number,mas.scenario_name from TestSet ts,TestSetLines tsl,ScriptMaster mas where ts.test_set_id  = tsl.testSet.test_set_id and tsl.script_id = mas.script_id and  ts.test_set_id =(:testsetId) AND tsl.script_id in (:scriptidlist)");
			fetchsummary.setParameter("testsetId", testsetid);
			fetchsummary.setParameterList("scriptidlist", scriptIds);
		} else if (testSetLineId != 0) {
			fetchsummary = session.createQuery(
					"select ts.test_set_id,tsl.script_id ,tsl.seq_num,tsl.issue_key,ts.test_set_name,tsl.test_set_line_id,tsl.status,ts.configuration_id,tsl.script_number,mas.scenario_name from TestSet ts,TestSetLines tsl,ScriptMaster mas where ts.test_set_id  = tsl.testSet.test_set_id and tsl.script_id = mas.script_id and  ts.test_set_id =(:testsetId) AND tsl.test_set_line_id = (:testsetlineid)");
			fetchsummary.setParameter("testsetId", testsetid);
			fetchsummary.setParameter("testsetlineid", testSetLineId);
		}
		List<Object> summaryresult = new ArrayList<>();
		if (fetchsummary != null) {
			summaryresult = (List<Object>) fetchsummary.list();
		}
		return summaryresult;
	}

	@SuppressWarnings("unchecked")
	public List<String> createDescription(TestRunVO slist) {
		Session session = entityManager.unwrap(Session.class);
		Query<?> fetchdescription = session.createQuery(
				"select a.line_error_message from TestSetScriptParam a join  a.testSetLines b  on a.script_id=b.script_id  join b.testSet c on c.test_set_id="
						+ slist.getTest_set_id() + " and b.script_id=" + slist.getScript_id()
						+ " and upper(a.line_execution_status)='FAIL'");

		return (List<String>) fetchdescription.list();
	}

	@SuppressWarnings("unchecked")
	public List<String> getJiraIssueUrl(Integer configurationId) {
		Session session = entityManager.unwrap(Session.class);
		Query<?> fetchjiraissueurl = session
				.createQuery("select jira_issue_url from ConfigTable where configuration_id=" + configurationId);
	
		return (List<String>) fetchjiraissueurl.list();
	}

	public int updateIssueKey(String issueKey, TestRunVO slist, int count) {
		Session session = entityManager.unwrap(Session.class);
		Query<?> updateissuekey = session.createQuery("update TestSetLines a set a.issue_key='" + issueKey
				+ "'  where a.test_set_line_id=" + slist.getTest_set_line_id());
		count += updateissuekey.executeUpdate();
		return count;
	}

}
