package com.frequentis.cis.connector.callback;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frequentis.cis.callback.AppCallback;
import com.frequentis.cis.core.payload.CISPayload;

public class AppRestCallback implements AppCallback {
	
	private Logger log = Logger.getLogger(this.getClass());
	private String callbackURL;
	
	public AppRestCallback(String callbackURL) {
		this.callbackURL = callbackURL;
	}

	@Override
	public void msgReceived(CISPayload payload) {
		log.info("--> msgReceived");
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonPayload = null;
		try {
			jsonPayload = mapper.writeValueAsString(payload);
		} catch (JsonProcessingException e1) {
			log.error("Error parsing Client Information!", e1);
		}
		
		String response = postHTTPRequest(callbackURL, "application/json", jsonPayload);

		log.info("msgReceived -->");
	}

	@Override
	public void msgReceived(String arg0) {
		// TODO Auto-generated method stub

	}

	public String getCallbackURL() {
		return callbackURL;
	}

	public void setCallbackURL(String callbackURL) {
		this.callbackURL = callbackURL;
	}
	
	private String postHTTPRequest(String url, String contentType, String msgParam) {
		log.info("--> postHTTPRequest");
		log.debug("url: " + url + ", contentType: " + contentType + ", msgParam: " + msgParam);
		String response = null;
		
		try {
			HttpURLConnection connection = (HttpURLConnection) (new URL(url)).openConnection();
	        connection.setRequestMethod("POST");
	        connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", contentType);
			if (msgParam != null) {
				byte[] postDataBytes = msgParam.getBytes("UTF-8");
				connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
				connection.getOutputStream().write(postDataBytes);
			}
			
			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			response = "";
			while ((line = reader.readLine()) != null) {
				response += line;
			}
			
			reader.close();
			
			log.debug("sendMessage: " + response);
	        
		} catch (Exception e) {
			log.error("Error distributing the Message!", e);
			response = null;
		}
		
		return response;
		
	}
}
