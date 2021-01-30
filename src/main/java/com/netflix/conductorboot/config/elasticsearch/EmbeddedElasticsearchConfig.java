package com.netflix.conductorboot.config.elasticsearch;

import com.netflix.conductorboot.constants.Constants;
import com.netflix.conductorboot.lib.embedded.elastic.EmbeddedElastic;
import com.netflix.conductorboot.lib.embedded.elastic.PopularProperties;
import com.netflix.conductorboot.service.embedded.elastic.EmbeddedElasticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Configuration
@Profile(Constants.EMBEDDED_ELASTICSEARCH)
public class EmbeddedElasticsearchConfig {

    private final Logger logger = LoggerFactory.getLogger(EmbeddedElasticsearchConfig.class.getSimpleName());

    @Value("${elasticsearch.http.port:9200}")
    public int ELASTICSEARCH_HTTP_PORT;

    @Value("${elasticsearch.version:5.5.2}")
    public String ELASTICSEARCH_VERSION;

    @Value("${elasticsearch.tcp.port:9300}")
    public int ELASTICSEARCH_TCP_PORT;

    @Value("${elasticsearch.dir:embedded/persistence}")
    public String ELASTICSEARCH_DIR;

    @Value("${elasticsearch.cleanInstallationDirectoryOnStop:false}")
    public boolean ELASTICSEARCH_CLEANUP;

    @Value("${elasticsearch.startup.time-out:60}")
    public long ELASTICSEARCH_STARTUP_TIMEOUT_IN_SECONDS;

    @Value("${workflow.elasticsearch.index.name:conductor}")
    public String WORKFLOW_ELASTICSEARCH_INDEX_NAME;

    @Value("${elasticsearch.resource.binary.path:embedded/resources/elasticsearch-${elasticsearch.version}}")
    public String ELASTICSEARCH_RESOURCE_BINARY_PATH;

    @Value("${elasticsearch.resource.binary.name:elasticsearch-${elasticsearch.version}.zip}")
    public String ELASTICSEARCH_RESOURCE_BINARY_NAME;

    @Autowired
    private EmbeddedElasticService embeddedElasticService;

    @Bean
    public EmbeddedElastic embeddedElastic() throws IOException {

        Path path = Paths.get(new ClassPathResource(ELASTICSEARCH_RESOURCE_BINARY_PATH).getPath());

        if (!Files.exists(path)) {
            Files.createDirectories(path);
            logger.info("Embedded Elasticsearch Resource Directory created");
        } else {

            logger.info("Embedded Elasticsearch Resource Directory  already exists");
        }

        File elasticsearchResourceFile = new File(ELASTICSEARCH_RESOURCE_BINARY_PATH+"/"+ELASTICSEARCH_RESOURCE_BINARY_NAME);

        logger.info("Obtaining Elasticsearch Resource from ClassPathResource : " + ELASTICSEARCH_RESOURCE_BINARY_NAME);

        if(elasticsearchResourceFile.exists())
        {
            logger.info("Found ClassPathResource : "+ELASTICSEARCH_RESOURCE_BINARY_NAME+ " and File URL is : "+"file:///"+elasticsearchResourceFile.getAbsolutePath());

            EmbeddedElastic embeddedElastic = EmbeddedElastic.builder()
                    .withInstallationDirectory(new File(ELASTICSEARCH_DIR))
                    .withDownloadUrl(new URL("file:///"+elasticsearchResourceFile.getAbsolutePath()))
                    .withDownloaderConnectionTimeout(900, TimeUnit.SECONDS)
                    .withElasticVersion(ELASTICSEARCH_VERSION)
                    .withCleanInstallationDirectoryOnStop(ELASTICSEARCH_CLEANUP)
                    .withStartTimeout(ELASTICSEARCH_STARTUP_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                    .withSetting(PopularProperties.HTTP_PORT, ELASTICSEARCH_HTTP_PORT)
                    .withSetting(PopularProperties.TRANSPORT_TCP_PORT, ELASTICSEARCH_TCP_PORT)
                    .withSetting(PopularProperties.CLUSTER_NAME, WORKFLOW_ELASTICSEARCH_INDEX_NAME+"_cluster")
                    .withIndex(WORKFLOW_ELASTICSEARCH_INDEX_NAME)
                    .build();

            this.embeddedElasticService.setEmbeddedElastic(embeddedElastic);

            logger.info("Configured Embedded Persistent Elasticsearch with attached bin and no data directory.");

            return embeddedElastic;
        }
        else {
            logger.warn("ClassPathResource Not Found !!!");

            EmbeddedElastic embeddedElastic = EmbeddedElastic.builder()
                    .withInstallationDirectory(new File(ELASTICSEARCH_DIR))
                    .withDownloadDirectory(new File(ELASTICSEARCH_RESOURCE_BINARY_PATH).getAbsoluteFile())
                    .withDownloaderConnectionTimeout(900, TimeUnit.SECONDS)
                    .withElasticVersion(ELASTICSEARCH_VERSION)
                    .withCleanInstallationDirectoryOnStop(ELASTICSEARCH_CLEANUP)
                    .withStartTimeout(ELASTICSEARCH_STARTUP_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                    .withSetting(PopularProperties.HTTP_PORT, ELASTICSEARCH_HTTP_PORT)
                    .withSetting(PopularProperties.TRANSPORT_TCP_PORT, ELASTICSEARCH_TCP_PORT)
                    .withSetting(PopularProperties.CLUSTER_NAME, WORKFLOW_ELASTICSEARCH_INDEX_NAME+"_cluster")
                    .withIndex(WORKFLOW_ELASTICSEARCH_INDEX_NAME)
                    .build();

            this.embeddedElasticService.setEmbeddedElastic(embeddedElastic);

            logger.info("Configured Embedded Persistent Elasticsearch with downloaded bin and no data directory.");

            return embeddedElastic;
        }
    }
}
