/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author nkenn
 */

public interface TransactionRepo extends MongoRepository<Transaction, String>, TransactionRepoCustom {
    
}



