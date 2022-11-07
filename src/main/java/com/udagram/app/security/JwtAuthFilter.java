package com.udagram.app.security;

import java.io.IOException;
import java.time.LocalDate;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.udagram.app.request.UserRequest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtAuthFilter extends UsernamePasswordAuthenticationFilter {
	private final AuthenticationManager authenticationManager;
	
	public JwtAuthFilter(AuthenticationManager auth) {
		this.authenticationManager = auth;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse resp) throws AuthenticationException {
		System.out.println("*************************");
		ObjectMapper objectMapper= new ObjectMapper();
		
		try {
			UserRequest reqUser = objectMapper.readValue(req.getInputStream(), UserRequest.class);
			System.out.println(reqUser.getEmail());
			System.out.println(reqUser.getPassword());
			UsernamePasswordAuthenticationToken upa = new UsernamePasswordAuthenticationToken(reqUser.getEmail(), reqUser.getPassword());
			return authenticationManager.authenticate(upa);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, 
											HttpServletResponse response, 
											FilterChain chain,
											Authentication authResult) throws IOException, ServletException {
		String token = Jwts.builder()
			.setSubject(authResult.getName())
			.claim("authorities", authResult.getAuthorities())
			.setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(2)))
			.signWith(SignatureAlgorithm.HS256, Constant.SECRET)
			.compact();		
		response.addHeader(Constant.JWT_HEADER_NAME, Constant.HEADER_PREFIX+token);
	}
	
}
