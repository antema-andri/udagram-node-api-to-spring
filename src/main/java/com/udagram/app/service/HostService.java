package com.udagram.app.service;

public interface HostService {
	String getHost();
	String getHostWithContextPath();
	String getImageUrl(String imageName);
	String pathUploadFile();
	String getUrl(String url);
}
