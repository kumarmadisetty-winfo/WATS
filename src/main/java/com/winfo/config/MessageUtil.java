package com.winfo.config;

import java.text.MessageFormat;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@PropertySource("classpath:messages.properties")
@ConfigurationProperties
@Data
@Component
public class MessageUtil {
	private HealthCheck healthCheck;
	private CopyTestRunService copyTestRunService;
	private ObjectStore objectStore;
	private CopyDataCustomerService copyDataCustomerService;
	private DeleteDataDao deleteDataDao;
	private Deletion deletion;
	private CommonObjectStoreUtils commonObjectStoreUtils;
	private JiraTicketBugService jiraTicketBugService;
	
	@Data
	public static class Error {
		private String DbAccessibilityMessage;
		private String SeleniumGridMessage;
		private String ObjectStoreAccess;
		private String SharePointAccess;
		private String InvalidScript;
		private String InvalidProductVersion;
		private String ProductVersionMissing;
		private String ScriptNotFound;
		private String PdfAndScreenshotNotDeleted;
		private String FileNotPresent;
		private String FailedToReturnTheFile;
		private String DownloadFailed;
		private String IssueAlreadyExists;
		private String NotAbleToCreateIssue;
	}
	
	@Data
	public static class Success {
		private String SanityCheckMessage;
		private String CopyData;
		private String ScriptAdded;
		private String ScriptDeleted;
		private String PdfAndScreenshotDeleted;
		private String ScreenshotNotPresent;
		private String ScreenshotDeleted;
		private String PdfNotPresent;
		private String PdfDeleted;
		private String IssueCreated;
		
	}
	@Data
	public static class CopyTestRunService {
		private Success success;
		private Error error;
	}
	@Data
	public static class HealthCheck{
		private Error error;
		private Success success;
	}
	@Data
	public static class ObjectStore{
		private String ConfigFileIOException;
		private String AccessDeniedException;
	}
	@Data
	public static class CopyDataCustomerService{
		private Error error;
		private Success success;
	}
	@Data
	public static class DeleteDataDao{
		private Error error;
		private Success success;
	}
	@Data
	public static class Deletion{
		private Error error;
		private Success success;
	}
	@Data
	public static class CommonObjectStoreUtils{
		private Error error;
		private Success success;
	}
	@Data
	public static class JiraTicketBugService{
		private Error error;
		private Success success;
	}
	
	public static String getMessage(String code, Object... args) {
		return MessageFormat.format(code, args);
	}
}
