/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.response;

import com.steinacoz.tixx.tixxbayserver.dao.SaleTransactionDao;
import com.steinacoz.tixx.tixxbayserver.dao.TicketSaleTransactionDao;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author nkenn
 */
public class SalesByMonthResponse {
    private String status;
    private String message;
    
    private BigDecimal week1;
    private BigDecimal week2;
    private BigDecimal week3;
    private BigDecimal week4;
    
    private List<SaleTransactionDao> week1data;
    private List<SaleTransactionDao> week2data;
    private List<SaleTransactionDao> week3data;
    private List<SaleTransactionDao> week4data;
    
    private List<SaleTransactionDao> sales;

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

    public BigDecimal getWeek1() {
        return week1;
    }

    public void setWeek1(BigDecimal week1) {
        this.week1 = week1;
    }

    public BigDecimal getWeek2() {
        return week2;
    }

    public void setWeek2(BigDecimal week2) {
        this.week2 = week2;
    }

    public BigDecimal getWeek3() {
        return week3;
    }

    public void setWeek3(BigDecimal week3) {
        this.week3 = week3;
    }

    public BigDecimal getWeek4() {
        return week4;
    }

    public void setWeek4(BigDecimal week4) {
        this.week4 = week4;
    }

    public List<SaleTransactionDao> getWeek1data() {
        return week1data;
    }

    public void setWeek1data(List<SaleTransactionDao> week1data) {
        this.week1data = week1data;
    }

    public List<SaleTransactionDao> getWeek2data() {
        return week2data;
    }

    public void setWeek2data(List<SaleTransactionDao> week2data) {
        this.week2data = week2data;
    }

    public List<SaleTransactionDao> getWeek3data() {
        return week3data;
    }

    public void setWeek3data(List<SaleTransactionDao> week3data) {
        this.week3data = week3data;
    }

    public List<SaleTransactionDao> getWeek4data() {
        return week4data;
    }

    public void setWeek4data(List<SaleTransactionDao> week4data) {
        this.week4data = week4data;
    }

    public List<SaleTransactionDao> getSales() {
        return sales;
    }

    public void setSales(List<SaleTransactionDao> sales) {
        this.sales = sales;
    }

    
    
    
}



