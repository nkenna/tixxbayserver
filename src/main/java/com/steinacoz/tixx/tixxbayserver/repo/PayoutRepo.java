/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.model.Event;
import com.steinacoz.tixx.tixxbayserver.model.Payout;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 *
 * @author nkenn
 */
@RepositoryRestResource(collectionResourceRel = "payout", path = "payout")
public interface PayoutRepo extends MongoRepository<Payout, String> {
    List<Payout> findByEventCode(String eventCode);
    List<Payout> findByWalletId(String walletId);
    List<Payout> findByUsername(String username);
    
    List<Payout> findByStatus(String status);
    List<Payout> findByPayType(String payType);
}





