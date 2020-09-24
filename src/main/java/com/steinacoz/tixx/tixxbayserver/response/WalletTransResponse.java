/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.response;

import com.steinacoz.tixx.tixxbayserver.dao.WalletDao;
import com.steinacoz.tixx.tixxbayserver.model.Wallet;
import com.steinacoz.tixx.tixxbayserver.model.WalletTransaction;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author nkenn
 */
public class WalletTransResponse {
   private String status;
    private String message;
    private List<WalletTransaction> trans; 

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

    public List<WalletTransaction> getTrans() {
        return trans;
    }

    public void setTrans(List<WalletTransaction> trans) {
        this.trans = trans;
    }
    
    
}


