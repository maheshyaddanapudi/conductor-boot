package com.netflix.conductorboot.config.db;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(
	    value="wrapper_db", 
	    havingValue = "mysql", 
	    matchIfMissing = false)
public class MySQLDbConfig {

	private final Logger logger = LoggerFactory.getLogger(MySQLDbConfig.class.getSimpleName());
	
	@Bean("datasource")
    public DataSource dataSource(
                          @Value("${spring.datasource.username:conductor}") String datasourceUsername,
                          @Value("${spring.datasource.password:conductor}") String datasourcePassword,
                          @Value("${spring.datasource.url:jdbc:mysql://localhost:3306/conductor?createDatabaseIfNotExist=true&autoReconnect=true&verifyServerCertificate=false&useSSL=false}") String datasourceUrl,
                          @Value("${spring.datasource.driver-class-name:com.mysql.cj.jdbc.Driver}") String datasourceDriver){

		System.setProperty("db", "mysql");
		System.setProperty("jdbc.url", datasourceUrl);
	    System.setProperty("jdbc.username", datasourceUsername);
	    System.setProperty("jdbc.password", datasourcePassword);
	    System.setProperty("flyway.validate-on-migrate", "false");
		System.setProperty("flyway.baseline-on-migrate", "false");
		System.setProperty("flyway.ignore-missing-migrations", "true");

		logger.info("JDBC MYSQL Properties are SET for CONDUCTOR");
		
		return DataSourceBuilder
                .create()
                .username(datasourceUsername)
                .password(datasourcePassword)
                .url(datasourceUrl)
                .driverClassName(datasourceDriver)
                .build();
    }
}
