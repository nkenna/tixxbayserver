/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author nkenn
 */
public class WalletDao {
    private String id;
    private String walletid;
    private String ownerid;
    private BigDecimal balance;
    private LocalDateTime createddate;
    private LocalDateTime updateddate;
    private boolean status;
    private UserDao owner;
    private List<TransactionDao> transactions;
    private List<TixxTagDao> bands;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWalletid() {
        return walletid;
    }

    public void setWalletid(String walletid) {
        this.walletid = walletid;
    }

    public String getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public LocalDateTime getCreateddate() {
        return createddate;
    }

    public void setCreateddate(LocalDateTime createddate) {
        this.createddate = createddate;
    }

    public LocalDateTime getUpdateddate() {
        return updateddate;
    }

    public void setUpdateddate(LocalDateTime updateddate) {
        this.updateddate = updateddate;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public UserDao getOwner() {
        return owner;
    }

    public void setOwner(UserDao owner) {
        this.owner = owner;
    }

    public List<TransactionDao> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionDao> transactions) {
        this.transactions = transactions;
    }

    public List<TixxTagDao> getBands() {
        return bands;
    }

    public void setBands(List<TixxTagDao> bands) {
        this.bands = bands;
    }
    
    
    
}


