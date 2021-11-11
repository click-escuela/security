package click.escuela.security.services;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import click.escuela.security.api.UserApi;
import click.escuela.security.exception.SchoolException;
import click.escuela.security.mapper.UserDetailsMapper;
import click.escuela.security.model.Role;
import click.escuela.security.model.School;
import click.escuela.security.model.User;
import click.escuela.security.model.repository.RoleRepository;
import click.escuela.security.model.repository.SchoolRepository;
import click.escuela.security.model.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SchoolRepository schoolRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) {

		return userRepository.findByUserName(username).map(UserDetailsMapper::build)
				.orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));

	}

	public User getUser(String id) {
		UUID uuid = UUID.fromString(id);
		return userRepository.findById(uuid).orElse(null);
	}

	public boolean existUser(String username) {
		return userRepository.findByUserName(username).isPresent();

	}

	public UserApi save(UserApi user) throws SchoolException {
		School school = schoolRepository.findById(UUID.fromString(user.getSchoolId())).map(p -> p)
				.orElseThrow(() -> new SchoolException("La escuela no existe"));

		String password = generatePass(user.getSurname());
		Role userRole = roleRepository.findByName(user.getRole());
		String userName = generateUser(user.getName(), user.getSurname());
		
		if(Boolean.TRUE.equals(userNoExist(user.getUserId()))) {
			User userToSave = new User();
			userToSave.setUserName(userName);
			userToSave.setPassword(bCryptPasswordEncoder.encode(password));
			userToSave.setRole(userRole);
			userToSave.setName(user.getName());
			userToSave.setEmail(user.getEmail());
			userToSave.setSchool(school);
			userToSave.setUserId(UUID.fromString(user.getUserId()));
			userRepository.save(userToSave);
			user.setPassword(password);
			user.setUserName(userName);
			return user;
		}
		else {
			return null;
		}
	}
	
	private Boolean userNoExist(String userId) {
		Boolean userNoExist = false;
		Optional<User> user = userRepository.findByUserId(UUID.fromString(userId));
		if(!user.isPresent()) {
			userNoExist = true;
		}
		return userNoExist;
	}

	private String generateUser(String name, String surname) {
		String username = name.substring(0, 1).concat(surname);
		boolean existUsername = true;
		Integer count = 0;
		existUsername = existUser(username);

		while (existUsername) {
			count++;
			existUsername = existUser(username.concat(count.toString()));
			username = username.concat(count.toString());

		}
		return username;
	}

	private String generatePass(String surname) {
		Integer numero = ThreadLocalRandom.current().nextInt(100, 998 + 1);
		return surname.concat(numero.toString());
	}

}
