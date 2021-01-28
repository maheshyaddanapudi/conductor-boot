package com.netflix.conductorboot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.elasticsearch.rest.RestClientAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import com.netflix.conductor.bootstrap.Main;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {RestClientAutoConfiguration.class})
public class ConductorBootApplication {

	@Value("${conductor.server.startup.enabled:true}")
	public boolean CONDUCTOR_SERVER_STARTUP_ENABLED;

	public static void main(String[] args) {
		SpringApplication.run(ConductorBootApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void startConductor() throws Exception {

		if(CONDUCTOR_SERVER_STARTUP_ENABLED)
		{
			String args[] = new String[0];
			Main.main(args);
		}
	}

}
