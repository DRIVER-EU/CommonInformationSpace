package com.frequentis.cis.connector.callback;

import java.util.HashMap;
import java.util.Map;

import com.frequentis.cis.callback.AppCallback;
import com.frequentis.cis.core.callback.handler.ConnectorCallbackHandlerImpl;
import com.frequentis.cis.exception.config.CISConfigException;

/**
 * The AppCallbackHandlerImpl holds the callback(s) for sending messages to the Application.
 * 
 * @author TObritzh
 *
 */
public class AppCallbackHandlerImpl {

	private static AppCallbackHandlerImpl aMe;
	
	private Map<String, AppCallback> callbackMap = new HashMap<String, AppCallback>();
	
	/**
	 * Default Constructor
	 */
	private AppCallbackHandlerImpl() {
		
	}
	
	/**
	 * This method is initializing the single instance of the AppCallbackhandler.
	 * 
	 * @return The single instance of AppCallbackHandlerImpl
	 */
	public static synchronized AppCallbackHandlerImpl getInstance() {
		if (AppCallbackHandlerImpl.aMe == null) {
			AppCallbackHandlerImpl.aMe = new AppCallbackHandlerImpl();
		}
		return AppCallbackHandlerImpl.aMe;
	}
	
	
	/**
	 * This method stored the given callback for a specific message type (eg. CAP, TSO) in the Instance.
	 * 
	 * @param msgType The specific message tape for which the callback will be registered
	 * @param callback The Callback implementaion (impl. AppCallback)
	 * 
	 * @exception CISConfigException will be thrown in case of misconfiguration of the CIS Adaptor
	 */
	public void registerCAPCallback(String msgType, AppCallback callback) throws CISConfigException {
		
		if (callbackMap.get(msgType) == null) {
			callbackMap.put(msgType, callback);
		}
		
		// register ConnectoprCallback on Core
		ConnectorCallbackHandlerImpl.getInstance().registerCallback(new ConnectorCallbackImpl());
	}
	
	/**
	 * This method returns the callback for a specific message type.
	 * 
	 * @param msgType the messge type for which the callback should be returned.
	 * @return The Callback implementation. If no callback was found, null is returned.
	 */
	public AppCallback getCallback(String msgType) {
		return callbackMap.get(msgType);
	}
}
