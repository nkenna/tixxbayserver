/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.response;

import com.steinacoz.tixx.tixxbayserver.dao.VendorEventSaleDao;
import com.steinacoz.tixx.tixxbayserver.model.Event;
import java.util.List;

/**
 *
 * @author nkenn
 */
public class EventResponseVendor {
    private String status;
    private String message;
    private Event event;
    private List<Event> events;
    List<VendorEventSaleDao> eventSales;

    public List<VendorEventSaleDao> getEventSales() {
        return eventSales;
    }

    public void setEventSales(List<VendorEventSaleDao> eventSales) {
        this.eventSales = eventSales;
    }
    
    

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

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
    
    
    
    
}



