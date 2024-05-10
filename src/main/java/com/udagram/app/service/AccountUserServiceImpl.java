package com.udagram.app.service;

import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udagram.app.dao.UserRepository;
import com.udagram.app.entities.User;
import com.udagram.app.request.ResponseAuth;
import com.udagram.app.security.Constant;
import com.udagram.app.security.UserDetailsServiceImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Transactional
public class AccountUserServiceImpl implements AccountUserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserDetailsService userDetailsService;
	
	/*
	 * return new user if note exist
	 * and null if user already exist
	 */
	@Override
	public User createUser(User user) {
		User userExist = userRepository.findUserByEmail(user.getEmail());
		if(userExist!=null) return null;
		String hashedPassword = new BCryptPasswordEncoder().encode(user.getPasswordHash());
		user.setPasswordHash(hashedPassword);
		return userRepository.save(user);
	}
	
	public HashMap<String, Object> authenticate(String login, String password) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetails userDetails = userDetailsService.loadUserByUsername(login);
		String token=Jwts.builder()
				.setSubject(userDetails.getUsername())
				.claim("authorities", userDetails.getAuthorities())
				.setExpiration(new Date(System.currentTimeMillis()+864_000_000)) // 864_000_000 <=> 10days
				.signWith(SignatureAlgorithm.HS512,  Constant.SECRET)
				.compact();
		token = Constant.HEADER_PREFIX + token;
		HashMap<String, Object> hashMapAuh = new HashMap<String, Object>();
		hashMapAuh.put("user", new User(null, userDetails.getUsername(), null, null, null));
		hashMapAuh.put("token", token);
		return hashMapAuh;
	}
	
	/*
	 * Registration and authentication
	 * return hashmap if new user and authenticate
	 * and null if the user already exist
	 */
	public HashMap<String, Object> registerAuth(String login, String password) {
		User newUser = createUser(new User(null, login, password, new Date(), new Date()));
		if(newUser != null) {
			//Authenticate the new user
			HashMap<String, Object> hashMapAuth = authenticate(newUser.getEmail(), password);
			return hashMapAuth;
		}
		return null;
	}
	
}
