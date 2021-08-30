package com.winfo.dao;

import java.util.ArrayList;
import java.util.Iterator;
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
import com.winfo.vo.DomGenericResponseBean;

@Repository
public class DeleteDataDAO {

	@Autowired
	private EntityManager entityManager;
	public  List<DomGenericResponseBean> deleteData(@RequestBody DeleteScriptsData deletescriptsdata) throws ParseException {
		Session session = entityManager.unwrap(Session.class);
		Transaction tx = session.beginTransaction();
		int deleted = 0;
		int deleted1=0;
		
		String script_number=null;
		
		List<DomGenericResponseBean> bean = new ArrayList<DomGenericResponseBean>();
        List<Integer> script_Ids= deletescriptsdata.getScript_id();
        int i=0;
        int count=0;
        try {
        for(i=0;i<script_Ids.size();i++)
        {
        	 count=0;
            Integer script_Id=script_Ids.get(i);
            if(script_Ids.size()==1) {
            	Query query2=session.createQuery("select script_number from ScriptMaster where script_id="+script_Id);
            	script_number =(String) query2.getSingleResult();
            	
            }
            Query query=session.createQuery("delete from ScriptMaster where script_id="+script_Id);
            Query query1=session.createQuery("delete from ScriptMetaData where script_id="+script_Id);      
        
             deleted =query1.executeUpdate();
             deleted1 =query.executeUpdate();

            
         
            if(deleted==0 && deleted1==0) {
            	count=1;
            }
          
            }
        }
        catch(Exception e) {
        	e.printStackTrace();
        }
        DomGenericResponseBean response = new DomGenericResponseBean();
            if(count==1) {
            response.setStatus(404);
            response.setStatusMessage("ERROR");
            response.setDescription("Script Not Found");
            bean.add(response);
            }
            else {
                response.setStatus(200);
                response.setStatusMessage("SUCCESS");
                if(script_Ids.size()==1)
                {
                	response.setDescription(script_number+" Script Deleted Successfully");
                }
                else
                {
                response.setDescription(i +" Scripts Deleted Successfully");
                }
                bean.add(response);
            }
        
         tx.commit(); 
         return bean;  
	}
}
