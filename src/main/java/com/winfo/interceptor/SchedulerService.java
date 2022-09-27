package com.winfo.interceptor;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.winfo.dao.VmInstanceDAO;
import com.winfo.services.VMDetailesService;

@Component
public class SchedulerService {
	public static final Logger log = Logger.getLogger(SchedulerService.class);
	@Autowired
	private VmInstanceDAO vmInstanceDAO;
	@Autowired
	private VMDetailesService vmDetaliesService;
	@Value("${costoptimisationFlag}")
	private boolean flag; 
	
  @Scheduled(cron = "${scheduling.job.cron}")
   @Transactional
   public void schedulingToStopVm() throws Exception {
	   try {
	    boolean scriptsActive=vmInstanceDAO.isAnyScriptsInprogresOrInqueue();
	    if(!scriptsActive && flag) {
	    	vmDetaliesService.stopInstance();
	    	log.info("all vms are stooped");
	    }
	   }catch (Exception e) {
		   log.error("exception"+e);
	}
   }
}