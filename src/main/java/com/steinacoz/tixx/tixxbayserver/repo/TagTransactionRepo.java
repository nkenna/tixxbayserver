/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.model.SaleTransaction;
import com.steinacoz.tixx.tixxbayserver.model.TagTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 *
 * @author nkenn
 */
@RepositoryRestResource(collectionResourceRel = "tagtrans", path = "tagtrans")
public interface TagTransactionRepo extends MongoRepository<TagTransaction, String> {
    
}



