/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.model.EventTeam;
import java.util.List;

/**
 *
 * @author nkenn
 */
public interface EventTeamRepoCustom {
    List<EventTeam> getTeamsBy3wks();
}


