/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.response;

import java.time.LocalDateTime;

/**
 *
 * @author nkenn
 */

   public class Data {
    private long id;
    private String domain;
    private String status;
    private String reference;
    private long amount;
    private String message;
    private String gatewayResponse;
    private LocalDateTime dataPaidAt;
    private LocalDateTime dataCreatedAt;
    private String channel;
    private String currency;
    private String ipAddress;   
    private Object orderID;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;    
    private LocalDateTime transactionDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGatewayResponse() {
        return gatewayResponse;
    }

    public void setGatewayResponse(String gatewayResponse) {
        this.gatewayResponse = gatewayResponse;
    }

    public LocalDateTime getDataPaidAt() {
        return dataPaidAt;
    }

    public void setDataPaidAt(LocalDateTime dataPaidAt) {
        this.dataPaidAt = dataPaidAt;
    }

    public LocalDateTime getDataCreatedAt() {
        return dataCreatedAt;
    }

    public void setDataCreatedAt(LocalDateTime dataCreatedAt) {
        this.dataCreatedAt = dataCreatedAt;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Object getOrderID() {
        return orderID;
    }

    public void setOrderID(Object orderID) {
        this.orderID = orderID;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
    

    
    
}
 




