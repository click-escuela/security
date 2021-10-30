package click.escuela.security.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserApi {

	private String name;
	private String surname;
	private String schoolId;
	private Role role;
}
