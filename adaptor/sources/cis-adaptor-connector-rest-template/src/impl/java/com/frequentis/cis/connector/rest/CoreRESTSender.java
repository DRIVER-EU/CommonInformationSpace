package com.frequentis.cis.connector.rest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frequentis.cis.core.edxlde.parameters.DEParameters;
import com.frequentis.cis.core.payload.CISPayload;
import com.frequentis.cis.core.payload.objects.CISOtherContent;
import com.frequentis.cis.core.payload.objects.CISXMLContent;
import com.frequentis.cis.core.properties.PropertiesReader;
import com.frequentis.cis.core.rest.OtherContentNotify;
import com.frequentis.cis.core.rest.XMLContentNotify;
import com.frequentis.cis.exception.communication.CISCommunicationException;
import com.frequentis.cis.exception.invalid.xml.CISInvalidXMLObject;

public class CoreRESTSender {
	
	private Logger log = Logger.getLogger(this.getClass());
	private String coreEndpointUrl = PropertiesReader.getInstance().getPropertie("core.rest.endpoint");
	
	public CoreRESTSender() {
		log.info("--> CoreRESTSender");
	}
	
	public void notify(String msgType, CISPayload msg, Map<DEParameters, String> deParameters, boolean dontEnvelope) throws CISInvalidXMLObject, CISCommunicationException {
		log.info("--> notify");
		
		ObjectMapper mapper = new ObjectMapper();
		String response = null;
		
		try {
			if (msg instanceof CISXMLContent) {
				CISXMLContent xmlPayload = (CISXMLContent)msg;
				XMLContentNotify xmlNotify = new XMLContentNotify(msgType, xmlPayload, deParameters, dontEnvelope);
				
				response = postHTTPRequest(coreEndpointUrl + "/CISCore/sendXMLMessage","application/json",mapper.writeValueAsString(xmlNotify));
				
			} else if (msg instanceof CISOtherContent) {
				CISOtherContent otherPayload = (CISOtherContent)msg;
				OtherContentNotify otherNotify = new OtherContentNotify(msgType, otherPayload, deParameters, dontEnvelope);
				
				response = postHTTPRequest(coreEndpointUrl + "/CISCore/sendOtherMessage","application/json",mapper.writeValueAsString(otherNotify));
			}
		} catch (JsonProcessingException e1) {
			log.error("Error parsing Notify Information!", e1);
		}
		
		log.info("notify -->");
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
