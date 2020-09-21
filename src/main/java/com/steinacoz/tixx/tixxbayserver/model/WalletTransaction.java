/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.model;

import com.steinacoz.tixx.tixxbayserver.dao.UserDao;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.annotation.Id;

/**
 *
 * @author nkenn
 */
public class WalletTransaction {
    @Id private String id;
    private String transRef;
    private LocalDateTime transDate;
    private BigDecimal totalAmount;
    private String transType;
    private UserDao boughtBy;
    private String walletId;
    private String walletOwnerUsername;
    private String eventCode;
    private String narration;
    private Location location;
    private double ticketDiscount;
    private List<String> ticketCodes;
    
    

    public double getTicketDiscount() {
        return ticketDiscount;
    }

    public void setTicketDiscount(double ticketDiscount) {
        this.ticketDiscount = ticketDiscount;
    }
    
    

    public String getId() {
        return id;
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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public UserDao getBoughtBy() {
        return boughtBy;
    }

    public void setBoughtBy(UserDao boughtBy) {
        this.boughtBy = boughtBy;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getWalletOwnerUsername() {
        return walletOwnerUsername;
    }

    public void setWalletOwnerUsername(String walletOwnerUsername) {
        this.walletOwnerUsername = walletOwnerUsername;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<String> getTicketCodes() {
        return ticketCodes;
    }

    public void setTicketCodes(List<String> ticketCodes) {
        this.ticketCodes = ticketCodes;
    }
    
    
}






