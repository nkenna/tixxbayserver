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
public class TixxTagSearchRequest {
    private String taguuid;
	private String addedById;
	private String updatedById;
	private String role; //ADMIN, VENDOR, EVENT_MANAGER or AGENT
	private String wornedById;
	private String wornedByName;
	private String wornedByPosition;
	private boolean flag;
	private boolean active;
	private BigDecimal previousAmount; //payment
	private BigDecimal availableAmount; //payment
	private BigDecimal defaultAmount;
	private BigDecimal amount;
	private String walletId;
	private String narration;

    public String getTaguuid() {
        return taguuid;
    }

    public void setTaguuid(String taguuid) {
        this.taguuid = taguuid;
    }

    public String getAddedById() {
        return addedById;
    }

    public void setAddedById(String addedById) {
        this.addedById = addedById;
    }

    public String getUpdatedById() {
        return updatedById;
    }

    public void setUpdatedById(String updatedById) {
        this.updatedById = updatedById;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getWornedById() {
        return wornedById;
    }

    public void setWornedById(String wornedById) {
        this.wornedById = wornedById;
    }

    public String getWornedByName() {
        return wornedByName;
    }

    public void setWornedByName(String wornedByName) {
        this.wornedByName = wornedByName;
    }

    public String getWornedByPosition() {
        return wornedByPosition;
    }

    public void setWornedByPosition(String wornedByPosition) {
        this.wornedByPosition = wornedByPosition;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public BigDecimal getPreviousAmount() {
        return previousAmount;
    }

    public void setPreviousAmount(BigDecimal previousAmount) {
        this.previousAmount = previousAmount;
    }

    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(BigDecimal availableAmount) {
        this.availableAmount = availableAmount;
    }

    public BigDecimal getDefaultAmount() {
        return defaultAmount;
    }

    public void setDefaultAmount(BigDecimal defaultAmount) {
        this.defaultAmount = defaultAmount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }
        
        
        
        
        
}


