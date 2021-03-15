package com.github.maheshyaddanapudi.netflix.conductorboot.config.flyway;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.github.maheshyaddanapudi.netflix.conductorboot.constants.Constants;

@Configuration
@Profile(value = {Constants.MARIADB4J, Constants.MYSQL})
public class FlywayOptionalAutoConfiguration {

    @Autowired
    DataSource dataSource;

    @Bean(initMethod = Constants.MIGRATE)
    public Flyway flyway() {
        Flyway flyway = new Flyway();
        flyway.setValidateOnMigrate(false);
        flyway.setBaselineVersionAsString(Constants.ZERO);
        flyway.setBaselineOnMigrate(true);
        flyway.setDataSource(dataSource);
        return flyway;
    }
}
