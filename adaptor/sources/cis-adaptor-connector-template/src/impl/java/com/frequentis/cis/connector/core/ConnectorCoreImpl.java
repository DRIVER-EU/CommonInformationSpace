package com.frequentis.cis.connector.core;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.frequentis.cis.connector.core.cap.CapConnectorCoreExt;
import com.frequentis.cis.connector.core.mlp.MLPConnectorCoreExt;
import com.frequentis.cis.connector.core.othercontent.OtherContentConnectorCoreExt;
import com.frequentis.cis.core.CISCore;
import com.frequentis.cis.core.CISCoreImpl;
import com.frequentis.cis.core.edxlde.parameters.DEParameters;
import com.frequentis.cis.core.object.AdaptorConfiguration;
import com.frequentis.cis.core.object.Cgor;
import com.frequentis.cis.core.object.CgorInvitation;
import com.frequentis.cis.core.object.Organisation;
import com.frequentis.cis.core.object.Participant;
import com.frequentis.cis.core.payload.CISPayload;
import com.frequentis.cis.core.payload.objects.CISXMLContent;
import com.frequentis.cis.exception.communication.CISCommunicationException;
import com.frequentis.cis.exception.config.CISConfigException;
import com.frequentis.cis.exception.invalid.CISInvalidObjectException;

public class ConnectorCoreImpl implements ConnectorCore {
	
	private Logger log = Logger.getLogger(this.getClass());
	CapConnectorCoreExt capConnector = new CapConnectorCoreExt();
	MLPConnectorCoreExt mlpConnector = new MLPConnectorCoreExt();
	OtherContentConnectorCoreExt otherContentConnector = new OtherContentConnectorCoreExt();
	private CISCore core = null;
	
	public ConnectorCoreImpl() throws CISConfigException {
		core = CISCoreImpl.getInstance();
	}
	
	public AdaptorConfiguration getAdaptorConfiguration() {
		log.info("--> getAdaptorConfiguration");
		
		log.info("getAdaptorConfiguration -->");
		return core.getAdaptorConfiguration();
	}
	
	public void notify(String msgType, Object msg,  Map<DEParameters, String> deParameters, boolean dontEnvelope) throws CISInvalidObjectException, CISCommunicationException {
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
		log.info("sendMessage -->");
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
		log.info("receviedMessage -->");
	}
	
	public List<Cgor> getCgorList() throws CISCommunicationException {
		log.info("--> getCgorList");
		List<Cgor> cgors = new ArrayList<Cgor>();
		
		cgors = core.getCgorList();
		
		log.info("getCgorList -->");
		return cgors;
	}
	
	public void openCgor(String cgorName, String organisation) throws CISCommunicationException {
		log.info("--> openCgor");
		
		core.openCgor(cgorName, organisation);
		
		log.info("openCgor -->");
	}
	
	public void closeCgor(String cgorName) throws CISCommunicationException {
		log.info("--> closeCgor");
		
		core.closeCgor(cgorName);
		
		log.info("closeCgor -->");
	}
	
	public Cgor getInfoOfCGOR(String cgorName) throws CISCommunicationException {
		log.info("--> getInfoOfCGOR");
		
		Cgor cgor = null;
		cgor = core.getInfoOfCGOR(cgorName);
		
		log.info("getInfoOfCGOR -->");
		return cgor;
	}
	
	public void inviteInCGOR(String cgorName, String organisation) throws CISCommunicationException {
		log.info("--> inviteInCGOR");
		
		core.inviteInCGOR(cgorName, organisation);
		
		log.info("inviteInCGOR -->");
	}
	
	public void addParticipantInCGOR(String cgorName, String organisation) throws CISCommunicationException {
		log.info("--> addParticipantInCGOR");
		
		core.addParticipantInCGOR(cgorName, organisation);
		
		log.info("addParticipantInCGOR -->");
	}
	
	public List<Participant> getParticipantsFromCGOR(String cgorName) throws CISCommunicationException {
		log.info("--> getParticipantsFromCGOR");
		
		List<Participant> participants = new ArrayList<Participant>();
		participants = core.getParticipantsFromCGOR(cgorName);
		
		log.info("getParticipantsFromCGOR -->");
		return participants;
	}
	
	public Participant getParticipantFromCGOR(String cgorName, String participant) throws CISCommunicationException {
		log.info("--> getParticipantFromCGOR");
		
		Participant part = null;
		part = core.getParticipantFromCGOR(cgorName, participant);
		
		log.info("getParticipantFromCGOR -->");
		return part;
	}
	
	public List<Organisation> getOrganisationList() throws CISCommunicationException {
		log.info("--> getOrganisationList");
		
		log.info("getOrganisationList -->");
		return core.getOrganisationList();
	}
	
	public void createOrganisation(String name) throws CISCommunicationException {
		log.info("--> createOrganisation: " + name);
		
		core.createOrganisation(name);
		
		log.info("createOrganisation -->");
	}
	
	public Organisation getOrganisationByName(String name) throws CISCommunicationException {
		log.info("--> getOrganisationByName: " + name);
		
		log.info("getOrganisationByName -->");
		return core.getOrganisationByName(name);
	}
	
	public List<CgorInvitation> getOrganisationInvitations(String organisation) throws CISCommunicationException {
		log.info("--> getOrganisationInvitations: " + organisation);
		
		log.info("getOrganisationInvitations -->");
		return core.getOrganisationInvitations(organisation);
	}
	
	public void sendInvitationResponse(String organisation, String cgorName, boolean accept) throws CISCommunicationException {
		log.info("--> sendInvitationResponse: " + organisation + ", cgorName: " + cgorName + ", accept: " + accept);
		
		core.sendInvitationResponse(organisation, cgorName, accept);
		
		log.info("sendInvitationResponse -->");
	}

}
