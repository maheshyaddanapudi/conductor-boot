package com.github.maheshyaddanapudi.netflix.conductorboot.config.cloud;

import java.sql.SQLException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.relational.DataSourceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;

import com.github.maheshyaddanapudi.netflix.conductorboot.constants.Constants;
import com.netflix.archaius.api.annotations.Configuration;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@Profile(Constants.CF_MYSQL)
public class PcfDataSourceConfiguration {

  private final Logger logger = LoggerFactory.getLogger(PcfDataSourceConfiguration.class.getSimpleName());

  @Bean(Constants.CLOUD)
  public Cloud cloud() {
    return new CloudFactory().getCloud();
  }

  @Bean(Constants.DATASOURCE)
  @DependsOn(Constants.CLOUD)
  public DataSource dataSource() throws SQLException {

    final Properties dataSourceProperties = new Properties();

    dataSourceProperties.setProperty(Constants.CACHE_PREP_STMTS,Constants.TRUE);
    dataSourceProperties.setProperty(Constants.PREP_STMT_CACHE_SIZE,Constants._256);
    dataSourceProperties.setProperty(Constants.PREP_STMT_CACHE_SQL_LIMIT,Constants._2048);
    dataSourceProperties.setProperty(Constants.USER_SERVER_PREP_STMTS,Constants.TRUE);
    dataSourceProperties.setProperty(Constants.USE_LEGACY_DATETIME_CODE,Constants.FALSE);
    dataSourceProperties.setProperty(Constants.SERVER_TIMEZONE,Constants.UTC);
    dataSourceProperties.setProperty(Constants.CONNECTION_COLLATION,Constants.utf8mb4_unicode_ci);
    dataSourceProperties.setProperty(Constants.USE_SSL,Constants.FALSE);
    dataSourceProperties.setProperty(Constants.AUTO_RECONNECT,Constants.TRUE);

    final Map<String, Object> connectionProperties = new HashMap<String, Object>();

    connectionProperties.put(Constants.POOL_NAME, Constants.PRE_CONDUCTOR);
    connectionProperties.put(Constants.MAX_POOL_SIZE, Integer.parseInt(Constants._100));
    connectionProperties.put(Constants.MAX_LIFETIME, Duration.ofMinutes(5).toMillis());
    connectionProperties.put(Constants.CONNECTION_INIT_SQL, Constants.CONNECTION_INIT_SQL_VALUE);
    connectionProperties.put(Constants.DATASOURCE_PROPERTIES, dataSourceProperties);

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