package click.escuela.security.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import click.escuela.security.mapper.UserDetailsMapper;
import click.escuela.security.model.Role;
import click.escuela.security.model.User;
import click.escuela.security.model.repository.RoleRepository;
import click.escuela.security.model.repository.UserRepository;


@Service
public class UserService implements UserDetailsService{

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) {
		
		final User user = userRepository.findByName(username);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password");
		}

		return UserDetailsMapper.build(user);
	}
	
	
	public User getUser(String id) {
		UUID uuid = UUID.fromString(id);
		return userRepository.findById(uuid).get();
	}

	
	public User save(User user) {
		Role userRole = roleRepository.findByName(user.getRole().getName());

		User userToSave = User.builder().name(user.getName()).password(user.getPassword()).role(userRole).build();

		return userRepository.save(userToSave);
	}

}
