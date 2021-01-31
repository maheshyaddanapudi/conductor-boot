package com.netflix.conductorboot.config.env;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.netflix.conductorboot.lib.embedded.elastic.EmbeddedElastic;
import com.netflix.conductorboot.service.embedded.elastic.EmbeddedElasticService;
import org.apache.catalina.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;

public class GracefulShutdown implements TomcatConnectorCustomizer, ApplicationListener<ContextClosedEvent> {
	
	private static final Logger log = LoggerFactory.getLogger(GracefulShutdown.class);
	
	private volatile Connector connector;
	
	@Autowired(required = false)
	public MariaDB4jSpringService mariaDB4jSpringService;
	
	@Autowired(required = false)
	public EmbeddedElastic embeddedElastic;
	
	@Value("${awaitTermination:120}")
	private int awaitTermination;

	@Override
	public void customize(Connector connector) {
		this.connector = connector;
	}

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {

		try{
			this.connector.pause();
		}
		catch(NullPointerException npe)
		{
			// Do Nothing
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		if(null!=this.embeddedElastic)
		{
			log.info("### Shutting Down Embedded Elasticsearch ###");
			this.embeddedElastic.stop();
			log.info("$$$ Embedded Elasticsearch Shutdown Status : "+this.embeddedElastic.isElasticServerStopped()+" $$$");
		}

		if(null!=this.mariaDB4jSpringService)
		{
			log.info("### Shutting Down Embedded MariaDB ###");
			this.mariaDB4jSpringService.stop();
			log.info("$$$ Embedded MariaDB Shutdown Status : "+!this.mariaDB4jSpringService.isRunning()+" $$$");
		}

		try{
			Executor executor = this.connector.getProtocolHandler().getExecutor();
			if (executor instanceof ThreadPoolExecutor) {
				try {
					ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
					threadPoolExecutor.shutdown();
					if (!threadPoolExecutor.awaitTermination(awaitTermination, TimeUnit.SECONDS)) {
						log.warn("Conductor Boot thread pool did not shut down gracefully within " + awaitTermination
								+ " seconds. Proceeding with forceful shutdown");
					}
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				} catch(NullPointerException npe)
				{

				}
			}
		}
		catch(NullPointerException npe)
		{
			// Do Nothing
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
