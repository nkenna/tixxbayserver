/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.dao.CityDao;
import java.util.List;

/**
 *
 * @author nkenn
 */
public interface CityRepoCustom {
    List<CityDao> getAllCities();
    CityDao getCityByName(String name);
}



