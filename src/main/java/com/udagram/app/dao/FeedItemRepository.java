package com.udagram.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.udagram.app.entities.FeedItem;

@RepositoryRestResource(path = "feed")
public interface FeedItemRepository extends JpaRepository<FeedItem, Long>{

}
