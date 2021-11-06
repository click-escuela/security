package click.escuela.security.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import click.escuela.security.api.UserApi;
import click.escuela.security.exception.SchoolException;
import click.escuela.security.model.User;
import click.escuela.security.services.UserService;

@Controller
@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

	@Autowired
	private UserService userService;
	

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<User> getUser(@PathVariable String id) {
		final User user = userService.getUser(id);

		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<UserApi> saveUser(@RequestBody UserApi userApi) throws SchoolException {
		final UserApi userToSave = userService.save(userApi);
		return new ResponseEntity<>(userToSave, HttpStatus.OK);
	}
	
}
