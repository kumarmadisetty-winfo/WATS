package com.winfo.interceptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;

/**
 * A program that demonstrates how to upload files from local computer to a
 * remote FTP server using Apache Commons Net API.
 * 
 * @author www.codejava.net
 */
public class FTPCOnnc {
	
	Logger log = Logger.getLogger("Logger");
	public static void main1() {
		String server = "192.168.1.203";
		int port = 21;
		String user = "wats_ebs";
		String pass = "2020@Winfo";

		FTPClient ftpClient = new FTPClient();
		try {

			ftpClient.connect(server, port);
			ftpClient.login(user, pass);
			ftpClient.enterLocalPassiveMode();

			File firstLocalFile = new File(File.separator + "EBS-Automation" + File.separator + "EBS Automation-POC"
					+ File.separator + "robot files" + File.separator + "OTC.AR.2020Updatedd.robot");

			String firstRemoteFile = "OTC.AR.2020Updatedd.robot";
			InputStream inputStream = new FileInputStream(firstLocalFile);
			
			ftpClient.storeFile(firstRemoteFile, inputStream);
			inputStream.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
