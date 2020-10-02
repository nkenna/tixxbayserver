/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.model;

import java.util.List;

/**
 *
 * @author nkenn
 */
public class DashboardData {
    private List<User> users;
    private List<Event> event;
    private List<EventTeam> teams;
    private List<SaleTransaction> saleTrans;
    private List<TicketSaleTransaction> ticketTrans;
    private List<WalletTransaction> walletTrans; 

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Event> getEvent() {
        return event;
    }

    public void setEvent(List<Event> event) {
        this.event = event;
    }

    public List<EventTeam> getTeams() {
        return teams;
    }

    public void setTeams(List<EventTeam> teams) {
        this.teams = teams;
    }

    public List<SaleTransaction> getSaleTrans() {
        return saleTrans;
    }

    public void setSaleTrans(List<SaleTransaction> saleTrans) {
        this.saleTrans = saleTrans;
    }

    public List<TicketSaleTransaction> getTicketTrans() {
        return ticketTrans;
    }

    public void setTicketTrans(List<TicketSaleTransaction> ticketTrans) {
        this.ticketTrans = ticketTrans;
    }

    public List<WalletTransaction> getWalletTrans() {
        return walletTrans;
    }

    public void setWalletTrans(List<WalletTransaction> walletTrans) {
        this.walletTrans = walletTrans;
    }
    
    
    
}



