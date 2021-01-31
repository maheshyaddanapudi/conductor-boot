package com.github.my.netflix.conductorboot.db.repositories.embedded.oauth2;

import javax.transaction.Transactional;

import com.github.my.netflix.conductorboot.constants.Constants;
import com.github.my.netflix.conductorboot.db.entities.embedded.oauth2.User;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Profile(Constants.EMBEDDED_OAUTH2)
@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);
	
	@Modifying
	@Query(value = "INSERT INTO oauth_user (username,password,email,client,enabled,account_expired,credentials_expired,account_locked) VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8)", nativeQuery = true)
	void insertNewUser(String username, String password, String email, String client, boolean enabled, boolean accountExpired, boolean credentials_expired, boolean account_locked);

	@Modifying
	@Query(value = "DELETE FROM oauth_user WHERE client = ?1", nativeQuery = true)
	void deleteUsersByClientId(String client);
	
	@Modifying
	@Query(value = "DELETE FROM oauth_role_user where user_id in (SELECT id FROM oauth_user where client = ?1)", nativeQuery = true)
	void deleteUserRoleMappingsByClientId(String clientId);
	
	@Modifying
	@Query(value = "DELETE FROM oauth_role_user where user_id ?1", nativeQuery = true)
	void deleteUserRoleMappingsByUserId(int userId);
	
	@Modifying
	@Query(value = "INSERT INTO oauth_role_user (role_id, user_id) VALUES ( ?1 , ?2 )", nativeQuery = true)
	void mapRoleToUser(int roleId, int userId);

	@Query(value="SELECT client FROM oauth_user WHERE id = ?1", nativeQuery=true)
	String getClientIdByUserId(int userId);

}


