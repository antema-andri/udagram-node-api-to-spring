package com.udagram.app.request;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class FeedRequest {
	private String caption;
	private String url;
	private Date createdAt;
    private Date updatedAt;
}
