package com.frequentis.cis.connector.core;

import java.io.InvalidObjectException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.frequentis.cis.connector.core.cap.CapConnectorCoreExt;
import com.frequentis.cis.connector.core.mlp.MLPConnectorCoreExt;
import com.frequentis.cis.connector.core.othercontent.OtherContentConnectorCoreExt;
import com.frequentis.cis.core.edxlde.parameters.DEParameters;
import com.frequentis.cis.core.payload.CISPayload;
import com.frequentis.cis.core.payload.objects.CISXMLContent;

public class ConnectorCoreImpl implements ConnectorCore {
	
	private Logger log = Logger.getLogger(this.getClass());
	CapConnectorCoreExt capConnector = new CapConnectorCoreExt();
	MLPConnectorCoreExt mlpConnector = new MLPConnectorCoreExt();
	OtherContentConnectorCoreExt otherContentConnector = new OtherContentConnectorCoreExt();
	
	public void notify(String msgType, Object msg,  Map<DEParameters, String> deParameters, boolean dontEnvelope) throws InvalidObjectException {
		log.info("--> sendMessage");
		
		if (msgType.equalsIgnoreCase(CoreConstants.MSGTYPE_CAP)) {
			capConnector.notify(msg, deParameters, dontEnvelope);
		} else if (msgType.equalsIgnoreCase(CoreConstants.MSGTYPE_MLP)) {
			mlpConnector.notify(msg, deParameters, dontEnvelope);
		} else if (msgType.equalsIgnoreCase(CoreConstants.MSGTYPE_WFS)) {
			otherContentConnector.notify(CoreConstants.MIME_TYPE_WFS, msg, deParameters, dontEnvelope);
		} else if (msgType.equalsIgnoreCase(CoreConstants.MSGTYPE_WMS)) {
			otherContentConnector.notify(CoreConstants.MIME_TYPE_WMS, msg, deParameters, dontEnvelope);
		} else if (msgType.equalsIgnoreCase(CoreConstants.MSGTYPE_KMZ)) {
			otherContentConnector.notify(CoreConstants.MIME_TYPE_KMZ, msg, deParameters, dontEnvelope);
		}
	}
	
	public void msgReceived(CISPayload payload) throws Exception {
		log.info("--> receviedMessage");
		log.debug(payload);
		
		if (payload instanceof CISXMLContent) {
			CISXMLContent xmlCont = (CISXMLContent)payload;
			String xmlMsg = xmlCont.getEmbeddedXMLContent();
			if (xmlMsg.indexOf(CoreConstants.CAPMSG) != -1) {
				log.info("CAP Message received, forwarding to CAP Connector!");
				// CAP Message
				capConnector.msgReceived(payload);
			} else if (xmlMsg.indexOf(CoreConstants.MLPMSG) != -1) {
				log.info("MLP Message received, forwarding to MLP Connector!");
				mlpConnector.msgReceived(payload);
			}
		} else {
			
		}
		
	}

}
