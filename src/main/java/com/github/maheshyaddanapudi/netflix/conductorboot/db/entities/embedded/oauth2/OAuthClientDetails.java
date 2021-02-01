package com.github.maheshyaddanapudi.netflix.conductorboot.db.entities.embedded.oauth2;

import com.github.maheshyaddanapudi.netflix.conductorboot.constants.Constants;
import org.springframework.context.annotation.Profile;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="oauth_client_details")
@Profile(Constants.EMBEDDED_OAUTH2)
public class OAuthClientDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private int id;

	@Column(name = "client_id", nullable = false, updatable = false,unique = true)
	private String clientId;
	
	@Column(name = "client_secret", nullable = false)
	private String clientSecret;
	
	@Column(name = "resource_ids", nullable = false)
	private String resourceIds;
	
	@Column(name = "scope", nullable = false)
	private String scope;
	
	@Column(name = "authorized_grant_types", nullable = true)
	private String authorizedGrantTypes;
	
	@Column(name = "web_server_redirect_uri", nullable = true)
	private String webServerRedirectUri;
	
	@Column(name = "authorities", nullable = false)
	private String authorities;
	
	@Column(name = "access_token_validity", nullable = false)
	private int accessTokenValidityInSeconds;
	
	@Column(name = "refresh_token_validity", nullable = false)
	private int refreshTokenValidityInSeconds;
	
	@Column(name = "additional_information", nullable = false)
	private String additionalInformation;
	
	@Column(name = "autoapprove", nullable = false)
	private String autoApprove;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "insert_timestamp", nullable = false)
	private Date insertTimestamp;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_timestamp", nullable = false)
	private Date updateTimestamp;
	
	@PrePersist
	protected void onCreate() {
		
		this.resourceIds = Constants.resourceIds;
		this.authorizedGrantTypes = Constants.authorizedGrantTypes;
		this.additionalInformation = "{}";
		this.autoApprove = null;
		
		updateTimestamp = insertTimestamp = new Date();
	}
	
	@PreUpdate
	protected void onUpdate() {
		updateTimestamp = new Date();
	}

	public OAuthClientDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getResourceIds() {
		return resourceIds;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getAuthorizedGrantTypes() {
		return authorizedGrantTypes;
	}

	public String getWebServerRedirectUri() {
		return webServerRedirectUri;
	}

	public void setWebServerRedirectUri(String webServerRedirectUri) {
		this.webServerRedirectUri = webServerRedirectUri;
	}

	public String getAuthorities() {
		return authorities;
	}

	public void setAuthorities(String authorities) {
		this.authorities = authorities;
	}

	public int getAccessTokenValidityInSeconds() {
		return accessTokenValidityInSeconds;
	}

	public void setAccessTokenValidityInSeconds(int accessTokenValidityInSeconds) {
		this.accessTokenValidityInSeconds = accessTokenValidityInSeconds;
	}

	public int getRefreshTokenValidityInSeconds() {
		return refreshTokenValidityInSeconds;
	}

	public void setRefreshTokenValidityInSeconds(int refreshTokenValidityInSeconds) {
		this.refreshTokenValidityInSeconds = refreshTokenValidityInSeconds;
	}

	public String getAdditionalInformation() {
		return additionalInformation;
	}

	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}

	public String getAutoApprove() {
		return autoApprove;
	}

	public void setAutoApprove(String autoApprove) {
		this.autoApprove = autoApprove;
	}

	public Date getInsertTimestamp() {
		return insertTimestamp;
	}

	public void setInsertTimestamp(Date insertTimestamp) {
		this.insertTimestamp = insertTimestamp;
	}

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "OAuthClientDetails [clientId=" + clientId + ", clientSecret=" + clientSecret + ", resourceIds="
				+ resourceIds + ", scope=" + scope + ", authorizedGrantTypes=" + authorizedGrantTypes
				+ ", webServerRedirectUri=" + webServerRedirectUri + ", authorities=" + authorities
				+ ", accessTokenValidityInSeconds=" + accessTokenValidityInSeconds + ", refreshTokenValidityInSeconds="
				+ refreshTokenValidityInSeconds + ", additionalInformation=" + additionalInformation + ", autoApprove="
				+ autoApprove + ", insertTimestamp=" + insertTimestamp + ", updateTimestamp=" + updateTimestamp + "]";
	}
}
