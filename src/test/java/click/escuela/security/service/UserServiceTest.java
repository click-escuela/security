package click.escuela.security.service;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import click.escuela.security.api.UserApi;
import click.escuela.security.exception.SchoolException;
import click.escuela.security.model.Role;
import click.escuela.security.model.School;
import click.escuela.security.model.User;
import click.escuela.security.model.repository.RoleRepository;
import click.escuela.security.model.repository.SchoolRepository;
import click.escuela.security.model.repository.UserRepository;
import click.escuela.security.services.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
	
	@Mock
	private SchoolRepository schoolRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private RoleRepository roleRepository;
	
	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;


	private UserService userService = new UserService();
	private UserApi userApi;
	private UUID userId;
	private UUID schoolId;
	private Role userRole = new Role();

	@Before
	public void setUp() {
		schoolId = UUID.randomUUID();
		userId = UUID.randomUUID();
		userApi = UserApi.builder().userId(UUID.randomUUID().toString()).userName("OscarGomez").email("oscar@gmail.com")
				.name("Oscar").password("Oscar2020").surname("gomez").schoolId(schoolId.toString())
				.role("STUDENT").build();
		School school = new School();
		school.setId(UUID.fromString(userApi.getSchoolId()));
		Optional<School> optionalSchool = Optional.of(school);
		userRole.setDescription("Role");
		userRole.setId(UUID.randomUUID());
		userRole.setName(userApi.getRole());
		
		when(roleRepository.findByName(userApi.getRole())).thenReturn(userRole);
		when(schoolRepository.findById(UUID.fromString(userApi.getSchoolId()))).thenReturn(optionalSchool);
		ReflectionTestUtils.setField(userService, "schoolRepository", schoolRepository);
		ReflectionTestUtils.setField(userService, "userRepository", userRepository);
		ReflectionTestUtils.setField(userService, "roleRepository", roleRepository);
		ReflectionTestUtils.setField(userService, "bCryptPasswordEncoder", bCryptPasswordEncoder);

	}
	
	@Test
	public void whenSaveOk() throws SchoolException {
		userService.save(userApi);
		verify(userRepository).save(Mockito.any());
	}
	
	@Test
	public void whenSaveOkButUserExist() throws SchoolException {
		User user = new User();
		Optional<User> optional = Optional.of(user);
		userApi.setUserId(userId.toString());
		when(userRepository.findByUserId(UUID.fromString(userApi.getUserId()))).thenReturn(optional);	
		UserApi nullUser= userService.save(userApi);
		assertThat(nullUser).isNull();
	}

	@Test
	public void whenSaveOkButUserNameExist() throws SchoolException {
		userApi.setUserName("Ogomez");
		User user = new User();
		Optional<User> optional = Optional.of(user);
		when(userRepository.findByUserName(userApi.getUserName())).thenReturn(optional);
		userService.save(userApi);
		verify(userRepository).save(Mockito.any());
	}
	
	@Test
	public void whenSaveError() throws SchoolException {
		userApi.setSchoolId(UUID.randomUUID().toString());
		assertThatExceptionOfType(SchoolException.class).isThrownBy(() -> {
			 userService.save(userApi);
		}).withMessage("La escuela no existe");
	}
	
	@Test
	public void whenGetUserIsOk() {
		UUID id = UUID.randomUUID();
		User user = new User();
		user.setId(id);
		Optional<User> optional = Optional.of(user);
		when(userRepository.findById(id)).thenReturn(optional);
		userService.getUser(id.toString());
		verify(userRepository).findById(id);
	}
	
	
	@Test
	public void whenLoadByUserIsOk() {
		User user = new User();
		School school = new School();
		school.setId(schoolId);
		user.setName(userApi.getName());
		user.setId(UUID.randomUUID());
		user.setSchool(school);
		user.setUserName(userApi.getUserName());
		user.setRole(userRole);
		user.setPassword(userApi.getPassword());
		Optional<User> optional = Optional.of(user);
		when(userRepository.findByUserName(userApi.getUserName())).thenReturn(optional);
		userService.loadUserByUsername(userApi.getUserName());
		verify(userRepository).findByUserName(userApi.getUserName());
		
		userApi.setUserName("Cualquiera");
		boolean hasError = false;
		try {
			userService.loadUserByUsername(userApi.getUserName());
		} catch(UsernameNotFoundException e) {
			hasError = true;
		}
		assertThat(hasError).isTrue();
	}
}
