package com.winfo.dao;


import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import com.winfo.vo.ScriptId;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.jfree.date.DateUtilities;
import org.json.JSONArray;
//import org.json.JSONObject;
import org.json.simple.JSONObject;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.Gson;
import com.winfo.model.FetchData;
import com.winfo.model.FetchDataMetadata;
import com.winfo.vo.DomGenericResponseBean;

import reactor.core.publisher.Mono;

import com.winfo.model.ScriptMaster;

@Repository

public class CustomerToCentralGetDao {

	@Autowired
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public JSONObject customerRepoData(ScriptId scriptID) throws ParseException {
	
		Session session = entityManager.unwrap(Session.class);
	
		@SuppressWarnings("unchecked")
		
		String product_version=scriptID.getProduct_version();
		List<Integer> script_Ids= scriptID.getScript_id();
		
	
		int i=0;
			 JSONObject responseDetailsJson = new JSONObject();
		
		  JSONArray jsonArrayMaster = new JSONArray();
			 JSONObject jsonMaster = new JSONObject();
		for(i=0;i<script_Ids.size();i++)
		{
			int dependency=0;
			int customerId=0;

			Integer script_Id=script_Ids.get(i);
			System.out.println(script_Id);
			Query query3=session.createQuery("select product_version from ScriptMaster where script_id="+script_Id);
			List<String> result2 = (List<String>) query3.list();
			
			String product_version_db=result2.get(0);
			if(product_version.equals(product_version_db))
			{
			
		Query query=session.createQuery("select script_id,script_number,process_area,sub_process_area,module,role,end2end_scenario,scenario_name,scenario_description,expected_result,selenium_test_script_name,selenium_test_method,dependency,product_version,standard_custom,test_script_status,author,created_by,creation_date,updated_by,update_date,customer_id,customisation_reference,attribute1,attribute2,attribute3,attribute4,attribute5,attribute6,attribute7,attribute8,attribute9,attribute10,priority from ScriptMaster where script_id="+script_Id);
		Query query1=session.createQuery("select  line_number,input_parameter,action,xpath_location,xpath_location1,created_by,creation_date,updated_by,update_date,step_desc,field_type,hint,script_number,datatypes,unique_mandatory,validation_type,validation_name   from ScriptMetaData where script_id="+script_Id);
		List<Object> result = (List<Object>) query.list(); 
		List<FetchData> finalresult=new ArrayList<FetchData>();
		Iterator itr = result.iterator();
		List<Object> result1 = (List<Object>) query1.list(); 
		List<FetchDataMetadata> finalresult1=new ArrayList<FetchDataMetadata>();
		Iterator itr1 = result1.iterator();
		while(itr.hasNext()){
			   Object[] obj = (Object[]) itr.next();
			   FetchData fetchData=new FetchData();
			   if(String.valueOf(obj[1]).equals("null")) {
				   fetchData.setScript_number(null);
	           }
			   else {
			   fetchData.setScript_number(String.valueOf(obj[1]));
			   }
			   if(String.valueOf(obj[2]).equals("null")) {
				   fetchData.setProcess_area(null);
	           }
			   else {
			   fetchData.setProcess_area(String.valueOf(obj[2]));
			   }
			   if(String.valueOf(obj[3]).equals("null")) {
				   fetchData.setSub_process_area(null);
	           }
			   else {
			   fetchData.setSub_process_area(String.valueOf(obj[3]));
			   }
			   if(String.valueOf(obj[4]).equals("null")) {
				   fetchData.setModule(null);
	           }
			   else {
			   fetchData.setModule(String.valueOf(obj[4]));
			   }
			   if(String.valueOf(obj[5]).equals("null")) {
				   fetchData.setRole(null);
	           }
			   else {
			   fetchData.setRole(String.valueOf(obj[5]));
			   }
			   if(String.valueOf(obj[6]).equals("null")) {
				   fetchData.setEnd2end_scenario(null);
	           }
			   else {
			   fetchData.setEnd2end_scenario(String.valueOf(obj[6]));
			   }
			   if(String.valueOf(obj[7]).equals("null")) {
				   fetchData.setScenario_name(null);
	           }
			   else {
			   fetchData.setScenario_name(String.valueOf(obj[7]));
			   }
			   if(String.valueOf(obj[8]).equals("null")) {
				   fetchData.setScenario_description(null);
	           }
			   else {
			   fetchData.setScenario_description(String.valueOf(obj[8]));
			   }
			   if(String.valueOf(obj[9]).equals("null")) {
				   fetchData.setExpected_result(null);
	           }
			   else {
			   fetchData.setExpected_result(String.valueOf(obj[9]));
			   }
			   if(String.valueOf(obj[10]).equals("null")) {
				   fetchData.setSelenium_test_script_name(null);
	           }
			   else {
			   fetchData.setSelenium_test_script_name(String.valueOf(obj[10]));
			   }
			   if(String.valueOf(obj[11]).equals("null")) {
				   fetchData.setSelenium_test_method(null);
	           }
			   else {
			   fetchData.setSelenium_test_method(String.valueOf(obj[11]));
			   }
	         
	           if(String.valueOf(obj[12]).equals("null")) {
	               dependency=0;
	           }
	           else {
	               dependency=1;
	               Integer script_id_dependency=Integer.parseInt(String.valueOf(obj[12]));
	               if(script_Ids.contains(script_id_dependency)) {
	               }
	               else {
	            	   script_Ids.add(script_id_dependency);
	               }
	               Query querydep=session.createQuery("select script_number from ScriptMaster where script_id="+script_Id);
	               List<String> dep_sname = (List<String>) querydep.list();
	   			
	   			String dep_script_name=dep_sname.get(0);
	               fetchData.setDependency(Integer.parseInt(String.valueOf(obj[12])));
	               fetchData.setDependent_script_num(dep_script_name);
	               
	           }
	           if(String.valueOf(obj[13]).equals("null")) {
				   fetchData.setProduct_version(null);
	           }
			   else {
			   fetchData.setProduct_version(String.valueOf(obj[13]));
			   }
	          
	           if(String.valueOf(obj[14]).equals("null")) {
				   fetchData.setStandard_custom(null);
	           }
			   else {
			   fetchData.setStandard_custom(String.valueOf(obj[14]));
			   }
	           if(String.valueOf(obj[15]).equals("null")) {
				   fetchData.setTest_script_status(null);
	           }
			   else {
			   fetchData.setTest_script_status(String.valueOf(obj[15]));
			   }
	           if(String.valueOf(obj[16]).equals("null")) {
				   fetchData.setAuthor(null);
	           }
			   else {
			   fetchData.setAuthor(String.valueOf(obj[16]));
			   }
	           if(String.valueOf(obj[17]).equals("null")) {
				   fetchData.setCreated_by(null);
	           }
			   else {
			   fetchData.setCreated_by(String.valueOf(obj[17]));
			   }
	           
	        
	           fetchData.setCreation_date((Date) ( obj[18]));
	           if(String.valueOf(obj[19]).equals("null")) {
				   fetchData.setUpdated_by(null);
	           }
			   else {
			   fetchData.setUpdated_by(String.valueOf(obj[19]));
			   }
	           
	           fetchData.setUpdate_date((Date )(obj[20]));
	           if(String.valueOf(obj[21]).equals("null")) {
	               customerId=0;
	           }
	           else {
	               customerId=1;
	               fetchData.setCustomer_id(Integer.parseInt(String.valueOf(obj[21])));
	           }
	           if(String.valueOf(obj[22]).equals("null")) {
				   fetchData.setCustomisation_refrence(null);
	           }
			   else {
			   fetchData.setCustomisation_refrence(String.valueOf(obj[22]));
			   }
	         
	           if(String.valueOf(obj[23]).equals("null")) {
				   fetchData.setAttribute1(null);
	           }
			   else {
			   fetchData.setAttribute1(String.valueOf(obj[23]));
			   }
	           if(String.valueOf(obj[24]).equals("null")) {
				   fetchData.setAttribute2(null);
	           }
			   else {
			   fetchData.setAttribute2(String.valueOf(obj[24]));
			   }
	           if(String.valueOf(obj[25]).equals("null")) {
				   fetchData.setAttribute3(null);
	           }
			   else {
			   fetchData.setAttribute3(String.valueOf(obj[25]));
			   }
	           if(String.valueOf(obj[26]).equals("null")) {
				   fetchData.setAttribute4(null);
	           }
			   else {
			   fetchData.setAttribute4(String.valueOf(obj[26]));
			   }
	           if(String.valueOf(obj[27]).equals("null")) {
				   fetchData.setAttribute5(null);
	           }
			   else {
			   fetchData.setAttribute5(String.valueOf(obj[27]));
			   }
	           if(String.valueOf(obj[28]).equals("null")) {
				   fetchData.setAttribute6(null);
	           }
			   else {
			   fetchData.setAttribute6(String.valueOf(obj[28]));
			   }
	           if(String.valueOf(obj[29]).equals("null")) {
				   fetchData.setAttribute7(null);
	           }
			   else {
			   fetchData.setAttribute7(String.valueOf(obj[29]));
			   }
	           if(String.valueOf(obj[30]).equals("null")) {
				   fetchData.setAttribute8(null);
	           }
			   else {
			   fetchData.setAttribute8(String.valueOf(obj[30]));
			   }
	           if(String.valueOf(obj[31]).equals("null")) {
				   fetchData.setAttribute9(null);
	           }
			   else {
			   fetchData.setAttribute9(String.valueOf(obj[31]));
			   }
	           if(String.valueOf(obj[32]).equals("null")) {
				   fetchData.setAttribute10(null);
	           }
			   else {
			   fetchData.setAttribute10(String.valueOf(obj[32]));
			   }
	           
	           fetchData.setPriority(Integer.parseInt(String.valueOf(obj[33])));
			 
			   finalresult.add(fetchData);
			}
		
		while(itr1.hasNext()) {
			 Object[] obj1 = (Object[]) itr1.next();
			 FetchDataMetadata fetchDataMetadata=new FetchDataMetadata();
			 fetchDataMetadata.setLine_number(Integer.parseInt(String.valueOf(obj1[0])));
			 if(String.valueOf(obj1[1]).equals("null"))
			 {
				 fetchDataMetadata.setInput_parameter(null);
			 }
			 else {
         fetchDataMetadata.setInput_parameter(String.valueOf(obj1[1]));
			 }
			 if(String.valueOf(obj1[2]).equals("null"))
			 {
				 fetchDataMetadata.setAction(null);
			 }
			 else {
         fetchDataMetadata.setAction(String.valueOf(obj1[2]));
			 }
			 if(String.valueOf(obj1[3]).equals("null"))
			 {
				 fetchDataMetadata.setXpath_location(null);
			 }
			 else {
         fetchDataMetadata.setXpath_location(String.valueOf(obj1[3]));
			 }
			 if(String.valueOf(obj1[4]).equals("null"))
			 {
				 fetchDataMetadata.setXpath_location1(null);
			 }
			 else {
         fetchDataMetadata.setXpath_location1(String.valueOf(obj1[4]));
			 }
			 if(String.valueOf(obj1[5]).equals("null"))
			 {
				 fetchDataMetadata.setCreated_by(null);
			 }
			 else {
         fetchDataMetadata.setCreated_by(String.valueOf(obj1[5]));
			 }
			
         fetchDataMetadata.setCreation_date((Date) obj1[6]);
         if(String.valueOf(obj1[7]).equals("null"))
			 {
				 fetchDataMetadata.setUpdated_by(null);
			 }
			 else {
      fetchDataMetadata.setUpdated_by(String.valueOf(obj1[7]));
			 }
        
         fetchDataMetadata.setUpdate_date(((Date) obj1[8]));
         if(String.valueOf(obj1[9]).equals("null"))
			 {
				 fetchDataMetadata.setStep_desc(null);
			 }
			 else {
				 fetchDataMetadata.setStep_desc(String.valueOf(obj1[9]));
			 }
         if(String.valueOf(obj1[10]).equals("null"))
			 {
				 fetchDataMetadata.setField_type(null);
			 }
			 else {
				 fetchDataMetadata.setField_type(String.valueOf(obj1[10]));
			 }
         if(String.valueOf(obj1[11]).equals("null"))
			 {
				 fetchDataMetadata.setHint(null);
			 }
			 else {
				 fetchDataMetadata.setHint(String.valueOf(obj1[11]));
			 }
         if(String.valueOf(obj1[12]).equals("null"))
      		 {
      			 fetchDataMetadata.setScript_number(null);
      		 }
      		 else {
      fetchDataMetadata.setScript_number(String.valueOf(obj1[12]));
      		 }
            if(String.valueOf(obj1[13]).equals("null"))
      		 {
      			 fetchDataMetadata.setDatatypes("NA");
      		 }
      		 else {
      fetchDataMetadata.setDatatypes(String.valueOf(obj1[13]));
      		 }
            if(String.valueOf(obj1[14]).equals("null"))
      		 {
      			 fetchDataMetadata.setUnique_mandatory("NA");
      		 }
      		 else {
      fetchDataMetadata.setUnique_mandatory(String.valueOf(obj1[14]));
      		 }
            if(String.valueOf(obj1[15]).equals("null"))
      		 {
      			 fetchDataMetadata.setValidation_type("NA");
      		 }
      		 else {
      fetchDataMetadata.setValidation_type(String.valueOf(obj1[15]));
      		 }
            if(String.valueOf(obj1[16]).equals("null"))
      		 {
      			 fetchDataMetadata.setValidation_name("NA");
      		 }
      		 else {
      fetchDataMetadata.setValidation_name(String.valueOf(obj1[16]));
      		 }



         finalresult1.add(fetchDataMetadata);
		}

     		for(FetchData slist:finalresult) {
  	    	  jsonMaster.put("script_number", slist.getScript_number());
               jsonMaster.put("process_area", slist.getProcess_area());
               jsonMaster.put("sub_process_area", slist.getSub_process_area());
               jsonMaster.put("module",slist.getModule());
               jsonMaster.put("role", slist.getRole());
               jsonMaster.put("scenario_name", slist.getScenario_name());
               jsonMaster.put("scenario_description", slist.getScenario_description());
               jsonMaster.put("product_version", slist.getProduct_version());
               jsonMaster.put("standard_custom", slist.getStandard_custom());
               jsonMaster.put("test_script_status", slist.getTest_script_status());
               jsonMaster.put("priority", slist.getPriority());
               if(customerId==0) {
            	   jsonMaster.put("customer_id",null);
               }
               else {
                   jsonMaster.put("customer_id",slist.getCustomer_id());
               }
             
               if(dependency==0) {
            	   jsonMaster.put("dependency",null);
            	   jsonMaster.put("dependent_script_num",null);
               }
               else {
                  
                   jsonMaster.put("dependency",slist.getDependency());
                   jsonMaster.put("dependent_script_num",slist.getDependent_script_num());
                   
               }
               jsonMaster.put("end2end_scenario",slist.getEnd2end_scenario());
               String expected_result=slist.getExpected_result();
               if(expected_result==null)
               {
            	   jsonMaster.put("expected_result",null);
               }
               else {
                 String str1="\"";
                 String str2="\\\\";
                 String str3=str2+"\"";
                 String replaceQuotes= expected_result.replace(str1,str3);
                 jsonMaster.put("expected_result",replaceQuotes);
               }
               jsonMaster.put("selenium_test_script_name",slist.getSelenium_test_script_name());
               jsonMaster.put("selenium_test_method",slist.getSelenium_test_method());
               jsonMaster.put("author",slist.getAuthor());
               jsonMaster.put("created_by",slist.getCreated_by());
               Date date =(Date) slist.getCreation_date(); 
               DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");  
               String strDate = dateFormat.format(date);
               jsonMaster.put("creation_date",strDate);
               jsonMaster.put("updated_by",slist.getUpdated_by());
               Date date1 =(Date) slist.getUpdate_date();
               String strDate1 = dateFormat.format(date1);
               jsonMaster.put("update_date",strDate1);
               jsonMaster.put("customatisation_refrence",slist.getCustomisation_refrence());
               jsonMaster.put("attribute1",slist.getAttribute1());
               jsonMaster.put("attribute2",slist.getAttribute2());
               jsonMaster.put("attribute3",slist.getAttribute3());
               jsonMaster.put("attribute4",slist.getAttribute4());
               jsonMaster.put("attribute5",slist.getAttribute5());
               jsonMaster.put("attribute6",slist.getAttribute6());
               jsonMaster.put("attribute7",slist.getAttribute7());
               jsonMaster.put("attribute8",slist.getAttribute8());
               jsonMaster.put("attribute9",slist.getAttribute9());
               jsonMaster.put("attribute10",slist.getAttribute10());
               jsonMaster.put("priority",slist.getPriority());
  			  
  		
  		 }
  	  

			 JSONArray jsonArrayMetaData = new JSONArray();
			  JSONObject jsonMetadata = new JSONObject();
		  for(FetchDataMetadata slist1:finalresult1) {
			  jsonMetadata.put("line_number",slist1.getLine_number());
	          jsonMetadata.put("input_parameter",slist1.getInput_parameter());
	          jsonMetadata.put("action",slist1.getAction());
	          jsonMetadata.put("xpath_location",slist1.getXpath_location());
	          jsonMetadata.put("xpath_location1",slist1.getXpath_location1());
	          jsonMetadata.put("created_by",slist1.getCreated_by());
	          Date date =(Date) slist1.getCreation_date(); 
	          DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");  
	          String strDate = dateFormat.format(date);
	          jsonMetadata.put("creation_date",strDate);
	          jsonMetadata.put("updated_by",slist1.getUpdated_by());
	          Date date1 =(Date) slist1.getUpdate_date();
	          String strDate1 = dateFormat.format(date1);
	          jsonMetadata.put("update_date",strDate1);
	          jsonMetadata.put("step_desc",slist1.getStep_desc());
	          jsonMetadata.put("field_type",slist1.getField_type());
	          jsonMetadata.put("hint",slist1.getHint());
	          jsonMetadata.put("script_number",slist1.getScript_number());
	          jsonMetadata.put("datatypes",slist1.getDatatypes());
	          jsonMetadata.put("unique_mandatory",slist1.getUnique_mandatory());
	          jsonMetadata.put("validation_type",slist1.getValidation_type());
	          jsonMetadata.put("validation_name",slist1.getValidation_name());
	         
	          jsonArrayMetaData.put(jsonMetadata.toString());
		  }	 
		  jsonMaster.put("MetaDataList", jsonArrayMetaData);
	  jsonArrayMaster.put(jsonMaster.toString());
	  responseDetailsJson.put("data", jsonArrayMaster);
  }
}
		String str1="\""+"{"+"\"";
	      String str2="{"+"\"";
	      String str3="\""+"}"+"\"";
	      String str4="\""+"}";
	      String str5="}"+"\"";
	      String str6="}";
	      String str7="\"\"";
	      String str8="\\";
	      String str9="\""+str8+"\"";
	      String str10="\"";
	      String str11=str10+str8+str10;
	      String str12=str8+str7;
	      
	      
	      responseDetailsJson.put("data", jsonArrayMaster);
	      String replaceSlash=responseDetailsJson.toString().replace("\\","");
	      String replaceQuotes= replaceSlash.replace(str1,str2);
	       String finalJSONString=replaceQuotes.replace(str3,str4);
	       String finalJSONString1=finalJSONString.replace(str5,str6);
	       String finalJSONString2=finalJSONString1.replace(str7,str9);
	       String finalJSONString3=finalJSONString2.replace(str11+" ",str12+" ");
	       String finalJSONString4=finalJSONString3.replace(str11+",",str12+",");
		    
	     
	       
			 JSONParser parser = new JSONParser(); 
			 JSONObject finalJSONObject = (JSONObject) parser.parse(finalJSONString4);
		
		
			return finalJSONObject;
	}
}

