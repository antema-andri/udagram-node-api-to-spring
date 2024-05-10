package com.udagram.app.web;

import java.io.IOException;
import java.net.URISyntaxException;
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
import com.udagram.app.service.AccountUserService;
import com.udagram.app.service.FeedItemService;

@RestController
public class UdagramRestController {
	@Autowired
	private AccountUserService accountUserService;
	@Autowired
	private FeedItemService feedItemService;
	
	public UdagramRestController(AccountUserService accountUserService) {
		this.accountUserService = accountUserService;
	}
	
	/*
	 * User Authentication
	 */
	@RequestMapping(value="/users/auth/login",method=RequestMethod.POST)
	public ResponseEntity<?> authenticateUser(@RequestBody UserRequest loginRequest) {
		HashMap<String, Object> hashMapAuth = accountUserService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
		ResponseAuth responseAuth = new ResponseAuth((String) hashMapAuth.get("token"), (User) hashMapAuth.get("user"));
		return ResponseEntity.ok(responseAuth);
	}
	
	/*
	 * New user Registration and authentication
	 */
	@RequestMapping(value="/users/auth", method=RequestMethod.POST)
	public ResponseEntity<?> saveUser(@RequestBody UserRequest userRequest) {
		String errMsg = "User already exists";
		HashMap<String, Object> hashMapAuth = accountUserService.registerAuth(userRequest.getEmail(), userRequest.getPassword());
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
		Collection<FeedItem> feedItems = feedItemService.getList();
		ResponseFeeds responseFeeds = new ResponseFeeds(feedItems.size(), feedItems);
		return ResponseEntity.ok(responseFeeds);
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
		FeedItem newFeed = feedItemService.save(feedItemInfo);
		return ResponseEntity.ok(newFeed);
	}
	
	/*
	 * Get signed url to upload image
	 */
	@RequestMapping(path = "/feed/signed-url/{image_name}",  method=RequestMethod.GET)
	public ResponseEntity<?> getSignedUrlToUpload(@PathVariable(name="image_name") String image, HttpServletRequest request) {
		String signedUrl = feedItemService.getSignedUrl(image);
		System.out.println(signedUrl);
		return ResponseEntity.ok("{\"url\":\""+ signedUrl +"\"}");
	}
	
	/*
	 * Upload file
	 */
	@PutMapping(value="/upload/storage/{image_name}")
	public void upload(@PathVariable(name="image_name") String image, HttpServletRequest request) throws IOException {
		feedItemService.uploadImage(image, request.getInputStream());
	}
	
	/*
	 * Get the image url
	 */
	@GetMapping(path="/image/{image_name}", produces=MediaType.IMAGE_JPEG_VALUE)
	public byte[] getImageByName(@PathVariable(name="image_name") String image) throws IOException, URISyntaxException {
		return feedItemService.getImageByname(image);
	}
	
}
