/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.response;

import com.steinacoz.tixx.tixxbayserver.dao.ChildTicketDao;
import com.steinacoz.tixx.tixxbayserver.dao.TicketDao;
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
    private List<TicketDao> tickets;
    private TicketDao ticket;
    private ChildTicket childTicket;
    private List<ChildTicket> childTickets;
    private ChildTicketDao ticketCheckin;
    private List<ChildTicketDao> ticketCheckins;

    public ChildTicketDao getTicketCheckin() {
        return ticketCheckin;
    }

    public void setTicketCheckin(ChildTicketDao ticketCheckin) {
        this.ticketCheckin = ticketCheckin;
    }

    public List<ChildTicketDao> getTicketCheckins() {
        return ticketCheckins;
    }

    public void setTicketCheckins(List<ChildTicketDao> ticketCheckins) {
        this.ticketCheckins = ticketCheckins;
    }
    
    
    

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

    public List<TicketDao> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketDao> tickets) {
        this.tickets = tickets;
    }

    public TicketDao getTicket() {
        return ticket;
    }

    public void setTicket(TicketDao ticket) {
        this.ticket = ticket;
    }

    
    
    
}





