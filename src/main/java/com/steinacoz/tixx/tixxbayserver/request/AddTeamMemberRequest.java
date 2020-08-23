/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.request;

/**
 *
 * @author nkenn
 */
public class AddTeamMemberRequest {
    private String teamRef;
    private String userEmail;
    private String username;
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
    

    public String getTeamRef() {
        return teamRef;
    }

    public void setTeamRef(String teamRef) {
        this.teamRef = teamRef;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    
}




