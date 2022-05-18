package com.winfo.dao;

import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Repository;

@Repository
@RefreshScope
public class ExecutionHistoryRepository {

	@PersistenceContext
	EntityManager em;
	
	
	public void updateExecHistoryTbl(String testSetLineId, Date startDate, Date endDate, String status) {
		Format dateFormat = new SimpleDateFormat("M/dd/yyyy HH:mm:ss");
		String startTime = dateFormat.format(startDate);
		String endTime = dateFormat.format(endDate);
		try {
			Session session = em.unwrap(Session.class);
			int nextExecNo = getNextExecutionNum();
			String instQry = "INSERT INTO WIN_TA_EXECUTION_HISTORY (EXECUTION_ID, TEST_SET_LINE_ID, EXECUTION_START_TIME, EXECUTION_END_TIME, STATUS) VALUES ('"
					+ (nextExecNo) + "','" + testSetLineId + "'," + "TO_TIMESTAMP('"+startTime+"','MM/DD/YYYY HH24:MI:SS')"+ "," +"TO_TIMESTAMP('"+endTime+"','MM/DD/YYYY HH24:MI:SS')" + ",'" + status + "')";
			System.out.println(instQry);
			Query instQuery = session.createSQLQuery(instQry);
			instQuery.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getNextExecutionNum() {
		Session session = em.unwrap(Session.class);
		String sql = "SELECT WIN__TA_EXECUTION_ID_SEQ.NEXTVAL FROM DUAL";
		SQLQuery query = session.createSQLQuery(sql);

		List results = query.list();
		if (results.size() > 0) {
			System.out.println(results.get(0));

			BigDecimal bigDecimal = (BigDecimal) results.get(0);
			Integer id = Integer.parseInt(bigDecimal.toString());
			return id;
		} else {
			return 0;
		}
	}
}
