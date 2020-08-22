/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.response;

import com.steinacoz.tixx.tixxbayserver.model.EventTeam;
import java.util.List;

/**
 *
 * @author nkenn
 */
public class EventTeamResponse {
    private String status;
    private String message;
    private EventTeam team;
    private List<EventTeam> teams;

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

    public EventTeam getTeam() {
        return team;
    }

    public void setTeam(EventTeam team) {
        this.team = team;
    }

    public List<EventTeam> getTeams() {
        return teams;
    }

    public void setTeams(List<EventTeam> teams) {
        this.teams = teams;
    }
    
    
}



