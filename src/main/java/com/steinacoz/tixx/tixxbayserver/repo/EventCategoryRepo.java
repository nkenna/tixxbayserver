/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.model.EventCategory;
import com.steinacoz.tixx.tixxbayserver.model.EventKey;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 *
 * @author nkenn
 */
@RepositoryRestResource(collectionResourceRel = "eventcategory", path = "eventcategory")
public interface EventCategoryRepo extends MongoRepository<EventCategory, String>, EventCategoryRepoCustom {
    EventCategory findByCategory(String category);
}




