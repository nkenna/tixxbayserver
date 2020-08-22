/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.request;

/**
 *
 * @author nkenn
 */
public class IssueTicketRequest {
    private String ticketCode;
    private String eventCode;
    private String eventId;
    private String issuerUsername;
    private String issuedToUsername;
    private int quantity;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    
    

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getIssuerUsername() {
        return issuerUsername;
    }

    public void setIssuerUsername(String issuerUsername) {
        this.issuerUsername = issuerUsername;
    }

    public String getIssuedToUsername() {
        return issuedToUsername;
    }

    public void setIssuedToUsername(String issuedToUsername) {
        this.issuedToUsername = issuedToUsername;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    
}



