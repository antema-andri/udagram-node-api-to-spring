package com.udagram.app.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.udagram.app.dao.FeedItemRepository;
import com.udagram.app.entities.FeedItem;
import com.udagram.app.exceptions.FileStorageException;

@Service
public class FeedItemServiceImpl implements FeedItemService{
	@Autowired
	private FeedItemRepository feedItemRepository;
	@Autowired
	private HostService hostService;
	
	private static final Logger logger = LogManager.getLogger(FeedItemServiceImpl.class);
	
	@Override
	public Collection<FeedItem> getList() {
		return IteratorUtils.toList(feedItemRepository.findAll().iterator());
	}
	
	@Override
	public FeedItem save(FeedItem feedItemInfo) {
		FeedItem feedItem =new FeedItem();
		feedItem.setCaption(feedItemInfo.getCaption());
		String imgName = feedItemInfo.getUrl();
		feedItem.setUrl(hostService.getImageUrl(imgName));
		feedItem.setCreatedAt(new Date());
		feedItem.setUpdatedAt(new Date());
		return feedItemRepository.save(feedItem);
	}
	
	public String getSignedUrl(String imageName) {
		String signedUrl = hostService.getUrl("/upload/storage/"+imageName);
		return signedUrl.replace(" ", "");
	}
	
	@Override
	public void uploadImage(String imageName, InputStream fileInputStream) {
		try {
			String pathFile = hostService.pathUploadFile() + imageName;
			File targetFile = new File(pathFile);
			java.nio.file.Files.copy(fileInputStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			IOUtils.closeQuietly(fileInputStream);
		} catch(IOException e) {
			throw new FileStorageException("File not found");
		}
	}
	
	@Override
	public byte[] getImageByname(String imageName) {
		try {
			String sl = File.separator;
			File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX+ "static/s3/" + imageName);
			return Files.readAllBytes(file.toPath());
		} catch (FileNotFoundException e) {
			logger.error("File " + imageName + "not found");
		}
		catch(IOException e) {
			logger.error("Could not read file " + imageName);
		}
		return null;
	}

}
