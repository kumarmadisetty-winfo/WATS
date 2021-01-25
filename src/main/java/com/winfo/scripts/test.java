package com.winfo.scripts;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class test {

	public static void main(String[] args) throws Exception {
//		String urlStr = "http://10.110.1.44:8080/executeTestScript";
		String urlStr = "http://localhost:8081/copyTestrun";
		
		try {
			runWithParams(urlStr, "802");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void runWithParams(String urlStr, String testScriptId) throws Exception {
		String param = "{\"testScriptNo\": \"" + testScriptId + "\"}";
		System.out.println("Param : " + param);

		HttpURLConnection conn = null;
		URL url = new URL(urlStr);
		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setDoOutput(true);
		OutputStream os = null;
		try {
			os = conn.getOutputStream();
			os.write(param.getBytes());
			os.flush();
		}finally {
			os.close();
		}

		System.out.println("ResponseCode ::" + conn.getResponseCode());
		if (!((conn.getResponseCode() == HttpURLConnection.HTTP_CREATED) || (conn.getResponseCode() == HttpURLConnection.HTTP_OK))) {
			throw new Exception("Test script execution failed");
		}
	}
}

