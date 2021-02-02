package com.winfo.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.winfo.vo.DomGenericResponseBean;

@Service
public class CopyTestRunService {
	@Autowired
	CopyTestRunDao copyTestrunDao;
	@Transactional
	public DomGenericResponseBean copyTestrun(@Valid CopytestrunVo copyTestrunvo) throws InterruptedException {
		Testrundata getTestrun=copyTestrunDao.getdata(copyTestrunvo.getTestScriptNo());
		List<Integer> sequencenumbers=copyTestrunDao.getIds();
		Testrundata setTestrundata=new Testrundata();
		setTestrundata.setTest_set_desc(getTestrun.getTest_set_desc());
		setTestrundata.setTest_set_comments(getTestrun.getTest_set_comments());
		setTestrundata.setEnabled(getTestrun.getEnabled());
		setTestrundata.setDescription(getTestrun.getDescription());
		setTestrundata.setEffective_from(getTestrun.getEffective_from());
		setTestrundata.setEffective_to(getTestrun.getEffective_to());
		setTestrundata.setTestsetname(copyTestrunvo.getNewtestrunname());
		setTestrundata.setConfigurationid(copyTestrunvo.getConfiguration());
		setTestrundata.setProjectid(copyTestrunvo.getProject());
		 List<ScriptsData> listsScriptdata=new ArrayList<>();
		for(ScriptsData getScriptdata:getTestrun.getScriptsdata()) {
			
			ScriptsData setScriptdata=new ScriptsData();
			setScriptdata.setScriptid(getScriptdata.getScriptid());
			setScriptdata.setCreatedby(getScriptdata.getCreatedby());
			setScriptdata.setCreationdate(getScriptdata.getCreationdate());
			setScriptdata.setEnabled(getScriptdata.getEnabled());
			setScriptdata.setScriptnumber(getScriptdata.getScriptnumber());
			setScriptdata.setSeqnum(getScriptdata.getSeqnum());
			setScriptdata.setStatus("new");
			setTestrundata.addScriptsdata(setScriptdata);
			for(ScritplinesData getScriptlinedata:getScriptdata.getScriptslinedata()) {
				ScritplinesData setScriptlinedata=new ScritplinesData();
	
				String getInputvalues=getScriptlinedata.getInput_value();
				System.out.println(getScriptlinedata.getInput_parameter().substring(0, 3));
				if(getScriptlinedata.getInput_parameter().substring(0, 3).contains("(#")) {
					if(getScriptlinedata.getValidationtype().equalsIgnoreCase("alphanumeric")) {
						DateFormat dateformate = new SimpleDateFormat("dd-MM-yy HH:mm:ss.SSS");
						Date dateobj = new Date();
						String covertDateobj=dateformate.format(dateobj);
						Thread.sleep(1);
						covertDateobj=covertDateobj.replaceAll("[^0-9]", "");
						int fistOff=Integer.parseInt(covertDateobj.substring(0, 8));
						int secondHalf=Integer.parseInt(covertDateobj.substring(8, 15));
						String hexaDecimal=Integer.toString(fistOff , 36)+Integer.toString(secondHalf , 36);
						hexaDecimal=getInputvalues.substring(0, 5)+hexaDecimal;
						setScriptlinedata.setInput_value(hexaDecimal);
					}
					else if(getScriptlinedata.getValidationtype().equalsIgnoreCase("numeric")) {
						DateFormat dateformate = new SimpleDateFormat("dd-MM-yy HH:mm:ss.SSS");
						Date dateobj = new Date();
						String covertDateobj=dateformate.format(dateobj);
						Thread.sleep(1);
						covertDateobj=covertDateobj.replaceAll("[^0-9]", "");
						setScriptlinedata.setInput_value(covertDateobj);
					}
					}else {
						setScriptlinedata.setInput_value(getScriptlinedata.getInput_value());
					}
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
				setScriptlinedata.setValidationtype(getScriptlinedata.getValidationtype());
				setScriptdata.addScriptlines(setScriptlinedata);
				}
			}
		copyTestrunDao.saveTestrun(setTestrundata);
//		setTestrundata.setScriptsdata(listsScriptdata);
		

		return null;
	
	}

}
