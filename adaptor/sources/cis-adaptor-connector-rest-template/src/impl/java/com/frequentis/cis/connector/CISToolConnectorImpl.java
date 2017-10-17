package com.frequentis.cis.connector;

import java.io.InvalidObjectException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.frequentis.cis.callback.AppCallback;
import com.frequentis.cis.connector.callback.AppCallbackHandlerImpl;
import com.frequentis.cis.connector.core.ConnectorCore;
import com.frequentis.cis.connector.core.ConnectorCoreImpl;
import com.frequentis.cis.connector.criteria.Criteria;
import com.frequentis.cis.core.edxlde.parameters.DEParameters;

/**
 * The Connector implementation that implements methods for sending messages from an application
 * to the CIS Core for distributing them in the CIS.
 * 
 * @author TObritzh
 *
 */
public class CISToolConnectorImpl implements CISToolConnector {

	private Logger log = Logger.getLogger(this.getClass());
	
	private ConnectorCore connectorCore = new ConnectorCoreImpl();
	
	/**
	 * Connector Constructor
	 */
	public CISToolConnectorImpl() {
		log.info("CISToolConnectorImpl");
	}

	/**
	 * This method registers the given callback for specific message types and given message criterias on the
	 * connector which itself is responsible for retrieving messages distributed via the CIS.
	 * 
	 * @param msgType Specifies the type of the message (eg: CAP, TSO...)
	 * @param criteria Specific message criterias (eg: message status, sender information...)
	 * @param callback The callback implementation for retrieving messages from the CIS
	 */
	@Override
	public void registerForMsg(String msgType, Criteria criteria,
			AppCallback callback) {
		log.info("registerForMsg - msgType: " + msgType);
		
		AppCallbackHandlerImpl.getInstance().registerCAPCallback(msgType, callback);

	}

	/**
	 * Calling this method invokes the sending of a given message, identified by the message type.
	 * This method can also handle additional EDXL DE parameters that have to be set in the EDXL DE envelope.
	 * If no parameters are given, parameters can be defined in the deParameters.properites file.
	 *  
	 *  @param msgTye Specifies the type of the message (eg: CAP, TSO...)
	 *  @param msg The String representation of the meesag that has to be send
	 *  @param deParameters The additional parameters that should be set in the EDXL DE envelope message   
	 */
	@Override
	public void notify(String msgType, Object msg,  Map<DEParameters, String> deParameters, boolean dontEnvelope) {
		log.info("notify - msgType: " + msgType);
		log.debug("msg: " + msg + ", DE Param: " + deParameters);

		try {
			connectorCore.notify(msgType, msg, deParameters, dontEnvelope);
		} catch (InvalidObjectException e) {
			log.error("Error sending the message!", e);
		}
	}

}
