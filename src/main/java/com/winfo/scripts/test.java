package com.winfo.scripts;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.winfo.services.FetchConfigVO;
import com.winfo.services.FetchMetadataListVO;
import com.winfo.services.FetchMetadataVO;

 

public class test/* extends SeleniumKeyWords */{

 

	public static void main(String[] args) throws Exception {
//		
//		   String urlStr = "http://watsudgp03.winfosolutions.com:8080/executeTestScript"; 
		String urlStr = "http://localhost:8080/executeTestScript";
		  
		   try { 
			   runWithParams(urlStr, "463"); 
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
