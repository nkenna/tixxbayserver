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
public class IssuedTicket {
    @Id private String id;
    private String ticketTitle;
    private List<String> ticketCode;
    private String eventCode;
    private LocalDateTime issuedDate;
    private int quantity;
    private BigDecimal totalAmount;
    private BigDecimal unitAmunt;
    private User issuer;
    private User issuedTo;

    public String getId() {
        return id;
    }

    public String getTicketTitle() {
        return ticketTitle;
    }

    public void setTicketTitle(String ticketTitle) {
        this.ticketTitle = ticketTitle;
    }

    

    public List<String> getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(List<String> ticketCode) {
        this.ticketCode = ticketCode;
    }

    

   

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public LocalDateTime getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDateTime issuedDate) {
        this.issuedDate = issuedDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getUnitAmunt() {
        return unitAmunt;
    }

    public void setUnitAmunt(BigDecimal unitAmunt) {
        this.unitAmunt = unitAmunt;
    }

    public User getIssuer() {
        return issuer;
    }

    public void setIssuer(User issuer) {
        this.issuer = issuer;
    }

    public User getIssuedTo() {
        return issuedTo;
    }

    public void setIssuedTo(User issuedTo) {
        this.issuedTo = issuedTo;
    }
    
    
}






