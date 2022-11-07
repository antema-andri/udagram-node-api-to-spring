package com.udagram.app.request;

import com.udagram.app.entities.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class ResponseAuth {
	private String token;
	private User user;
}
