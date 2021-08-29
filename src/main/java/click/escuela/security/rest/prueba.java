package click.escuela.security.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class prueba {

	
	@GetMapping(value = "/prueba")
	public ResponseEntity<String> prueba(){
		return ResponseEntity.status(HttpStatus.OK).body("llego");

	}
}
