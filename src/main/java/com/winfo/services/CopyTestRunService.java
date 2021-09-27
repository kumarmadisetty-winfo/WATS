package com.winfo.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winfo.dao.CopyTestRunDao;
import com.winfo.model.ScriptsData;
import com.winfo.model.ScritplinesData;
import com.winfo.model.Testrundata;
import com.winfo.vo.CopytestrunVo;

@Service
public class CopyTestRunService {
	@Autowired
	CopyTestRunDao copyTestrunDao;
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
		
//		copyTestrunDao.saveTestrun(setTestrundata);
		 List<ScriptsData> listsScriptdata=new ArrayList<>();
		for(ScriptsData getScriptdata:getTestrun.getScriptsdata()) {
			int sectiptid=copyTestrunDao.getscrtiptIds();
			 System.out.println("sectiptid"+sectiptid);
			ScriptsData setScriptdata=new ScriptsData();
			setScriptdata.setTestsetlineid(sectiptid);
			setScriptdata.setScriptid(getScriptdata.getScriptid());
			setScriptdata.setCreatedby(copyTestrunvo.getCreated_by());
			setScriptdata.setCreationdate(copyTestrunvo.getCreation_date());
			setScriptdata.setEnabled("Y");
			setScriptdata.setScriptnumber(getScriptdata.getScriptnumber());
			setScriptdata.setSeqnum(getScriptdata.getSeqnum());
			setScriptdata.setStatus("New");
			setScriptdata.setLastupdatedby(null);
			setScriptdata.setScriptUpadated("N");
			setScriptdata.setUpdateddate(null);
			setScriptdata.setTestsstlinescriptpath(getScriptdata.getTestsstlinescriptpath());
			setScriptdata.setExecutedby(null);
			setScriptdata.setExecutionstarttime(null);
			setScriptdata.setExecutionendtime(null);
			setTestrundata.addScriptsdata(setScriptdata);
			for(ScritplinesData getScriptlinedata:getScriptdata.getScriptslinedata()) {
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
		if("y".equalsIgnoreCase(copyTestrunvo.getIncrement_value())&&(getScriptlinedata.getUniquemandatory()!=null&&getScriptlinedata.getUniquemandatory()!="NA")&&(getScriptlinedata.getUniquemandatory().equalsIgnoreCase("Unique")||getScriptlinedata.getUniquemandatory().equalsIgnoreCase("Both"))) {
			if((getScriptlinedata.getDatatypes()!=null&&getScriptlinedata.getDatatypes()!="NA")&&getScriptlinedata.getDatatypes().equalsIgnoreCase("Alpha-Numeric")) {
				DateFormat dateformate = new SimpleDateFormat("dd-MM-yy HH:mm:ss.SSS");
				Date dateobj = new Date();
				String covertDateobj=dateformate.format(dateobj);
				Thread.sleep(1);
				covertDateobj=covertDateobj.replaceAll("[^0-9]", "");
				int fistOff=Integer.parseInt(covertDateobj.substring(0, 8));
				int secondHalf=Integer.parseInt(covertDateobj.substring(8, 15));
				String hexaDecimal=Integer.toString(fistOff , 36)+Integer.toString(secondHalf , 36);
				if(getInputvalues==null||"copynumber".equalsIgnoreCase(getScriptlinedata.getAction())) {
					hexaDecimal=getInputvalues;
					if(actionsList.contains(getScriptlinedata.getAction())){
						setScriptdata.setScriptUpadated("Y");
					}
				}else if("paste".equalsIgnoreCase(getScriptlinedata.getAction())&&"copyTestRun".equalsIgnoreCase(copyTestrunvo.getRequesttype())) {
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
				if(getInputvalues==null||"copynumber".equalsIgnoreCase(getScriptlinedata.getAction())) {
					setScriptlinedata.setInput_value(getInputvalues);
					if(actionsList.contains(getScriptlinedata.getAction())){
						setScriptdata.setScriptUpadated("Y");
					}
				}else if("paste".equalsIgnoreCase(getScriptlinedata.getAction())&&"copyTestRun".equalsIgnoreCase(copyTestrunvo.getRequesttype())) {
					setScriptlinedata.setInput_value(getInputvalues.replace(getInputvalues.split(">")[0], copyTestrunvo.getNewtestrunname()));
				}else {
				setScriptlinedata.setInput_value(covertDateobj);
				}
			}
			}else if("Mandatory".equalsIgnoreCase(getScriptlinedata.getUniquemandatory())){
				if(getInputvalues==null||"copynumber".equalsIgnoreCase(getScriptlinedata.getAction())) {
					setScriptlinedata.setInput_value(null);
					if(actionsList.contains(getScriptlinedata.getAction())){
						setScriptdata.setScriptUpadated("Y");
					}

				}else if("paste".equalsIgnoreCase(getScriptlinedata.getAction())&&"copyTestRun".equalsIgnoreCase(copyTestrunvo.getRequesttype())) {
					setScriptlinedata.setInput_value(getInputvalues.replace(getInputvalues.split(">")[0], copyTestrunvo.getNewtestrunname()));
				}
				else {
				setScriptlinedata.setInput_value(getScriptlinedata.getInput_value());
				}
			}else {
					if(getInputvalues==null||"copynumber".equalsIgnoreCase(getScriptlinedata.getAction())) {
						setScriptlinedata.setInput_value(null);

					}else if("paste".equalsIgnoreCase(getScriptlinedata.getAction())&&"copyTestRun".equalsIgnoreCase(copyTestrunvo.getRequesttype())) {
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