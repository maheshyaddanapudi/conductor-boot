package com.github.maheshyaddanapudi.netflix.conductorboot.config.embedded.oauth2;

import com.github.maheshyaddanapudi.netflix.conductorboot.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.Map;

@Profile(Constants.EMBEDDED_OAUTH2)
public class CustomOauth2RequestFactory extends DefaultOAuth2RequestFactory {
	
	@Autowired
	private TokenStore tokenStore;
	
	@Autowired
	private UserDetailsService userDetailsService;

	public CustomOauth2RequestFactory(ClientDetailsService clientDetailsService) {
		super(clientDetailsService);
	}


	@Override
	public TokenRequest createTokenRequest(Map<String, String> requestParameters,
			ClientDetails authenticatedClient) {
		if (requestParameters.get(Constants.GRANT_TYPE).equals(Constants.REFRESH_TOKEN)) {
			OAuth2Authentication authentication = tokenStore.readAuthenticationForRefreshToken(
					tokenStore.readRefreshToken(requestParameters.get(Constants.REFRESH_TOKEN)));
			SecurityContextHolder.getContext()
					.setAuthentication(new UsernamePasswordAuthenticationToken(authentication.getName(), null,
							userDetailsService.loadUserByUsername(authentication.getName()).getAuthorities()));
		}
		return super.createTokenRequest(requestParameters, authenticatedClient);
	}
}
