/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;


import com.steinacoz.tixx.tixxbayserver.dao.EventDao;
import com.steinacoz.tixx.tixxbayserver.model.Event;
import com.steinacoz.tixx.tixxbayserver.model.Location;
import java.util.List;
import org.springframework.data.geo.Point;

/**
 *
 * @author nkenn
 */
public interface EventRepoCustom {
    EventDao getEventByEventCode(String eventCode);
    List<EventDao> aggregateAllEvents();
    List<EventDao> aggregateAllEventsByName(String title);
    List<EventDao> aggregateAllEventsByStatus();
    List<EventDao> aggregateAllEventsByCreator(String creatorId);
    List<EventDao> aggregateAllEventsByState(String state);
    List<EventDao> aggregateAllEventsByLga(String lga);
    List<EventDao> aggregateAllEventsByCountry(String Country);
    List<EventDao> aggregateAllEventsBy3Weeks();
    List<EventDao> aggregateAllEventsByShuffle();
    
    List<EventDao> aggregateAllEventsByUserLocation(String country, String state, String lga);
    List<EventDao> aggregateAllEventsByUserGPSLocation(Point point);    
    EventDao getEventByVendor(String eventCode);
    List<Event> getEventsCreatedBy3wks();
    
}





















