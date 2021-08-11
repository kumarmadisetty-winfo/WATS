package com.winfo.scripts;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

public class test {

	public static void main(String[] args) throws Exception {
//		String urlStr = "http://192.168.1.201:8080/executeTestScript";
		String urlStr = "http://localhost:8083/executeTestScript";
//		String str=env.getProperty("message.default.welcome");
//		System.out.println(instanceName+"instanceName"+str);
		
		try {
			runWithParams(urlStr, "3389");
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

