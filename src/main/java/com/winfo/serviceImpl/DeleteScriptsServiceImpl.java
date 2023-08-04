package com.winfo.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import com.winfo.model.ScriptMaster;
import com.winfo.repository.ScriptMasterRepository;
import com.winfo.repository.TestSetLinesRepository;
import com.winfo.service.DeleteScriptsService;
import com.winfo.utils.Constants;
import com.winfo.vo.DeleteScriptsVO;
import com.winfo.vo.ResponseDto;

@Repository
public class DeleteScriptsServiceImpl implements DeleteScriptsService {

	public static final Logger logger = Logger.getLogger(DeleteScriptsServiceImpl.class);
	@Autowired
	private TestSetLinesRepository testSetLinesRepository;

	@Autowired
	private ScriptMasterRepository scriptMasterRepository;

	@Transactional
	public ResponseDto deleteData(@RequestBody DeleteScriptsVO deletescriptsdata) {

		try {
			List<ScriptMaster>listOfTotalDeletedScripts=new ArrayList<>();
			List<ScriptMaster>listOfScriptsPresentInTestRun=new ArrayList<>();
			String responseDescription;
			if (deletescriptsdata.isDeleteAll()) {
				List<ScriptMaster> listOfScriptsFromProductVersion = scriptMasterRepository.findByProductVersion(deletescriptsdata.getProductVersion());
				List<ScriptMaster> listOfScriptsNotPresentInTestRun = listOfScriptsFromProductVersion.parallelStream()
						.filter(scriptMaster -> testSetLinesRepository.countByScriptId(scriptMaster.getScriptId()) == 0).collect(Collectors.toList());
				listOfScriptsPresentInTestRun=listOfScriptsFromProductVersion.parallelStream()
						.filter(scriptMaster->!listOfScriptsNotPresentInTestRun.contains(scriptMaster)).collect(Collectors.toList());
				listOfTotalDeletedScripts=listOfScriptsNotPresentInTestRun.parallelStream().map(scriptMaster ->{
					scriptMasterRepository.deleteByScriptId(scriptMaster.getScriptId());
					return scriptMaster;
				}).collect(Collectors.toList());
			} else {
				List<Integer> listOfScriptIds = deletescriptsdata.getScriptId();
				List<ScriptMaster> listOfScripts = scriptMasterRepository.findByScriptIds(listOfScriptIds);
				List<ScriptMaster> listOfScriptsNotPresentInTestRun = listOfScripts.parallelStream()
						.filter(scriptMaster ->testSetLinesRepository.countByScriptId(scriptMaster.getScriptId()) == 0).collect(Collectors.toList());
				listOfScriptsPresentInTestRun=listOfScripts.parallelStream()
						.filter(scriptMaster->!listOfScriptsNotPresentInTestRun.contains(scriptMaster)).collect(Collectors.toList());
				listOfTotalDeletedScripts=listOfScriptsNotPresentInTestRun.parallelStream().map(scriptMaster ->{
					scriptMasterRepository.deleteByScriptId(scriptMaster.getScriptId());
					return scriptMaster;
				}).collect(Collectors.toList());
			}
			
			responseDescription=(listOfTotalDeletedScripts.size()==1?listOfTotalDeletedScripts.get(0).getScriptNumber()+" is successfully deleted"
					:listOfTotalDeletedScripts.size()>1?listOfTotalDeletedScripts.size()+" scripts are successfully deleted":"");
			responseDescription=(responseDescription!=""? responseDescription+", ":"")+(listOfScriptsPresentInTestRun.size()==1?
					listOfScriptsPresentInTestRun.get(0).getScriptNumber()+" is not able to delete because this script is added in Test Runs"
					:(listOfScriptsPresentInTestRun.size()>1?listOfScriptsPresentInTestRun.size()+ " scripts are not able to delete because those scripts are added in Test Runs":""));

			logger.info(responseDescription);
			return new ResponseDto(HttpStatus.OK.value(), Constants.SUCCESS, responseDescription);
		} catch (Exception e) {
			logger.error("Error occurred while deleting scripts, " + e.getMessage());
			return new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR, e.getMessage());
		}
	}
}
