package com.github.maheshyaddanapudi.netflix.conductorboot.config.embedded.db;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;
import com.github.maheshyaddanapudi.netflix.conductorboot.constants.Constants;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.time.Duration;
import java.util.Properties;

@Configuration
@Profile(Constants.MARIADB4J)
public class EmbeddedMariaDbConfig {

	private final Logger logger = LoggerFactory.getLogger(EmbeddedMariaDbConfig.class.getSimpleName());
	
	@Value("${mariadb4j.maxConnections:100}")
	public String maxConnections;
	
	@Value("${mariadb4j.port:0}")
	public int mariadb4jPort;

	@Value("${MARIADB4J_DIR:embedded/persistence/mariadb4j}")
	public String MARIADB4J_DIR;
	
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
	
    @Bean(Constants.MARIADB4J_SPRING_SERVICE)
    public MariaDB4jSpringService mariaDB4jSpringService() {

    	logger.info("Creating MariaDB4JSpringService.");
    	
    	MariaDB4jSpringService mariaDB4jSpringService = new MariaDB4jSpringService();

		logger.info("Setting MariaDB4JSpringService configuration arguments.");
    	
    	mariaDB4jSpringService.getConfiguration().addArg(Constants.MARIADB_ARGS_MAX_CONNECTIONS + maxConnections);
    	mariaDB4jSpringService.getConfiguration().addArg(Constants.MARIADB_ARGS_WAIT_TIMEOUT + waitTimeout);
    	mariaDB4jSpringService.getConfiguration().addArg(Constants.MARIADB_ARGS_CONNECT_TIMEOUT + connectTimeout);
    	
    	
    	if( (null == MARIADB4J_DIR || Constants.NONE.equalsIgnoreCase(MARIADB4J_DIR)) && (null == mariadb4jDataDir || Constants.NONE.equalsIgnoreCase(mariadb4jDataDir)) )
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
    	
    	if( (null != MARIADB4J_DIR && !Constants.NONE.equalsIgnoreCase(MARIADB4J_DIR)) && (null != mariadb4jDataDir && !Constants.NONE.equalsIgnoreCase(mariadb4jDataDir)) )
    	{
			logger.info("Setting MariaDB4JSpringService data dir to "+mariadb4jDataDir);
    		mariaDB4jSpringService.setDefaultDataDir(mariadb4jDataDir);
    	}
    	
    	if( (null != MARIADB4J_DIR && !Constants.NONE.equalsIgnoreCase(MARIADB4J_DIR)) && (null != mariadb4jLibDir && !Constants.NONE.equalsIgnoreCase(mariadb4jLibDir)) )
    	{
			logger.info("Setting MariaDB4JSpringService lib dir to "+mariadb4jLibDir);
    		mariaDB4jSpringService.setDefaultLibDir(mariadb4jLibDir);
    	}
    	
    	if( (null != MARIADB4J_DIR && !Constants.NONE.equalsIgnoreCase(MARIADB4J_DIR)) && (null != mariadb4jBaseDir && !Constants.NONE.equalsIgnoreCase(mariadb4jBaseDir)) )
    	{
			logger.info("Setting MariaDB4JSpringService base dir to "+mariadb4jBaseDir);
    		mariaDB4jSpringService.setDefaultBaseDir(mariadb4jBaseDir);
    	}
    	
        return mariaDB4jSpringService;
    }

    @Bean(Constants.DATASOURCE)
    @DependsOn(Constants.MARIADB4J_SPRING_SERVICE)
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

		final Properties dataSourceProperties = new Properties();

		dataSourceProperties.setProperty(Constants.POOL_NAME, Constants.PRE_CONDUCTOR);
		dataSourceProperties.setProperty(Constants.MAX_LIFETIME, String.valueOf(Duration.ofMinutes(5).toMillis()));
		dataSourceProperties.setProperty(Constants.CONNECTION_INIT_SQL, Constants.CONNECTION_INIT_SQL_VALUE);
		dataSourceProperties.setProperty(Constants.DRIVER_CLASS_NAME, datasourceDriver);
		dataSourceProperties.setProperty("jdbcUrl", databaseUrl);
		dataSourceProperties.setProperty("username", datasourceUsername);
		dataSourceProperties.setProperty("password", datasourcePassword);
		dataSourceProperties.setProperty(Constants.MAX_POOL_SIZE, Constants._100);
		dataSourceProperties.setProperty(Constants.MIN_IDLE, Constants._2);
		dataSourceProperties.setProperty(Constants.dataSource + Constants.DOT + Constants.CACHE_PREP_STMTS,Constants.TRUE);
		dataSourceProperties.setProperty(Constants.dataSource + Constants.DOT + Constants.PREP_STMT_CACHE_SIZE, Constants._256);
		dataSourceProperties.setProperty(Constants.dataSource + Constants.DOT + Constants.PREP_STMT_CACHE_SQL_LIMIT, Constants._2048);
		dataSourceProperties.setProperty(Constants.dataSource + Constants.DOT + Constants.USER_SERVER_PREP_STMTS,Constants.TRUE);
		dataSourceProperties.setProperty(Constants.dataSource + Constants.DOT + Constants.USE_LEGACY_DATETIME_CODE,Constants.FALSE);
		dataSourceProperties.setProperty(Constants.dataSource + Constants.DOT + Constants.SERVER_TIMEZONE,Constants.UTC);
		dataSourceProperties.setProperty(Constants.dataSource + Constants.DOT + Constants.CONNECTION_COLLATION,Constants.utf8mb4_unicode_ci);
		dataSourceProperties.setProperty(Constants.dataSource + Constants.DOT + Constants.USE_SSL,Constants.FALSE);
		dataSourceProperties.setProperty(Constants.dataSource + Constants.DOT + Constants.AUTO_RECONNECT,Constants.TRUE);

		final HikariConfig hikariConfig = new HikariConfig(dataSourceProperties);

		final HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
	    
        return hikariDataSource;
    }
}
