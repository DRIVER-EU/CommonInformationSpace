package com.frequentis.cis.connector.core.othercontent;

import java.io.InvalidObjectException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.frequentis.cis.core.edxlde.parameters.DEParameters;

public class FrqOtherContentConnectorCoreExt {

	private Logger log = Logger.getLogger(this.getClass());
	
	public FrqOtherContentConnectorCoreExt() {
		
	}
	
	/**
	 * This method receives an Object (CAP XML as String), validates the XML against the defined schema and
	 * forwards it to the CIS CIS Core
	 * 
	 * @param msg the CAP XML String
	 * @throws InvalidObjectException when the XML is no valid CAP message
	 */
	public void notify(Object msg, Map<DEParameters, String> deParameters, boolean dontEnvelope) throws InvalidObjectException {
		log.info("--> notify");
		
		log.info("notify -->");
	}

	/**
	 * This message is called by the ConnectorCore when a message was receive. It will be translatet to the Application
	 * representation and the translatet message if send via the registered callback to the application.
	 * 
	 * @param msg
	 * @throws InvalidObjectException
	 */
	public void msgReceived(String msg) throws InvalidObjectException {
		log.info("--> messageReceived");
		
		
		log.info("messageReceived -->");
	}
}
