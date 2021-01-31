package com.github.maheshyaddanapudi.netflix.conductorboot;

import com.github.maheshyaddanapudi.netflix.conductorboot.config.env.EnvironmentVariablesToSystemPropertiesMappingConfiguration;
import com.github.maheshyaddanapudi.netflix.conductorboot.lib.embedded.elastic.EmbeddedElastic;
import com.github.maheshyaddanapudi.netflix.conductorboot.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.rest.RestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import com.netflix.conductor.bootstrap.Main;
import com.github.maheshyaddanapudi.netflix.conductorboot.config.env.GracefulShutdown;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
@EnableZuulProxy
@CrossOrigin("*")
@EnableAutoConfiguration(exclude = {
		RestClientAutoConfiguration.class,
		DataSourceAutoConfiguration.class,
		CassandraAutoConfiguration.class,
		SecurityAutoConfiguration.class,
		ManagementWebSecurityAutoConfiguration.class,
		UserDetailsServiceAutoConfiguration.class,
		FlywayAutoConfiguration.class
})
public class ConductorBootApplication {

	private final Logger logger = LoggerFactory.getLogger(ConductorBootApplication.class.getSimpleName());

	@Autowired
	EnvironmentVariablesToSystemPropertiesMappingConfiguration environmentVariablesToSystemPropertiesMappingConfiguration;

	@Value("${conductor.server.startup.enabled:true}")
	public boolean CONDUCTOR_SERVER_STARTUP_ENABLED;

	@Value("${elasticsearch.startup.enabled:true}")
	public boolean ELASTICSEARCH_STARTUP_ENABLED;

	@Value("${elasticsearch.version:5.5.2}")
	public String ELASTICSEARCH_VERSION;

	@Value("${elasticsearch.type:EMBEDDED}")
	public String ELASTICSEARCH_TYPE;

	@Value("${elasticsearch.dir:embedded/persistence}")
	public String ELASTICSEARCH_DIR;

	@Autowired(required = false)
	public EmbeddedElastic embeddedElastic;

	private boolean elasticsearchInstallationExists;

	private static String[] args_buffer;

	public static void main(String[] args) {

		args_buffer = args;
		SpringApplication.run(ConductorBootApplication.class, args);
	}

	@EventListener(ApplicationStartedEvent.class)
	public void mapEnvToProp()
	{
		File elasticsearchInstallation = new File(ELASTICSEARCH_DIR+"/elasticsearch-"+ELASTICSEARCH_VERSION);
		this.elasticsearchInstallationExists = elasticsearchInstallation.exists();
		this.environmentVariablesToSystemPropertiesMappingConfiguration.mapEnvToProp();
	}

	@EventListener(ApplicationReadyEvent.class)
	public void startup() throws Exception {
		if(ELASTICSEARCH_TYPE.equalsIgnoreCase(Constants.SPRING_BOOT_EMBEDDED) && ELASTICSEARCH_STARTUP_ENABLED)
			startEmbeddedElasticsearch();
		else if(ELASTICSEARCH_TYPE.equalsIgnoreCase(Constants.SPRING_BOOT_EMBEDDED))
			logger.warn("Embedded Elasticsearch IS CONFIGURED BUT NOT Started - elasticsearch.type: "+ELASTICSEARCH_TYPE+" && elasticsearch.startup.enabled: "+ELASTICSEARCH_STARTUP_ENABLED+" (Check this property or Enable 'embedded-elasticsearch' profile)");
		else
			logger.warn("Embedded Elasticsearch NEITHER Configured NOR Started - elasticsearch.type: "+ELASTICSEARCH_TYPE+" (Check this property or Enable 'embedded-elasticsearch' profile)"+" && elasticsearch.startup.enabled: "+ELASTICSEARCH_STARTUP_ENABLED);
		startConductor();
	}

	public void startConductor() throws Exception {

		if(CONDUCTOR_SERVER_STARTUP_ENABLED)
		{
			logger.info("Starting Integrated Conductor Server.");
			Main.main(args_buffer);
			logger.info("Integrated Conductor Server Startup Command Fired. Verify Conductor Server Startup from further logs.");
		}
		else
			logger.warn("Conductor Server Startup is DISABLED by default. Enable 'conductor' profile or Set this property conductor.server.startup.enabled to true to enable Integrated Conductor Server.");
	}

	public void startEmbeddedElasticsearch() throws InterruptedException, IOException {

		if(this.elasticsearchInstallationExists)
		{
			logger.info("Embedded Elasticsearch Installation Skipped!");
			this.embeddedElastic.start(false);
		}
		else
			this.embeddedElastic.start(true);
	}

	@Bean
	public GracefulShutdown gracefulShutdown() {
		return new GracefulShutdown();
	}

	@Bean
	public ConfigurableServletWebServerFactory webServerFactory(final GracefulShutdown gracefulShutdown) {
		TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
		factory.addConnectorCustomizers(gracefulShutdown);
		return factory;
	}

}
