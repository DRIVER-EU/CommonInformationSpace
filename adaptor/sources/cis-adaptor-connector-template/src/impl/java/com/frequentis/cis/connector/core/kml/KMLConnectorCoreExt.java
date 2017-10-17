package com.frequentis.cis.connector.core.kml;

import java.io.InvalidObjectException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.frequentis.cis.connector.callback.AppCallbackHandlerImpl;
import com.frequentis.cis.connector.core.CoreConstants;
import com.frequentis.cis.core.CISCore;
import com.frequentis.cis.core.CISCoreImpl;
import com.frequentis.cis.core.edxlde.parameters.DEParameters;
import com.frequentis.cis.core.payload.CISPayload;
import com.frequentis.cis.core.payload.objects.CISXMLContent;
import com.frequentis.cis.exception.communication.CISCommunicationException;
import com.frequentis.cis.exception.config.CISConfigException;
import com.frequentis.cis.exception.invalid.CISInvalidObjectException;
import com.frequentis.cis.exception.invalid.xml.CISInvalidXMLObject;

public class KMLConnectorCoreExt {

	private Logger log = Logger.getLogger(this.getClass());
	
	private CISCore core = null;
	
	public KMLConnectorCoreExt() throws CISConfigException {
		core = CISCoreImpl.getInstance();
	}
	/**
	 * This method receives an Object (CAP XML as String), validates the XML against the defined schema and
	 * forwards it to the CIS CIS Core
	 * 
	 * @param msg the CAP XML String
	 * @throws InvalidObjectException when the XML is no valid CAP message
	 */
	public void notify(Object msg, Map<DEParameters, String> deParameters, boolean dontEnvelope) throws CISInvalidObjectException, CISCommunicationException {
		log.info("notify");
		
		String kmlMsg = null;
		if (msg instanceof String) {
			kmlMsg = (String)msg;
		} else {
			throw new CISInvalidObjectException("Given Object is no valid KML Message!");
		}
		
		try {
			CISXMLContent xmlCont = new CISXMLContent();
			xmlCont.setEmbeddedXMLContent(kmlMsg);
			core.notify(CoreConstants.MSGTYPE_KML, xmlCont, deParameters, false);
		} catch (CISCommunicationException | CISInvalidXMLObject e) {
			log.error("notify: " + e);
			throw e;
		}
		
	}
	
	/**
	 * This message is called by the ConnectorCore when a message was receive. It will be translatet to the Application
	 * representation and the translatet message if send via the registered callback to the application.
	 * @param msg
	 * @throws InvalidObjectException
	 */
	public void msgReceived(CISPayload payload) throws InvalidObjectException {
		log.info("--> messageReceived");
		
	    // translate the Object

		// create and forward the message to the connector
		AppCallbackHandlerImpl.getInstance().getCallback(CoreConstants.MSGTYPE_KML).msgReceived(payload);
	}

}
