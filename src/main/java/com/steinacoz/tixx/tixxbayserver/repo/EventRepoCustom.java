/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.dao.EventDao;
import com.steinacoz.tixx.tixxbayserver.model.Event;
import java.util.List;

/**
 *
 * @author nkenn
 */
public interface EventRepoCustom {
    List<EventDao> aggregateAllEvents();
    List<EventDao> aggregateAllEventsByCreator(String creatorId);
}





