package com.github.maheshyaddanapudi.netflix.conductorboot.config.external.db;

import com.github.maheshyaddanapudi.netflix.conductorboot.constants.Constants;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.time.Duration;
import java.util.Properties;

@Configuration
@Profile(Constants.MYSQL)
public class MySQLDbConfig {

	private final Logger logger = LoggerFactory.getLogger(MySQLDbConfig.class.getSimpleName());
	
	@Bean("datasource")
    public DataSource dataSource(
                          @Value("${spring.datasource.username:conductor}") String datasourceUsername,
                          @Value("${spring.datasource.password:conductor}") String datasourcePassword,
                          @Value("${spring.datasource.url:jdbc:mysql://localhost:3306/conductor??createDatabaseIfNotExist=true&autoReconnect=true&verifyServerCertificate=false&useSSL=false&allowPublicKeyRetrieval=true&useMysqlMetadata=true}") String datasourceUrl,
                          @Value("${spring.datasource.driver-class-name:com.mysql.cj.jdbc.Driver}") String datasourceDriver){

		System.setProperty(Constants.DB, Constants.MYSQL);
		System.setProperty(Constants.JDBC_URL, datasourceUrl);
		System.setProperty(Constants.JDBC_USERNAME, datasourceUsername);
		System.setProperty(Constants.JDBC_PASSWORD, datasourcePassword);
		System.setProperty(Constants.FLYWAY_VALIDATE_ON_MIGRATE, Constants.FALSE);
		System.setProperty(Constants.FLYWAY_BASELINE_ON_MIGRATE, Constants.TRUE);
		System.setProperty(Constants.FLYWAY_IGNORE_MISSING_MIGRATIONS, Constants.TRUE);

		logger.info("JDBC MYSQL Properties are SET for CONDUCTOR");

		final Properties dataSourceProperties = new Properties();

		dataSourceProperties.setProperty("poolName", "pre-conductor");
		dataSourceProperties.setProperty("maxLifetime", String.valueOf(Duration.ofMinutes(5).toMillis()));
		dataSourceProperties.setProperty("connectionInitSql", "set character_set_client = utf8mb4;");
		dataSourceProperties.setProperty("driverClassName", datasourceDriver);
		dataSourceProperties.setProperty("jdbcUrl", datasourceUrl);
		dataSourceProperties.setProperty("username", datasourceUsername);
		dataSourceProperties.setProperty("password", datasourcePassword);
		dataSourceProperties.setProperty("maximumPoolSize", "100");
		dataSourceProperties.setProperty("minimumIdle", "2");
		dataSourceProperties.setProperty("dataSource.cachePrepStmts","true");
		dataSourceProperties.setProperty("dataSource.prepStmtCacheSize", "256");
		dataSourceProperties.setProperty("dataSource.prepStmtCacheSqlLimit", "2048");
		dataSourceProperties.setProperty("dataSource.useServerPrepStmts","true");
		dataSourceProperties.setProperty("dataSource.useLegacyDatetimeCode","false");
		dataSourceProperties.setProperty("dataSource.serverTimezone","UTC");
		dataSourceProperties.setProperty("dataSource.connectionCollation","utf8mb4_unicode_ci");
		dataSourceProperties.setProperty("dataSource.useSSL","false");
		dataSourceProperties.setProperty("dataSource.autoReconnect","true");

		final HikariConfig hikariConfig = new HikariConfig(dataSourceProperties);

		final HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
		
		return hikariDataSource;
    }
}
