package click.escuela.security.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import click.escuela.security.services.UserService;

@Component
public class UserGenerator {

	@Autowired
	private UserService userService;
	
	public String generateUser(String name, String surname) {
		String username = name.substring(0, 1).concat(surname);
		boolean existUsername = false;
		
		while(!existUsername) {
			existUsername = userService.existUser(username);
		}
		
		return username;
	}
}
