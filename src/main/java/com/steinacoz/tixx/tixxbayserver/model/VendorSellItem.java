/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.model;

import java.math.BigDecimal;
import org.springframework.data.annotation.Id;

/**
 *
 * @author nkenn
 */
public class VendorSellItem {
    @Id private String id;
    private String name;
    private BigDecimal amount;
    private int quantity;
    private String vendorUsername;
    private String eventCode;

    public String getId() {
        return id;
    }

   

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getVendorUsername() {
        return vendorUsername;
    }

    public void setVendorUsername(String vendorUsername) {
        this.vendorUsername = vendorUsername;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }
    
    
    
    
}






