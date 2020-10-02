/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.response;

import com.steinacoz.tixx.tixxbayserver.model.DashboardData;

/**
 *
 * @author nkenn
 */
public class DashboardDataResponse {
    private String status;
    private String message;
    private DashboardData data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DashboardData getData() {
        return data;
    }

    public void setData(DashboardData data) {
        this.data = data;
    }
    
    
}


