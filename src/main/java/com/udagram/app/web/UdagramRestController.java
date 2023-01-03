package com.udagram.app.web;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.udagram.app.entities.FeedItem;
import com.udagram.app.entities.User;
import com.udagram.app.request.FeedRequest;
import com.udagram.app.request.ResponseAuth;
import com.udagram.app.request.ResponseFeeds;
import com.udagram.app.request.UserRequest;
import com.udagram.app.service.AccountUserServiceImpl;
import com.udagram.app.service.FeedItemServiceImpl;

@RestController
public class UdagramRestController {
	@Autowired
	private AccountUserServiceImpl accountUserServiceImpl;
	@Autowired
	private FeedItemServiceImpl feedItemServiceImpl;
	
	public UdagramRestController(AccountUserServiceImpl accountUserServiceImpl) {
		this.accountUserServiceImpl = accountUserServiceImpl;
	}
	
	/*
	 * User Authentication
	 */
	@RequestMapping(value="/users/auth/login",method=RequestMethod.POST)
	public ResponseEntity<?> authenticateUser(@RequestBody UserRequest loginRequest) {
		HashMap<String, Object> hashMapAuth = accountUserServiceImpl.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
		ResponseAuth responseAuth = new ResponseAuth((String) hashMapAuth.get("token"), (User) hashMapAuth.get("user"));
		return ResponseEntity.ok(responseAuth);
	}
	
	/*
	 * New user Registration and authentication
	 */
	@RequestMapping(value="/users/auth", method=RequestMethod.POST)
	public ResponseEntity<?> saveUser(@RequestBody UserRequest userRequest) {
		String errMsg = "User already exists";
		HashMap<String, Object> hashMapAuth = accountUserServiceImpl.registerAuth(userRequest.getEmail(), userRequest.getPassword());
		if(hashMapAuth != null) {
			ResponseAuth responseAuth = new ResponseAuth((String) hashMapAuth.get("token"), (User) hashMapAuth.get("user"));
			return ResponseEntity.ok(responseAuth);
		}
		return ResponseEntity.status(422).body("{\"auth\":\""+false+"\", \"message\":\""+errMsg+"\"}");
	}
	
	/*
	 * feedItem list
	 */
	@GetMapping(value = "/feed")
	public ResponseEntity<?> getAllUsers() {
		return feedItemServiceImpl.getAllUsers();
	}
	
	/*
	 * Create new feed
	 */
	@PostMapping(value = "/feed")
	public ResponseEntity<?> saveFeeItem(@RequestBody FeedRequest feedRequest, HttpServletRequest httpServletRequest) {
		if(httpServletRequest.getHeader("authorization") == null) {
			String msg = "No authorization headers.";
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\""+msg+"\"}");
		}
		
		FeedItem feedItemInfo = new FeedItem(null, feedRequest.getCaption(), feedRequest.getUrl(), null, null);
		FeedItem newFeed = feedItemServiceImpl.save(feedItemInfo);
		return ResponseEntity.ok(newFeed);
	}
	
	/*
	 * Get signed url to upload image
	 */
	@RequestMapping(path = "/feed/signed-url/{image_name}",  method=RequestMethod.GET)
	public ResponseEntity<?> getSignedUrlToUpload(@PathVariable(name="image_name") String image, HttpServletRequest request) {
		String signedUrl = feedItemServiceImpl.getSignedUrl(image);
		System.out.println(signedUrl);
		return ResponseEntity.ok("{\"url\":\""+ signedUrl +"\"}");
	}
	
	/*
	 * Upload file
	 */
	@PutMapping(value="/upload/storage/{image_name}")
	public void upload(@PathVariable(name="image_name") String image, HttpServletRequest request) {
		try {
			feedItemServiceImpl.uploadImage(image, request.getInputStream());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/*
	 * Get the image url
	 */
	@GetMapping(path="/image/{image_name}", produces=MediaType.IMAGE_JPEG_VALUE)
	public byte[] getImageByName(@PathVariable(name="image_name") String image) {
		return feedItemServiceImpl.getImageByname(image);
	}
	
}
