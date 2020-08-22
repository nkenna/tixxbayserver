/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.model.EventCategory;
import com.steinacoz.tixx.tixxbayserver.model.EventTeam;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 *
 * @author nkenn
 */
@RepositoryRestResource(collectionResourceRel = "teams", path = "teams")
public interface EventTeamRepo extends MongoRepository<EventTeam, String> {
    List<EventTeam> findByEventCode(String eventCode);
    List<EventTeam> findByEventId(String eventId);
    EventTeam findByTeamRef(String teamRef);
}




