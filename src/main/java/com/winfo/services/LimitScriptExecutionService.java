package com.winfo.services;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import com.winfo.dao.LimitScriptExecutionDao;
import com.winfo.dao.VmInstanceDAO;
import com.winfo.exception.WatsEBSCustomException;
import com.winfo.model.ExecutionAudit;

@Service
@RefreshScope
public class LimitScriptExecutionService {
	Logger log = Logger.getLogger("Logger");

	@Autowired
	private LimitScriptExecutionDao limitScriptExecutionDao;

	@Autowired
	private VmInstanceDAO vmInstanceDao;

	@Value("${smpt.host}")
	private String host;
	
	@Value("${smpt.port}")
	private String port;
	
	@Value("${smpt.from.mail}")
	private String fromMail;
	
	public Map<Integer, Boolean> getLimitedCoundiationExaption(FetchConfigVO fetchConfigVO, List<FetchMetadataVO> fetchMetadataListVO, LinkedHashMap<String, List<FetchMetadataVO>> metaDataMap, String args) {
		boolean flag=false;
		int remaingScriptsCount=0;
		 Map<Integer, Boolean> mutableMap=new TreeMap<Integer, Boolean>();
		try {
		int threshold = fetchConfigVO.getMax_num_scripts();
//		int limitedExecutionCount = limitScriptExecutionService.getLimitedCountForConfiguration(args);
		int scriptsPassCount=getPassedScriptsCount(fetchConfigVO.getStart_date(),fetchConfigVO.getEnd_date());
		int inprogressandInqueueCount=getInprogressAndInqueueCount();
		int percentageCount=Math.round((threshold*(80.0f/100.0f)));
		scriptsPassCount=scriptsPassCount+metaDataMap.size()+inprogressandInqueueCount;
		if (percentageCount <= scriptsPassCount && threshold > scriptsPassCount) {
			sendAlertmail(fetchMetadataListVO.get(0).getExecuted_by(),
					fetchMetadataListVO.get(0).getSmtp_from_mail(), args);
		} else if (threshold < scriptsPassCount) {
			remaingScriptsCount=threshold-(scriptsPassCount+inprogressandInqueueCount);
			sendExceptionmail(fetchMetadataListVO.get(0).getExecuted_by(),
					fetchMetadataListVO.get(0).getSmtp_from_mail(), args);
			flag=true;
		
		}
		}catch (Exception e) {
			System.out.println("limited sctipt condiation filed " + e);
			log.error("limited sctipt condiation filed " + e);
		}
		mutableMap.put(remaingScriptsCount,flag);
		return mutableMap;
	}
	
	@Transactional
	public int getLimitedCountForConfiguration(String testRunNo) {
		log.info("goto limitScriptExecutionDao class");
		System.out.println("goto limitScriptExecutionDao class");
		return limitScriptExecutionDao.getLimitedCountForConfiguration(testRunNo);
	}

	@Transactional
	public void insertTestRunScriptData(FetchConfigVO fetchConfigVO, List<FetchMetadataVO> fetchMetadataListVO,
			String scriptId, String scriptNumber, String status, Date startDate, Date endDate) {
		try {
			ExecutionAudit executionAudit = new ExecutionAudit();
			String testSetId = fetchMetadataListVO.get(0).getTest_set_id();
			executionAudit.setTestsetid(testSetId);
			executionAudit.setScriptid(scriptId);
			executionAudit.setScriptnumber(scriptNumber);
			executionAudit.setExecutionstarttime(startDate);
			executionAudit.setExecutionendtime(endDate);
			executionAudit.setStatus(status);
			limitScriptExecutionDao.insertTestrundata(executionAudit);
			System.out.println("data added successfully");
			log.info("data added successfully");
		} catch (Exception e) {
			System.out.println("testrun data not added " + e);
			log.error("testrun data not added " + e);
		}
	}

