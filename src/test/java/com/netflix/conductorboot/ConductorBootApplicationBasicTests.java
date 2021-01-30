package com.netflix.conductorboot;


import com.netflix.conductorboot.constants.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = {Constants.TEST})
public class ConductorBootApplicationBasicTests {

	@Test
	public void contextLoads() {
	}
}
