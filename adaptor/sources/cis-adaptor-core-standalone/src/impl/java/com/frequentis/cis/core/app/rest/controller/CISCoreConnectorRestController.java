package com.frequentis.cis.core.app.rest.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.QueryParam;

import org.apache.log4j.Logger;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.frequentis.cis.core.CISCore;
import com.frequentis.cis.core.CISCoreImpl;
import com.frequentis.cis.core.callback.RestConnectorCallback;
import com.frequentis.cis.core.callback.handler.ConnectorCallbackHandler;
import com.frequentis.cis.core.callback.handler.ConnectorCallbackHandlerImpl;
import com.frequentis.cis.core.edxlde.parameters.DEParameters;
import com.frequentis.cis.core.object.AdaptorConfigurationImpl;
import com.frequentis.cis.core.object.Cgor;
import com.frequentis.cis.core.object.CgorInvitation;
import com.frequentis.cis.core.object.Organisation;
import com.frequentis.cis.core.object.Participant;
import com.frequentis.cis.core.payload.objects.CISOtherContent;
import com.frequentis.cis.core.payload.objects.CISXMLContent;
import com.frequentis.cis.core.properties.PropertiesReader;
import com.frequentis.cis.core.rest.OtherContentNotify;
import com.frequentis.cis.core.rest.Response;
import com.frequentis.cis.core.rest.XMLContentNotify;
import com.frequentis.cis.exception.communication.CISCommunicationException;
import com.frequentis.cis.exception.config.CISConfigException;
import com.frequentis.cis.exception.invalid.xml.CISInvalidXMLObject;

@RestController
public class CISCoreConnectorRestController implements ResourceProcessor<RepositoryLinksResource> {

	private Logger log = Logger.getLogger(this.getClass());
	private CISCore cisCore = null; 
	private ConnectorCallbackHandler callbackHandler = ConnectorCallbackHandlerImpl.getInstance();
	
