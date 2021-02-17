package com.winfo.dao;



import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

import com.winfo.controller.CustomerToCentralPostRest;
import com.winfo.model.FetchData;
import com.winfo.model.FetchDataMetadata;
import com.winfo.model.ScriptMaster;
import com.winfo.services.CustomerToCentralPostService;
import com.winfo.vo.CopyDataDetails;
import com.winfo.vo.DomGenericResponseBean;
import com.winfo.vo.WatsMasterDataVOList;

import reactor.core.publisher.Mono;

@Repository
public class CopyDataDao {
	
	
	public String webClientService(JSONObject json2) {
		
			WebClient webClient = WebClient.create("http://watsdev01.winfosolutions.com:8080/copyData_post");
		
			Mono<String> result = webClient.post().syncBody(json2).retrieve().bodyToMono(String.class);
			String response = result.block();
			
		//	response=response+wrong_dependency_scripts;
			System.out.println(response);
			return response;
			}
	//@SuppressWarnings("unchecked")
	@Autowired
	private EntityManager entityManager;
	public String copyData(CopyDataDetails copyDataDetails ) throws ParseException {
	
		String productVersionOld=copyDataDetails.getProduct_version_old();
		String productVersionNew=copyDataDetails.getProduct_version_new();
		Session session = entityManager.unwrap(Session.class);
		int i;
		int j=0;
		int k=0;
		int dependency=0;
		int customerId=0;
		JSONObject responseDetailsJson = new JSONObject();
		JSONArray jsonArrayMaster = new JSONArray();
		JSONObject jsonMaster = new JSONObject();
		Query query3=session.createQuery("select script_number from ScriptMaster where product_version='"+productVersionOld+"'");
          List<String> script_numbers = (List<String>) query3.list();
          Query query4=session.createQuery("select script_number from ScriptMaster where product_version='"+productVersionNew+"'");
          List<String> script_numbers1 = (List<String>) query4.list();
          Query querysid=session.createQuery("select script_id from ScriptMaster where product_version='"+productVersionOld+"'");
          List<Integer> script_Ids = (List<Integer>) querysid.list();
          Query querysid1=session.createQuery("select script_id from ScriptMaster where product_version='"+productVersionNew+"'");
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
        	  
  			System.out.println(script_Id);
        	Query query=session.createQuery("select script_id,script_number,process_area,sub_process_area,module,role,end2end_scenario,scenario_name,scenario_description,expected_result,selenium_test_script_name,selenium_test_method,dependency,product_version,standard_custom,test_script_status,author,created_by,creation_date,updated_by,update_date,customer_id,customisation_reference,attribute1,attribute2,attribute3,attribute4,attribute5,attribute6,attribute7,attribute8,attribute9,attribute10,priority from ScriptMaster where script_number='"+script_num+"'");		
          	Query query1=session.createQuery("select  line_number,input_parameter,action,xpath_location,xpath_location1,created_by,creation_date,updated_by,update_date,step_desc,field_type,hint   from ScriptMetaData where script_number='"+script_num+"'");
          
		List<Object> result = (List<Object>) query.list(); 
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
		//Iterator itr1 = result1.iterator();
		//System.out.println(result.get(12));
		if(dep!=null) {
		//Integer dependency_script=Integer.parseInt(String.valueOf(result.get(12)));
		Query querydependency=session.createQuery("select product_version from ScriptMaster where script_id="+dep);
        List<String> productversionlist = (List<String>) querydependency.list();
        String productversion=productversionlist.get(0);
        if(!productversion.equals(productVersionOld))
        {
        	wrong_dependency_scripts.add(script_num);
        	continue;
        }
      }
		List<Object> result1 = (List<Object>) query1.list(); 
		List<FetchDataMetadata> finalresult1=new ArrayList<FetchDataMetadata>();
		Iterator itr1 = result1.iterator();
		while(itr.hasNext()){
			   Object[] obj = (Object[]) itr.next();
			   FetchData fetchData=new FetchData();
			   fetchData.setScript_number(String.valueOf(obj[1]));
	           fetchData.setProcess_area(String.valueOf(obj[2]));
	           fetchData.setSub_process_area(String.valueOf(obj[3]));
	           fetchData.setModule(String.valueOf(obj[4]));
	           fetchData.setRole(String.valueOf(obj[5]));
	           fetchData.setEnd2end_scenario(String.valueOf(obj[6]));
	           fetchData.setScenario_name(String.valueOf(obj[7]));
	           fetchData.setScenario_description(String.valueOf(obj[8]));
	           fetchData.setExpected_result(String.valueOf(obj[9]));
	           fetchData.setSelenium_test_script_name(String.valueOf(obj[10]));
	           fetchData.setSelenium_test_method(String.valueOf(obj[11]));
	           System.out.println(String.valueOf(obj[12]));
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
	               fetchData.setDependency(Integer.parseInt(String.valueOf(obj[12])));
	           }
	           fetchData.setProduct_version(productVersionNew);
	           fetchData.setStandard_custom(String.valueOf(obj[14]));
	           fetchData.setTest_script_status(String.valueOf(obj[15]));
	           fetchData.setAuthor(String.valueOf(obj[16]));
	           fetchData.setCreated_by(String.valueOf(obj[17]));
	           fetchData.setCreation_date((Date) ( obj[18]));
	           fetchData.setUpdated_by(String.valueOf(obj[19]));
	           fetchData.setUpdate_date((Date )(obj[20]));
	           if(String.valueOf(obj[21]).equals("null")) {
	               customerId=0;
	           }
	           else {
	               customerId=1;
	               fetchData.setCustomer_id(Integer.parseInt(String.valueOf(obj[21])));
	           }
	           fetchData.setCustomisation_refrence(String.valueOf(obj[22]));
	           fetchData.setAttribute1(String.valueOf(obj[23]));
	           fetchData.setAttribute2(String.valueOf(obj[24]));
	           fetchData.setAttribute3(String.valueOf(obj[25]));
	           fetchData.setAttribute4(String.valueOf(obj[26]));
	           fetchData.setAttribute5(String.valueOf(obj[27]));
	           fetchData.setAttribute6(String.valueOf(obj[28]));
	           fetchData.setAttribute7(String.valueOf(obj[29]));
	           fetchData.setAttribute8(String.valueOf(obj[30]));
	           fetchData.setAttribute9(String.valueOf(obj[31]));
	           fetchData.setAttribute10(String.valueOf(obj[32]));
	           fetchData.setPriority(Integer.parseInt(String.valueOf(obj[33])));
			 
			   finalresult.add(fetchData);
			}
		
