package com.frequentis.cis.core.app.connector.callback;

import com.frequentis.cis.core.callback.connector.ConnectorCallback;
import com.frequentis.cis.core.callback.connector.message.CallbackMessage;

public class ConnectorRestCallback implements ConnectorCallback {
	
	private String restCallbackUrl = null;
	
	public ConnectorRestCallback(String restCallbackUrl) {
		this.restCallbackUrl = restCallbackUrl;
	}
	
	public void msgReceived(String msg) {
		
	}
	
	public void msgReceived(CallbackMessage msg) {
		
	}

	public String getRestCallbackUrl() {
		return restCallbackUrl;
	}

	public void setRestCallbackUrl(String restCallbackUrl) {
		this.restCallbackUrl = restCallbackUrl;
	}
}
