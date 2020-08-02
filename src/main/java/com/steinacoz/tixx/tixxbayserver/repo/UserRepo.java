/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.model.User;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 *
 * @author nkenn
 */
@RepositoryRestResource(collectionResourceRel = "user", path = "user")
public interface UserRepo extends MongoRepository<User, String>, UserRepoCustom {
    
    User findByUsername(String username);
    User findByMobileNumber(String mobileNumber);	
    User findByEmail(String email);
    User findByWalletId(String walletId);
    
	
    @Query(value="{}", fields="{password : 0}")
    List<User> findAllAndExcludePassword();
    
}




