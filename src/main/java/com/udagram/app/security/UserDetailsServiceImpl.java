package com.udagram.app.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.udagram.app.dao.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		com.udagram.app.entities.User userFromDb = userRepository.findUserByEmail(username);
		if(userFromDb==null) throw new UsernameNotFoundException("user not found");
		String email = userFromDb.getEmail();
		String passWord =  userFromDb.getPasswordHash();
		Collection<GrantedAuthority> authorities=new ArrayList<>();  
        authorities.add(new SimpleGrantedAuthority(Constant.ROLE_LIST.get(0)));        
		UserDetails userDetails = User.builder().username(email).password(passWord).authorities(authorities).build();
		return userDetails;
	}

}
