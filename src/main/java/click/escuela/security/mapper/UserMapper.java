package click.escuela.security.mapper;

import click.escuela.security.api.AuthorizationApi;
import click.escuela.security.model.User;

public class UserMapper {

	private UserMapper() {
	}

/*	public static UserResponse toResponse(User user) {
		return UserResponse.builder().name(user.getName()).id(user.getId()).build();
	}*/

	public static User toDomain(AuthorizationApi authorizationRequest) {
		return User.builder().name(authorizationRequest.getUserName()).password(authorizationRequest.getPassword())
				.build();
	}
}
