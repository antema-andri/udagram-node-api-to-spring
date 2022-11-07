package com.udagram.app.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Collection;


import com.udagram.app.entities.FeedItem;

public interface FeedItemService {
	Collection<FeedItem> getList();
	FeedItem save(FeedItem feedItem);
	
	String getSignedUrl(String image);
	void uploadImage(String image, InputStream inputStream)  throws IOException;
	byte[] getImageByname(String image)  throws IOException, URISyntaxException;
}
