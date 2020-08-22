/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.response;

import com.steinacoz.tixx.tixxbayserver.dao.TicketSaleTransactionDao;
import java.util.List;

/**
 *
 * @author nkenn
 */
public class TicketSalesByMonthResponse {
    private String status;
    private String message;
    
    private int week1;
    private int week2;
    private int week3;
    private int week4;
    
    private List<TicketSaleTransactionDao> week1data;
    private List<TicketSaleTransactionDao> week2data;
    private List<TicketSaleTransactionDao> week3data;
    private List<TicketSaleTransactionDao> week4data;
    
    private List<TicketSaleTransactionDao> ticketSales;

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
    
    
    

    public int getWeek1() {
        return week1;
    }

    public void setWeek1(int week1) {
        this.week1 = week1;
    }

    public int getWeek2() {
        return week2;
    }

    public void setWeek2(int week2) {
        this.week2 = week2;
    }

    public int getWeek3() {
        return week3;
    }

    public void setWeek3(int week3) {
        this.week3 = week3;
    }

    public int getWeek4() {
        return week4;
    }

    public void setWeek4(int week4) {
        this.week4 = week4;
    }

    public List<TicketSaleTransactionDao> getWeek1data() {
        return week1data;
    }

    public void setWeek1data(List<TicketSaleTransactionDao> week1data) {
        this.week1data = week1data;
    }

    public List<TicketSaleTransactionDao> getWeek2data() {
        return week2data;
    }

    public void setWeek2data(List<TicketSaleTransactionDao> week2data) {
        this.week2data = week2data;
    }

    public List<TicketSaleTransactionDao> getWeek3data() {
        return week3data;
    }

    public void setWeek3data(List<TicketSaleTransactionDao> week3data) {
        this.week3data = week3data;
    }

    public List<TicketSaleTransactionDao> getWeek4data() {
        return week4data;
    }

    public void setWeek4data(List<TicketSaleTransactionDao> week4data) {
        this.week4data = week4data;
    }

    public List<TicketSaleTransactionDao> getTicketSales() {
        return ticketSales;
    }

    public void setTicketSales(List<TicketSaleTransactionDao> ticketSales) {
        this.ticketSales = ticketSales;
    }
    
    
}





