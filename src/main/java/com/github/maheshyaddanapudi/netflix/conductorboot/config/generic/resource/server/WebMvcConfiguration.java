package com.github.maheshyaddanapudi.netflix.conductorboot.config.generic.resource.server;

import com.github.maheshyaddanapudi.netflix.conductorboot.constants.Constants;
import com.github.maheshyaddanapudi.netflix.conductorboot.modal.embedded.resource.server.CustomPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.MethodParameter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@Profile({Constants.EMBEDDED_OAUTH2, Constants.EXTERNAL_ADFS})
@EnableWebSecurity
public class WebMvcConfiguration implements WebMvcConfigurer {

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(currentUserHandlerMethodArgumentResolver());
	}

	@Bean
	public HandlerMethodArgumentResolver currentUserHandlerMethodArgumentResolver() {
		return new HandlerMethodArgumentResolver() {
			@Override
			public boolean supportsParameter(MethodParameter parameter) {
				return parameter.getParameterType().equals(CustomPrincipal.class);
			}

			@Override
			public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
					NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
				try {
					return (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				} catch (Exception e) {
					return null;
				}
			}
		};
	}
}
