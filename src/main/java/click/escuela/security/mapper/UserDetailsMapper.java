package click.escuela.security.mapper;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import click.escuela.security.model.User;

import java.util.HashSet;
import java.util.Set;

public class UserDetailsMapper {

	private UserDetailsMapper() {}
	public static UserDetails build(User user) {
		return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), getAuthorities(user));
	}

	private static Set<? extends GrantedAuthority> getAuthorities(User user) {

		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()));

		return authorities;
	}
}
