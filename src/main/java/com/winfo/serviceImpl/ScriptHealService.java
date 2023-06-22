package com.winfo.serviceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import com.winfo.model.ScriptMaster;
import com.winfo.repository.ScriptMasterRepository;
import com.winfo.utils.CommonObjectStoreUtils;

@Service
public class ScriptHealService {

	@Autowired
	ScriptMasterRepository scriptMasterRepository;
	@Autowired
	CommonObjectStoreUtils commonObjectStoreUtils;
	
	public Map<Integer, Map<String,String>> getNewInputParameters(int scriptId) throws IOException {

		ScriptMaster scriptMaster = scriptMasterRepository.findByScriptId(scriptId);
		PDDocument document = commonObjectStoreUtils.readFileFromCommonObjectStore("Script Heal/"+
				scriptMaster.getTargetApplication(),scriptMaster.getProductVersion()+" release notes.pdf");
		PDFTextStripper textStripper = new PDFTextStripper();
		String text = textStripper.getText(document);
		
		Map<Integer, Map<String,String>> result=new HashMap<Integer, Map<String, String>>();
		scriptMaster.getScriptMetaDatalist().parallelStream().forEach((metadata)->{
			Map<String,String> oldNewInputParameters=new HashMap<String, String>();
			String[] inputParameters=metadata.getInputParameter().split(">");
			for(int i=0;i<inputParameters.length;i++) {
				if(text.indexOf(inputParameters[i])!=-1){
					String val=text.substring(text.indexOf(inputParameters[i])+inputParameters[i].length()+1);
					val=val.substring(0,val.indexOf("\n")).trim();
					oldNewInputParameters.put(inputParameters[i],val);						
				}
			}
			if(oldNewInputParameters.size()>0) {
				result.put(metadata.getLineNumber(),oldNewInputParameters);					
			}
		});
        document.close();
        return result;
		
	}
}
