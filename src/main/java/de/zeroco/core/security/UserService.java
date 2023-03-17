package de.zeroco.core.security;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.getUserByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("Could not find user");
		}
	        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),getAuthorities(user));
	}
	public Collection<? extends GrantedAuthority> getAuthorities(User user) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRoles());
        return Arrays.asList(authority);
    }

	 
}
