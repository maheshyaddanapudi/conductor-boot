package com.github.maheshyaddanapudi.netflix.conductorboot.service.embedded.oauth2;

import com.github.maheshyaddanapudi.netflix.conductorboot.constants.Constants;
import com.github.maheshyaddanapudi.netflix.conductorboot.db.entities.embedded.oauth2.User;
import com.github.maheshyaddanapudi.netflix.conductorboot.db.repositories.embedded.oauth2.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service(value = "userDetailsService")
@Profile(Constants.EMBEDDED_OAUTH2)
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String input) {
		User user = userRepository.findByUsername(input);

		if (user == null)
			throw new BadCredentialsException("Bad credentials");

		new AccountStatusUserDetailsChecker().check(user);

		return user;
	}
}
