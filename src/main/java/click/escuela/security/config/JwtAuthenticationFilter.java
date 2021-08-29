package click.escuela.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;


import click.escuela.security.api.AuthorizationApi;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import click.escuela.security.utils.TokenProvider;
import static click.escuela.security.utils.Constants.HEADER_AUTHORIZATION_KEY;
import static click.escuela.security.utils.Constants.TOKEN_BEARER_PREFIX;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
		super.setAuthenticationFailureHandler(new JwtAuthenticationFailureHandler());
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			AuthorizationApi userCredentials = new ObjectMapper().readValue(request.getInputStream(), AuthorizationApi.class);

			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					userCredentials.getUserName(), userCredentials.getPassword()));
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		String token = TokenProvider.generateToken(authResult);
		response.addHeader(HEADER_AUTHORIZATION_KEY, TOKEN_BEARER_PREFIX + " " + token);
	}
}
