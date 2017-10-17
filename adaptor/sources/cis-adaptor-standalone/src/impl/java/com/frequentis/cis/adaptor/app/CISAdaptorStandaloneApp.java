package com.frequentis.cis.adaptor.app;

import static springfox.documentation.builders.PathSelectors.regex;

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

import com.frequentis.cis.adaptor.app.callback.AppRestCallback;
import com.frequentis.cis.connector.CISToolConnectorImpl;
import com.frequentis.cis.core.properties.PropertiesReader;
import com.frequentis.cis.exception.config.CISConfigException;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@EnableSwagger2
@ComponentScan
public class CISAdaptorStandaloneApp {
	
	private Logger log = null;
	
	public CISAdaptorStandaloneApp() throws CISConfigException {
		ConsoleAppender console = new ConsoleAppender(); //create appender
		  //configure the appender
		  String PATTERN = "%d [%p|%c|%C{1}] %m%n";
		  console.setLayout(new PatternLayout(PATTERN)); 
		  console.setThreshold(Level.DEBUG);
		  console.activateOptions();
		  //add appender to any Logger (here is root)
		  Logger.getRootLogger().addAppender(console);
	
		  FileAppender fa = new FileAppender();
		  fa.setName("CISAdaptorLogger");
		  fa.setFile("./log/CISAdaptor.log");
		  fa.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
		  fa.setThreshold(Level.DEBUG);
		  fa.setAppend(true);
		  fa.activateOptions();
	
		  //add appender to any Logger (here is root)
		  Logger.getRootLogger().addAppender((Appender)fa);
		  //repeat with all other desired appenders
		  
		  log = Logger.getLogger(this.getClass());
		  
		  String ignoreStr = PropertiesReader.getInstance().getPropertie("startup.ignore.error");
		  Boolean ignoreError = false;
		  if (ignoreStr != null) {
			  ignoreError = Boolean.parseBoolean(ignoreStr);
		  }
		  log.info("Ignore Errors on AdaptorStartup: " + ignoreError);
		  
		  try {
			  CISToolConnectorImpl connector = new CISToolConnectorImpl();
			  
			  String callbackURL = PropertiesReader.getInstance().getPropertie("application.callback.rest.endpoint");
			  if (callbackURL != null) {
				  connector.registerForMsg("CAP", null, new AppRestCallback(callbackURL));
				  connector.registerForMsg("MLP", null, new AppRestCallback(callbackURL));
			  }
		  } catch (Exception e) {
			  if (!ignoreError) {
				  throw e;
			  }
		  }
		  
		  log.info("Init. CISAdaptorStandaloneApp");
	}
	
	public static void main(String[] args) throws Exception {
        SpringApplication.run(CISAdaptorStandaloneApp.class, args);
    }
	
	@Bean
    public Docket newsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("CISAdaptorStandalone")
                .apiInfo(apiInfo())
                .select()
                .paths(regex("/CISConnector.*"))
                .build();
    }
	
	private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("CISAdaptor REST Interface API Doc.")
                .description("This is the CISAdaptor REST Interface API Documentation made with Swagger.")
                .contact("Thomas Obritzhauser")
                .version("1.0")
                .build();
    }
}
