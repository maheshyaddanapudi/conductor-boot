package com.github.maheshyaddanapudi.netflix.conductorboot.config.env;

import com.github.maheshyaddanapudi.netflix.conductorboot.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.Iterator;
import java.util.Properties;

@Configuration
@SuppressWarnings({ "rawtypes" })
public class EnvironmentVariablesToSystemPropertiesMappingConfiguration {

	private final Logger logger = LoggerFactory.getLogger(EnvironmentVariablesToSystemPropertiesMappingConfiguration.class.getSimpleName());

	@Autowired
	ApplicationContext ctx;

	@Value("${ELASTICSEARCH_HOST:elasticsearch}")
	public String ELASTICSEARCH_HOST;

	@Value("${ELASTICSEARCH_PORT:0}")
	public int ELASTICSEARCH_PORT;

	@Value("${ELASTICSEARCH_URL:HTTP://${ELASTICSEARCH_HOST}:${ELASTICSEARCH_PORT}")
	public String ELASTICSEARCH_URL;

	public void mapEnvToProp() {

		logger.info("Mapping Spring Boot properties to System Properties started.");

		Properties props = new Properties();

		for (Iterator it = ((AbstractEnvironment) ctx.getEnvironment()).getPropertySources().iterator(); it.hasNext();) 
		{	
			PropertySource propertySource = (PropertySource) it.next();
			
			if (propertySource instanceof OriginTrackedMapPropertySource) 
			{
				props.putAll(((MapPropertySource) propertySource).getSource());
			}
		}
		
		props.forEach((type, value) -> {
			System.setProperty(String.valueOf(type), String.valueOf(value));
		});

		logger.info("Mapping Spring Boot properties to System Properties completed.");

		if(ELASTICSEARCH_PORT > 0)
		{
			logger.info("Elasticsearch URL is being configured.");

			System.setProperty(Constants.WORKFLOW_ELASTICSEARCH_URL, ELASTICSEARCH_URL);

			logger.info("Elasticsearch URL is configured to : " + ELASTICSEARCH_URL);
		}
		else
			logger.warn("Elasticsearch properties not found. Assuming Embedded Elasticsearch from Integrated Conductor Server will be used.");

	}

}
