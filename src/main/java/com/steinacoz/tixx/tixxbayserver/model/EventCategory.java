/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.model;

import org.springframework.data.annotation.Id;

/**
 *
 * @author nkenn
 */
public class EventCategory {
    @Id private String id;
    private String category;

    public String getId() {
        return id;
    }

   

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    
    
}


