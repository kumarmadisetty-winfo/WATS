package com.winfo.interceptor;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.winfo.dao.VmInstanceDAO;
import com.winfo.services.VMDetailesService;

@Component
public class SchedulerService {
	Logger log = Logger.getLogger("Logger");
	@Autowired
	private VmInstanceDAO vmInstanceDAO;
	@Autowired
	private VMDetailesService vmDetaliesService;
  @Scheduled(cron = "${scheduling.job.cron}")
   @Transactional
   public void schedulingToStopVm() throws Exception {
	   try {
	    Boolean scriptsActive=vmInstanceDAO.isAnyScriptsInprogresOrInqueue();
	    if(!scriptsActive) {
	    	vmDetaliesService.stopInstance();
	    	log.info("all vms are stooped");
	    }
	   }catch (Exception e) {
		   log.error("exception"+e);
	}
   }
}