		while(itr1.hasNext()) {
			 Object[] obj1 = (Object[]) itr1.next();
			 FetchDataMetadata fetchDataMetadata=new FetchDataMetadata();
			 fetchDataMetadata.setLine_number(Integer.parseInt(String.valueOf(obj1[0])));
          fetchDataMetadata.setInput_parameter(String.valueOf(obj1[1]));
          fetchDataMetadata.setAction(String.valueOf(obj1[2]));
          fetchDataMetadata.setXpath_location(String.valueOf(obj1[3]));
          fetchDataMetadata.setXpath_location1(String.valueOf(obj1[4]));
          fetchDataMetadata.setCreated_by(String.valueOf(obj1[5]));
          fetchDataMetadata.setCreation_date((Date) obj1[6]);
          fetchDataMetadata.setUpdated_by(String.valueOf(obj1[7]));
          fetchDataMetadata.setUpdate_date(((Date) obj1[8]));
          fetchDataMetadata.setStep_desc(String.valueOf(obj1[9]));
          fetchDataMetadata.setField_type(String.valueOf(obj1[10]));
          fetchDataMetadata.setHint(String.valueOf(obj1[11]));
          finalresult1.add(fetchDataMetadata);
		}

		for(FetchData slist:finalresult) {
	    	  //FetchData slist=new FetchData();
			  //jsonMaster.put("script_id",slist.getScript_id()); 
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
         }
         else {
            
             jsonMaster.put("dependency",slist.getDependency());
         }
         jsonMaster.put("end2end_scenario",slist.getEnd2end_scenario());

         String expected_result=slist.getExpected_result();
       
         String str1="\"";
         String str2="\\\\";
         String str3=str2+"\"";
         String replaceQuotes= expected_result.replace(str1,str3);
         jsonMaster.put("expected_result",replaceQuotes);
         jsonMaster.put("expected_result",slist.getExpected_result());
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
	          jsonArrayMetaData.put(jsonMetadata.toString());
		  }	 
	  jsonMaster.put("MetaDataList", jsonArrayMetaData);
	  jsonArrayMaster.put(jsonMaster.toString());
	  responseDetailsJson.put("data", jsonArrayMaster);
}
      System.out.println(i);    
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
//       int n=0;
//	
//		  for(n=200;n<=300;n++) { System.out.print(finalJSONString3.charAt(n)); }
//		    
       System.out.println(finalJSONString1.length()); 
       
		 JSONParser parser = new JSONParser(); 
		 JSONObject json1 = (JSONObject) parser.parse(finalJSONString4);
		
	return webClientService(json1);
	}
}
