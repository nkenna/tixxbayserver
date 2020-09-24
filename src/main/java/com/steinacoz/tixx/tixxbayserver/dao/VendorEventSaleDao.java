/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.dao;

import com.steinacoz.tixx.tixxbayserver.model.Event;

/**
 *
 * @author nkenn
 */
public class VendorEventSaleDao {
    private Event event;
    private long totalSales;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public long getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(long totalSales) {
        this.totalSales = totalSales;
    }
    
    
}


