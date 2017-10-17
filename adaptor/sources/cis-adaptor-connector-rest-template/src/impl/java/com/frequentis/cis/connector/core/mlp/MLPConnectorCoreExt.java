package com.frequentis.cis.connector.core.mlp;

import java.io.InvalidObjectException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.frequentis.cis.connector.callback.AppCallbackHandlerImpl;
import com.frequentis.cis.connector.core.CoreConstants;
import com.frequentis.cis.connector.rest.CoreRESTSender;
import com.frequentis.cis.core.edxlde.parameters.DEParameters;
import com.frequentis.cis.core.payload.CISPayload;
import com.frequentis.cis.core.payload.objects.CISXMLContent;
import com.frequentis.cis.exception.communication.CISCommunicationException;
import com.frequentis.cis.exception.invalid.xml.CISInvalidXMLObject;

public class MLPConnectorCoreExt {

	private Logger log = Logger.getLogger(this.getClass());
	
	private CoreRESTSender restSender = new CoreRESTSender();
	
	public MLPConnectorCoreExt() {
		
	}
	
	/**
	 * This method receives an Object (MLP as String) and forwards it to the CIS CIS Core
	 * 
	 * @param msg the MLP String
	 * @throws InvalidObjectException when the String is not valid MLP
	 */
	public void notify(Object msg, Map<DEParameters, String> deParameters, boolean dontEnvelope) throws InvalidObjectException {
		log.info("--> notify");
		
		String msgStr = null;
		if (msg instanceof String) {
			msgStr = (String)msg;
			if (msgStr.startsWith("<?xml version")) {
				int idx = msgStr.indexOf("<svc_");
				if (idx != -1) {
					msgStr = msgStr.substring(idx);
				}
			}
		} else {
			throw new InvalidObjectException("Given Object is no valid MLP Message!");
		}
		
		try {
			CISXMLContent xmlCont = new CISXMLContent();
			xmlCont.setEmbeddedXMLContent(msgStr);
			restSender.notify(CoreConstants.MSGTYPE_MLP, xmlCont, null, false);
		} catch (CISCommunicationException | CISInvalidXMLObject e) {
			log.error("notify: " + e);
		}
		
		log.info("notify -->");
	}

	/**
	 * This message is called by the ConnectorCore when a message was receive. It will be translatet to the Application
	 * representation and the translatet message if send via the registered callback to the application.
	 * 
	 * @param msg
	 * @throws InvalidObjectException
	 */
	public void msgReceived(CISPayload payload) throws InvalidObjectException {
		log.info("--> messageReceived");
		
		// create and forward the message to the connector
		if (AppCallbackHandlerImpl.getInstance().getCallback(CoreConstants.MSGTYPE_MLP) != null) {
			AppCallbackHandlerImpl.getInstance().getCallback(CoreConstants.MSGTYPE_MLP).msgReceived(payload);
		}
		
		log.info("messageReceived -->");
	}

}
