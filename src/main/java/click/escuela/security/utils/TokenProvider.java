package click.escuela.security.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import static click.escuela.security.utils.Constants.*;

public class TokenProvider {

	private TokenProvider() {
	}

	public static JwtBuilder generateToken(Authentication authentication) {
		// Genera el token con roles, issuer, fecha, expiración (8h)
		SecretKey key = Keys.hmacShaKeyFor(Base64.getEncoder().encode(SIGNING_KEY.getBytes()));
		
		final String authorities = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		return Jwts.builder()
				.setSubject(authentication.getName())
				.claim(AUTHORITIES_KEY, authorities)
				.signWith(key,SignatureAlgorithm.HS256)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setIssuer(ISSUER_TOKEN)
				.setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS*1000));
	}

	public static UsernamePasswordAuthenticationToken getAuthentication(final String token,
			final UserDetails userDetails) {
		SecretKey key = Keys.hmacShaKeyFor(Base64.getEncoder().encode(SIGNING_KEY.getBytes()));
		final JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(key).build();

		final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);

		final Claims claims = claimsJws.getBody();

		final Collection<SimpleGrantedAuthority> authorities =
				Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
						.map(SimpleGrantedAuthority::new)
						.collect(Collectors.toList());

		return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
	}

	public static String getUserName(final String token) {
		SecretKey key = Keys.hmacShaKeyFor(Base64.getEncoder().encode(SIGNING_KEY.getBytes()));

		final JwtParser jwtParser =  Jwts.parserBuilder().setSigningKey(key).build();

		final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);

		return claimsJws.getBody().getSubject();
	}

}
