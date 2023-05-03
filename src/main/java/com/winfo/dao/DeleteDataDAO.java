package com.winfo.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import com.winfo.config.MessageUtil;
import com.winfo.model.ScriptMaster;
import com.winfo.utils.Constants;
import com.winfo.vo.DeleteScriptsData;
import com.winfo.vo.DomGenericResponseBean;

@Repository
public class DeleteDataDAO {

	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	ApplicationContext appContext;
	
	@Autowired
	private MessageUtil messageUtil;

	public List<DomGenericResponseBean> deleteData(@RequestBody DeleteScriptsData deletescriptsdata) {
		Session session = entityManager.unwrap(Session.class);
		Transaction tx = session.beginTransaction();
		int deleted = 0;
		int deleted1 = 0;

		String scriptNumber = null;

		List<DomGenericResponseBean> bean = new ArrayList<>();
		List<Integer> scriptIds = deletescriptsdata.getScript_id();
		int i = 0;
		int count = 0;

		if (deletescriptsdata.isDeleteAll()) {

			List<ScriptMaster> listOfScriptmasterObj = appContext.getBean(this.getClass()).getScriptIdFromProductVersion(deletescriptsdata.getProd_ver());
			for(ScriptMaster scriptMasterObj : listOfScriptmasterObj) {
				scriptIds.add(scriptMasterObj.getScriptId());
				session.delete(scriptMasterObj);
				i++;
			}
			
		} else {
			try {
				for (i = 0; i < scriptIds.size(); i++) {
					count = 0;
					Integer scriptId = scriptIds.get(i);
					if (scriptIds.size() == 1) {
						Query<?> query2 = session
								.createQuery("select scriptNumber from ScriptMaster where scriptId=" + scriptId);
						scriptNumber = (String) query2.getSingleResult();

					}
					Query<?> query = session.createQuery("delete from ScriptMaster where scriptId=" + scriptId);
					Query<?> query1 = session
							.createQuery("delete from ScriptMetaData where scriptMaster.scriptId=" + scriptId);

					deleted = query1.executeUpdate();
					deleted1 = query.executeUpdate();

					if (deleted == 0 && deleted1 == 0) {
						count = 1;
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		DomGenericResponseBean response = new DomGenericResponseBean();
		if (count == 1) {
			response.setStatus(404);
			response.setStatusMessage(Constants.ERROR);
			response.setDescription(messageUtil.getDeleteDataDao().getError().getScriptNotFound());
			bean.add(response);
		} else {
			response.setStatus(200);
			response.setStatusMessage(Constants.SUCCESS);
			if (scriptIds.size() == 1) {
				response.setDescription(MessageUtil.getMessage(messageUtil.getDeleteDataDao().getSuccess().getScriptDeleted(),scriptNumber));
			} else {
				response.setDescription(MessageUtil.getMessage(messageUtil.getDeleteDataDao().getSuccess().getScriptDeleted(),i));
			}
			bean.add(response);
		}

		tx.commit();
		return bean;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<ScriptMaster> getScriptIdFromProductVersion(String productVersion) {
		
		Session session = entityManager.unwrap(Session.class);
		
		Query<?> query = session.createQuery("from ScriptMaster where productVersion =:productVersion");
		query.setParameter("productVersion", productVersion);
		
		return (List<ScriptMaster>)query.getResultList();
		
	}
}
