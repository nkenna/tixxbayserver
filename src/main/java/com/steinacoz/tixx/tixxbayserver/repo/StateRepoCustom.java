/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.dao.StateDao;
import com.steinacoz.tixx.tixxbayserver.model.State;
import java.util.List;

/**
 *
 * @author nkenn
 */
public interface StateRepoCustom {
    List<StateDao> getAllStates();
    StateDao getStateByName(String name);
}




