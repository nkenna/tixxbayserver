/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.response;

import com.steinacoz.tixx.tixxbayserver.dao.WalletDao;
import com.steinacoz.tixx.tixxbayserver.model.Wallet;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author nkenn
 */
public class WalletResponse {
    private String status;
    private String message;
    private BigDecimal balance;
    private WalletDao wallet;
    private List<Wallet> wallets;

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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public WalletDao getWallet() {
        return wallet;
    }

    public void setWallet(WalletDao wallet) {
        this.wallet = wallet;
    }

    public List<Wallet> getWallets() {
        return wallets;
    }

    public void setWallets(List<Wallet> wallets) {
        this.wallets = wallets;
    }

    
    
    
}




