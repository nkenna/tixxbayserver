/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.annotation.Id;

/**
 *
 * @author nkenn
 */
public class Transaction {
    @Id private String id;
    private String transRef;
    private String payRef;
    private String payId;
    private LocalDateTime transDate;
    private boolean transStatus;
    private String payStatus;
    private BigDecimal amount;
    private String transType;
    private String walletId;
    private String taguuid;
    private String usernameTo;
    private String userIdTo;
    private String usernameFrom;
    private String userIdFrom;
    private String narration;
    private Location location;
    private String couponCode;
    private List<String> ticketCode;

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }
    
    

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

    public String getPayRef() {
        return payRef;
    }

    public void setPayRef(String payRef) {
        this.payRef = payRef;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
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

    public String getUsernameTo() {
        return usernameTo;
    }

    public void setUsernameTo(String usernameTo) {
        this.usernameTo = usernameTo;
    }

    public String getUserIdTo() {
        return userIdTo;
    }

    public void setUserIdTo(String userIdTo) {
        this.userIdTo = userIdTo;
    }

    public String getUsernameFrom() {
        return usernameFrom;
    }

    public void setUsernameFrom(String usernameFrom) {
        this.usernameFrom = usernameFrom;
    }

    public String getUserIdFrom() {
        return userIdFrom;
    }

    public void setUserIdFrom(String userIdFrom) {
        this.userIdFrom = userIdFrom;
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

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public boolean isTransStatus() {
        return transStatus;
    }

    public void setTransStatus(boolean transStatus) {
        this.transStatus = transStatus;
    }

    public List<String> getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(List<String> ticketCode) {
        this.ticketCode = ticketCode;
    }
    
    

   
    
    

   
        
}









