package com.winfo.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winfo.dao.CopyDataCustomerDao;
import com.winfo.model.ScriptMaster;
import com.winfo.vo.CopyDataDetails;
import com.winfo.vo.DomGenericResponseBean;

@Service
public class CopyDataCustomerService {
	@Autowired
	CopyDataCustomerDao dao;

	@Transactional
	public DomGenericResponseBean copyData(CopyDataDetails copyDataDetails) {

		List<ScriptMaster> masterDtlsforOldProductVersion = dao
				.getScriptMasterDtlByProductVersion(copyDataDetails.getProductVersionOld());

		List<ScriptMaster> masterDtlsforNewProductVersion = dao
				.getScriptMasterDtlByProductVersion(copyDataDetails.getProductVersionNew());

		Map<String, ScriptMaster> mapOfScriptMasterNew = new HashMap<>();

		for (ScriptMaster newScriptMasterDtl : masterDtlsforNewProductVersion) {

			mapOfScriptMasterNew.put(newScriptMasterDtl.getScriptNumber(), newScriptMasterDtl);

		}
		Map<Integer, ScriptMaster> mapOfScriptMasterNewToUpdate = new HashMap<>();
		
		List<ScriptMaster> newScriptWithNewProductVersion = new ArrayList<>();

		int count = 0;
		for (ScriptMaster oldScriptMasterDtl : masterDtlsforOldProductVersion) {

			if (mapOfScriptMasterNew.containsKey(oldScriptMasterDtl.getScriptNumber())) {
				return new DomGenericResponseBean(409, "error",
						"Source script already present in Target Location. Please check the Script in target product version.");
			} else {
				count++;
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				ScriptMaster newScriptMasterDtl;
				newScriptMasterDtl = mapper.convertValue(oldScriptMasterDtl, ScriptMaster.class);

					mapOfScriptMasterNewToUpdate.put(oldScriptMasterDtl.getScriptId(), newScriptMasterDtl);
					newScriptMasterDtl.setProductVersion(copyDataDetails.getProductVersionNew());
					newScriptWithNewProductVersion.add(newScriptMasterDtl);
				}
				
			}

		for (ScriptMaster masterObj : newScriptWithNewProductVersion) {
			dao.updateScriptDtlsInMasterTable(masterObj);
		}

		return new DomGenericResponseBean(200, "success",
				count + " Script'(s) Copied" + " to " + copyDataDetails.getProductVersionNew() + " Successfully");

	}		
		
}
