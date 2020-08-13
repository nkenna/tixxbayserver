/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.response;

import com.steinacoz.tixx.tixxbayserver.dao.UserDao;
import com.steinacoz.tixx.tixxbayserver.dao.WalletDao;
import com.steinacoz.tixx.tixxbayserver.model.Transaction;
import com.steinacoz.tixx.tixxbayserver.model.User;
import com.steinacoz.tixx.tixxbayserver.model.Wallet;
import java.util.List;

/**
 *
 * @author nkenn
 */
public class UserResponse {
    
    private String status;
    private String message;
    private UserDao user;
    private String token;
    private List<User> users;
    private List<UserDao> allUsers;
    private List<Wallet> wallet;
    private List<Transaction> donetrans;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    

    public List<Transaction> getDonetrans() {
        return donetrans;
    }

    public void setDonetrans(List<Transaction> donetrans) {
        this.donetrans = donetrans;
    }

    public List<Wallet> getWallet() {
        return wallet;
    }

    public void setWallet(List<Wallet> wallet) {
        this.wallet = wallet;
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

    public UserDao getUser() {
        return user;
    }

    public void setUser(UserDao user) {
        this.user = user;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

  

    
    

    

    public List<UserDao> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(List<UserDao> allUsers) {
        this.allUsers = allUsers;
    }
    
    
    
}











