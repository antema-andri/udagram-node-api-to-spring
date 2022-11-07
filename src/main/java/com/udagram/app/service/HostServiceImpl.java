package com.udagram.app.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HostServiceImpl implements HostService{
	@Value("${server.servlet.context-path}")
	private String contextPath;
	@Value("${server.address}")
	private String serverAdress;
	@Value("${server.port}")
	private Integer serverPort;
	
	@Override
	public String getHost() {
		String baseUrl = "http://" + serverAdress + ":" + serverPort;
		return baseUrl.replace(" ", "");
	}

	@Override
	public String getHostWithContextPath() {
		return getHost() + contextPath;
	}

	@Override
	public String getImageUrl(String imageName) {
		return getHostWithContextPath() + "/image/"+ imageName;
	}

	@Override
	public String pathUploadFile() {
		String sl = File.separator;
		String rep = "src/main/resources/static/s3/";
		return rep.replace("/", sl);
	}

	@Override
	public String getUrl(String url) {
		return getHostWithContextPath() + url;
	}
	
}
