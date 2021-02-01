package com.github.maheshyaddanapudi.netflix.conductorboot.service.external.adfs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.maheshyaddanapudi.netflix.conductorboot.constants.Constants;
import com.github.maheshyaddanapudi.netflix.conductorboot.dtos.internal.external.adfs.ADFSToken;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedAuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedPrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.util.Assert;

import java.util.*;

@Profile(Constants.EXTERNAL_ADFS)
public class AdfsUserInfoTokenServices implements ResourceServerTokenServices {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final String userInfoEndpointUrl;
    private final String clientId;
    private String tokenType = DefaultOAuth2AccessToken.BEARER_TYPE;
    private AuthoritiesExtractor authoritiesExtractor = new FixedAuthoritiesExtractor();
    private PrincipalExtractor principalExtractor = new FixedPrincipalExtractor();

    public AdfsUserInfoTokenServices(String userInfoEndpointUrl, String clientId) {
        this.userInfoEndpointUrl = userInfoEndpointUrl;
        this.clientId = clientId;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public void setAuthoritiesExtractor(AuthoritiesExtractor authoritiesExtractor) {
        Assert.notNull(authoritiesExtractor, "AuthoritiesExtractor must not be null");
        this.authoritiesExtractor = authoritiesExtractor;
    }

    public void setPrincipalExtractor(PrincipalExtractor principalExtractor) {
        Assert.notNull(principalExtractor, "PrincipalExtractor must not be null");
        this.principalExtractor = principalExtractor;
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken)
            throws AuthenticationException, InvalidTokenException {
        Map<String, Object> map = getMap(this.userInfoEndpointUrl, accessToken);
        if (map.containsKey("error")) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("userinfo returned error: " + map.get("error"));
            }
            throw new InvalidTokenException(accessToken);
        }
        return extractAuthentication(map);
    }

    private OAuth2Authentication extractAuthentication(Map<String, Object> map) {
        Object principal = getPrincipal(map);
        List<GrantedAuthority> authorities = this.authoritiesExtractor.extractAuthorities(map);

        try{
            Gson gson = new Gson();
            String mapDetails = gson.toJson(map);
            ADFSToken adfsToken = gson.fromJson(mapDetails, ADFSToken.class);
            if(null!=adfsToken.getRole())
            {
                List<String> rolesList = new ArrayList<String>();
                rolesList = Arrays.asList(adfsToken.getRole());
                for(String aRole: rolesList)
                {
                    authorities.add(new SimpleGrantedAuthority(aRole));
                }
            }
        }
        catch(Exception ex)
        {
            logger.warn("Exception while decoding ADFS Token : \n"+ex.getMessage());
        }

        OAuth2Request request = new OAuth2Request(
                (Map)null, this.clientId, (Collection)null, true,
                (Set)null, (Set)null, (String)null, (Set)null, (Map)null);
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(principal, "N/A", authorities);
        token.setDetails(map);
        return new OAuth2Authentication(request, token);
    }

    protected Object getPrincipal(Map<String, Object> map) {
        Object principal = this.principalExtractor.extractPrincipal(map);
        return (principal == null ? "unknown" : principal);
    }

    public OAuth2AccessToken readAccessToken(String accessToken) {
        throw new UnsupportedOperationException("Not supported: read access token");
    }

    /**
     * Get map is a custom implementation and extracts the OAuth2 Access
     * information for the JWS token returned by ADSF
     * @param path
     * @param accessToken
     * @return
     */
    private Map<String, Object> getMap(String path, String accessToken) {
        this.logger.debug("Getting user info from: " + path);

        try {
            DefaultOAuth2AccessToken oauthToken = new DefaultOAuth2AccessToken(accessToken);
            oauthToken.setTokenType(this.tokenType);

            logger.debug("Oauth Token: " + oauthToken.getValue());

            // the token is made up of 3 parts; header, payload and signature
            // grab the payload of the token that is encoded
            String encodedPayload = oauthToken.getValue().split("\\.")[1];

            logger.debug("Token: Encoded Payload: " + encodedPayload);
            String jsonPayload = new String(Base64.getDecoder().decode(encodedPayload.getBytes()));
            logger.debug("JSON Token Payload: " + jsonPayload);

            ObjectMapper mapper = new ObjectMapper();

            return mapper.readValue(jsonPayload, new TypeReference<Map<String, Object>>(){});

        } catch (Exception ex) {
            this.logger.warn("Could not fetch user details: " + ex.getClass() + ", " + ex.getMessage());
            return Collections.<String, Object>singletonMap("error", "Could not fetch user details");
        }
    }
}