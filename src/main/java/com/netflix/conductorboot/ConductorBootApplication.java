package com.netflix.conductorboot;

import com.netflix.conductorboot.config.env.EnvironmentVariablesToSystemPropertiesMappingConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.elasticsearch.rest.RestClientAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import com.netflix.conductor.bootstrap.Main;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {RestClientAutoConfiguration.class})
public class ConductorBootApplication {

	private final Logger logger = LoggerFactory.getLogger(ConductorBootApplication.class.getSimpleName());

	@Autowired
	EnvironmentVariablesToSystemPropertiesMappingConfiguration environmentVariablesToSystemPropertiesMappingConfiguration;

	@Value("${conductor.server.startup.enabled:true}")
	public boolean CONDUCTOR_SERVER_STARTUP_ENABLED;

	public static void main(String[] args) {
		SpringApplication.run(ConductorBootApplication.class, args);
	}

	@EventListener(ApplicationStartedEvent.class)
	public void mapEnvToProp()
	{
		this.environmentVariablesToSystemPropertiesMappingConfiguration.mapEnvToProp();
	}

	@EventListener(ApplicationReadyEvent.class)
	public void startConductor() throws Exception {

		if(CONDUCTOR_SERVER_STARTUP_ENABLED)
		{
			logger.info("Starting Integrated Conductor Server.");
			String args[] = new String[0];
			Main.main(args);
			logger.info("Integrated Conductor Server Startup Command Fired. Verify Conductor Server Startup from further logs.");
		}
		else
			logger.warn("Conductor Server Startup is DISABLED by default.");
	}

}
