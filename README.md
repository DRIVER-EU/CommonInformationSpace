# CommonInformationSpace
The DRIVER+ adapter for the Common Information Space.

# How to install the CIS Core to the local Repository:

1. Clone the Git repository `git clone https://github.com/DRIVER-EU/CommonInformationSpace.git` or use your Git Client UI.
2. Ensure you have maven on your computer and in the path. If not, you can download this from [here](https://maven.apache.org/download.cgi).
3. Execute the bat script 'mvnInstalls.bat' at `adaptor/maven/dependencies/`. 

Check your local maven repository (default `<userdir>/.m2/repository`) if the files were installed.

## Adaptor templates
### what and how to do the CIS implementation.

1. The current ConnectorTemplate is able to send and receive CAP, EMSI, WMS, WFS, KML, KMZ and MLP if you just need one of them, you don't have to do anything else, just use the template
2. If you need an adaptation of e.g. CAP handling (translation from app object structure to CAP) I would propose to copy this and rename it to any other project name, as the template will be updated, and to avoid overwriting your implementation
2. In the connector sources in the implementation following files are interesting for you: ```ConnectorCoreImpl.java``` and the example CAP implementation: ```CapConnectorCoreExt.java```
3. For sending CAP message, you have to “translate” your content to the CAP Object structure, provided by the google library. If you have already a cap xml you can use this as it is shown in the example. if not, you send your java object structure from the application to the connector, and in the ConnectorCoreExt you are doing the translation.
4. In the ConnectorCoreImpl you can see in the method: 
```java
public void msgReceived(CISPayload payload) throws InvalidObjectException {
	log.info("--> messageReceived");
	
    // translate the Object

	// create and forward the message to the connector
	AppCallbackHandlerImpl.getInstance().getCallback(CoreConstants.MSGTYPE_CAP).msgReceived(payload);
}
```

In this you have to change the checking of the type, if your xml is no CAP message, todo the forwarding to the spec. ConnectorCoreExt (yours).

That’s everything you have to do, the rest is done already ;-)

Depending on your application, you could add the connector and core library directly to your application (deep integration), and use directly the java interfaces (implementation). If this is not possible, then you have to use the standalone application. This exposed a REST IF to which you can POST the messages.
To send a message to the StandaloneApplication you have to run it (e.g. directly in eclipse as java application).
The REST endpoint is reachable via the POST to http://localhost:8090/notify
As parameters you have to specify msgType (one of: CAP, EMSI, KML, KMZ, WMS, WFS, MLP) and msg (the message itself (cap xml, kml xml, WFS uri, WMS uri, MLP string...)

## Adaptor standalone
see: HowTo.docx

### Testing
The adaptor provides a Swagger-UI. You can all this with: http://localhost:80/core/swagger-ui.html on that you can test the functionality and basic communication configuration.
