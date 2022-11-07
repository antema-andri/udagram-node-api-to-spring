package com.udagram.app.request;

import java.util.Collection;

import com.udagram.app.entities.FeedItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class ResponseFeeds {
	private Integer count;
	private Collection<FeedItem> rows;
}
