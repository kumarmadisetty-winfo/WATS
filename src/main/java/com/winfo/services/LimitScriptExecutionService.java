package com.winfo.services;

import java.io.IOException;
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

@Service
@RefreshScope
public class LimitScriptExecutionService {
	Logger log = Logger.getLogger("Logger");

	@Autowired
	private LimitScriptExecutionDao limitScriptExecutionDao; 
	
	@Autowired
	private VmInstanceDAO vmInstanceDao;
	
	@Value("${spring.mail.host}")
    private String hostName;
	
	@Value("${spring.mail.username}")
    private String username;
	
	@Value("${spring.mail.password}")
    private String password;
	
	
	@Transactional
	public int getLimitedCountForConfiguration(String testRunNo) {
		log.info("goto limitScriptExecutionDao class");
		System.out.println("goto limitScriptExecutionDao class");
		return limitScriptExecutionDao.getLimitedCountForConfiguration(testRunNo);
	}
	@Transactional
	public void insertTestRunScriptData(FetchConfigVO fetchConfigVO, List<FetchMetadataVO> fetchMetadataListVO, String scriptId, String scriptNumber, String status, Date startDate, Date endDate) {
		try {
		ExecutionAudit executionAudit=new ExecutionAudit();
		String testSetId = fetchMetadataListVO.get(0).getTest_set_id();
		String customer_Name = fetchMetadataListVO.get(0).getCustomer_name();
		String test_Run_Name = fetchMetadataListVO.get(0).getTest_run_name();
		String ExecutedBy = fetchMetadataListVO.get(0).getExecuted_by();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:aa");
		
		String starttime=dateFormat.format(startDate);
		String endtime=dateFormat.format(endDate);
		executionAudit.setTestsetid(testSetId);
		executionAudit.setScriptid(scriptId);
		executionAudit.setScriptnumber(scriptNumber);
		executionAudit.setExecutionstarttime(starttime);
		executionAudit.setExecutionendtime(endDate);
		executionAudit.setStatus(status);
		limitScriptExecutionDao.insertTestrundata(executionAudit);
		System.out.println("data added successfully");
		log.info("data added successfully");
		}catch (Exception e) {
			System.out.println("testrun data not added "+e);
			log.error("testrun data not added "+e);
			}
	}
	@Transactional
	public void sendmail(String name, String fromMail) throws AddressException, MessagingException, IOException {
		  String toMail=limitScriptExecutionDao.getMailId(name);
		   Properties props = new Properties();
		   props.put("mail.smtp.auth", "true");
		   props.put("mail.smtp.starttls.enable", "true");
		   props.put("mail.smtp.host", hostName);
		   props.put("mail.smtp.port", "587");
		   
		   Session session = Session.getInstance(props, new javax.mail.Authenticator() {
		      protected PasswordAuthentication getPasswordAuthentication() {
		         return new PasswordAuthentication(username, password);
		      }
		   });
		   Message msg = new MimeMessage(session);
		   msg.setFrom(new InternetAddress(fromMail, false));
		   msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse("chakradhar.mandapati@winfosolutions.com"));
		   msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMail));
		   msg.setSubject("Alert message");
//		   msg.setContent("Alert message", "text/html");
		   msg.setText("Alert message");
		   msg.setSentDate(new Date());

		   MimeBodyPart messageBodyPart = new MimeBodyPart();
//		   messageBodyPart.setContent("Alert message", "text");
		   messageBodyPart.setText("execution sctipts 80% completed");

		   Multipart multipart = new MimeMultipart();
		   multipart.addBodyPart(messageBodyPart);
//		   MimeBodyPart attachPart = new MimeBodyPart();
//
//		   attachPart.setText("execution sctipts 80% completed");
//		   multipart.addBodyPart(attachPart);
		   msg.setContent(multipart);
		   Transport.send(msg); 	
		   }
	@Transactional
	public int getPassedScriptsCount() {
		
		return limitScriptExecutionDao.getPassedScriptsCount();
	}
	@Transactional
	public int getInprogressAndInqueueCount() {
		return vmInstanceDao.getInprogressAndInqueueCount();
		
	}
	@Transactional
	public void sendmail1() throws AddressException, MessagingException, IOException {
		    Properties props = new Properties();
	        props.put("mail.smtp.host", "watsdev01.winfosolutions.com");
	        props.put("mail.smtp.port", "6");
	        props.put("mail.debug", "true");
	        Session session = Session.getDefaultInstance(props);
	        MimeMessage message = new MimeMessage(session);
	        message.setFrom(new InternetAddress("chakradhar.mandapati@winfosolutions.com"));
	        message.setRecipient(RecipientType.TO, new InternetAddress("chakradhar.mandapati@winfosolutions.com"));
	        message.setSubject("Notification");
	        message.setText("Successful!", "UTF-8"); // as "text/plain"
	        message.setSentDate(new Date());
	        Transport.send(message);	
		   }
}
