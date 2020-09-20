/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.response;

import com.steinacoz.tixx.tixxbayserver.dao.UserDao;
import com.steinacoz.tixx.tixxbayserver.model.TixxTag;
import com.steinacoz.tixx.tixxbayserver.model.User;
import com.steinacoz.tixx.tixxbayserver.model.Wallet;
import java.math.BigDecimal;

/**
 *
 * @author nkenn
 */
public class WalletfundtagResponse {
    private String status;
    private String message;
    private BigDecimal amount;
    private Wallet wallet;
    private TixxTag tag;
    private UserDao user;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public TixxTag getTag() {
        return tag;
    }

    public void setTag(TixxTag tag) {
        this.tag = tag;
    }

    public UserDao getUser() {
        return user;
    }

    public void setUser(UserDao user) {
        this.user = user;
    }

    
    
    
}





