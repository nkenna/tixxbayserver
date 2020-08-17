/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.model.BankDetail;
import com.steinacoz.tixx.tixxbayserver.model.EventKey;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 *
 * @author nkenn
 */
@RepositoryRestResource(collectionResourceRel = "eventkey", path = "eventkey")
public interface EventKeyRepo extends MongoRepository<EventKey, String> {
    EventKey findByEventId(String eventId);
}




