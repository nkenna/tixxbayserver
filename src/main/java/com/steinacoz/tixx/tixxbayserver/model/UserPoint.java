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
public class UserPoint {
    @Id private String id;
    //0.2pt for account creation
    //0.5pt for making transaction with tixxbay tag
    //1pt for attending event with tixx tag
    //1pt if your referred starts selling ticket
    
    private double points; 
    private String username;
    private String taguuid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTaguuid() {
        return taguuid;
    }

    public void setTaguuid(String taguuid) {
        this.taguuid = taguuid;
    }
    
    
}



