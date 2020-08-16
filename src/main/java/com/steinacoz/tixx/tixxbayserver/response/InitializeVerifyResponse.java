/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author nkenn
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class InitializeVerifyResponse {
    private boolean status;
    private String message;
    private Data data;

    
    public boolean getStatus() { return status; }
    
    public void setStatus(boolean value) { this.status = value; }

    
    public String getMessage() { return message; }
    
    public void setMessage(String value) { this.message = value; }

    
    public Data getData() { return data; }
    
    public void setData(Data value) { this.data = value; }
}





