package com.frequentis.cis.adaptor.app.rest.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.apache.log4j.Logger;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.frequentis.cis.core.CISCore;
import com.frequentis.cis.core.CISCoreImpl;
import com.frequentis.cis.core.properties.PropertiesReader;
import com.frequentis.cis.core.rest.Response;
import com.frequentis.cis.exception.communication.CISCommunicationException;
import com.frequentis.cis.exception.config.CISConfigException;
import com.frequentis.cis.exception.invalid.xml.CISInvalidXMLObject;


@RestController
public class CISCoreDistributorRestController implements ResourceProcessor<RepositoryLinksResource> {

	private Logger log = Logger.getLogger(this.getClass());
	private CISCore cisCore = null;
	
	public CISCoreDistributorRestController() throws CISConfigException {
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
		resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(CISCoreDistributorRestController.class).postmessage("EDXL DE")).withRel("post_messages"));
	    return resource;
	}
	

	@ApiOperation(value = "postmessage", nickname = "postmessage")
	@RequestMapping(value = "/CISConnector/postmessage", method = RequestMethod.POST)
	@ApiImplicitParams({
        @ApiImplicitParam(name = "message", value = "the EDXL-DE message that has to be forwarded to the application", required = true, dataType = "String")
      })
	@ApiResponses(value = { 
            @ApiResponse(code = 200, message = "Success", response = Response.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Response.class),
            @ApiResponse(code = 500, message = "Failure", response = Response.class)}) 
	public ResponseEntity<Response> postmessage(@RequestBody String message) {
		log.info("--> post_messages");
		log.debug(message);
		
		Response response = new Response();
		
		try {
			cisCore.msgReceived(message);
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
		
		response.setMessage("The object was successfully distributed!");
		
		log.info("post_messages -->");
	    return new ResponseEntity<Response>(response, HttpStatus.OK);
		
	}
	
	@ApiOperation(value = "testendpoint", nickname = "testendpoint")
	@RequestMapping(value = "/CISConnector/testendpoint", method = RequestMethod.POST)
	@ApiImplicitParams({
        @ApiImplicitParam(name = "message", value = "the EDXL-DE message that has to be forwarded to the application", required = true, dataType = "String")
      })
	@ApiResponses(value = { 
            @ApiResponse(code = 200, message = "Success", response = Response.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Response.class),
            @ApiResponse(code = 500, message = "Failure", response = Response.class)}) 
	public ResponseEntity<Response> testendpoint(@RequestBody String message) {
		log.info("--> post_messages");
		log.debug(message);
		
		Response response = new Response();
		response.setMessage("The object was successfully distributed!");
		
		log.info("post_messages -->");
	    return new ResponseEntity<Response>(response, HttpStatus.OK);
		
	}
	
	
}
