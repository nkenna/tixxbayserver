/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.model.ERole;
import com.steinacoz.tixx.tixxbayserver.model.Payout;
import com.steinacoz.tixx.tixxbayserver.model.Role;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 *
 * @author nkenn
 */
@RepositoryRestResource(collectionResourceRel = "userrole", path = "userrole")
public interface RoleRepo extends MongoRepository<Role, String> {
    Role findByName(String name);
}





