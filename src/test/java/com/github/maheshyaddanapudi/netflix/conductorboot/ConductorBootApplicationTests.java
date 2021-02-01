package com.github.maheshyaddanapudi.netflix.conductorboot;

import com.github.maheshyaddanapudi.netflix.conductorboot.constants.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.rest.RestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration(exclude = {
		RestClientAutoConfiguration.class,
		DataSourceAutoConfiguration.class,
		CassandraAutoConfiguration.class,
		SecurityAutoConfiguration.class,
		ManagementWebSecurityAutoConfiguration.class,
		UserDetailsServiceAutoConfiguration.class,
		FlywayAutoConfiguration.class
})
@ActiveProfiles(profiles = {Constants.TEST, Constants.MARIADB4J, Constants.EMBEDDED_ELASTICSEARCH, Constants.EMBEDDED_OAUTH2, Constants.SECURITY})
class ConductorBootApplicationTests {

	@Test
	public void customTests(){
		String args[] = {"--spring.output.ansi.enabled=always"};
		SpringApplication.run(ConductorBootApplication.class, args);
	}

}
