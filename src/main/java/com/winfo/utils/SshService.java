package com.winfo.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.winfo.exception.WatsEBSException;

@Service
public class SshService {
	public static final Logger logger = LogManager.getLogger(SshService.class);

	private static final String SFTP = "sftp";
	private static final String EX_TYPE = "Auth fail";

	public static Session sftpSessionPasswordless(String localPrivateKeyPath, String host, String user)
			throws JSchException {
		JSch jsch = new JSch();
		Session session = null;
		jsch.addIdentity(localPrivateKeyPath);
		session = jsch.getSession(user, host, 22);
		session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		return session;
	}

	public static Session sftpSession(String sftpUser, String sftpPwd, String sftphost, int sftpPort) {
		Session session = null;
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(sftpUser, sftphost, sftpPort);
			session.setPassword(sftpPwd);
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
		} catch (JSchException jse) {
			if (jse.getMessage().equals(EX_TYPE)) {
				throw new WatsEBSException(500, "Please verify ssh user credentials", jse);
			} else {
				throw new WatsEBSException(500, "Exception Occurred while creating Sftp Connection", jse);
			}
		}
		return session;
	}

	public void runScript(String inputContent, String host, String username, String password, String destPath) {
		try {
			Channel channel = null;
			Session session = null;
			session = sftpSession(username, password, host, 22);
			channel = (session).openChannel(SFTP);
			channel.connect();
			ChannelSftp sftpChannel = (ChannelSftp) channel;
			InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes());

			sftpChannel.put(inputStream, destPath);
			ChannelExec execChannel = (ChannelExec) session.openChannel("exec");
			execChannel.setCommand("python " + destPath);
			logger.info(execChannel.getExitStatus());
			String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
			logger.info("---");
			logger.info(result);
			closeSftp(null, execChannel);
			closeSftp(session, sftpChannel);

		} catch (SftpException | JSchException | IOException e) {
			throw new WatsEBSException(500, "Exception Occurred while writing file via ssh", e);
		}
	}

	public static void closeSftp(Session session, Channel channel) {
		if (channel != null && channel.isConnected()) {
			channel.disconnect();
		}
		logger.info("Closing sftp session");
		if (session != null && session.isConnected()) {
			session.disconnect();
		}
	}

}
