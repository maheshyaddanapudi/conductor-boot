package com.github.maheshyaddanapudi.netflix.conductorboot.config.external.oauth2;

import com.github.maheshyaddanapudi.netflix.conductorboot.dtos.internal.resource.server.ResourceRoleMappingDTO;
import com.google.gson.Gson;
import com.github.maheshyaddanapudi.netflix.conductorboot.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Configuration
@EnableResourceServer
@Profile(Constants.EXTERNAL_OAUTH2)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class OAuth2ResourceServerConfiguration extends ResourceServerConfigurerAdapter
{
    private Logger logger = LoggerFactory.getLogger(OAuth2ResourceServerConfiguration.class);

    @Value("${security.oauth2.resource.mapping}")
    private String resourceMapping;

    @Override
    public void configure(final HttpSecurity http) throws Exception {

        if(null!=this.resourceMapping)
        {
            Gson gson = new Gson();

            ResourceRoleMappingDTO[] resRoleMappingDTOs = gson.fromJson(this.resourceMapping, ResourceRoleMappingDTO[].class);

            String finalLog = Constants.STRING_INITIALIZR;

            finalLog = finalLog + "\nResource Mapping to be done for : "+ resRoleMappingDTOs.length+" path mappings\n";

            int pathCounter = 1;

            for(ResourceRoleMappingDTO aResourceRoleMapping : resRoleMappingDTOs)
            {
                logger.warn("\n\t Starting to Map: \t"+ aResourceRoleMapping.getEndpoint());

                finalLog = finalLog + "\n"+pathCounter+")\t Starting to Map: \t"+ aResourceRoleMapping.getEndpoint();

                String allowedRoles = "";

                for(String aRole: aResourceRoleMapping.getRoles())
                {
                    if(Constants.STRING_INITIALIZR.equalsIgnoreCase(allowedRoles))
                    {
                        allowedRoles = aRole;
                    }
                    else
                    {
                        allowedRoles = allowedRoles + ", " + aRole;
                    }
                }

                for(String aHTTPMethod: aResourceRoleMapping.getHttp_methods()) {

                    switch(aHTTPMethod) {
                        case "GET":
                            http.authorizeRequests()
                                    .antMatchers(HttpMethod.GET,
                                            aResourceRoleMapping.getEndpoint()) .hasAnyAuthority(allowedRoles);

                            finalLog = finalLog + "\n\n############################## OAUTH2 API Security Mapping - START ####################################"
                                    + "\n\t Mapped URL: "+aResourceRoleMapping.getEndpoint()
                                    + "\n\t Mapped HTTP Method: GET"
                                    + "\n\t Mapped Roles: " + Arrays.asList(aResourceRoleMapping.getRoles()).toString()
                                    + "\n############################## OAUTH2 API Security Mapping - END ######################################\n\n";
                            break;
                        case "POST":
                            http.authorizeRequests()
                                    .antMatchers(HttpMethod.POST,
                                            aResourceRoleMapping.getEndpoint()) .hasAnyAuthority(allowedRoles);
                            finalLog = finalLog + "\n\n############################## OAUTH2 API Security Mapping - START ####################################"
                                    + "\n\t Mapped URL: "+aResourceRoleMapping.getEndpoint()
                                    + "\n\t Mapped HTTP Method: POST"
                                    + "\n\t Mapped Roles: " + Arrays.asList(aResourceRoleMapping.getRoles()).toString()
                                    + "\n############################## OAUTH2 API Security Mapping - END ######################################\n\n";
                            break;
                        case "PUT":
                            http.authorizeRequests()
                                    .antMatchers(HttpMethod.PUT,
                                            aResourceRoleMapping.getEndpoint()) .hasAnyAuthority(allowedRoles);
                            finalLog = finalLog + "\n\n############################## OAUTH2 API Security Mapping - START ####################################"
                                    + "\n\t Mapped URL: "+aResourceRoleMapping.getEndpoint()
                                    + "\n\t Mapped HTTP Method: PUT"
                                    + "\n\t Mapped Roles: " + Arrays.asList(aResourceRoleMapping.getRoles()).toString()
                                    + "\n############################## OAUTH2 API Security Mapping - END ######################################\n\n";
                            break;
                        case "PATCH":
                            http.authorizeRequests()
                                    .antMatchers(HttpMethod.PATCH,
                                            aResourceRoleMapping.getEndpoint()) .hasAnyAuthority(allowedRoles);
                            finalLog = finalLog + "\n\n############################## OAUTH2 API Security Mapping - START ####################################"
                                    + "\n\t Mapped URL: "+aResourceRoleMapping.getEndpoint()
                                    + "\n\t Mapped HTTP Method: PATCH"
                                    + "\n\t Mapped Roles: " + Arrays.asList(aResourceRoleMapping.getRoles()).toString()
                                    + "\n############################## OAUTH2 API Security Mapping - END ######################################\n\n";
                            break;
                        case "DELETE":
                            http.authorizeRequests()
                                    .antMatchers(HttpMethod.DELETE,
                                            aResourceRoleMapping.getEndpoint()) .hasAnyAuthority(allowedRoles);
                            finalLog = finalLog + "\n\n############################## OAUTH2 API Security Mapping - START ####################################"
                                    + "\n\t Mapped URL: "+aResourceRoleMapping.getEndpoint()
                                    + "\n\t Mapped HTTP Method: DELETE"
                                    + "\n\t Mapped Roles: " + Arrays.asList(aResourceRoleMapping.getRoles()).toString()
                                    + "\n############################## OAUTH2 API Security Mapping - END ######################################\n\n";
                            break;
                        default:
                            logger.error("No matching HTTP Method case found for - "+ aHTTPMethod);
                            break;
                    }
                }

                pathCounter++;
            }

            logger.warn(finalLog);
        }
        else
        {
            logger.error("NO RESOURCE SECURITY MAPPING DONE");
        }

        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/**").permitAll()
                .anyRequest().authenticated()
                .and().cors().disable().csrf().disable().httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .accessDeniedHandler(
                        (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED));
    }
}