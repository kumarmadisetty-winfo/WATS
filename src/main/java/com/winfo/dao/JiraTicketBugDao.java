package com.winfo.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.winfo.model.TestSet;
import com.winfo.model.TestSetLine;
import com.winfo.repository.TestSetRepository;
import com.winfo.repository.UserSchedulerJobRepository;
import com.winfo.vo.TestRunVO;

@SuppressWarnings("unchecked")
@Repository
public class JiraTicketBugDao {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private UserSchedulerJobRepository userSchedulerJobRepository;
	
	@Autowired
	private TestSetRepository testSetRepository;

	public List<Object> createJiraTicket(Integer testsetid, List<Integer> scriptIds, Integer testSetLineId, int jobId)

	{
		Session session = entityManager.unwrap(Session.class);
		Query<?> fetchsummary = null;
		if (!scriptIds.isEmpty()) {
			fetchsummary = session.createQuery(
					"select ts.testRunId,tsl.scriptId ,tsl.seqNum,tsl.issueKey,ts.testRunName,tsl.testRunScriptId,tsl.status,ts.configurationId,tsl.scriptNumber,mas.scenarioName from TestSet ts,TestSetLine tsl,ScriptMaster mas where ts.testRunId  = tsl.testRun.testRunId and tsl.scriptId = mas.scriptId and  ts.testRunId =(:testsetId) AND tsl.scriptId in (:scriptidlist)");
			fetchsummary.setParameter("testsetId", testsetid);
			fetchsummary.setParameterList("scriptidlist", scriptIds);
		} else if (testSetLineId != 0) {
			fetchsummary = session.createQuery(
					"select ts.testRunId,tsl.scriptId ,tsl.seqNum,tsl.issueKey,ts.testRunName,tsl.testRunScriptId,tsl.status,ts.configurationId,tsl.scriptNumber,mas.scenarioName from TestSet ts,TestSetLine tsl,ScriptMaster mas where ts.testRunId  = tsl.testRun.testRunId and tsl.scriptId = mas.scriptId and  ts.testRunId =(:testsetId) AND tsl.testRunScriptId = (:testsetlineid)");
			fetchsummary.setParameter("testsetId", testsetid);
			fetchsummary.setParameter("testsetlineid", testSetLineId);
		} else if (jobId != 0) {
			List<String> scheduleTestRunNames=userSchedulerJobRepository.getTestSetNames(jobId);
			List<TestSet> testRuns=testSetRepository.findByTestRunNameIn(scheduleTestRunNames);
			List<Integer> testRunIds=testRuns.parallelStream().map(TestSet::getTestRunId).collect(Collectors.toList());
			scriptIds = testRuns.parallelStream()
				    .flatMap(testSet -> testSet.getTestRunScriptDatalist().parallelStream()
				        .filter(testSetLine -> "Fail".equalsIgnoreCase(testSetLine.getStatus()))
				        .map(TestSetLine::getScriptId)).distinct().collect(Collectors.toList());
			fetchsummary = session.createQuery(
					"select ts.testRunId,tsl.scriptId ,tsl.seqNum,tsl.issueKey,ts.testRunName,tsl.testRunScriptId,tsl.status,ts.configurationId,tsl.scriptNumber,mas.scenarioName from TestSet ts,TestSetLine tsl,ScriptMaster mas where ts.testRunId  = tsl.testRun.testRunId and tsl.scriptId = mas.scriptId and  ts.testRunId in (:testsetIds) AND tsl.scriptId in (:scriptidlist)");
			fetchsummary.setParameter("testsetIds", testRunIds);
			fetchsummary.setParameterList("scriptidlist", scriptIds);
		}
		List<Object> summaryresult = new ArrayList<>();
		if (fetchsummary != null) {
			summaryresult = (List<Object>) fetchsummary.list();
		}
		return summaryresult;
	}

	public List<String> createDescription(TestRunVO slist) {
		Session session = entityManager.unwrap(Session.class);
		Query<?> fetchdescription = session.createQuery(
				"select a.lineErrorMessage from TestSetScriptParam a join  a.testSetLine b  on a.scriptId=b.scriptId  join b.testRun c on c.testRunId="
						+ slist.getTestSetId() + " and b.scriptId=" + slist.getScriptId()
						+ " and upper(a.lineExecutionStatus)='FAIL'");

		return (List<String>) fetchdescription.list();
	}

	public List<String> getJiraIssueUrl(Integer configurationId) {
		Session session = entityManager.unwrap(Session.class);
		Query<?> fetchjiraissueurl = session
				.createQuery("select jira_issue_url from ConfigTable where configuration_id=" + configurationId);

		return (List<String>) fetchjiraissueurl.list();
	}

	public int updateIssueKey(String issueKey, TestRunVO slist, int count) {
		Session session = entityManager.unwrap(Session.class);
		Query<?> updateissuekey = session.createQuery("update TestSetLine a set a.issueKey='" + issueKey
				+ "'  where a.testRunScriptId=" + slist.getTestSetLineId());
		count += updateissuekey.executeUpdate();
		return count;
	}

}
