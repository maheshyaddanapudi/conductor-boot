package com.github.my.netflix.conductorboot.db.repositories.embedded.oauth2;

import com.github.my.netflix.conductorboot.constants.Constants;
import com.github.my.netflix.conductorboot.db.entities.embedded.oauth2.OAuthRole;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

@Profile(Constants.EMBEDDED_OAUTH2)
public interface OAuthRoleRepository extends JpaRepository<OAuthRole, Integer> {
	
	OAuthRole findByName(String name);

}
