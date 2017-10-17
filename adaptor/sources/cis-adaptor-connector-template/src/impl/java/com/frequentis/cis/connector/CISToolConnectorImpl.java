package com.frequentis.cis.connector;

import java.io.InvalidObjectException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.frequentis.cis.callback.AppCallback;
import com.frequentis.cis.connector.callback.AppCallbackHandlerImpl;
import com.frequentis.cis.connector.core.ConnectorCore;
import com.frequentis.cis.connector.core.ConnectorCoreImpl;
import com.frequentis.cis.connector.criteria.Criteria;
import com.frequentis.cis.core.edxlde.parameters.DEParameters;
import com.frequentis.cis.core.object.AdaptorConfiguration;
import com.frequentis.cis.core.object.Cgor;
import com.frequentis.cis.core.object.CgorInvitation;
import com.frequentis.cis.core.object.Organisation;
import com.frequentis.cis.core.object.Participant;
import com.frequentis.cis.exception.CISException;
import com.frequentis.cis.exception.communication.CISCommunicationException;
import com.frequentis.cis.exception.config.CISConfigException;

/**
 * The Connector implementation that implements methods for sending messages from an application
 * to the CIS Core for distributing them in the CIS.
 * 
 * @author TObritzh
 *
 */
public class CISToolConnectorImpl implements CISToolConnector {

	private Logger log = Logger.getLogger(this.getClass());
	
	private ConnectorCore connectorCore = null;
	
	/**
	 * Connector Constructor
	 */
	public CISToolConnectorImpl() throws CISConfigException {
		log.info("CISToolConnectorImpl");
		connectorCore = new ConnectorCoreImpl();
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
			AppCallback callback) throws CISConfigException {
		log.info("registerForMsg - msgType: " + msgType);
		
		AppCallbackHandlerImpl.getInstance().registerCAPCallback(msgType, callback);

	}
	
	/**
	 * Configuration handling
	 */
	
	public AdaptorConfiguration getAdaptorConfiguration () {
		log.info("--> getAdaptorConfiguration");
		
		log.info("getAdaptorConfiguration -->");
		return connectorCore.getAdaptorConfiguration();
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
	public void notify(String msgType, Object msg,  Map<DEParameters, String> deParameters, boolean dontEnvelope) throws CISException {
		log.info("notify - msgType: " + msgType);
		log.debug("msg: " + msg + ", DE Param: " + deParameters);

		connectorCore.notify(msgType, msg, deParameters, dontEnvelope);
	}
	
	public List<Cgor> getCgorList() throws CISCommunicationException {
		log.info("getCgorList");
		return connectorCore.getCgorList();
	}
	
	public void openCgor(String cgorName, String organisation) throws CISCommunicationException {
		log.info("openCgor");
		connectorCore.openCgor(cgorName, organisation);
	}
	
	public void closeCgor(String cgorName) throws CISCommunicationException {
		log.info("closeCgor");
		connectorCore.closeCgor(cgorName);
	}
	
	public Cgor getInfoOfCGOR(String cgorName) throws CISCommunicationException {
		log.info("getInfoOfCGOR");
		return connectorCore.getInfoOfCGOR(cgorName);
	}
	
	public void inviteInCGOR(String cgorName, String organisation) throws CISCommunicationException {
		log.info("inviteInCGOR");
		connectorCore.inviteInCGOR(cgorName, organisation);
	}
	
	public void addParticipantInCGOR(String cgorName, String organisation) throws CISCommunicationException {
		log.info("addParticipantInCGOR");
		connectorCore.addParticipantInCGOR(cgorName, organisation);
	}
	
	public List<Participant> getParticipantsFromCGOR(String cgorName) throws CISCommunicationException {
		log.info("getParticipantsFromCGOR");
		return connectorCore.getParticipantsFromCGOR(cgorName);
	}
	
	public Participant getParticipantFromCGOR(String cgorName, String participant) throws CISCommunicationException {
		log.info("getParticipantFromCGOR");
		return connectorCore.getParticipantFromCGOR(cgorName, participant);
	}
	
	public List<Organisation> getOrganisationList() throws CISCommunicationException {
		log.info("getParticipantFromCGOR");
		return connectorCore.getOrganisationList();
	}
	
	public void createOrganisation(String name) throws CISCommunicationException {
		log.info("getParticipantFromCGOR");
		connectorCore.createOrganisation(name);
	}
	
	public Organisation getOrganisationByName(String name) throws CISCommunicationException {
		log.info("getParticipantFromCGOR");
		return connectorCore.getOrganisationByName(name);
	}
	
	public List<CgorInvitation> getOrganisationInvitations(String organisation) throws CISCommunicationException {
		log.info("getOrganisationInvitations");
		return connectorCore.getOrganisationInvitations(organisation);
	}
	
	public void sendInvitationResponse(String organisation, String cgorName, boolean accept) throws CISCommunicationException {
		log.info("sendInvitationResponse");
		connectorCore.sendInvitationResponse(organisation, cgorName, accept);
	}
}
