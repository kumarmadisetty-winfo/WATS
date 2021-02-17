package com.winfo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import com.winfo.vo.DeleteScriptsData;

@Repository
public class DeleteDataDAO {

	@Autowired
	private EntityManager entityManager;
	public String deleteData(@RequestBody DeleteScriptsData deletescriptsdata) throws ParseException {
		Session session = entityManager.unwrap(Session.class);
		Transaction tx = session.beginTransaction();
		//String product_version=deletescriptsdata.getProduct_version();
		List<Integer> script_Ids= deletescriptsdata.getScript_id();
		int i=0;
		for(i=0;i<script_Ids.size();i++)
		{
			Integer script_Id=script_Ids.get(i);
			Query query=session.createQuery("delete from ScriptMaster where script_id="+script_Id);
			Query query1=session.createQuery("delete from ScriptMetaData where script_id="+script_Id);
			//Query query=session.createQuery("delete ScriptMaster from ScriptMaster  INNER JOIN  ScriptMetadata  on  ScriptMster.script_id=ScriptMetaData.script_id where script_id="+script_Id);
			
			query.executeUpdate();
		query1.executeUpdate();
			
			
		}
		
		tx.commit();
		
		return "{\"status\":200,\"statusMessage\":\"SUCCESS\",\"description\":\"Deleted Successfully\"}";
	}

}
