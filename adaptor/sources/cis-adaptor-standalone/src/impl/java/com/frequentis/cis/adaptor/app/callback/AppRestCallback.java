package com.frequentis.cis.adaptor.app.callback;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frequentis.cis.callback.AppCallback;
import com.frequentis.cis.core.payload.CISPayload;
import com.frequentis.cis.core.payload.objects.CISXMLContent;
import com.frequentis.cis.core.properties.PropertiesReader;

public class AppRestCallback implements AppCallback {
	
	private Logger log = Logger.getLogger(this.getClass());
	private String callbackURL;
	
	private Boolean sendOrigMessage = false;
	
	public AppRestCallback(String callbackURL) {
		log.info("--> AppRestCallback: " + callbackURL);
		this.callbackURL = callbackURL;
		
		String sendOrig = PropertiesReader.getInstance().getPropertie("application.callback.send.orig");
		if (sendOrig != null) {
			sendOrigMessage = Boolean.parseBoolean(sendOrig);
			log.info("AppRestCallback: " + sendOrigMessage);
		}
		log.info("AppRestCallback -->");
	}

	@Override
	public void msgReceived(CISPayload payload) {
		log.info("--> msgReceived, sendOrigMessage: " + sendOrigMessage);
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonPayload = null;
		
		if (!sendOrigMessage) {
			try {
				jsonPayload = mapper.writeValueAsString(payload);
			} catch (JsonProcessingException e1) {
				log.error("Error parsing Client Information!", e1);
			}
			postHTTPRequest(callbackURL, "application/json", jsonPayload);
		} else {
			if (payload instanceof CISXMLContent) {
				postHTTPRequest(callbackURL, "application/xml", ((CISXMLContent)payload).getEmbeddedXMLContent());
			}
		}

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
