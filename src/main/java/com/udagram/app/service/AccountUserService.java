package com.udagram.app.service;

import java.util.HashMap;

import com.udagram.app.entities.User;

public interface AccountUserService {
	User createUser(User user);
	HashMap<String, Object> authenticate(String login, String password);
	HashMap<String, Object> registerAuth(String login, String password);
}
