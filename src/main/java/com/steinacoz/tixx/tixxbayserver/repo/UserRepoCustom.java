/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.dao.UserDao;
import com.steinacoz.tixx.tixxbayserver.model.User;
import java.util.List;

/**
 *
 * @author nkenn
 */
public interface UserRepoCustom {
    List<UserDao> aggregateAllUsers();  
    UserDao getUserByEmail(String email);
    UserDao getUserByPhoneNumber(String phoneNumber);
    List<User> getUsersLast30days();
    
}













