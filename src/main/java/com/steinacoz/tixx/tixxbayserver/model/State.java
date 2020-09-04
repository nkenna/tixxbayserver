/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.model;

import java.util.List;
import org.springframework.data.annotation.Id;

/**
 *
 * @author nkenn
 */
public class State {
    @Id private String id;
    private String name;
    private String country;
    

    public String getId() {
        return id;
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    
    
    
}




