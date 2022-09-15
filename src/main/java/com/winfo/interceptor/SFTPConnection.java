/*
 * package com.winfo.interceptor;
 * 
 * 
 * import java.io.FileInputStream; import java.io.IOException; import
 * java.io.OutputStream; import java.net.URL; import java.net.URLConnection;
 * 
 * import com.jcraft.jsch.*;
 * 
 * public class SFTPConnection {
 * 
 * private static final String REMOTE_HOST = "192.168.1.203"; private static
 * final String USERNAME = "wats_ebs"; private static final String PASSWORD =
 * "2020@Winfo"; private static final int REMOTE_PORT = 21; private static final
 * int SESSION_TIMEOUT = 10000; private static final int CHANNEL_TIMEOUT = 5000;
 * 
 * // public static void main(String[] args) { // // String localFile =
 * "C:\\Users\\Winfo solutions\\Downloads\\testFile.txt"; // String remoteFile =
 * "C:\\FTP\\wats_winfo\\afile.txt"; // // Session jschSession = null; // // try
 * { // // JSch jsch = new JSch(); // //
 * jsch.setKnownHosts("/home/mkyong/.ssh/known_hosts"); // jschSession =
 * jsch.getSession(USERNAME, REMOTE_HOST, REMOTE_PORT); // // // authenticate
 * using private key // // jsch.addIdentity("/home/mkyong/.ssh/id_rsa"); // //
 * // authenticate using password // jschSession.setPassword(PASSWORD); // // //
 * 10 seconds session timeout // jschSession.connect(); // // Channel sftp =
 * jschSession.openChannel("sftp"); // // // 5 seconds timeout //
 * sftp.connect(); // // ChannelSftp channelSftp = (ChannelSftp) sftp; // // //
 * transfer file from local to remote server // channelSftp.put(localFile,
 * remoteFile); // // // download file from remote server to local // //
 * channelSftp.get(remoteFile, localFile); // // channelSftp.exit(); // // }
 * catch (JSchException | SftpException e) { // // e.printStackTrace(); // // }
 * finally { // if (jschSession != null) { // jschSession.disconnect(); // } //
 * } // // System.out.println("Done"); // } //public static void main(String
 * args[]) //{ // String ftpUrl = "ftp://192.168.1.203/"; // String host =
 * "192.168.1.203"; // String user = "wats_ebs"; // String pass = "2020@Winfo";
 * // String filePath = "C:\\Users\\Winfo solutions\\Downloads\\testFile.txt";
 * // String uploadPath = "C:\\FTP\\wats_winfo\\afile.txt"; // // ftpUrl =
 * String.format(ftpUrl, user, pass, host, uploadPath); //
 * System.out.println("Upload URL: " + ftpUrl); // // try { // URL url = new
 * URL(ftpUrl); // URLConnection conn = url.openConnection(); // OutputStream
 * outputStream = conn.getOutputStream(); // FileInputStream inputStream = new
 * FileInputStream(filePath); // // byte[] buffer = new byte[99999]; // int
 * bytesRead = -1; // while ((bytesRead = inputStream.read(buffer)) != -1) { //
 * outputStream.write(buffer, 0, bytesRead); // } // // inputStream.close(); //
 * outputStream.close(); // // System.out.println("File uploaded"); // } catch
 * (IOException ex) { // ex.printStackTrace(); // } //} }
 */