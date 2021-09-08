package click.escuela.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;


import click.escuela.security.api.AuthorizationApi;
import click.escuela.security.api.dtos.ResponseLogin;

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
import java.time.LocalDateTime;

import click.escuela.security.utils.TokenProvider;
import io.jsonwebtoken.JwtBuilder;

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
		String role = authResult.getAuthorities().toString();
		JwtBuilder token = TokenProvider.generateToken(authResult);

		ObjectMapper objectMapper = new ObjectMapper();
		
		ResponseLogin responseLogin = new ResponseLogin(token.compact(), role, LocalDateTime.now());
		String responseJson = objectMapper.writeValueAsString(responseLogin);

		response.getWriter().write(responseJson);
	}
}
