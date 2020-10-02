/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.model.User;
import com.steinacoz.tixx.tixxbayserver.model.WalletTransaction;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 *
 * @author nkenn
 */
@RepositoryRestResource(collectionResourceRel = "wallettrans", path = "wallettrans")
public interface WalletTransactionRepo extends MongoRepository<WalletTransaction, String>, WalletTransactionRepoCustom {
    List<WalletTransaction> findByEventCode(String eventCode);
    List<WalletTransaction> findByWalletId(String walletId);
}




