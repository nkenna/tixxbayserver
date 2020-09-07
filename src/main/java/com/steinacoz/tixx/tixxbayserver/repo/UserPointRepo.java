/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.model.TixxTag;
import com.steinacoz.tixx.tixxbayserver.model.UserPoint;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 *
 * @author nkenn
 */
@RepositoryRestResource(collectionResourceRel = "userpoint", path = "userpoint")
public interface UserPointRepo extends MongoRepository<UserPoint, String> {
    UserPoint findByUsername(String username);
    UserPoint findByTaguuid(String taguuid);
    
    public List<UserPoint> findByPointsGreaterThanQueryOrEqualTo(double points);
    
}






