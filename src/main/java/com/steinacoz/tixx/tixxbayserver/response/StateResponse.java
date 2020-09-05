/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.response;

import com.steinacoz.tixx.tixxbayserver.dao.StateDao;
import com.steinacoz.tixx.tixxbayserver.model.City;
import com.steinacoz.tixx.tixxbayserver.model.State;
import java.util.List;

/**
 *
 * @author nkenn
 */
public class StateResponse {
    private String status;
    private String message;
    private List<StateDao> states;
    private StateDao state;
    private List<City> cities;

    public StateDao getState() {
        return state;
    }

    public void setState(StateDao state) {
        this.state = state;
    }
    
    

    public List<StateDao> getStates() {
        return states;
    }

    public void setStates(List<StateDao> states) {
        this.states = states;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
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
    
    
}






