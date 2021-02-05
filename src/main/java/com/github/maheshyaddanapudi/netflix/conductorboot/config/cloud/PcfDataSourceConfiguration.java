package com.github.maheshyaddanapudi.netflix.conductorboot.config.cloud;

import com.github.maheshyaddanapudi.netflix.conductorboot.constants.Constants;
import com.netflix.archaius.api.annotations.Configuration;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.relational.DataSourceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@Profile("cf-mysql")
public class PcfDataSourceConfiguration {

  private final Logger logger = LoggerFactory.getLogger(PcfDataSourceConfiguration.class.getSimpleName());

  @Bean("cloud")
  public Cloud cloud() {
    return new CloudFactory().getCloud();
  }

  @Bean("datasource")
  @DependsOn("cloud")
  public DataSource dataSource() throws SQLException {

    final Properties dataSourceProperties = new Properties();

    dataSourceProperties.setProperty("cachePrepStmts","true");
    dataSourceProperties.setProperty("prepStmtCacheSize","250");
    dataSourceProperties.setProperty("prepStmtCacheSqlLimit","2048");
    dataSourceProperties.setProperty("useServerPrepStmts","true");
    dataSourceProperties.setProperty("useLegacyDatetimeCode","false");
    dataSourceProperties.setProperty("serverTimezone","UTC");
    dataSourceProperties.setProperty("connectionCollation","utf8mb4_unicode_ci");
    dataSourceProperties.setProperty("useSSL","false");
    dataSourceProperties.setProperty("autoReconnect","true");

    final Map<String, Object> connectionProperties = new HashMap<String, Object>();

    connectionProperties.put("poolName", "pre-conductor");
    connectionProperties.put("maxPoolSize", 100);
    connectionProperties.put("maxLifetime", Duration.ofMinutes(5).toMillis());
    connectionProperties.put("connectionInitSql", "set character_set_client = utf8mb4;");
    connectionProperties.put("dataSourceProperties", dataSourceProperties);

    final DataSourceConfig serviceConfig = new DataSourceConfig(connectionProperties);

    DataSource cloudDataSource = cloud().getSingletonServiceConnector(DataSource.class, serviceConfig);

    final HikariDataSource hikariDataSource = cloudDataSource.unwrap((HikariDataSource.class));

    System.setProperty(Constants.DB, Constants.MYSQL);
    System.setProperty(Constants.JDBC_URL, hikariDataSource.getJdbcUrl());
    System.setProperty(Constants.JDBC_USERNAME, hikariDataSource.getUsername());
    System.setProperty(Constants.JDBC_PASSWORD, hikariDataSource.getPassword());
    System.setProperty(Constants.FLYWAY_VALIDATE_ON_MIGRATE, Constants.FALSE);
    System.setProperty(Constants.FLYWAY_BASELINE_ON_MIGRATE, Constants.TRUE);
    System.setProperty(Constants.FLYWAY_IGNORE_MISSING_MIGRATIONS, Constants.TRUE);

    logger.info("JDBC MYSQL Properties are SET for CONDUCTOR");

    return hikariDataSource;
  }

}