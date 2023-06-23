package com.winfo.serviceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.winfo.model.ScriptMaster;
import com.winfo.repository.ScriptMasterRepository;
import com.winfo.utils.CommonObjectStoreUtils;
import com.winfo.vo.ScriptHealVo;

@Service
public class ScriptHealService {

	@Autowired
	ScriptMasterRepository scriptMasterRepository;
	@Autowired
	CommonObjectStoreUtils commonObjectStoreUtils;
	
	public ResponseEntity<List<ScriptHealVo>> getNewInputParameters(String targetApplication, String productVersion, String module) throws IOException {

		List<ScriptMaster> scriptMasters = scriptMasterRepository.findByTargetApplicationAndProductVersionAndModule(targetApplication,productVersion,module);
		PDDocument document = commonObjectStoreUtils.readFileFromCommonObjectStore("Script Heal/"+
				targetApplication,productVersion+" release notes.pdf");
		PDFTextStripper textStripper = new PDFTextStripper();
		String text = textStripper.getText(document);
		
		List<ScriptHealVo> listOfOldNewInputParameters=new ArrayList<ScriptHealVo>();
		scriptMasters.parallelStream().forEach((scriptMaster)->{
			scriptMaster.getScriptMetaDatalist().parallelStream().forEach((metadata)->{
				if(!"".equals(metadata.getInputParameter()) && metadata.getInputParameter()!=null) {
					String[] inputParameters=metadata.getInputParameter().split(">");
					Arrays.stream(inputParameters).forEach((inputParameter)->{
						if(text.indexOf(inputParameter)!=-1){
							String val=text.substring(text.indexOf(inputParameter)+inputParameter.length()+1);
							val=val.substring(0,val.indexOf("\n")).trim();						
							ScriptHealVo OldNewInputParameter=new ScriptHealVo();
							OldNewInputParameter.setScriptNumber(metadata.getScriptNumber());
							OldNewInputParameter.setLineNumber(metadata.getLineNumber());
							OldNewInputParameter.setOldInputParameter(inputParameter);
							OldNewInputParameter.setNewInputParameter(val);
							listOfOldNewInputParameters.add(OldNewInputParameter);
						}
					});
				}
			});	
		});
        document.close();
        return new ResponseEntity<List<ScriptHealVo>>(listOfOldNewInputParameters,HttpStatus.OK) ;
		
	}
}
