package com.frequentis.cis.connector.core.cap;

import java.io.InvalidObjectException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.xml.sax.SAXParseException;

import com.frequentis.cis.connector.callback.AppCallbackHandlerImpl;
import com.frequentis.cis.connector.core.CoreConstants;
import com.frequentis.cis.connector.rest.CoreRESTSender;
import com.frequentis.cis.core.CISCore;
import com.frequentis.cis.core.CISCoreImpl;
import com.frequentis.cis.core.edxlde.parameters.DEParameters;
import com.frequentis.cis.core.payload.CISPayload;
import com.frequentis.cis.core.payload.objects.CISXMLContent;
import com.frequentis.cis.exception.communication.CISCommunicationException;
import com.frequentis.cis.exception.invalid.xml.CISInvalidXMLObject;
import com.google.publicalerts.cap.Alert;
import com.google.publicalerts.cap.CapException;
import com.google.publicalerts.cap.CapXmlParser;
import com.google.publicalerts.cap.NotCapException;

public class CapConnectorCoreExt {

	private CapXmlParser capParser = new CapXmlParser(true);
		
	private Logger log = Logger.getLogger(this.getClass());
	
	//private CISCore core = CISCoreImpl.getInstance();
	private CoreRESTSender restSender = new CoreRESTSender();
	
	public CapConnectorCoreExt() {
		
	}
	/**
	 * This method receives an Object (CAP XML as String), validates the XML against the defined schema and
	 * forwards it to the CIS CIS Core
	 * 
	 * @param msg the CAP XML String
	 * @throws InvalidObjectException when the XML is no valid CAP message
	 */
	public void notify(Object msg, Map<DEParameters, String> deParameters, boolean dontEnvelope) throws InvalidObjectException {
		log.info("notify");
		
		String capMsgStr = null;
		if (msg instanceof String) {
			capMsgStr = (String)msg;
		} else {
			throw new InvalidObjectException("Given Object is no valid CAP Message!");
		}
		
		Alert capMsg = null;
		try {
			capMsg = capParser.parseFrom(capMsgStr);
		} catch (NotCapException | SAXParseException | CapException e) {
			log.error("notify: " + e);
			throw new InvalidObjectException("Message is no valid CAP message!" + e.getMessage());
		}
		
		try {
			CISXMLContent xmlCont = new CISXMLContent();
			xmlCont.setEmbeddedXMLContent(capMsgStr);
			restSender.notify(CoreConstants.MSGTYPE_CAP, xmlCont, null, false);
		} catch (CISCommunicationException | CISInvalidXMLObject e) {
			log.error("notify: " + e);
		}
		
	}
	
	/**
	 * This message is called by the ConnectorCore when a message was received. It will be translatet to the Application
	 * representation and the translatet message if send via the registered callback to the application.
	 * @param msg
	 * @throws InvalidObjectException
	 */
	public void msgReceived(CISPayload payload) throws InvalidObjectException {
		log.info("--> messageReceived");
		
		//Parse your object to your domain objects here
		
		// create and forward the message to the connector
		AppCallbackHandlerImpl.getInstance().getCallback(CoreConstants.MSGTYPE_CAP).msgReceived(payload);
	}

}
