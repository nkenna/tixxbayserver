/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.response;

import com.steinacoz.tixx.tixxbayserver.dao.EventCategoryDao;
import com.steinacoz.tixx.tixxbayserver.model.EventCategory;
import java.util.List;

/**
 *
 * @author nkenn
 */
public class EventCategoryResponse {
    private String status;
    private String message;
    private EventCategoryDao eventCategory;
    private List<EventCategoryDao> eventCategories;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public EventCategoryDao getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(EventCategoryDao eventCategory) {
        this.eventCategory = eventCategory;
    }

    public List<EventCategoryDao> getEventCategories() {
        return eventCategories;
    }

    public void setEventCategories(List<EventCategoryDao> eventCategories) {
        this.eventCategories = eventCategories;
    }
    
    
    
}


