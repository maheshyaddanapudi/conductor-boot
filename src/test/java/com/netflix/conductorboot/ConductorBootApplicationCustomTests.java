package com.netflix.conductorboot;

import com.netflix.conductorboot.constants.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = {Constants.TEST, Constants.MARIADB4J, Constants.EMBEDDED_ELASTICSEARCH})
public class ConductorBootApplicationCustomTests {

    @Test
    public void customTests(){
        String args[] = {"--spring.output.ansi.enabled=always"};
        SpringApplication.run(ConductorBootApplication.class, args);
    }
}
