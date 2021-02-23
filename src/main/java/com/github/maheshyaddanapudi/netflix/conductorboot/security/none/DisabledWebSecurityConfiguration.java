package com.github.maheshyaddanapudi.netflix.conductorboot.security.none;

import com.github.maheshyaddanapudi.netflix.conductorboot.constants.Constants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@ConditionalOnProperty(value = Constants.OAUTH_SECUIRTY, havingValue = Constants.NONE, matchIfMissing = true)
public class DisabledWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.cors().disable()
				.httpBasic().disable()
				.authorizeRequests()
				.antMatchers("/**").permitAll()
				.anyRequest().permitAll()
				.and()
				.authorizeRequests()
				.and()
				.exceptionHandling()
				.authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED));

	}
}