	@Transactional
	public void sendAlertmail(String name, String fromMail1, String testRunId){
		try {
		String toMail = limitScriptExecutionDao.getToMailId(name);
		String ccMail = limitScriptExecutionDao.getCCmailId(testRunId);
		Properties props = System.getProperties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);
		props.put("mail.debug", "true");
		Session session = Session.getDefaultInstance(props);
		String subject = "Warning - 80% Limit Completed";
		String htmlBody = "<html><body>" + "        <p>Hi,<br><br>Dear User<b>"
				+ "        <br><br>You have reached 80% of your threshold limit for the number of script execution. Please reach out to WATS support team to enhance your usage limit.<br><br>"
				+ "        Regards,<br><b>WATS</b>." + "        </p>" + "    </body>" + "</html>";
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(fromMail));
		message.setRecipient(RecipientType.TO, new InternetAddress(toMail));
		message.setRecipient(RecipientType.CC, new InternetAddress(ccMail));
		message.setSubject(subject);
		message.setContent(htmlBody,"text/html");
		message.setSentDate(new Date());
		Transport.send(message);
		}catch (Exception e) {
			System.out.println("respect alert mail not sent  " + e);
			log.error("respect alert mail not sent" + e);
		}
	}
	@Transactional
	public void sendExceptionmail(String name, String fromMail1, String testRunId) {
		try {
		String toMail = limitScriptExecutionDao.getToMailId(name);
		String ccMail = limitScriptExecutionDao.getCCmailId(testRunId);
		Properties props = System.getProperties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);
		props.put("mail.debug", "true");
		Session session = Session.getDefaultInstance(props);
		String subnect = "Error - 100% Limit Completed";
		String htmlBody = "<html><body>" + "        <p>Hi,<br><br>Dear User<b>"
				+ "        <br><br>You have exceeded your threshold limit for the number of scripts execution. Please reach out to WATS support team to enhance your usage limit.<br><br>"
				+ "        Regards,<br><b>WATS</b>." + "        </p>" + "    </body>" + "</html>";
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(fromMail));
		message.setRecipient(RecipientType.TO, new InternetAddress(toMail));
		message.setRecipient(RecipientType.CC, new InternetAddress(ccMail));
		message.setSubject(subnect);
		message.setContent(htmlBody,"text/html");
		message.setSentDate(new Date());
		Transport.send(message);
		}catch (Exception e) {
			System.out.println("respect execuption mail not sent " + e);
			log.error("respect execuption mail not sent  " + e);
		}
	}

	@Transactional
	public int getPassedScriptsCount(String startDate, String endDate) {

		String startDate1 = null;
		String endDate1 = null;
		try {
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
		Date date1 = inputFormat.parse(startDate);
		Date date2 = inputFormat.parse(endDate);
		startDate1= outputFormat.format(date1);
		endDate1= outputFormat.format(date2);
		System.out.println(startDate1+"enddate"+endDate1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return limitScriptExecutionDao.getPassedScriptsCount(startDate1,endDate1);
	}

	@Transactional
	public int getInprogressAndInqueueCount() {
		return vmInstanceDao.getInprogressAndInqueueCount();

	}
	@Transactional
	public Map<Date,Long> getStarttimeandExecutiontime(String testSetid) {
		return vmInstanceDao.getStarttimeandExecutiontime(testSetid);	
	}
	@Transactional

	public void updateTestrunTimes(Date tStarttime, Date tendtime, long tdiffMinutes, String testSetid) {
		 vmInstanceDao.updateTestrunTimes(tStarttime,tendtime,tdiffMinutes,testSetid);		
	}
	@Transactional

	public void updateTestrunTimes1(Date tendtime, long tdiffMinutes, String testSetid) {
		 vmInstanceDao.updateTestrunTimes1(tendtime,tdiffMinutes,testSetid);		
		
	}

	@Transactional
	public int getFailedScriptRunCount(String testSetLineId, String testSetId) {
		// TODO Auto-generated method stub
		return limitScriptExecutionDao.getFailedScriptRunCount(testSetLineId,testSetId);
	}

	public void renameFailedFile(List<FetchMetadataVO> fetchMetadataListVO, FetchConfigVO fetchConfigVO,
			String pdffileName, int failedScriptRunCount) {
		String Folder = (fetchConfigVO.getPdf_path() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
				+ fetchMetadataListVO.get(0).getTest_run_name() + "/");
		//String Folder="C:\\Users\\Winfo Solutions\\Desktop\\new\\";
//		String Folder = "/objstore/udgsup/UDG SUPPORT/UDG - PPM  (copy)/";
		File file = new File(Folder + pdffileName);
		String pdfname = pdffileName.substring(0, pdffileName.indexOf(".pdf"));
		String renameFolder = (fetchConfigVO.getPdf_path() + fetchMetadataListVO.get(0).getCustomer_name() + "/"
				+ fetchMetadataListVO.get(0).getTest_run_name() + "/"+pdfname+ "_RUN1.pdf");
		 File renameFile = new File(renameFolder);
		 boolean flag = file.renameTo(renameFile);
		  
	        // if renameTo() return true then if block is
	        // executed
	        if (flag == true) {
	            System.out.println("File Successfully Rename");
	        }
	        // if renameTo() return false then else block is
	        // executed
	        else {
	            System.out.println("Operation Failed");
	        }
	}
	@Transactional
	public void updateFaileScriptscount(String testSetLineId, String testSetId) {
		limitScriptExecutionDao.updateFaileScriptscount(testSetLineId,testSetId);
		
	}
	
	@Transactional
	public void insertTestRunScriptData(String scriptId, String scriptNumber, String status, Date startDate,
			Date endDate, String testRunId) {
		try {
			ExecutionAudit executionAudit = new ExecutionAudit();
			String testSetId = testRunId;
			executionAudit.setTestsetid(testSetId);
			executionAudit.setScriptid(scriptId);
			executionAudit.setScriptnumber(scriptNumber);
			executionAudit.setExecutionstarttime(startDate);
			executionAudit.setExecutionendtime(endDate);
			executionAudit.setStatus(status);
			limitScriptExecutionDao.insertTestrundata(executionAudit);
			log.info("data added successfully");
		} catch (Exception e) {
			log.error("testrun data not added " + e);
			throw new WatsEBSCustomException(500, "Exception occured while inserting test run pdf records", e);
		}
	}

	@Transactional
	public int getFailScriptRunCount(String testSetLineId, String testSetId) {
		return limitScriptExecutionDao.getFailScriptRunCount(testSetLineId, testSetId);
	}

	@Transactional
	public void updateFailScriptRunCount(int failedRunCount, String testSetLineId, String testSetId) {
		limitScriptExecutionDao.updateFailScriptRunCount(failedRunCount, testSetId, testSetLineId);
	}
	
	@Transactional
	public boolean updateStatusCheck(FetchConfigVO fetchConfigVO, String testRunId, String scriptId,
			String scriptNumber, String status) {
		try {
			ExecutionAudit executionAudit = new ExecutionAudit();
			String testSetId = testRunId;
			executionAudit.setTestsetid(testSetId);
			executionAudit.setScriptid(scriptId);
			executionAudit.setScriptnumber(scriptNumber);
			executionAudit.setExecutionstarttime(fetchConfigVO.getStarttime());
			executionAudit.setStatus(status);
			if (limitScriptExecutionDao.findCountOfExecAuditRecords(executionAudit) == 0) {
				return true;
			}
		} catch (Exception e) {
			throw new WatsEBSCustomException(500, "Exception occured while checking update status of Script Run", e);
		}
		return false;
	}

}
