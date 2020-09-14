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
public class SaleTransaction {
    @Id private String id;
    private String transRef;
    private LocalDateTime transDate;    
    private BigDecimal totalAmount;
    private UserDao boughtBy;
    private UserDao soldBy;
    private String eventCode;
    private String narration;
    private Location location;
    private String taguuid;
    private String transType;

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
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

   

    public UserDao getBoughtBy() {
        return boughtBy;
    }

    public void setBoughtBy(UserDao boughtBy) {
        this.boughtBy = boughtBy;
    }

    public UserDao getSoldBy() {
        return soldBy;
    }

    public void setSoldBy(UserDao soldBy) {
        this.soldBy = soldBy;
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

    public String getTaguuid() {
        return taguuid;
    }

    public void setTaguuid(String taguuid) {
        this.taguuid = taguuid;
    }
    
    
}







