package com.udagram.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.udagram.app.dao.UserRepository;
import com.udagram.app.entities.User;

@SpringBootApplication
public class UdagramApplication implements CommandLineRunner{
	@Autowired
	UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(UdagramApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		userRepository.save(new User(null, "admin@gmail.com", new BCryptPasswordEncoder().encode("123"), null, null));
	}

}
