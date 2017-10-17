package com.frequentis.cis.connector.core;

import java.io.InvalidObjectException;
import java.util.List;
import java.util.Map;

import com.frequentis.cis.core.edxlde.parameters.DEParameters;
import com.frequentis.cis.core.object.AdaptorConfiguration;
import com.frequentis.cis.core.object.Cgor;
import com.frequentis.cis.core.object.CgorInvitation;
import com.frequentis.cis.core.object.Organisation;
import com.frequentis.cis.core.object.Participant;
import com.frequentis.cis.core.payload.CISPayload;
import com.frequentis.cis.exception.communication.CISCommunicationException;
import com.frequentis.cis.exception.invalid.CISInvalidObjectException;

public interface ConnectorCore {
	
	public AdaptorConfiguration getAdaptorConfiguration();
	
	public void notify(String msgType, Object msg,  Map<DEParameters, String> deParameters, boolean dontEnvelope) throws CISInvalidObjectException, CISCommunicationException;
	
	public void msgReceived(CISPayload payload) throws Exception;
	
	public List<Cgor> getCgorList() throws CISCommunicationException;
	
	public void openCgor(String cgorName, String organisation) throws CISCommunicationException;
	
	public void closeCgor(String cgorName) throws CISCommunicationException;
	
	public Cgor getInfoOfCGOR(String cgorName) throws CISCommunicationException;
	
	public void inviteInCGOR(String cgorName, String organisation) throws CISCommunicationException;
	
	public void addParticipantInCGOR(String cgorName, String organisation) throws CISCommunicationException;
	
	public List<Participant> getParticipantsFromCGOR(String cgorName) throws CISCommunicationException;
	
	public Participant getParticipantFromCGOR(String cgorName, String participant) throws CISCommunicationException;

	public List<Organisation> getOrganisationList() throws CISCommunicationException;
	
	public void createOrganisation(String name) throws CISCommunicationException;
	
	public Organisation getOrganisationByName(String name) throws CISCommunicationException;
	
	public List<CgorInvitation> getOrganisationInvitations(String organisation) throws CISCommunicationException;
	
	public void sendInvitationResponse(String organisation, String cgorName, boolean accept) throws CISCommunicationException;
}
