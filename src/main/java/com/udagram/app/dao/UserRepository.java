package com.udagram.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.udagram.app.entities.User;

@RepositoryRestResource(path = "user")
public interface UserRepository extends JpaRepository<User, Long>{
	public User findUserByEmail(String email);
}
