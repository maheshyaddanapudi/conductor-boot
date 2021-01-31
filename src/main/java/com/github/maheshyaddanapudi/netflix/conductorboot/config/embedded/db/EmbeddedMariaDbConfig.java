package com.github.maheshyaddanapudi.netflix.conductorboot.config.embedded.db;

import javax.sql.DataSource;

import com.github.maheshyaddanapudi.netflix.conductorboot.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(Constants.MARIADB4J)
public class EmbeddedMariaDbConfig {

	private final Logger logger = LoggerFactory.getLogger(EmbeddedMariaDbConfig.class.getSimpleName());
	
	@Value("${mariadb4j.maxConnections:100}")
	public String maxConnections;
	
	@Value("${mariadb4j.port:0}")
	public int mariadb4jPort;
	
	@Value("${mariadb4j.dataDir:NONE}")
	public String mariadb4jDataDir;
	
	@Value("${mariadb4j.libDir:NONE}")
	public String mariadb4jLibDir;
	
	@Value("${mariadb4j.baseDir:NONE}")
	public String mariadb4jBaseDir;
	
	@Value("${mariadb4j.deleteBaseAndDataDirOnShutdown:false}")
	public boolean deleteBaseAndDataDirOnShutdown;
	
	@Value("${mariadb4j.security.disabled:true}")
	public boolean securityDisabled;
	
	@Value("${mariadb4j.wait.timeout:18000000}")
	public String waitTimeout;
	
	@Value("${mariadb4j.connect.timeout:31536000}")
	public String connectTimeout;
	
    @Bean("mariaDB4jSpringService")
    public MariaDB4jSpringService mariaDB4jSpringService() {

    	logger.info("Creating MariaDB4JSpringService.");
    	
    	MariaDB4jSpringService mariaDB4jSpringService = new MariaDB4jSpringService();

		logger.info("Setting MariaDB4JSpringService configuration arguments.");
    	
    	mariaDB4jSpringService.getConfiguration().addArg(Constants.MARIADB_ARGS_MAX_CONNECTIONS + maxConnections);
    	mariaDB4jSpringService.getConfiguration().addArg(Constants.MARIADB_ARGS_WAIT_TIMEOUT + waitTimeout);
    	mariaDB4jSpringService.getConfiguration().addArg(Constants.MARIADB_ARGS_CONNECT_TIMEOUT + connectTimeout);
    	
    	
    	if(null == mariadb4jDataDir || Constants.NONE.equalsIgnoreCase(mariadb4jDataDir))
    		logger.error("Captured Data Directory as Empty !");
    	else
		{
			mariaDB4jSpringService.getConfiguration().setDeletingTemporaryBaseAndDataDirsOnShutdown(deleteBaseAndDataDirOnShutdown);
			logger.info("Setting MariaDB4JSpringService folders deletion on shutdown to "+deleteBaseAndDataDirOnShutdown);
		}

		logger.info("Setting MariaDB4JSpringService security disabled to "+securityDisabled);
    	mariaDB4jSpringService.getConfiguration().setSecurityDisabled(securityDisabled);
    	
    	if(mariadb4jPort>0)
    	{
			logger.info("Setting MariaDB4JSpringService port to "+mariadb4jPort);
    		mariaDB4jSpringService.setDefaultPort(mariadb4jPort);
    	}
    	
    	if(null != mariadb4jDataDir && !Constants.NONE.equalsIgnoreCase(mariadb4jDataDir))
    	{
			logger.info("Setting MariaDB4JSpringService data dir to "+mariadb4jDataDir);
    		mariaDB4jSpringService.setDefaultDataDir(mariadb4jDataDir);
    	}
    	
    	if(null != mariadb4jLibDir && !Constants.NONE.equalsIgnoreCase(mariadb4jLibDir))
    	{
			logger.info("Setting MariaDB4JSpringService lib dir to "+mariadb4jLibDir);
    		mariaDB4jSpringService.setDefaultLibDir(mariadb4jLibDir);
    	}
    	
    	if(null != mariadb4jBaseDir && !Constants.NONE.equalsIgnoreCase(mariadb4jBaseDir))
    	{
			logger.info("Setting MariaDB4JSpringService base dir to "+mariadb4jBaseDir);
    		mariaDB4jSpringService.setDefaultBaseDir(mariadb4jBaseDir);
    	}
    	
        return mariaDB4jSpringService;
    }

    @Bean("datasource")
    @DependsOn("mariaDB4jSpringService")
    public DataSource dataSource(MariaDB4jSpringService mariaDB4jSpringService,
                          @Value("${spring.datasource.name:conductor}") String databaseName,
                          @Value("${spring.datasource.username:conductor}") String datasourceUsername,
                          @Value("${spring.datasource.password:conductor}") String datasourcePassword,
                          @Value("${spring.datasource.driver-class-name:org.mariadb.jdbc.Driver}") String datasourceDriver) throws ManagedProcessException {

    	//Create our database with default root user and no password
        mariaDB4jSpringService.getDB().createDB(databaseName);

        DBConfigurationBuilder config = mariaDB4jSpringService.getConfiguration();

        String databaseUrl = config.getURL(databaseName) +"?"+ Constants.MARIADB_URL_EXTN_AUTO_RECONNECT +"&"+ Constants.MARIADB_URL_EXTN_USE_MYSQL_METADATA;

		logger.info("Setting Database details as System Properties");
	    
        System.setProperty(Constants.DB, Constants.MYSQL);
	    System.setProperty(Constants.JDBC_URL, databaseUrl);
	    System.setProperty(Constants.JDBC_USERNAME, datasourceUsername);
	    System.setProperty(Constants.JDBC_PASSWORD, datasourcePassword);
	    System.setProperty(Constants.FLYWAY_VALIDATE_ON_MIGRATE, Constants.FALSE);
	    System.setProperty(Constants.FLYWAY_BASELINE_ON_MIGRATE, Constants.TRUE);
	    System.setProperty(Constants.FLYWAY_IGNORE_MISSING_MIGRATIONS, Constants.TRUE);

		logger.info("Building Embedded MariaDB Datasource.");
	    
        return DataSourceBuilder
                .create()
                .username(datasourceUsername)
                .password(datasourcePassword)
                .url(databaseUrl)
                .driverClassName(datasourceDriver)
                .build();
    }
}
