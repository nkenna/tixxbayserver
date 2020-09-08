/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.request;

import com.steinacoz.tixx.tixxbayserver.model.Location;
import com.steinacoz.tixx.tixxbayserver.model.ParentTicketSellData;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author nkenn
 */
public class SellTicketReqest {
    private BigDecimal totalAmount;
    private BigDecimal unitAmount;
    //private String ticketCode;
    private String eventCode;
    private List<ParentTicketSellData> parentTicketData;
    private Location location;
    //private int quantity;
    private String reference;
    private String boughtByEmail;

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(BigDecimal unitAmount) {
        this.unitAmount = unitAmount;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public List<ParentTicketSellData> getParentTicketData() {
        return parentTicketData;
    }

    public void setParentTicketData(List<ParentTicketSellData> parentTicketData) {
        this.parentTicketData = parentTicketData;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getBoughtByEmail() {
        return boughtByEmail;
    }

    public void setBoughtByEmail(String boughtByEmail) {
        this.boughtByEmail = boughtByEmail;
    }
    
    

    @Override
    public String toString() {
        return "SellTicketReqest{" + "totalAmount=" + totalAmount + ", unitAmount=" + unitAmount + ", eventCode=" + eventCode + ", parentTicketData=" + parentTicketData + ", location=" + location + ", reference=" + reference + ", boughtByEmail=" + boughtByEmail + '}';
    }

    
    
}








