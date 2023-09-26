package com.winfo.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.winfo.exception.WatsEBSException;
import com.winfo.utils.DateUtils;
import com.winfo.vo.EmailParamDto;

@Service
public class SendMailServiceImpl {

	public static final Logger logger = Logger.getLogger(SendMailServiceImpl.class);

	@Value("${smpt.from.mail}")
	private String userName;

	@Value("${smpt.password}")
	private String password;

	@Value("${smtp.homepage.link}")
	private String link;

	@Autowired
	Environment environment;

	public List<String> downloadAttachments(Message message) {
		List<String> downloadedAttachments = new ArrayList<>();
		Multipart multiPart;
		try {
			multiPart = (Multipart) message.getContent();

			int numberOfParts = multiPart.getCount();
			logger.info("Downloading attachments - {}" + numberOfParts);
			for (int partCount = 0; partCount < numberOfParts; partCount++) {
				MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
				if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
					String filePath = environment.getProperty("file.path") + File.separator + part.getFileName();
					logger.info("Downloading attachment to path - {}" + filePath);
					part.saveFile(filePath);
					downloadedAttachments.add(filePath);
					logger.info("Downloaded attachment to path - {}" + filePath);
				}
			}

		} catch (IOException | MessagingException e) {
			logger.error("Exception occurred while downloading attachments");
			ExceptionUtils.getStackTrace(e);

			throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred while downloading attachments", e);

		}
		return downloadedAttachments;
	}

	public List<String> readMail() {
		logger.info("Entered Read Mail Block");

		List<String> attachments = new ArrayList<>();
		try (Store store = setStore(); Folder inbox = store.getFolder("INBOX")) {
			inbox.open(Folder.READ_ONLY);
			Message[] arrayMessages = inbox.getMessages();
			for (int i = 0; i < arrayMessages.length; i++) {
				Message message = arrayMessages[i];

				if (message.getContentType().contains("multipart")) {
					attachments = downloadAttachments(message);
				}

			}
		} catch (MessagingException ex) {
			logger.error("Exception occurred while reading mail from mail box");
			ExceptionUtils.getStackTrace(ex);

			throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred while reading mail from mail box", ex);

		}
		return attachments;
	}

	public Store setStore() {
		try {
			logger.info("Setting store for Mail Box");
			Session session = getSession();
			Store store = session.getStore("pop3");
			store.connect(userName, password);
			return store;
		} catch (MessagingException ex) {
			logger.error("Exception occurred while connecting to Mail Host");
			ExceptionUtils.getStackTrace(ex);

			throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred while connecting to Mail Host", ex);

		}
	}

	public Session getSession() {
		final String username = userName;
		final String pass = password;
		Session session = null;
		try {
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp-mail.outlook.com");
			props.put("mail.smtp.port", "587");

			session = Session.getInstance(props, new javax.mail.Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, pass);
				}
			});
			session.setDebug(true);
		} catch (Exception e) {

			throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(),

					"Exception occurred while email authentication. User credentials were incorrect", e);
		}
		return session;
	}

	public void sendMail(EmailParamDto emailParamDto) {
		Session session = getSession();
		String successSubject = "FYI-Execution successfully completed  for ";
		String failSubject = "FYI-Execution failed.Technical error for ";
		String partialSubject = "FYI-Execution completed.Partially successfull for ";
		String subject = null;

		String passBody = "<html>\r\n" + "<body>\r\n"
				+ "        <p>Hi,<br><br>This notification is to update you that the execution of <b>"
				+ emailParamDto.getTestSetName()
				+ "</b>  has successfully completed with 100% Pass for all the scripts.\r\n"
				+ "         <br><br>Please login in Winfo Automation Test tool to review full results.<br><br>\r\n"
				+ "       <br>Please click <a href='" + link + "'><b>here</b></a> to open login page.<br><br>\r\n"
				+ "        Thanks,<br><b>Winfo Test Automation</b>.\r\n" + "        </p>\r\n" + "    </body>\r\n"
				+ "</html>";
		String failBody = "<html>\r\n" + "<body>\r\n"
				+ "        <p>Hi,<br><br>This notification is to inform you that due to the <b>Technical Error</b> the execution for <b>"
				+ emailParamDto.getTestSetName() + "</b> failed.\r\n"
				+ "        <br><br>Please review the reasons and once resolved resubmit the test run for execution.<br><br>\r\n"
				+ "       Please click <a href='" + link + "'><b>here</b></a> to open login page.<br><br>\r\n"
				+ "Thanks,<br><b>Winfo Test Automation</b>.\r\n" + "        </p>\r\n" + "    </body>\r\n" + "</html>";
		String partialPassBody = "<html>\r\n" + "<body>\r\n"
				+ "         <p>Hi,<br><br>This notification is to update you that the execution of <b>"
				+ emailParamDto.getTestSetName() + " has <b>successfully completed and only partially successfull ."
				+ "<b>" + (emailParamDto.getRequestCount() - emailParamDto.getFailCount()) + "</b> scripts out of <b>"
				+ emailParamDto.getRequestCount() + "</b> scripts were successfully completed.\r\n"
				+ "         <br><br>Please login in Winfo Automation Test tool to review full results.<br><br>\r\n"
				+ "       Please click <a href='" + link + "'><b>here</b></a> to open login page.<br><br>\r\n"
				+ "        Thanks,<br><b>Winfo Test Automation</b>.\r\n" + "         </p>\r\n" + "       </body>\r\n"
				+ "</html>";
		String body = null;
		if (emailParamDto.getRequestCount() <= emailParamDto.getPassCount() && emailParamDto.getFailCount() == 0) {
			subject = successSubject + emailParamDto.getTestSetName();
			body = passBody;
		} else if (emailParamDto.getRequestCount().equals(emailParamDto.getFailCount())) {
			subject = failSubject + emailParamDto.getTestSetName();
			body = failBody;
		} else {
			subject = partialSubject + emailParamDto.getTestSetName();
			body = partialPassBody;
		}

		try {

			MimeMessage message = new MimeMessage(session);
			message.setHeader("Content-Type", "text/html; charset=UTF-8");
			message.setFrom(new InternetAddress(userName));
			message.setSentDate(new Date());
			message.setRecipients(Message.RecipientType.TO,
					emailParamDto.getReceiver() != null ? InternetAddress.parse(emailParamDto.getReceiver()) : null);
			message.addRecipients(Message.RecipientType.CC,
					emailParamDto.getCcPerson() != null ? InternetAddress.parse(emailParamDto.getCcPerson()) : null);

			message.setSubject(subject);

			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(body, "utf-8", "html");
			MimeMultipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
			Transport.send(message);

		} catch (MessagingException e) {
			e.printStackTrace();

			throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred while sending mail for script run.", e);

		}
	}
	
	public void schedulerSendMail(EmailParamDto emailParamDto) {
		Session session = getSession();
		String subject = "FYI-Execution successfully completed  for " + emailParamDto.getJobName();

		String body = "<html>\r\n" + "<body>\r\n"
				+ "        <p>Hi,<br><br>This notification is to update you that the execution of job : <b>"
				+ emailParamDto.getJobName() + ".<br><br>"
				+ emailParamDto.getTestSetName() + "</b>" + " these testruns " 
				+ "has completed successfully.\r\n"
				+ "         <br><br>Please login in Winfo Automation Test tool to review full results.<br><br>\r\n"
				+ "       <br>Please click <a href='" + link + "'><b>here</b></a> to open login page.<br><br>\r\n"
				+ "        Thanks,<br><b>Winfo Test Automation</b>.\r\n" + "        </p>\r\n" + "    </body>\r\n"
				+ "</html>";

		try {

			MimeMessage message = new MimeMessage(session);
			message.setHeader("Content-Type", "text/html; charset=UTF-8");
			message.setFrom(new InternetAddress(userName));
			message.setSentDate(new Date());
			message.setRecipients(Message.RecipientType.TO,
					emailParamDto.getReceiver() != null ? InternetAddress.parse(emailParamDto.getReceiver()) : null);
			message.addRecipients(Message.RecipientType.CC,
					emailParamDto.getCcPerson() != null ? InternetAddress.parse(emailParamDto.getCcPerson()) : null);

			message.setSubject(subject);

			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(body, "utf-8", "html");
			MimeMultipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
			Transport.send(message);

		} catch (MessagingException e) {
			e.printStackTrace();
			throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred while sending mail for Scheduler Job ", e);
		}
	}
	
	
	public void initiationSendMail(EmailParamDto emailParamDto) {
		Session session = getSession();
		String subject = "FYI-Execution initiated for " + emailParamDto.getTestSetName();
		String date=DateUtils.getSysdate("dd-MMM-YYYY");
		LocalTime time=LocalTime.now();
		

		String body = "<html>\r\n" + "<body>\r\n"
				+ "        <p>Hi,<br><br>This notification is to update you that the execution of <b>"
				+ emailParamDto.getTestSetName() + "commenced at " +date+ "at time " +time+ ".<br><br>"
				+"Next notification will be sent once the execution is completed."
				+ "        Thanks,<br><b>Winfo Test Automation</b>.\r\n" + "        </p>\r\n" + "    </body>\r\n"
				+ "</html>";

		try {

			MimeMessage message = new MimeMessage(session);
			message.setHeader("Content-Type", "text/html; charset=UTF-8");
			message.setFrom(new InternetAddress(userName));
			message.setSentDate(new Date());
			message.setRecipients(Message.RecipientType.TO,
					emailParamDto.getReceiver() != null ? InternetAddress.parse(emailParamDto.getReceiver()) : null);
			message.addRecipients(Message.RecipientType.CC,
					emailParamDto.getCcPerson() != null ? InternetAddress.parse(emailParamDto.getCcPerson()) : null);

			message.setSubject(subject);

			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(body, "utf-8", "html");
			MimeMultipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
			Transport.send(message);

		} catch (MessagingException e) {
			logger.error("Exception occurred while sending initiation mail " +e.getMessage());
			throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred while sending initiation mail ", e);
		}
	}

}