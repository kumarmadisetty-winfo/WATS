package com.winfo.serviceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import com.winfo.dao.LimitScriptExecutionDao;
import com.winfo.dao.VmInstanceDAO;
import com.winfo.exception.WatsEBSException;
import com.winfo.model.ExecutionAudit;
import com.winfo.model.LookUp;
import com.winfo.vo.CustomerProjectDto;
import com.winfo.vo.FetchConfigVO;
import com.winfo.vo.ScriptDetailsDto;

@Service
@RefreshScope
public class LimitScriptExecutionService {
	public static final Logger logger = Logger.getLogger(LimitScriptExecutionService.class);

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

	@Transactional
	public int getLimitedCountForConfiguration(String testRunNo) {
		return limitScriptExecutionDao.getLimitedCountForConfiguration();
	}

	@Transactional
	public void insertTestRunScriptData(FetchConfigVO fetchConfigVO, List<ScriptDetailsDto> fetchMetadataListVO,
			String scriptId, String scriptNumber, String status, Date startDate, Date endDate, CustomerProjectDto customerDetails) {
		try {
			ExecutionAudit executionAudit = new ExecutionAudit();
			
			String testSetId = customerDetails.getTestSetId();
			executionAudit.setTestSetId(testSetId);
			executionAudit.setScriptId(scriptId);
			executionAudit.setScriptNumber(scriptNumber);
			executionAudit.setExecutionStartTime(startDate);
			executionAudit.setExecutionEndTime(endDate);
			executionAudit.setStatus(status);
			limitScriptExecutionDao.insertTestrundata(executionAudit);
			logger.info("Data added successfully");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Testrun data not added " + e.getMessage());
		}
	}

	@Transactional
	public void sendAlertmail(String name, String fromMail1, String testRunId) {
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
			message.setContent(htmlBody, "text/html");
			message.setSentDate(new Date());
			Transport.send(message);
		} catch (Exception e) {
			logger.error("Failed to send alert mail " + e.getMessage());
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
			message.setContent(htmlBody, "text/html");
			message.setSentDate(new Date());
			Transport.send(message);
		} catch (Exception e) {
			logger.error("Failed to send execution mail " + e);
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
			startDate1 = outputFormat.format(date1);
			endDate1 = outputFormat.format(date2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Failed to get passed scripts count " +e.getMessage());
		}
		return limitScriptExecutionDao.getPassedScriptsCount(startDate1, endDate1);
	}

	@Transactional
	public int getInprogressAndInqueueCount() {
		return vmInstanceDao.getInprogressAndInqueueCount();

	}

	@Transactional
	public Map<Date, Long> getStarttimeandExecutiontime(String testSetid) {
		return vmInstanceDao.getStarttimeandExecutiontime(testSetid);
	}

	@Transactional

	public void updateTestrunTimes(Date tStarttime, Date tendtime, long tdiffMinutes, String testSetid) {
		vmInstanceDao.updateTestrunTimes(tStarttime, tendtime, tdiffMinutes, testSetid);
	}

	@Transactional

	public void updateTestrunTimes1(Date tendtime, long tdiffMinutes, String testSetid) {
		vmInstanceDao.updateTestrunTimes1(tendtime, tdiffMinutes, testSetid);

	}

	@Transactional
	public int getFailedScriptRunCount(String testSetLineId, String testSetId) {
		// TODO Auto-generated method stub
		return limitScriptExecutionDao.getFailedScriptRunCount(testSetLineId, testSetId);
	}

	@Transactional
	public void updateFaileScriptscount(String testSetLineId, String testSetId) {
		limitScriptExecutionDao.updateFaileScriptscount(testSetLineId, testSetId);

	}

	@Transactional
	public void insertTestRunScriptData(String scriptId, String scriptNumber, String status, Date startDate,
			Date endDate, String testRunId) {
		try {
			ExecutionAudit executionAudit = new ExecutionAudit();
			String testSetId = testRunId;
			executionAudit.setTestSetId(testSetId);
			executionAudit.setScriptId(scriptId);
			executionAudit.setScriptNumber(scriptNumber);
			executionAudit.setExecutionStartTime(startDate);
			executionAudit.setExecutionEndTime(endDate);
			executionAudit.setStatus(status);
			limitScriptExecutionDao.insertTestrundata(executionAudit);
			logger.info("data added successfully");
		} catch (Exception e) {
			logger.error("testrun data not added " + e);
			throw new WatsEBSException(500, "Exception Occurred while inserting test run pdf records", e);
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
			executionAudit.setTestSetId(testSetId);
			executionAudit.setScriptId(scriptId);
			executionAudit.setScriptNumber(scriptNumber);
			executionAudit.setExecutionStartTime(fetchConfigVO.getStarttime());
			executionAudit.setStatus(status);
			if (limitScriptExecutionDao.findCountsOfExecAuditRecords(executionAudit) != null
					&& limitScriptExecutionDao.findCountsOfExecAuditRecords(executionAudit).longValue() == 0) {
				return true;
			}
		} catch (Exception e) {
			throw new WatsEBSException(500, "Exception Occurred while checking update status of Script Run", e);
		}
		return false;
	}

}
