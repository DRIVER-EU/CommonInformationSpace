package com.frequentis.cis.connector.core;

import java.io.InvalidObjectException;
import java.util.Map;

import com.frequentis.cis.core.edxlde.parameters.DEParameters;
import com.frequentis.cis.core.payload.CISPayload;

public interface ConnectorCore {
	
	public void notify(String msgType, Object msg,  Map<DEParameters, String> deParameters, boolean dontEnvelope) throws InvalidObjectException;
	
	public void msgReceived(CISPayload payload) throws Exception;

}
