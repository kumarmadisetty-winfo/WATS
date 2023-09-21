package com.winfo.serviceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.winfo.controller.ScriptHealController;
import com.winfo.exception.WatsEBSException;
import com.winfo.model.ScriptMaster;
import com.winfo.repository.ScriptMasterRepository;
import com.winfo.service.ScriptHealService;
import com.winfo.utils.CommonObjectStoreUtils;
import com.winfo.utils.Constants;
import com.winfo.vo.ScriptHealVo;

@Service
public class ScriptHealServiceImpl implements ScriptHealService{

	public static final Logger logger = Logger.getLogger(ScriptHealServiceImpl.class);
	@Autowired
	ScriptMasterRepository scriptMasterRepository;
	@Autowired
	CommonObjectStoreUtils commonObjectStoreUtils;
	
	public List<ScriptHealVo> getNewInputParameters(String targetApplication, String productVersion, String module) throws IOException {
		try {
			List<ScriptMaster> scriptMasters = scriptMasterRepository.findByTargetApplicationAndProductVersionAndModule(targetApplication,productVersion,module);
			PDDocument document = commonObjectStoreUtils.readFileFromCommonObjectStore(Constants.SCRIPT_HEAL+"/"+
					targetApplication,productVersion+" release notes.pdf");
			PDFTextStripper textStripper = new PDFTextStripper();
			String text = textStripper.getText(document);
			List<ScriptHealVo> listOfOldNewInputParameters=new ArrayList<ScriptHealVo>();
			if(scriptMasters.size()>0) {
				scriptMasters.parallelStream().forEach((scriptMaster)->{
					scriptMaster.getScriptMetaDatalist().parallelStream().forEach((metadata)->{
						if(!"".equals(metadata.getInputParameter()) && metadata.getInputParameter()!=null) {
							Arrays.stream(metadata.getInputParameter().split(">")).forEach((inputParameter)->{
								if(!"".equals(inputParameter) && inputParameter!=null && text.indexOf(inputParameter)!=-1){
									String val=text.substring(text.indexOf(inputParameter)+inputParameter.length()+1);
									val=val.substring(0,val.indexOf("\n")).trim();						
									ScriptHealVo oldNewInputParameter=new ScriptHealVo();
									oldNewInputParameter.setScriptNumber(metadata.getScriptNumber());
									oldNewInputParameter.setLineNumber(metadata.getLineNumber());
									oldNewInputParameter.setOldInputParameter(inputParameter);
									oldNewInputParameter.setNewInputParameter(val);
									listOfOldNewInputParameters.add(oldNewInputParameter);
								}
							});
						}
					});	
				});	
			}
	        document.close();
	        return listOfOldNewInputParameters;
	  	} catch (IOException e) {
      	logger.error("Exception Occurred while fetching the new Input Parameter");
      	throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception Occurred while fetching the new Input Parameter"); 
      }
	}
}
