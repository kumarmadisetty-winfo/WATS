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
import com.winfo.config.MessageUtil;
import com.winfo.dao.CopyDataCustomerDao;
import com.winfo.model.ScriptMaster;
import com.winfo.model.ScriptMetaData;
import com.winfo.utils.Constants;
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

		List<ScriptMaster> newScriptWithNewProductVersion = new ArrayList<>();

		int count = 0;
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		for (ScriptMaster oldScriptMasterDtl : masterDtlsforOldProductVersion) {

			if (!mapOfScriptMasterNew.containsKey(oldScriptMasterDtl.getScriptNumber())) {
				count++;
				ScriptMaster newScriptMasterDtl;
				newScriptMasterDtl = mapper.convertValue(oldScriptMasterDtl, ScriptMaster.class);
				newScriptMasterDtl.setScriptId(null);
				for(ScriptMetaData scriptMetaDataObj : newScriptMasterDtl.getScriptMetaDatalist()) {
					scriptMetaDataObj.setScriptMetaDataId(null);
				}
				newScriptMasterDtl.setProductVersion(copyDataDetails.getProductVersionNew());
				newScriptWithNewProductVersion.add(newScriptMasterDtl);
			}

		}
		Map<String, Integer> scriptMasterIdAndNumber = new HashMap<>();
		for (ScriptMaster masterObj : newScriptWithNewProductVersion) {
			masterObj = dao.insertScriptDtlsInMasterTable(masterObj);
			scriptMasterIdAndNumber.put(masterObj.getScriptNumber(), masterObj.getScriptId());
			if (masterObj.getParent() != null) {
				masterObj.setDependency(scriptMasterIdAndNumber.get(masterObj.getParent().getScriptNumber()));
				dao.insertScriptDtlsInMasterTable(masterObj);
			}
		}

		return new DomGenericResponseBean(200, Constants.SUCCESS, MessageUtil
				.getMessage("CopyDataCustomerService.Success.CopyData", count, copyDataDetails.getProductVersionNew()));

	}

}
