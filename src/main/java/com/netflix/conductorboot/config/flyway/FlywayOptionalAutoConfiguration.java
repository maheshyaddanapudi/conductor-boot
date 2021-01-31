package com.netflix.conductorboot.config.flyway;

import com.netflix.conductorboot.constants.Constants;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("conductor")
public class FlywayOptionalAutoConfiguration {

    @Autowired
    DataSource dataSource;

    @Bean(initMethod = "migrate")
    Flyway flyway() {
        Flyway flyway = new Flyway();
        flyway.setValidateOnMigrate(false);
        flyway.setBaselineVersionAsString("0");
        flyway.setBaselineOnMigrate(true);
        flyway.setDataSource(dataSource);
        return flyway;
    }
}
