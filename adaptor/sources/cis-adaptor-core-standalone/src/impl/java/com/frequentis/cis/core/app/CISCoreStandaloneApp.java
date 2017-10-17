package com.frequentis.cis.core.app;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.frequentis.cis.core.CISCoreImpl;
import com.frequentis.cis.core.callback.RestConnectorCallback;
import com.frequentis.cis.core.callback.handler.ConnectorCallbackHandlerImpl;
import com.frequentis.cis.core.properties.PropertiesReader;
import com.frequentis.cis.exception.config.CISConfigException;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static springfox.documentation.builders.PathSelectors.regex;

@SpringBootApplication
@EnableSwagger2
@ComponentScan
public class CISCoreStandaloneApp {
	
	private Logger log = null;
	
	public CISCoreStandaloneApp() throws CISConfigException {
		ConsoleAppender console = new ConsoleAppender(); //create appender
		  //configure the appender
		  String PATTERN = "%d [%p|%c|%C{1}] %m%n";
		  console.setLayout(new PatternLayout(PATTERN)); 
		  console.setThreshold(Level.DEBUG);
		  console.activateOptions();
		  //add appender to any Logger (here is root)
		  Logger.getRootLogger().addAppender(console);
	
		  FileAppender fa = new FileAppender();
		  fa.setName("CISCoreLogger");
		  fa.setFile("./log/CISCore.log");
		  fa.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
		  fa.setThreshold(Level.DEBUG);
		  fa.setAppend(true);
		  fa.activateOptions();
	
		  //add appender to any Logger (here is root)
		  Logger.getRootLogger().addAppender((Appender)fa);
		  //repeat with all other desired appenders
		  
		  log = Logger.getLogger(this.getClass());
		  
		  Boolean ignoreError = Boolean.parseBoolean(PropertiesReader.getInstance().getPropertie("startup.ignore.error"));
		  log.info("Ignore Errors on CoreStartup: " + ignoreError);
		  
		  try {
			  CISCoreImpl cisCore = CISCoreImpl.getInstance();
		  } catch (Exception e) {
			  if (!ignoreError) {
				  throw e;
			  }
		  }
		  
		  ConnectorCallbackHandlerImpl callbackHandler = ConnectorCallbackHandlerImpl.getInstance();
		  callbackHandler.registerCallback(new RestConnectorCallback(PropertiesReader.getInstance().getPropertie("core.connector.rest.callback")));
		  
		  log.info("Init. CISCoreStandaloneApp");
	}
	
	public static void main(String[] args) throws Exception {
        SpringApplication.run(CISCoreStandaloneApp.class, args);
    }
	
	@Bean
    public Docket newsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("CISCore")
                .apiInfo(apiInfo())
                .select()
                .paths(regex("/CISCore.*"))
                .build();
    }
	
	private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("CISCore REST Interface API Doc.")
                .description("This is the CISCore REST Interface API Documentation made with Swagger.")
                .contact("Thomas Obritzhauser")
                .version("1.0")
                .build();
    }

}
