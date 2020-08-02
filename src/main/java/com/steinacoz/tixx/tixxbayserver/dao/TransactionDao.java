/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.dao;

import com.steinacoz.tixx.tixxbayserver.model.TixxTag;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author nkenn
 */
public class TransactionDao {
    private String id;
	private String transRef;
	private LocalDateTime transDate;
	private BigDecimal amount;
	private String transType;
	private String walletId;
	private String taguuid;
	private String originId;
	private String toId;
	private String narration;
	private List<TixxTag> bands;
	private UserDao origin;
	private UserDao to;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransRef() {
        return transRef;
    }

    public void setTransRef(String transRef) {
        this.transRef = transRef;
    }

    public LocalDateTime getTransDate() {
        return transDate;
    }

    public void setTransDate(LocalDateTime transDate) {
        this.transDate = transDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getTaguuid() {
        return taguuid;
    }

    public void setTaguuid(String taguuid) {
        this.taguuid = taguuid;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public List<TixxTag> getBands() {
        return bands;
    }

    public void setBands(List<TixxTag> bands) {
        this.bands = bands;
    }

    public UserDao getOrigin() {
        return origin;
    }

    public void setOrigin(UserDao origin) {
        this.origin = origin;
    }

    public UserDao getTo() {
        return to;
    }

    public void setTo(UserDao to) {
        this.to = to;
    }
        
        
}


