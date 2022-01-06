package com.winfo.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winfo.dao.CopyTestRunDao;
import com.winfo.dao.CopyTestRunDao2;
import com.winfo.model.ScriptMaster;
import com.winfo.model.ScriptMetaData;
import com.winfo.model.ScriptsData;
import com.winfo.model.ScritplinesData;
import com.winfo.model.Testrundata;
import com.winfo.vo.CopytestrunVo;

@Service
public class CopyTestRunService2 {
	@Autowired
	CopyTestRunDao2 copyTestrunDao;
	@Transactional
	public int copyTestrun(@Valid CopytestrunVo copyTestrunvo) throws InterruptedException {
		Testrundata getTestrun=copyTestrunDao.getdata(copyTestrunvo.getTestScriptNo());
		int testrunid=copyTestrunDao.getIds();
         System.out.println("testrunid"+testrunid);
		Testrundata setTestrundata=new Testrundata();
		setTestrundata.setTestsetid(testrunid);
		setTestrundata.setTest_set_desc(getTestrun.getTest_set_desc());
		setTestrundata.setTest_set_comments(getTestrun.getTest_set_comments());
		setTestrundata.setEnabled("Y");
		setTestrundata.setDescription(getTestrun.getDescription());
		setTestrundata.setEffective_from(getTestrun.getEffective_from());
		setTestrundata.setEffective_to(getTestrun.getEffective_to());
		setTestrundata.setTestsetname(copyTestrunvo.getNewtestrunname());
		setTestrundata.setConfigurationid(copyTestrunvo.getConfiguration());
		setTestrundata.setProjectid(copyTestrunvo.getProject());
		setTestrundata.setCreatedby(copyTestrunvo.getCreated_by());
		setTestrundata.setLastupdatedby(null);
		setTestrundata.setCreationdate(copyTestrunvo.getCreation_date());
		setTestrundata.setUpdatedate(null);
		setTestrundata.setTscompleteflag("Active");
		setTestrundata.setPasspath(getTestrun.getPasspath());
		setTestrundata.setFailpath(getTestrun.getFailpath());
		setTestrundata.setExceptionpath(getTestrun.getExceptionpath());
		setTestrundata.setTrmode("ACTIVE");
		setTestrundata.setLastexecuteby(null);
		Comparator<ScriptsData> scriptComparator = (ScriptsData s1,ScriptsData s2)->s1.getSeqnum()-s2.getSeqnum(); 
//		copyTestrunDao.saveTestrun(setTestrundata);
		 //List<ScriptsData> listsScriptdata=new ArrayList<>();
		Collections.sort(getTestrun.getScriptsdata(),scriptComparator);
		 
		for(ScriptsData getScriptdata:getTestrun.getScriptsdata()) {
			ScriptMaster obj = copyTestrunDao.getScriptMasterInfo(getScriptdata.getScriptnumber(),setTestrundata.getProjectid());
			
			int sectiptid=copyTestrunDao.getscrtiptIds();
			System.out.println("sectiptid"+sectiptid);
			ScriptsData setScriptdata=new ScriptsData();
			if(obj!=null) {
			setScriptdata.setTestsetlineid(sectiptid);
			//setScriptdata.setScriptid(getScriptdata.getScriptid());
			//setScriptdata.setScriptid((Integer)obj[0]);
			setScriptdata.setScriptid(obj.getScript_id());
			setScriptdata.setCreatedby(copyTestrunvo.getCreated_by());
			setScriptdata.setCreationdate(copyTestrunvo.getCreation_date());
			setScriptdata.setEnabled("Y");
			//setScriptdata.setScriptnumber(getScriptdata.getScriptnumber());
			//setScriptdata.setScriptnumber((String)obj[1]);
			setScriptdata.setScriptnumber(obj.getScript_number());
			//setScriptdata.setSeqnum(getScriptdata.getSeqnum());
			setScriptdata.setSeqnum(getScriptdata.getSeqnum());
			setScriptdata.setStatus("New");
			setScriptdata.setLastupdatedby(null);
			setScriptdata.setScriptUpadated("N");
			setScriptdata.setUpdateddate(null);
			setScriptdata.setTestsstlinescriptpath(getScriptdata.getTestsstlinescriptpath());
			setScriptdata.setExecutedby(null);
			setScriptdata.setExecutionstarttime(null);
			setScriptdata.setExecutionendtime(null);
			setTestrundata.addScriptsdata(setScriptdata);}
			else {
				
				continue;
			}
			Integer newScriptParamSeq=0;
			Integer oldScriptParamSeq=0;
			List<ScriptMetaData>metadataList=copyTestrunDao.getScriptMetadataInfo(setScriptdata.getScriptid());
			/*for(ScritplinesData getScriptlinedata:getScriptdata.getScriptslinedata()) {
				
				ScritplinesData setScriptlinedata=new ScritplinesData();
				int sectiptlineid=copyTestrunDao.getscrtiptlineIds();
				 System.out.println("sectiptlineid"+sectiptlineid);
				
				setScriptlinedata.setTestscriptperamid(sectiptlineid);
				System.out.println(getScriptlinedata.getInput_parameter());
				addInputvalues(getScriptlinedata,setScriptlinedata, copyTestrunvo,setScriptdata);
			
				setScriptlinedata.setInput_parameter(getScriptlinedata.getInput_parameter());
				setScriptlinedata.setScript_id(getScriptlinedata.getScript_id());
				setScriptlinedata.setScript_number(getScriptlinedata.getScript_number());
				setScriptlinedata.setLine_number(getScriptlinedata.getLine_number());
				setScriptlinedata.setAction(getScriptlinedata.getAction());
				setScriptlinedata.setTest_run_param_desc(getScriptlinedata.getTest_run_param_desc());
				setScriptlinedata.setTest_run_param_name(getScriptlinedata.getTest_run_param_name());
				setScriptlinedata.setMetadata_id(getScriptlinedata.getMetadata_id());
				setScriptlinedata.setHint(getScriptlinedata.getHint());
				setScriptlinedata.setField_type(getScriptlinedata.getField_type());
				setScriptlinedata.setXpathlocation(getScriptlinedata.getXpathlocation());
				setScriptlinedata.setXpathlocation1(getScriptlinedata.getXpathlocation1());
				setScriptlinedata.setCreatedby(copyTestrunvo.getCreated_by());
				setScriptlinedata.setCreationdate(copyTestrunvo.getCreation_date());
				setScriptlinedata.setUpdateddate(null);
				setScriptlinedata.setLastupdatedby(null);
				setScriptlinedata.setLineexecutionstatues("New");
				setScriptlinedata.setLineerrormessage(null);
				setScriptlinedata.setDatatypes(getScriptlinedata.getDatatypes());
				setScriptlinedata.setUniquemandatory(getScriptlinedata.getUniquemandatory());
				
				setScriptdata.addScriptlines(setScriptlinedata);
				}*/
				List<ScritplinesData> scriptLineList = getScriptdata.getScriptslinedata();
				Comparator<ScritplinesData> comparator = (ScritplinesData s1, ScritplinesData s2)->s1.getLine_number()-s2.getLine_number();
				Collections.sort(scriptLineList, comparator);
				//int oldScriptSeqHolder=0;
				//int newScriptSeqHolder=0;
				ScriptMetaData metadata=null;
				Integer check=null;
				ScritplinesData getScriptlinedata=null;
				ScritplinesData setScriptlinedata=null;
				while(newScriptParamSeq < metadataList.size() && oldScriptParamSeq < scriptLineList.size() ) {
					metadata=metadataList.get(newScriptParamSeq);
					
					getScriptlinedata=scriptLineList.get(oldScriptParamSeq);
					
					if(!(newScriptParamSeq.equals(check))) {
					setScriptlinedata=new ScritplinesData();
					int sectiptlineid=copyTestrunDao.getscrtiptlineIds();
					 System.out.println("sectiptlineid"+sectiptlineid);
					 setScriptlinedata.setTestscriptperamid(sectiptlineid);
					 //setScriptlinedata.setInput_parameter((String)metadata[4]);
					 setScriptlinedata.setInput_parameter(metadata.getInput_parameter());
					 setScriptlinedata.setScript_id(setScriptdata.getScriptid());
					 setScriptlinedata.setScript_number(setScriptdata.getScriptnumber());
					 //setScriptlinedata.setLine_number((Integer)metadata[3]);
					 setScriptlinedata.setLine_number(metadata.getLine_number());
					 //setScriptlinedata.setAction((String)metadata[5]);
					 setScriptlinedata.setAction(metadata.getAction());
					 setScriptlinedata.setTest_run_param_desc(metadata.getStep_desc());
					 //setScriptlinedata.setTest_run_param_name(getScriptlinedata.getTest_run_param_name());
					 //setScriptlinedata.setMetadata_id((Integer)metadata[0]);
					 //setScriptlinedata.setHint((String)metadata[14]);
					 //setScriptlinedata.setField_type((String)metadata[13]);
					 //setScriptlinedata.setXpathlocation((String)metadata[6]);
					 //setScriptlinedata.setXpathlocation1((String)metadata[7]);
					 setScriptlinedata.setMetadata_id(metadata.getScript_meta_data_id());
					 setScriptlinedata.setHint(metadata.getHint());
					 setScriptlinedata.setField_type(metadata.getField_type());
					 setScriptlinedata.setXpathlocation(metadata.getXpath_location());
					 setScriptlinedata.setXpathlocation1(metadata.getXpath_location1());
					 setScriptlinedata.setCreatedby(copyTestrunvo.getCreated_by());
					 setScriptlinedata.setCreationdate(copyTestrunvo.getCreation_date());
					 setScriptlinedata.setUpdateddate(null);
					 setScriptlinedata.setLastupdatedby(null);
					 setScriptlinedata.setLineexecutionstatues("New");
					 setScriptlinedata.setLineerrormessage(null);
					 setScriptlinedata.setDatatypes(metadata.getDatatypes());
					 setScriptlinedata.setUniquemandatory(metadata.getUnique_mandatory());
					 check=newScriptParamSeq.intValue();
					}
					 if(setScriptlinedata.getInput_parameter()!=null && getScriptlinedata.getInput_parameter()!=null ) {
						 if(setScriptlinedata.getAction().equalsIgnoreCase(getScriptlinedata.getAction()) && setScriptlinedata.getInput_parameter().equalsIgnoreCase(getScriptlinedata.getInput_parameter()) && setScriptlinedata.getLine_number()== getScriptlinedata.getLine_number() ) {
						 //setScriptlinedata.setInput_value(getScriptlinedata.getInput_value());
							 addInputvalues(getScriptlinedata,setScriptlinedata, copyTestrunvo,setScriptdata);
						 }
						 else {
							 setScriptlinedata.setInput_value(null);
						 }
					 }
					 else {
						 setScriptlinedata.setInput_value(null);
					 }
					 if(setScriptlinedata.getLine_number() >= getScriptlinedata.getLine_number()) {
						 oldScriptParamSeq++;
						 
					 }
					 if(getScriptlinedata.getLine_number() >= setScriptlinedata.getLine_number()) {
						 newScriptParamSeq++;
						 
					 }
					 
					 //addInputvalues(getScriptlinedata,setScriptlinedata, copyTestrunvo,setScriptdata);
					 
					 setScriptdata.addScriptlines(setScriptlinedata);
				
				}
				newScriptParamSeq++;
				while(newScriptParamSeq < metadataList.size()) {
					metadata=metadataList.get(newScriptParamSeq);
					setScriptlinedata=new ScritplinesData();
					int sectiptlineid=copyTestrunDao.getscrtiptlineIds();
					 System.out.println("sectiptlineid"+sectiptlineid);
					 setScriptlinedata.setTestscriptperamid(sectiptlineid);
					 setScriptlinedata.setInput_parameter(metadata.getInput_parameter());
					 setScriptlinedata.setScript_id(setScriptdata.getScriptid());
					 setScriptlinedata.setScript_number(setScriptdata.getScriptnumber());
					 //setScriptlinedata.setLine_number((Integer)metadata[3]);
					 setScriptlinedata.setLine_number(metadata.getLine_number());
					 //setScriptlinedata.setAction((String)metadata[5]);
					 setScriptlinedata.setAction(metadata.getAction());
					 
					 setScriptlinedata.setMetadata_id(metadata.getScript_meta_data_id());
					 setScriptlinedata.setHint(metadata.getHint());
					 setScriptlinedata.setField_type(metadata.getField_type());
					 setScriptlinedata.setXpathlocation(metadata.getXpath_location());
					 setScriptlinedata.setXpathlocation1(metadata.getXpath_location1());
					 setScriptlinedata.setCreatedby(copyTestrunvo.getCreated_by());
					 setScriptlinedata.setCreationdate(copyTestrunvo.getCreation_date());
					 setScriptlinedata.setUpdateddate(null);
					 setScriptlinedata.setLastupdatedby(null);
					 setScriptlinedata.setLineexecutionstatues("New");
					 setScriptlinedata.setLineerrormessage(null);
					 setScriptlinedata.setDatatypes(metadata.getDatatypes());
					 setScriptlinedata.setUniquemandatory(metadata.getUnique_mandatory());
					 
					 setScriptlinedata.setInput_value(null);
					 
					 newScriptParamSeq++;
					 
				}
			
			}
		
//		setTestrundata.setScriptsdata(listsScriptdata);
		System.out.println("before saveTestrun");

		int newtestrun= copyTestrunDao.saveTestrun(setTestrundata);
		System.out.println("newtestrun 1:"+newtestrun);
	return newtestrun;
	}
	private void addInputvalues(ScritplinesData getScriptlinedata, ScritplinesData setScriptlinedata,CopytestrunVo copyTestrunvo, ScriptsData setScriptdata) throws InterruptedException {
		String getInputvalues=getScriptlinedata.getInput_value();
		String[] actios= {"clearandtype" ,"textarea", "selectAValue", "clickCheckbox","selectByText","clickButton Dropdown", "clickLinkAction","Table Dropdown Values",
				"Table SendKeys", "enterIntoTable" ,"SendKeys", "Login into Application", "Dropdown Values","typeAtPosition" ,"clickAndTypeAtPosition",
				"clickRadiobutton","clickCheckbox" ,"multipleSendKeys","multiplelinestableSendKeys","DatePicker","copynumber","copytext","paste"}; 
		List<String> actionsList = new ArrayList<>(Arrays.asList(actios));
		if("y".equalsIgnoreCase(copyTestrunvo.getIncrement_value())&&(setScriptlinedata.getUniquemandatory()!=null&&setScriptlinedata.getUniquemandatory()!="NA")&&(setScriptlinedata.getUniquemandatory().equalsIgnoreCase("Unique")||setScriptlinedata.getUniquemandatory().equalsIgnoreCase("Both"))) {
			if((setScriptlinedata.getDatatypes()!=null&&setScriptlinedata.getDatatypes()!="NA")&&setScriptlinedata.getDatatypes().equalsIgnoreCase("Alpha-Numeric")) {
				DateFormat dateformate = new SimpleDateFormat("dd-MM-yy HH:mm:ss.SSS");
				Date dateobj = new Date();
				String covertDateobj=dateformate.format(dateobj);
				Thread.sleep(1);
				covertDateobj=covertDateobj.replaceAll("[^0-9]", "");
				int fistOff=Integer.parseInt(covertDateobj.substring(0, 8));
				int secondHalf=Integer.parseInt(covertDateobj.substring(8, 15));
				String hexaDecimal=Integer.toString(fistOff , 36)+Integer.toString(secondHalf , 36);
				if(getInputvalues==null||"copynumber".equalsIgnoreCase(setScriptlinedata.getAction())) {
					hexaDecimal=getInputvalues;
					if(actionsList.contains(setScriptlinedata.getAction())){
						setScriptdata.setScriptUpadated("Y");
					}
				}else if("paste".equalsIgnoreCase(setScriptlinedata.getAction())&&"copyTestRun".equalsIgnoreCase(copyTestrunvo.getRequesttype())) {
					hexaDecimal=getInputvalues.replace(getInputvalues.split(">")[0], copyTestrunvo.getNewtestrunname());
				}
				else if(getInputvalues.length()>5) {
				hexaDecimal=getInputvalues.substring(0, 5)+hexaDecimal;
				}else {
					hexaDecimal=getInputvalues+hexaDecimal;
				}
				setScriptlinedata.setInput_value(hexaDecimal);
			}
			else {
				DateFormat dateformate = new SimpleDateFormat("dd-MM-yy HH:mm:ss.SSS");
				Date dateobj = new Date();
				String covertDateobj=dateformate.format(dateobj);
				Thread.sleep(1);
				covertDateobj=covertDateobj.replaceAll("[^0-9]", "");
				if(getInputvalues==null||"copynumber".equalsIgnoreCase(setScriptlinedata.getAction())) {
					setScriptlinedata.setInput_value(getInputvalues);
					if(actionsList.contains(setScriptlinedata.getAction())){
						setScriptdata.setScriptUpadated("Y");
					}
				}else if("paste".equalsIgnoreCase(setScriptlinedata.getAction())&&"copyTestRun".equalsIgnoreCase(copyTestrunvo.getRequesttype())) {
					setScriptlinedata.setInput_value(getInputvalues.replace(getInputvalues.split(">")[0], copyTestrunvo.getNewtestrunname()));
				}else {
				setScriptlinedata.setInput_value(covertDateobj);
				}
			}
			}else if("Mandatory".equalsIgnoreCase(setScriptlinedata.getUniquemandatory())){
				if(getInputvalues==null||"copynumber".equalsIgnoreCase(setScriptlinedata.getAction())) {
					setScriptlinedata.setInput_value(null);
					if(actionsList.contains(setScriptlinedata.getAction())){
						setScriptdata.setScriptUpadated("Y");
					}

				}else if("paste".equalsIgnoreCase(setScriptlinedata.getAction())&&"copyTestRun".equalsIgnoreCase(copyTestrunvo.getRequesttype())) {
					setScriptlinedata.setInput_value(getInputvalues.replace(getInputvalues.split(">")[0], copyTestrunvo.getNewtestrunname()));
				}
				else {
				setScriptlinedata.setInput_value(getScriptlinedata.getInput_value());
				}
			}else {
					if(getInputvalues==null||"copynumber".equalsIgnoreCase(setScriptlinedata.getAction())) {
						setScriptlinedata.setInput_value(null);

					}else if("paste".equalsIgnoreCase(setScriptlinedata.getAction())&&"copyTestRun".equalsIgnoreCase(copyTestrunvo.getRequesttype())) {
						setScriptlinedata.setInput_value(getInputvalues.replace(getInputvalues.split(">")[0], copyTestrunvo.getNewtestrunname()));
					}
					else {
					setScriptlinedata.setInput_value(getScriptlinedata.getInput_value());
					}
				}
		
	}
	@Transactional
	public int reRun(@Valid CopytestrunVo copyTestrunvo) throws InterruptedException {
		Testrundata getTestrun=copyTestrunDao.getdata(copyTestrunvo.getTestScriptNo());
		System.out.println("getTestrun infromation");
		for(ScriptsData getScriptdata:getTestrun.getScriptsdata()) {
			String status=getScriptdata.getStatus();
			if(status.equalsIgnoreCase("fail")) {
//				getTestrun.addScriptsdata(getScriptdata);
				for(ScritplinesData getScriptlinedata:getScriptdata.getScriptslinedata()) {
					ScritplinesData setScriptlinedata=new ScritplinesData();
				addInputvalues(getScriptlinedata,setScriptlinedata, copyTestrunvo,getScriptdata);
				getScriptlinedata.setInput_value(setScriptlinedata.getInput_value());
//				getScriptdata.addScriptlines(getScriptlinedata);
				}
			}
		}
		System.out.println("before update");
		int newtestrun= copyTestrunDao.update(getTestrun);
		System.out.println("newtestrun 2:"+newtestrun);
		return newtestrun;
	}

}