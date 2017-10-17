package com.frequentis.cis.connector.callback;

import org.apache.log4j.Logger;

import com.frequentis.cis.connector.core.ConnectorCore;
import com.frequentis.cis.connector.core.ConnectorCoreImpl;
import com.frequentis.cis.core.callback.connector.ConnectorCallback;
import com.frequentis.cis.core.callback.connector.message.CallbackMessage;

/**
 * The ConnectorCallbackImpl class is the implementation fo the callback interface defined by the core.
 * It will be registerd on the Core for retrieving messages from the core for futher processing.
 * 
 * @author TObritzh
 *
 */
public class ConnectorCallbackImpl implements ConnectorCallback {
	
	private Logger log = Logger.getLogger(this.getClass());
	
	ConnectorCore coreConnector = new ConnectorCoreImpl();

	/**
	 * Default Constructor
	 */
	public ConnectorCallbackImpl() {
		
	}
	
	/**
	 * The methoed is called by the Adaptor Core when a message was recieved and has to be forwarded to the Connector
	 * 
	 * @param msg The Callback message object that contains the message object(s)
	 */
	public void msgReceived(CallbackMessage msg) {
		log.info("--> messageReceived");
		
		try {
			coreConnector.msgReceived(msg.getPayload());
		} catch (Exception e) {
			log.error("Error forwarding messge to connector core!", e);
		}
	}

}
