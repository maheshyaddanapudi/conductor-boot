package com.github.maheshyaddanapudi.netflix.conductorboot.config.embedded.oauth2;

import com.github.maheshyaddanapudi.netflix.conductorboot.constants.Constants;
import com.github.maheshyaddanapudi.netflix.conductorboot.db.entities.embedded.oauth2.User;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.LinkedHashMap;
import java.util.Map;

@Profile(Constants.EMBEDDED_OAUTH2)
public class CustomTokenEnhancer extends JwtAccessTokenConverter {
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		User user = (User) authentication.getPrincipal();

		Map<String, Object> info = new LinkedHashMap<String, Object>(accessToken.getAdditionalInformation());

		info.put(Constants.EMAIL, user.getEmail());

		DefaultOAuth2AccessToken customAccessToken = new DefaultOAuth2AccessToken(accessToken);
		customAccessToken.setAdditionalInformation(info);

		return super.enhance(customAccessToken, authentication);
	}
}
