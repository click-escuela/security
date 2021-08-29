package click.escuela.security.services;

import java.util.List;
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
		List<Role> roles = roleRepository.findAll();
		if(roles.isEmpty()) {
			createRoles();
		}
		if(userRepository.findAll().isEmpty()) {
			createUsers();
		}
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
	
	public void createRoles() {
		Role role = new Role(UUID.randomUUID(),"ADMIN","administrador");
		roleRepository.save(role);
		
		role = new Role(UUID.randomUUID(),"TEACHER","teacher");
		roleRepository.save(role);

		role = new Role(UUID.randomUUID(),"STUDENT","student");
		roleRepository.save(role);

		role = new Role(UUID.randomUUID(),"PARENT","parent");
		roleRepository.save(role);
	}
	
	public void createUsers() {
		Role roleAdmin = roleRepository.findByName("ADMIN");
		Role roleTeacher = roleRepository.findByName("TEACHER");
		Role roleStudent = roleRepository.findByName("STUDENT");

		User user = User.builder().id(UUID.randomUUID())
				.name("admin")
				.password("$2a$04$GJ85Ihcglhbqac2zc3z3A.C3v55FMmN8.qGQ8FlNBCuyLtQ5/TyMO")
				.role(roleAdmin)
				.build();
		
		userRepository.save(user);
		
		 user = User.builder().id(UUID.randomUUID())
					.name("teacher")
					.password("$2a$04$XWpgKkCQaVRuXjB5f1hzt.pDa2NAzntroH3bELICZy8R8Q0L0SShO")
					.role(roleTeacher)
					.build();
		 
		 userRepository.save(user);
		 
		 user = User.builder().id(UUID.randomUUID())
					.name("student")
					.password("$2a$04$bmmWXec6YkFIesIxv2gx7.NbxRcMJ4UoRKT9Qoqk09pKegfcr3Zla")
					.role(roleStudent)
					.build();
		 
		 userRepository.save(user);

		 
	}

}
