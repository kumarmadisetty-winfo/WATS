package com.winfo.services;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

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
import com.winfo.model.ExecutionAudit;

@Service
@RefreshScope
public class LimitScriptExecutionService {
	Logger log = Logger.getLogger("Logger");

	@Autowired
	private LimitScriptExecutionDao limitScriptExecutionDao;

	@Autowired
	private VmInstanceDAO vmInstanceDao;

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
			String customer_Name = fetchMetadataListVO.get(0).getCustomer_name();
			String test_Run_Name = fetchMetadataListVO.get(0).getTest_run_name();
			String ExecutedBy = fetchMetadataListVO.get(0).getExecuted_by();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:aa");

			String starttime = dateFormat.format(startDate);
			String endtime = dateFormat.format(endDate);
			executionAudit.setTestsetid(testSetId);
			executionAudit.setScriptid(scriptId);
			executionAudit.setScriptnumber(scriptNumber);
			executionAudit.setExecutionstarttime(starttime);
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
	public void sendAlertmail(String name, String fromMail, String testRunId){
		try {
		String toMail = limitScriptExecutionDao.getToMailId(name);
		String ccMail = limitScriptExecutionDao.getCCmailId(testRunId);
		Properties props = System.getProperties();
		props.put("mail.smtp.host", "localhost");
		props.put("mail.smtp.port", "25");
		props.put("mail.debug", "true");
		Session session = Session.getDefaultInstance(props);
		String subnect = "Warning - 80% Limit Completed";
		String htmlBody = "<html><body>" + "        <p>Hi,<br><br>Dear User<b>"
				+ "        <br><br>As per your Licence, 80% of your test scripts have been executed. Can you please fallback to Winfo to extend your license?<br><br>"
				+ "        Regards,<br><b>WATS</b>." + "        </p>" + "    </body>" + "</html>";
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress("wats.cloud@winfosolutions.com"));
		message.setRecipient(RecipientType.TO, new InternetAddress(toMail));
		message.setRecipient(RecipientType.CC, new InternetAddress(ccMail));
		message.setSubject(subnect);
		message.setText(htmlBody); // as "text/plain"
		message.setSentDate(new Date());
		Transport.send(message);
		}catch (Exception e) {
			System.out.println("testrun data not added " + e);
			log.error("testrun data not added " + e);
		}
	}
	@Transactional
	public void sendExceptionmail(String name, String fromMail, String testRunId) {
		try {
		String toMail = limitScriptExecutionDao.getToMailId(name);
		String ccMail = limitScriptExecutionDao.getCCmailId(testRunId);
		Properties props = System.getProperties();
		props.put("mail.smtp.host", "localhost");
		props.put("mail.smtp.port", "25");
		props.put("mail.debug", "true");
		Session session = Session.getDefaultInstance(props);
		String subnect = "Error - 100% Limit Completed";
		String htmlBody = "<html><body>" + "        <p>Hi,<br><br>Dear User<b>"
				+ "        <br><br>As per your Licence, 100% of your test scripts have been executed. You can no longer use WATS until you update your license. Can you please fallback to Winfo to extend your license?<br><br>"
				+ "        Regards,<br><b>WATS</b>." + "        </p>" + "    </body>" + "</html>";
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress("wats.cloud@winfosolutions.com"));
		message.setRecipient(RecipientType.TO, new InternetAddress(toMail));
		message.setRecipient(RecipientType.CC, new InternetAddress(ccMail));
		message.setSubject(subnect);
		message.setText(htmlBody); // as "text/plain"
		message.setSentDate(new Date());
		Transport.send(message);
		}catch (Exception e) {
			System.out.println("testrun data not added " + e);
			log.error("testrun data not added " + e);
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

}
