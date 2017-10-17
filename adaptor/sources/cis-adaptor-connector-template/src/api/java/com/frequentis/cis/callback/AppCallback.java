package com.frequentis.cis.callback;

import com.frequentis.cis.core.payload.CISPayload;

public interface AppCallback {
	
	/**
	 * This method is called by the CISConnector CallbackHandler when a message was received that was translated
	 * to application specific object structure. 
	 * 
	 * @param objMsg Translated application specific object structure
	 */
	public void msgReceived(CISPayload objMsg);
	
	/**
	 * This method is called by the CISConnector CallbackHandler when a message was received that cannot be translated
	 * to application specific object structure. 
	 * 
	 * @param strMsg original string message
	 */
	public void msgReceived(String strMsg);

	
}
