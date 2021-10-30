package click.escuela.security.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import click.escuela.security.api.AuthorizationApi;
import click.escuela.security.mapper.UserMapper;
import click.escuela.security.model.User;
import click.escuela.security.services.EmailService;
import click.escuela.security.services.UserService;

@Controller
@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailService emailService;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<User> getUser(@PathVariable String id) {
		final User user = userService.getUser(id);

		if (user == null) {
			return ResponseEntity.notFound().build();
		}

		//UserResponse userResponse = UserMapper.toResponse(user);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<User> saveUser(@RequestBody AuthorizationApi userRequest) {
		userRequest.setPassword(bCryptPasswordEncoder.encode(userRequest.getPassword()));
		final User userToSave = userService.save(UserMapper.toDomain(userRequest));
		//emailService.sendEmail(userRequest.getPassword(), userRequest.getUserName(), email, schoolId);
		return new ResponseEntity<>(userToSave, HttpStatus.OK);
	}
	
}
