package com.frequentis.cis.connector.core.othercontent;

import java.io.InvalidObjectException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.frequentis.cis.connector.callback.AppCallbackHandlerImpl;
import com.frequentis.cis.connector.core.CoreConstants;
import com.frequentis.cis.connector.rest.CoreRESTSender;
import com.frequentis.cis.core.CISCore;
import com.frequentis.cis.core.CISCoreImpl;
import com.frequentis.cis.core.edxlde.parameters.DEParameters;
import com.frequentis.cis.core.payload.CISPayload;
import com.frequentis.cis.core.payload.objects.CISOtherContent;
import com.frequentis.cis.exception.communication.CISCommunicationException;
import com.frequentis.cis.exception.invalid.xml.CISInvalidXMLObject;

public class OtherContentConnectorCoreExt {

	private Logger log = Logger.getLogger(this.getClass());
	
	//private CISCore core = CISCoreImpl.getInstance();
	private CoreRESTSender restSender = new CoreRESTSender();
	
	public OtherContentConnectorCoreExt() {
		
	}
	
	/**
	 * This method receives an Object (CAP XML as String), validates the XML against the defined schema and
	 * forwards it to the CIS CIS Core
	 * 
	 * @param msg the CAP XML String
	 * @throws InvalidObjectException when the XML is no valid CAP message
	 */
	public void notify(String msgType, Object msg, Map<DEParameters, String> deParameters, boolean dontEnvelope) throws InvalidObjectException {
		log.info("--> notify");
		
		CISOtherContent otherCont = new CISOtherContent();
		String strMsg = null;
		if (msg instanceof String) {
			strMsg = (String)msg;
		}
		
		try {
			if (msgType.equalsIgnoreCase(CoreConstants.MIME_TYPE_KMZ)) {
				otherCont.setMimeType(CoreConstants.MIME_TYPE_KMZ);
				otherCont.setContentData(strMsg);
				restSender.notify(CoreConstants.MSGTYPE_KMZ, otherCont, null, false);
			} else if (msgType.equalsIgnoreCase(CoreConstants.MIME_TYPE_WMS)) {
				otherCont.setMimeType(CoreConstants.MIME_TYPE_WMS);
				otherCont.setUri(strMsg);
				restSender.notify(CoreConstants.MSGTYPE_WMS, otherCont, null, false);
			} else if (msgType.equalsIgnoreCase(CoreConstants.MIME_TYPE_WFS)) {
				otherCont.setMimeType(CoreConstants.MIME_TYPE_WFS);
				otherCont.setUri(strMsg);
				restSender.notify(CoreConstants.MSGTYPE_WFS, otherCont, null, false);
			} else if (msgType.equalsIgnoreCase(CoreConstants.MIME_TYPE_MLP)) {
				otherCont.setMimeType(CoreConstants.MIME_TYPE_MLP);
				otherCont.setContentData("<![CDATA[" + msg.toString() +"]]>");
				restSender.notify(CoreConstants.MSGTYPE_MLP, otherCont, null, false);
			}
		} catch (CISCommunicationException | CISInvalidXMLObject e) {
			log.error("notify: " + e);
		}
		
		
		log.info("notify -->");
	}

	/**
	 * This message is called by the ConnectorCore when a message was receive. It will be translatet to the Application
	 * representation and the translatet message if send via the registered callback to the application.
	 * 
	 * @param msg
	 * @throws InvalidObjectException
	 */
	public void msgReceived(CISPayload payload) throws InvalidObjectException {
		log.info("--> messageReceived");
		
		if (payload instanceof CISOtherContent) {
			CISOtherContent otherCont = (CISOtherContent)payload;
			if (otherCont.getMimeType().equalsIgnoreCase(CoreConstants.MIME_TYPE_KMZ)) {
				AppCallbackHandlerImpl.getInstance().getCallback(CoreConstants.MSGTYPE_KMZ).msgReceived(payload);
			} else if (otherCont.getMimeType().equalsIgnoreCase(CoreConstants.MIME_TYPE_WMS)) {
				AppCallbackHandlerImpl.getInstance().getCallback(CoreConstants.MSGTYPE_WMS).msgReceived(payload);
			} else if (otherCont.getMimeType().equalsIgnoreCase(CoreConstants.MIME_TYPE_WFS)) {
				AppCallbackHandlerImpl.getInstance().getCallback(CoreConstants.MSGTYPE_WFS).msgReceived(payload);
			} if (otherCont.getMimeType().equalsIgnoreCase(CoreConstants.MIME_TYPE_MLP)) {
				AppCallbackHandlerImpl.getInstance().getCallback(CoreConstants.MSGTYPE_MLP).msgReceived(payload);
			}
			
		} else {
			throw new InvalidObjectException("Object is not OtherContent Object!");
		}
		
		
		log.info("messageReceived -->");
	}
}
