package com.github.maheshyaddanapudi.netflix.conductorboot.db.repositories.embedded.oauth2;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import com.github.maheshyaddanapudi.netflix.conductorboot.constants.Constants;
import com.github.maheshyaddanapudi.netflix.conductorboot.db.entities.embedded.oauth2.OAuthRole;

@Profile(Constants.EMBEDDED_OAUTH2)
public interface OAuthRoleRepository extends JpaRepository<OAuthRole, Integer> {
	
	OAuthRole findByName(String name);

}
