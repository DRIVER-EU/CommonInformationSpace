package com.frequentis.cis.connector;

import java.util.Map;

import com.frequentis.cis.callback.AppCallback;
import com.frequentis.cis.connector.criteria.Criteria;
import com.frequentis.cis.core.edxlde.parameters.DEParameters;

public interface CISToolConnector {

	/**
	 * This method registers the given callback for specific message types and given message criterias on the
	 * connector which itself is responsible for retrieving messages distributed via the CIS.
	 * 
	 * @param msgType Specifies the type of the message (eg: CAP, TSO...)
	 * @param criteria Specific message criterias (eg: message status, sender information...)
	 * @param callback The callback implementation for retrieving messages from the CIS
	 */
	public void registerForMsg(String msgType, Criteria criteria, AppCallback callback);
	
	/**
	 * Calling this method invokes the sending of a given message, identified by the message type.
	 * This method can also handle additional EDXL DE parameters that have to be set in the EDXL DE envelope.
	 * If no parameters are given, parameters can be defined in the deParameters.properites file.
	 *  
	 *  @param msgTye Specifies the type of the message (eg: CAP, TSO...)
	 *  @param msg The String representation of the meesag that has to be send
	 *  @param deParameters The additional parameters that should be set in the EDXL DE envelope message   
	 */
	public void notify(String msgType, Object msg,  Map<DEParameters, String> deParameters, boolean dontEnvelope);
}
