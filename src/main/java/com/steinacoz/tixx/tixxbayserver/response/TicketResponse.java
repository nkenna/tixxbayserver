/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.response;

import com.steinacoz.tixx.tixxbayserver.model.ChildTicket;
import com.steinacoz.tixx.tixxbayserver.model.Ticket;
import java.util.List;

/**
 *
 * @author nkenn
 */
public class TicketResponse {
    private String status;
    private String message;
    private List<Ticket> tickets;
    private Ticket ticket;
    private ChildTicket childTicket;
    private List<ChildTicket> childTickets;

    public ChildTicket getChildTicket() {
        return childTicket;
    }

    public void setChildTicket(ChildTicket childTicket) {
        this.childTicket = childTicket;
    }

    public List<ChildTicket> getChildTickets() {
        return childTickets;
    }

    public void setChildTickets(List<ChildTicket> childTickets) {
        this.childTickets = childTickets;
    }
    
    

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

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
    
    
}



