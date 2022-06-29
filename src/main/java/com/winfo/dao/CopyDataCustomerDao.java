package com.winfo.dao;



import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.winfo.dao.CopyDataPostCustomerDao;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;
import com.winfo.model.FetchData;
import com.winfo.model.FetchDataMetadata;
import com.winfo.model.ScriptMaster;
import com.winfo.model.ScriptMetaData;
import com.winfo.vo.CopyDataDetails;
import com.winfo.vo.DomGenericResponseBean;
import com.winfo.vo.WatsMasterDataVOList;

import reactor.core.publisher.Mono;

@Repository
public class CopyDataCustomerDao {
	
	

	@Autowired
	private EntityManager entityManager;
	@Autowired
	private CopyDataPostCustomerDao dao;
	
	public List<DomGenericResponseBean> copyData(CopyDataDetails copyDataDetails ) throws ParseException {
	
		String productVersionOld=copyDataDetails.getProduct_version_old();
		String productVersionNew=copyDataDetails.getProduct_version_new();
		Session session = entityManager.unwrap(Session.class);
		int i;
		int j=0;
		int count=0;
		
	
		JSONObject responseDetailsJson = new JSONObject();
		JSONArray jsonArrayMaster = new JSONArray();
		JSONObject jsonMaster = new JSONObject();
		Query query3=session.createQuery("select script_number from ScriptMaster where product_version='"+productVersionOld+"' and standard_custom='Custom'");
          List<String> script_numbers = (List<String>) query3.list();
          Query query4=session.createQuery("select script_number from ScriptMaster where product_version='"+productVersionNew+"' and standard_custom='Custom'");
          List<String> script_numbers1 = (List<String>) query4.list();
          Query querysid=session.createQuery("select script_id from ScriptMaster where product_version='"+productVersionOld+"' and standard_custom='Custom'");
          List<Integer> script_Ids = (List<Integer>) querysid.list();
          Query querysid1=session.createQuery("select script_id from ScriptMaster where product_version='"+productVersionNew+"' and standard_custom='Custom'");
          List<Integer> script_id1 = (List<Integer>) querysid1.list();
          
         for(j=0;j<script_numbers1.size();j++)
         {
        	 if(script_numbers.contains(script_numbers1.get(j)))
        	 {
        		 script_numbers.remove(script_numbers1.get(j));
        		script_Ids.remove(script_id1.get(j));
        	 }
         }
		
         List<String> wrong_dependency_scripts=new ArrayList<String>();
          for(i=0;i<script_numbers.size();i++)
  		{
        	Integer script_Id=script_Ids.get(i);
        	  String script_num=script_numbers.get(i);
        	  
  			
        	Query query=session.createQuery("select script_id,script_number,process_area,sub_process_area,module,role,end2end_scenario,scenario_name,scenario_description,expected_result,selenium_test_script_name,selenium_test_method,dependency,product_version,standard_custom,test_script_status,author,created_by,creation_date,updated_by,update_date,customer_id,customisation_reference,attribute1,attribute2,attribute3,attribute4,attribute5,attribute6,attribute7,attribute8,attribute9,attribute10,priority,pluginFlag,targetApplication from ScriptMaster where script_number='"+script_num+"' and product_version='"+productVersionOld+"'");		
        	List<Object> result = (List<Object>) query.list();
        	Iterator itr6 = result.iterator();
        	Integer scriptId=null;
        	while(itr6.hasNext()){
 			   Object[] obj1 = (Object[]) itr6.next();
 			   if(!String.valueOf(obj1[0]).equals("null")) {
 				  scriptId=Integer.parseInt(String.valueOf(obj1[0]));
 			 
 			   break;
 			   }
 			   break;
 			   }
        	
        	Query query1=session.createQuery("select  line_number,input_parameter,action,xpath_location,xpath_location1,created_by,creation_date,updated_by,update_date,step_desc,field_type,hint,script_number,datatypes,unique_mandatory,validation_type,validation_name,metadataInputValue from ScriptMetaData where script_id="+scriptId);
        	List<Object> result1 = (List<Object>) query1.list();
        	Iterator itr1 = result1.iterator();
		List<FetchData> finalresult=new ArrayList<FetchData>();
		Iterator itr = result.iterator();
		Iterator itr5 = result.iterator();
		Integer dep = null;
		while(itr5.hasNext()){
			   Object[] obj1 = (Object[]) itr5.next();
			   if(!String.valueOf(obj1[12]).equals("null")) {
			   dep=Integer.parseInt(String.valueOf(obj1[12]));
			   break;
			   }
			   break;
			   }
		
		
		if(dep!=null) {
		
		Query querydependency=session.createQuery("select product_version,standard_custom from ScriptMaster where script_id="+dep);
        List<String> productversionlist = (List<String>) querydependency.list();
        String productversion=productversionlist.get(0);
        String standardcustom=productversionlist.get(1);
        if(!productversion.equals(productVersionOld) || !standardcustom.equals("Custom"))
        {
        	wrong_dependency_scripts.add(script_num);
        	continue;
        }
      }
		
		List<FetchDataMetadata> finalresult1=new ArrayList<FetchDataMetadata>();
		
		while(itr.hasNext()){
			   Object[] obj = (Object[]) itr.next();
			 
			   ScriptMaster master = new ScriptMaster();
			   if(String.valueOf(obj[1]).equals("null")) {
				   master.setScript_number(null);
	           }
			   else {
			  
			   master.setScript_number(String.valueOf(obj[1]));
			   }
			   if(String.valueOf(obj[2]).equals("null")) {
				   master.setProcess_area(null);
	           }
			   else {
			  
			   master.setProcess_area(String.valueOf(obj[2]));
			   }
			   if(String.valueOf(obj[3]).equals("null")) {
				   master.setSub_process_area(null);
	           }
			   else {
			 
			   master.setSub_process_area(String.valueOf(obj[3]));
			   }
			   if(String.valueOf(obj[4]).equals("null")) {
				   master.setModule(null);
	           }
			   else {
			   
			   master.setModule(String.valueOf(obj[4]));
			   }
			   if(String.valueOf(obj[5]).equals("null")) {
				   master.setRole(null);
	           }
			   else {
			  
			   master.setRole(String.valueOf(obj[5]));
			   }
			   if(String.valueOf(obj[6]).equals("null")) {
				   master.setEnd2end_scenario(null);
	           }
			   else {
			  
			   master.setEnd2end_scenario(String.valueOf(obj[6]));
			   }
			   if(String.valueOf(obj[7]).equals("null")) {
				   master.setScenario_name(null);
	           }
			   else {
			 
			   master.setScenario_name(String.valueOf(obj[7]));
			   }
			   if(String.valueOf(obj[8]).equals("null")) {
				   master.setScenario_description(null);
	           }
			   else {
			   master.setScenario_description(String.valueOf(obj[8]));
			   }
			   if(String.valueOf(obj[9]).equals("null")) {
				   master.setExpected_result(null);
	           }
			   else {
			   master.setExpected_result(String.valueOf(obj[9]));
			   }
			   if(String.valueOf(obj[10]).equals("null")) {
				   master.setSelenium_test_script_name(null);
	           }
			   else {
			   master.setSelenium_test_script_name(String.valueOf(obj[10]));
			   }
			   if(String.valueOf(obj[11]).equals("null")) {
				   master.setSelenium_test_method(null);
	           }
			   else {
			   master.setSelenium_test_method(String.valueOf(obj[11]));
			   }
			   if(String.valueOf(obj[12]).equals("null")) {
				   master.setDependency(null);
	               master.setDependent_script_num(null);
	           }
	           else {
	               
	               Integer script_id_dependency=Integer.parseInt(String.valueOf(obj[12]));
	               if(script_Ids.contains(script_id_dependency)) {
	               }
	               else {
	            	   script_Ids.add(script_id_dependency);
	               }
	               Query querydep=session.createQuery("select script_number from ScriptMaster where script_id="+script_Id);
	               List<String> dep_sname = (List<String>) querydep.list();
	   			
	   			String dep_script_name=dep_sname.get(0);
	               master.setDependency(Integer.parseInt(String.valueOf(obj[12])));
	               master.setDependent_script_num(dep_script_name);
	               
	           }
	           master.setProduct_version(productVersionNew);
	           if(String.valueOf(obj[14]).equals("null")) {
				   master.setStandard_custom(null);
	           }
			   else {
				   master.setStandard_custom(String.valueOf(obj[14]));
			   }
	           if(String.valueOf(obj[15]).equals("null")) {
	        	   master.setTest_script_status(null);
	           }
			   else {
				   master.setTest_script_status(String.valueOf(obj[15]));
			   }
	           if(String.valueOf(obj[16]).equals("null")) {
	        	   master.setAuthor(null);
	           }
			   else {
				   master.setAuthor(String.valueOf(obj[16]));
			   }
	           if(String.valueOf(obj[17]).equals("null")) {
	        	   master.setCreated_by(null);
	           }
			   else {
				   master.setCreated_by(String.valueOf(obj[17]));
			   }
	           
	           if(String.valueOf(obj[18]).equals("null")) {
	        	   master.setCreation_date(null);
	           }
			   else {
				   master.setCreation_date((Date) ( obj[18]));
			   }
	           
	           if(String.valueOf(obj[19]).equals("null")) {
	        	   master.setUpdated_by(null);
	           }
			   else {
				   master.setUpdated_by(String.valueOf(obj[19]));
			   }
	           
	           if(String.valueOf(obj[20]).equals("null")) {
	        	   master.setUpdate_date(null);
	           }
			   else {
				   master.setUpdate_date((Date )(obj[20]));
			   }
	           
	           if(String.valueOf(obj[21]).equals("null")) {
	              
	               master.setCustomer_id(null);
	           }
	           else {
	               
	               master.setCustomer_id(Integer.parseInt(String.valueOf(obj[21])));
	           }
	           if(String.valueOf(obj[22]).equals("null")) {
	        	   master.setCustomisation_reference(null);
	           }
			   else {
				   master.setCustomisation_reference(String.valueOf(obj[22]));
			   }
	         
	           if(String.valueOf(obj[23]).equals("null")) {
	        	   master.setAttribute1(null);
	           }
			   else {
				   master.setAttribute1(String.valueOf(obj[23]));
			   }
	           if(String.valueOf(obj[24]).equals("null")) {
	        	   master.setAttribute2(null);
	           }
			   else {
				   master.setAttribute2(String.valueOf(obj[24]));
			   }
	           if(String.valueOf(obj[25]).equals("null")) {
	        	   master.setAttribute3(null);
	           }
			   else {
				   master.setAttribute3(String.valueOf(obj[25]));
			   }
	           if(String.valueOf(obj[26]).equals("null")) {
	        	   master.setAttribute4(null);
	           }
			   else {
				   master.setAttribute4(String.valueOf(obj[26]));
			   }
	           if(String.valueOf(obj[27]).equals("null")) {
	        	   master.setAttribute5(null);
	           }
			   else {
				   master.setAttribute5(String.valueOf(obj[27]));
			   }
	           if(String.valueOf(obj[28]).equals("null")) {
	        	   master.setAttribute6(null);
	           }
			   else {
				   master.setAttribute6(String.valueOf(obj[28]));
			   }
	           if(String.valueOf(obj[29]).equals("null")) {
	        	   master.setAttribute7(null);
	           }
			   else {
				   master.setAttribute7(String.valueOf(obj[29]));
			   }
	           if(String.valueOf(obj[30]).equals("null")) {
	        	   master.setAttribute8(null);
	           }
			   else {
				   master.setAttribute8(String.valueOf(obj[30]));
			   }
	           if(String.valueOf(obj[31]).equals("null")) {
	        	   master.setAttribute9(null);
	           }
			   else {
				   master.setAttribute9(String.valueOf(obj[31]));
			   }
	           if(String.valueOf(obj[32]).equals("null")) {
	        	   master.setAttribute10(null);
	           }
			   else {
				   master.setAttribute10(String.valueOf(obj[32]));
			   }
	           
	           master.setPriority(Integer.parseInt(String.valueOf(obj[33])));
	           if(String.valueOf(obj[34]).equals("null")) {
	        	   master.setPluginFlag(null);
	           }
			   else {
				   master.setPluginFlag(String.valueOf(obj[34]));
			   }
	           if(String.valueOf(obj[35]).equals("null")) {
	        	   master.setTargetApplication(null);
	           }
			   else {
				   master.setTargetApplication(String.valueOf(obj[35]));
			   }
	           master.setAppr_for_migration(null); 
			   
			
		
		while(itr1.hasNext()) {
			 Object[] obj1 = (Object[]) itr1.next();
			 FetchDataMetadata fetchDataMetadata=new FetchDataMetadata();
			 ScriptMetaData metadata = new ScriptMetaData();
			 metadata.setLine_number(Integer.parseInt(String.valueOf(obj1[0])));
			 if(String.valueOf(obj1[1]).equals("null"))
			 {
				 metadata.setInput_parameter(null);
			 }
			 else {
				 metadata.setInput_parameter(String.valueOf(obj1[1]));
			 }
			 if(String.valueOf(obj1[2]).equals("null"))
			 {
				 metadata.setAction(null);
			 }
			 else {
				 metadata.setAction(String.valueOf(obj1[2]));
			 }
			 if(String.valueOf(obj1[3]).equals("null"))
			 {
				 metadata.setXpath_location(null);
			 }
			 else {
				 metadata.setXpath_location(String.valueOf(obj1[3]));
			 }
			 if(String.valueOf(obj1[4]).equals("null"))
			 {
				 metadata.setXpath_location1(null);
			 }
			 else {
				 metadata.setXpath_location1(String.valueOf(obj1[4]));
			 }
			 if(String.valueOf(obj1[5]).equals("null"))
			 {
				 metadata.setCreated_by(null);
			 }
			 else {
				 metadata.setCreated_by(String.valueOf(obj1[5]));
			 }
			
			 metadata.setCreation_date((Date) obj1[6]);
          if(String.valueOf(obj1[7]).equals("null"))
			 {
        	  metadata.setUpdated_by(null);
			 }
			 else {
				 metadata.setUpdated_by(String.valueOf(obj1[7]));
			 }
         
          metadata.setUpdate_date(((Date) obj1[8]));
          if(String.valueOf(obj1[9]).equals("null"))
			 {
        	  metadata.setStep_desc(null);
			 }
			 else {
				 metadata.setStep_desc(String.valueOf(obj1[9]));
			 }
          if(String.valueOf(obj1[10]).equals("null"))
			 {
        	  metadata.setField_type(null);
			 }
			 else {
				 metadata.setField_type(String.valueOf(obj1[10]));
			 }
          if(String.valueOf(obj1[11]).equals("null"))
			 {
        	  metadata.setHint(null);
			 }
			 else {
				 metadata.setHint(String.valueOf(obj1[11]));
			 }
          if(String.valueOf(obj1[12]).equals("null"))
 		 {
 			 metadata.setScript_number(null);
 		 }
 		 else {
 			 metadata.setScript_number(String.valueOf(obj1[12]));
 		 }
        if(String.valueOf(obj1[13]).equals("null"))
 		 {
     	   metadata.setDatatypes("NA");
 		 }
 		 else {
 			 metadata.setDatatypes(String.valueOf(obj1[13]));
 		 }
     if(String.valueOf(obj1[14]).equals("null"))
 		 {
     	metadata.setUnique_mandatory("NA");
 		 }
 		 else {
 			 metadata.setUnique_mandatory(String.valueOf(obj1[14]));
 		 }
     if(String.valueOf(obj1[15]).equals("null"))
 		 {
     	metadata.setValidation_type("NA");
 		 }
 		 else {
 			 metadata.setValidation_type(String.valueOf(obj1[15]));
 		 }
     if(String.valueOf(obj1[16]).equals("null"))
 		 {
     	metadata.setValidation_name("NA");
 		 }
 		 else {
     metadata.setValidation_name(String.valueOf(obj1[16]));
 		 }
			if (String.valueOf(obj1[17]).equals("null")) {
				metadata.setMetadata_inputvalue("NA");
				} else {
				metadata.setMetadata_inputvalue(String.valueOf(obj1[17]));
			}


          master.addMetadata(metadata);
		}
		dao.copyDataPost(master);
		count++;
		}
		 
		
	}
      
          List<DomGenericResponseBean> bean = new ArrayList<DomGenericResponseBean>();
	DomGenericResponseBean response = new DomGenericResponseBean();
	if(count!=0) {
	response.setStatus(200);
	response.setStatusMessage("SUCCESS");
	response.setDescription(i+" Script'(s) Copied" + " to " +  productVersionNew + " Successfully");
	
	bean.add(response);
	}
	else {
		response.setStatus(409);
		response.setStatusMessage("ERROR");
		response.setDescription("No New Scripts to be copied");
		bean.add(response);
	}
	return bean;
          
         
    }
}