	public CISCoreConnectorRestController() throws CISConfigException {
		String ignoreStr = PropertiesReader.getInstance().getPropertie("startup.ignore.error");
		Boolean ignoreError = false;
		if (ignoreStr != null) {
			ignoreError = Boolean.parseBoolean(ignoreStr);
		}
		try {
			cisCore = CISCoreImpl.getInstance();
		} catch (Exception e) {
			if (!ignoreError) {
				throw e;
			}
		}
	}
	
	
	@Override
	public RepositoryLinksResource process(RepositoryLinksResource resource) {
		resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(CISCoreConnectorRestController.class).sendXMLMessage(new XMLContentNotify("", new CISXMLContent(), new HashMap<DEParameters, String>(), false))).withRel("sendXMLMessage"));
		resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(CISCoreConnectorRestController.class).sendOtherMessage(new OtherContentNotify("", new CISOtherContent(), new HashMap<DEParameters, String>(), false))).withRel("sendOtherMessage"));
	    return resource;
	}
	
	@ApiOperation(value = "sendXMLMessage", nickname = "sendXMLMessage")
	@RequestMapping(value = "/CISCore/sendXMLMessage", method = RequestMethod.POST)
	@ApiImplicitParams({
        @ApiImplicitParam(name = "sendXMLMessage", value = "the notify object which contains message information", required = true, dataType = "Notify")
      })
	@ApiResponses(value = { 
            @ApiResponse(code = 200, message = "Success", response = Response.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Response.class),
            @ApiResponse(code = 500, message = "Failure", response = Response.class)}) 
	public ResponseEntity<Response> sendXMLMessage(@RequestBody XMLContentNotify notify) {
		log.info("--> sendXMLMessage");
		log.debug(notify.toString());
		
		Response response = new Response();
		
		try {
			cisCore.notify(notify.getMsgType(), notify.getXmlContent(), notify.getDeParameters(), notify.isDontEnvelope());
			response.setMessage("The object was successfully distributed!");
		} catch(CISInvalidXMLObject e) {
			log.error("Error executing the request: Invalid Object" , e);
			response.setMessage("Error Executing the request on the Server!");
			response.setDetails(e.getMessage());
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		} catch (CISCommunicationException e) {
			log.error("Error executing the request: Communication Error" , e);
			response.setMessage("Error Executing the request on the Server!");
			response.setDetails(e.getMessage());
			return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("sendXMLMessage -->");
	    return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "sendOtherMessage", nickname = "sendOtherMessage")
	@RequestMapping(value = "/CISCore/sendOtherMessage", method = RequestMethod.POST)
	@ApiImplicitParams({
        @ApiImplicitParam(name = "sendOtherMessage", value = "the notify object which contains message information", required = true, dataType = "Notify")
      })
	@ApiResponses(value = { 
            @ApiResponse(code = 200, message = "Success", response = Response.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Response.class),
            @ApiResponse(code = 500, message = "Failure", response = Response.class)}) 
	public ResponseEntity<Response> sendOtherMessage(@RequestBody OtherContentNotify notify) {
		log.info("--> sendOtherMessage");
		log.debug(notify.toString());
		
		Response response = new Response();
		
		try {
			cisCore.notify(notify.getMsgType(), notify.getOtherContent(), notify.getDeParameters(), notify.isDontEnvelope());
			response.setMessage("The object was successfully distributed!");
		} catch(CISInvalidXMLObject e) {
			log.error("Error executing the request: Invalid Object" , e);
			response.setMessage("Error Executing the request on the Server!");
			response.setDetails(e.getMessage());
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		} catch (CISCommunicationException e) {
			log.error("Error executing the request: Communication Error" , e);
			response.setMessage("Error Executing the request on the Server!");
			response.setDetails(e.getMessage());
			return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("sendOtherMessage -->");
	    return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/registerConnectorRestCallback", method = RequestMethod.POST)
	public ResponseEntity<Response> registerConnectorRestCallback(@RequestBody String callbackUrl) {
		log.info("--> registerConnectorRestCallback");
		log.debug(callbackUrl);
		
		Response response = new Response();
		
		callbackHandler.registerCallback(new RestConnectorCallback(callbackUrl));
		response.setMessage("Callback registered successfully!");
		
		log.info("registerRestCallback -->");
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "getAdaptorConfiguration", nickname = "getAdaptorConfiguration")
	@RequestMapping(value = "/CISCore/getAdaptorConfiguration", method = RequestMethod.GET)
	@ApiResponses(value = { 
            @ApiResponse(code = 200, message = "Success", response = AdaptorConfigurationImpl.class),
            @ApiResponse(code = 400, message = "Bad Request", response = AdaptorConfigurationImpl.class),
            @ApiResponse(code = 500, message = "Failure", response = AdaptorConfigurationImpl.class)}) 
	public ResponseEntity<AdaptorConfigurationImpl> getAdaptorConfiguration() {
		log.info("--> getAdaptorConfiguration");
		
		AdaptorConfigurationImpl config = (AdaptorConfigurationImpl)cisCore.getAdaptorConfiguration();
		
		HttpHeaders responseHeaders = new HttpHeaders();
		
		log.info("getAdaptorConfiguration -->");
		return new ResponseEntity<AdaptorConfigurationImpl>(config, responseHeaders, HttpStatus.OK);
	}
	
	@ApiOperation(value = "getCgorList", nickname = "getCgorList")
	@RequestMapping(value = "/CISCore/getCgorList", method = RequestMethod.GET)
	@ApiResponses(value = { 
            @ApiResponse(code = 200, message = "Success", response = ArrayList.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ArrayList.class),
            @ApiResponse(code = 500, message = "Failure", response = ArrayList.class)}) 
	public ResponseEntity<List<Cgor>> getCgorList() {
		log.info("--> getCgorList");
		
		List<Cgor> cgors;
		
		try {
			cgors = cisCore.getCgorList();
		} catch (CISCommunicationException e) {
			log.error("Error executing the request: Communication Error" , e);
			cgors = new ArrayList<Cgor>();
		}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		
		log.info("getCgorList -->");
		return new ResponseEntity<List<Cgor>>(cgors, responseHeaders, HttpStatus.OK);
	}
	
	@ApiOperation(value = "openCgor", nickname = "openCgor")
	@RequestMapping(value = "/CISCore/openCgor/{cgorName}", method = RequestMethod.POST)
	@ApiImplicitParams({
        @ApiImplicitParam(name = "cgorName", value = "name of the cgor", required = true, dataType = "String", paramType = "path"),
        @ApiImplicitParam(name = "organisation", value = "name of the organisation", required = true, dataType = "String", paramType = "query")
      })
	@ApiResponses(value = { 
            @ApiResponse(code = 200, message = "Success", response = Response.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Response.class),
            @ApiResponse(code = 500, message = "Failure", response = Response.class)}) 
	public ResponseEntity<Response> openCgor(@PathVariable String cgorName, @QueryParam("organisation") String organisation) {
		log.info("--> openCgor: " + cgorName);
		
		Response response = new Response();
		
		try {
			cisCore.openCgor(cgorName, organisation);
			response.setMessage("The CGOR was successfully Opened!");
		} catch (CISCommunicationException e) {
			log.error("Error executing the request: Communication Error" , e);
			response.setMessage("Error Executing the request on the Server!");
			response.setDetails(e.getMessage());
			return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("openCgor -->");
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "closeCgor", nickname = "closeCgor")
	@RequestMapping(value = "/CISCore/closeCgor/{cgorName}", method = RequestMethod.POST)
	@ApiImplicitParams({
        @ApiImplicitParam(name = "cgorName", value = "the CGOR name", required = true, dataType = "String", paramType = "path")
      })
	@ApiResponses(value = { 
            @ApiResponse(code = 200, message = "Success", response = Response.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Response.class),
            @ApiResponse(code = 500, message = "Failure", response = Response.class)})
	public ResponseEntity<Response> closeCgor(@PathVariable String cgorName) {
		log.info("--> closeCgor: " + cgorName);
		
		Response response = new Response();
		
		try {
			cisCore.closeCgor(cgorName);
			response.setMessage("The CGOR was successfully closed!");
		} catch (CISCommunicationException e) {
			log.error("Error executing the request: Communication Error" , e);
			response.setMessage("Error Executing the request on the Server!");
			response.setDetails(e.getMessage());
			return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("closeCgor -->");
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "getInfoOfCGOR", nickname = "getInfoOfCGOR")
	@RequestMapping(value = "/CISCore/getInfoOfCGOR/{cgorName}", method = RequestMethod.GET)
	@ApiImplicitParams({
        @ApiImplicitParam(name = "cgorName", value = "the CGOR name", required = true, dataType = "String", paramType = "path")
      })
	@ApiResponses(value = { 
            @ApiResponse(code = 200, message = "Success", response = Cgor.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Cgor.class),
            @ApiResponse(code = 500, message = "Failure", response = Cgor.class)})
	public ResponseEntity<Cgor> getInfoOfCGOR(@PathVariable String cgorName) throws CISCommunicationException {
		log.info("--> getInfoOfCGOR: " + cgorName);
		
		Response response = new Response();
		Cgor cgor= null;
		
		try {
			cgor = cisCore.getInfoOfCGOR(cgorName);
			response.setMessage("The CGOR was successfully loaded!");
		} catch (CISCommunicationException e) {
			log.error("Error executing the request: Communication Error" , e);
			response.setMessage("Error Executing the request on the Server!");
			response.setDetails(e.getMessage());
			return new ResponseEntity<Cgor>(cgor, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("getInfoOfCGOR -->");
		return new ResponseEntity<Cgor>(cgor, HttpStatus.OK);
	}
	
	@ApiOperation(value = "inviteInCGOR", nickname = "inviteInCGOR")
	@RequestMapping(value = "/CISCore/inviteInCGOR/{cgorId}", method = RequestMethod.POST)
	@ApiImplicitParams({
        @ApiImplicitParam(name = "cgorName", value = "the CGOR name", required = true, dataType = "String", paramType = "path"),
        @ApiImplicitParam(name = "organisation", value = "the Organisation name", required = true, dataType = "String", paramType = "query")
      })
	@ApiResponses(value = { 
            @ApiResponse(code = 200, message = "Success", response = Response.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Response.class),
            @ApiResponse(code = 500, message = "Failure", response = Response.class)})
	public ResponseEntity<Response> inviteInCGOR(@PathVariable String cgorName, @QueryParam("organisation") String organisation) {
		log.info("--> inviteInCGOR: " + organisation + ", organisation: " + organisation);
		
		Response response = new Response();
		
		try {
			cisCore.inviteInCGOR(cgorName, organisation);
			response.setMessage("The Participant was successfully Invited!");
		} catch (CISCommunicationException e) {
			log.error("Error executing the request: Communication Error" , e);
			response.setMessage("Error Executing the request on the Server!");
			response.setDetails(e.getMessage());
			return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("inviteInCGOR -->");
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "addParticipantInCGOR", nickname = "addParticipantInCGOR")
	@RequestMapping(value = "/CISCore/addParticipantInCGOR/{cgorId}", method = RequestMethod.POST)
	@ApiImplicitParams({
        @ApiImplicitParam(name = "cgorName", value = "the CGOR name", required = true, dataType = "String", paramType = "path"),
        @ApiImplicitParam(name = "organisation", value = "the Organisation name", required = true, dataType = "String", paramType = "query")
      })
	@ApiResponses(value = { 
            @ApiResponse(code = 200, message = "Success", response = Response.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Response.class),
            @ApiResponse(code = 500, message = "Failure", response = Response.class)})
	public ResponseEntity<Response> addParticipantInCGOR(@PathVariable String cgorName, @QueryParam("organisation") String organisation) {
		log.info("--> addParticipantInCGOR: " + cgorName + ", nodeId: " + organisation);
		
		Response response = new Response();
		
		try {
			cisCore.addParticipantInCGOR(cgorName, organisation);
			response.setMessage("The Participant was successfully added!");
		} catch (CISCommunicationException e) {
			log.error("Error executing the request: Communication Error" , e);
			response.setMessage("Error Executing the request on the Server!");
			response.setDetails(e.getMessage());
			return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("addParticipantInCGOR -->");
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "getParticipantsFromCGOR", nickname = "getParticipantsFromCGOR")
	@RequestMapping(value = "/CISCore/getParticipantsFromCGOR/{cgorId}", method = RequestMethod.GET)
	@ApiImplicitParams({
        @ApiImplicitParam(name = "cgorName", value = "the CGOR name", required = true, dataType = "String", paramType = "path")
      })
	@ApiResponses(value = { 
            @ApiResponse(code = 200, message = "Success", response = ArrayList.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ArrayList.class),
            @ApiResponse(code = 500, message = "Failure", response = ArrayList.class)}) 
	public ResponseEntity<List<Participant>> getParticipantsFromCGOR(@PathVariable String cgorName) {
		log.info("--> getParticipantsFromCGOR: " + cgorName);
		
		Response response = new Response();
		List<Participant> participants = new ArrayList<Participant>();
		try {
			participants = cisCore.getParticipantsFromCGOR(cgorName);
			response.setMessage("The participants were successfully received!");
		} catch (CISCommunicationException e) {
			log.error("Error executing the request: Communication Error" , e);
			response.setMessage("Error Executing the request on the Server!");
			response.setDetails(e.getMessage());
			return new ResponseEntity<List<Participant>>(participants, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("getParticipantsFromCGOR -->");
		return new ResponseEntity<List<Participant>>(participants, HttpStatus.OK);
	}
	
	@ApiOperation(value = "getParticipantFromCGOR", nickname = "getParticipantFromCGOR")
	@RequestMapping(value = "/CISCore/getParticipantFromCGOR/{cgorName}", method = RequestMethod.GET)
	@ApiImplicitParams({
        @ApiImplicitParam(name = "cgorName", value = "the CGOR name", required = true, dataType = "String", paramType = "path"),
        @ApiImplicitParam(name = "organisation", value = "the Organisation name", required = true, dataType = "String", paramType = "query")
      })
	@ApiResponses(value = { 
            @ApiResponse(code = 200, message = "Success", response = Participant.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Participant.class),
            @ApiResponse(code = 500, message = "Failure", response = Participant.class)})
	public ResponseEntity<Participant> getParticipantFromCGOR(@PathVariable String cgorName, @QueryParam("organisation") String organisation) {
		log.info("--> getParticipantFromCGOR: " + cgorName);
		
		Participant participant;
		
		try {
			participant = cisCore.getParticipantFromCGOR(cgorName, organisation);
		} catch (CISCommunicationException e) {
			log.error("Error executing the request: Communication Error" , e);
			participant = null;
		}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		
		log.info("getParticipantFromCGOR -->");
		return new ResponseEntity<Participant>(participant, responseHeaders, HttpStatus.OK);
	}
	
	@ApiOperation(value = "getOrganisationList", nickname = "getOrganisationList")
	@RequestMapping(value = "/CISCore/getOrganisationList", method = RequestMethod.GET)
	@ApiResponses(value = { 
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Failure", response = ResponseEntity.class)})
	public ResponseEntity<List<Organisation>> getOrganisationList() throws CISCommunicationException {
		log.info("--> getOrganisationList");
		List<Organisation> organisationList = new ArrayList<Organisation>();
		
		try {
			organisationList = cisCore.getOrganisationList();
		} catch (CISCommunicationException e) {
			log.error("Error executing the request: Communication Error" , e);
			organisationList = null;
		}
	
		HttpHeaders responseHeaders = new HttpHeaders();
		
		log.info("getOrganisationList -->");
		return new ResponseEntity<List<Organisation>>(organisationList, responseHeaders, HttpStatus.OK);
	}
	
	@ApiOperation(value = "getOrganisationInvitations", nickname = "getOrganisationInvitations")
	@RequestMapping(value = "/CISCore/getOrganisationInvitations/{organisation}", method = RequestMethod.GET)
	@ApiImplicitParams({
        @ApiImplicitParam(name = "organisation", value = "the Organisation name", required = true, dataType = "String", paramType = "path")
      })
	@ApiResponses(value = { 
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Failure", response = ResponseEntity.class)})
	public ResponseEntity<List<CgorInvitation>> getOrganisationInvitations(String organisation) throws CISCommunicationException {
		log.info("--> getOrganisationInvitations");
		List<CgorInvitation> cgorInvitationList = new ArrayList<CgorInvitation>();
		
		try {
			cgorInvitationList = cisCore.getOrganisationInvitations(organisation);
		} catch (CISCommunicationException e) {
			log.error("Error executing the request: Communication Error" , e);
			cgorInvitationList = null;
		}
	
		HttpHeaders responseHeaders = new HttpHeaders();
		
		log.info("getOrganisationInvitations -->");
		return new ResponseEntity<List<CgorInvitation>>(cgorInvitationList, responseHeaders, HttpStatus.OK);
	}
	
	@ApiOperation(value = "acceptInvitation", nickname = "acceptInvitation")
	@RequestMapping(value = "/CISCore/acceptInvitation/{organisation}/{cgorname}", method = RequestMethod.POST)
	@ApiImplicitParams({
        @ApiImplicitParam(name = "organisation", value = "the Organisation name", required = true, dataType = "String", paramType = "path"),
        @ApiImplicitParam(name = "cgorname", value = "the cgorname name", required = true, dataType = "String", paramType = "path")
      })
	@ApiResponses(value = { 
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Failure", response = ResponseEntity.class)})
	public ResponseEntity<Response> acceptInvitation(@PathVariable String organisation, @PathVariable String cgorname) throws CISCommunicationException {
		log.info("--> acceptInvitation: " + organisation + ", " + cgorname);
		
		Response response = new Response();
		try {
			cisCore.sendInvitationResponse(organisation, cgorname, true);
			response.setMessage("The accept was successfully sent!");
		} catch (CISCommunicationException e) {
			log.error("Error executing the request: Communication Error" , e);
			response.setMessage("Error Executing the request on the Server!");
			response.setDetails(e.getMessage());
			return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("acceptInvitation -->");
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "rejectInvitation", nickname = "rejectInvitation")
	@RequestMapping(value = "/CISCore/rejectInvitation/{organisation}/{cgorname}", method = RequestMethod.POST)
	@ApiImplicitParams({
        @ApiImplicitParam(name = "organisation", value = "the Organisation name", required = true, dataType = "String", paramType = "path"),
        @ApiImplicitParam(name = "cgorname", value = "the cgorname name", required = true, dataType = "String", paramType = "path")
      })
	@ApiResponses(value = { 
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Failure", response = ResponseEntity.class)})
	public ResponseEntity<Response> rejectInvitation(@PathVariable String organisation, @PathVariable String cgorname) throws CISCommunicationException {
		log.info("--> rejectInvitation: " + organisation + ", " + cgorname);
		
		Response response = new Response();
		try {
			cisCore.sendInvitationResponse(organisation, cgorname, false);
			response.setMessage("The reject was successfully sent!");
		} catch (CISCommunicationException e) {
			log.error("Error executing the request: Communication Error" , e);
			response.setMessage("Error Executing the request on the Server!");
			response.setDetails(e.getMessage());
			return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("rejectInvitation -->");
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "createOrganisation", nickname = "createOrganisation")
	@RequestMapping(value = "/CISCore/createOrganisation/{organisation}", method = RequestMethod.POST)
	@ApiImplicitParams({
        @ApiImplicitParam(name = "organisation", value = "the Organisation name", required = true, dataType = "String", paramType = "path")
      })
	@ApiResponses(value = { 
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Failure", response = ResponseEntity.class)})
	public ResponseEntity<Response> createOrganisation(@PathVariable String organisation) throws CISCommunicationException {
		log.info("--> createOrganisation: " + organisation);
		
		Response response = new Response();
		try {
			cisCore.createOrganisation(organisation);
			response.setMessage("The organisation was successfully added!");
		} catch (CISCommunicationException e) {
			log.error("Error executing the request: Communication Error" , e);
			response.setMessage("Error Executing the request on the Server!");
			response.setDetails(e.getMessage());
			return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("createOrganisation -->");
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "getOrganisationByName", nickname = "getOrganisationByName")
	@RequestMapping(value = "/CISCore/getOrganisationByName/{organisation}", method = RequestMethod.GET)
	@ApiImplicitParams({
        @ApiImplicitParam(name = "organisation", value = "the Organisation name", required = true, dataType = "String", paramType = "query")
      })
	@ApiResponses(value = { 
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Failure", response = ResponseEntity.class)})
	public ResponseEntity<Organisation> getOrganisationByName(@PathVariable String organisation) throws CISCommunicationException {
		log.info("--> getOrganisationByName: " + organisation);
		Organisation organisationRes = null;
		
		organisationRes = cisCore.getOrganisationByName(organisation);
		
		HttpHeaders responseHeaders = new HttpHeaders();
		log.info("getOrganisationByName -->");
		return new ResponseEntity<Organisation>(organisationRes, responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/CISCore/postmessage/xml", method = RequestMethod.POST)
	public ResponseEntity<Response> testXMLConnectorCallback(@RequestBody CISXMLContent xmlpayload) {
		log.info("--> testXMLConnectorCallback");
		Response response = new Response();
		
		log.info(xmlpayload);
		
		log.info("testXMLConnectorCallback -->");
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/CISCore/postmessage/other", method = RequestMethod.POST)
	public ResponseEntity<Response> testConnectorCallback(@RequestBody CISOtherContent otherpayload) {
		log.info("--> testConnectorCallback");
		Response response = new Response();
		
		log.info(otherpayload);
		
		log.info("testConnectorCallback -->");
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
}
