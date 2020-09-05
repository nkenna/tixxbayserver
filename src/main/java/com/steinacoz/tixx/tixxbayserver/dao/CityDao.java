/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.dao;

import com.steinacoz.tixx.tixxbayserver.model.State;

/**
 *
 * @author nkenn
 */
public class CityDao {
    private String id;
    private String name;
    private String state;
    private State stateData;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public State getStateData() {
        return stateData;
    }

    public void setStateData(State stateData) {
        this.stateData = stateData;
    }
    
    
    
}


