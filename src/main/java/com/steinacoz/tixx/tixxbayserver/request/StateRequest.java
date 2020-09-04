/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.request;

import com.steinacoz.tixx.tixxbayserver.model.State;
import java.util.List;

/**
 *
 * @author nkenn
 */
public class StateRequest {
    private List<State> state;

    public List<State> getState() {
        return state;
    }

    public void setState(List<State> state) {
        this.state = state;
    }
    
    
    
    
}




