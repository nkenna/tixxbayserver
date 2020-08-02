/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.model.TixxTag;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 *
 * @author nkenn
 */
@RepositoryRestResource(collectionResourceRel = "tixxtag", path = "tixxtag")
public interface TixxTagRepo extends MongoRepository<TixxTag, String>, TixxTagRepoCustom{
    TixxTag findByTaguuid(String uuid);
    List<TixxTag> findByAddedById(String addedById);
    List<TixxTag> findByUpdatedById(String updatedById);
    TixxTag findByWornedById(String wornedById);
    List<TixxTag> findByWornedByName(String wornedByName);
    List<TixxTag> findByWornedByPosition(String wornedByPosition);
    List<TixxTag> findByFlag(boolean flag);
    List<TixxTag> findByActive(boolean active);
}



