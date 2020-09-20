/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.request;

import java.math.BigDecimal;

/**
 *
 * @author nkenn
 */
public class WalletfundtagRequest {
    private String username;
    private String walletId;
    private String taguuid;
    private BigDecimal amount;

    public String getTaguuid() {
        return taguuid;
    }

    public void setTaguuid(String taguuid) {
        this.taguuid = taguuid;
    }
    
    

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    
}





