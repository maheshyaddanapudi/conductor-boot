package com.github.maheshyaddanapudi.netflix.conductorboot.config.external.db;

import com.github.maheshyaddanapudi.netflix.conductorboot.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile(Constants.MYSQL)
public class MySQLDbConfig {

	private final Logger logger = LoggerFactory.getLogger(MySQLDbConfig.class.getSimpleName());
	
	@Bean("datasource")
    public DataSource dataSource(
                          @Value("${spring.datasource.username:conductor}") String datasourceUsername,
                          @Value("${spring.datasource.password:conductor}") String datasourcePassword,
                          @Value("${spring.datasource.url:jdbc:mysql://localhost:3306/conductor?createDatabaseIfNotExist=true&autoReconnect=true&verifyServerCertificate=false&useSSL=false}") String datasourceUrl,
                          @Value("${spring.datasource.driver-class-name:com.mysql.cj.jdbc.Driver}") String datasourceDriver){

		if(!datasourceUrl.contains("useMysqlMetadata"))
		{
			datasourceUrl = datasourceUrl + "&" + Constants.MARIADB_URL_EXTN_USE_MYSQL_METADATA;
		}

		System.setProperty(Constants.DB, Constants.MYSQL);
		System.setProperty(Constants.JDBC_URL, datasourceUrl);
		System.setProperty(Constants.JDBC_USERNAME, datasourceUsername);
		System.setProperty(Constants.JDBC_PASSWORD, datasourcePassword);
		System.setProperty(Constants.FLYWAY_VALIDATE_ON_MIGRATE, Constants.FALSE);
		System.setProperty(Constants.FLYWAY_BASELINE_ON_MIGRATE, Constants.TRUE);
		System.setProperty(Constants.FLYWAY_IGNORE_MISSING_MIGRATIONS, Constants.TRUE);

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
