/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.request;

import com.steinacoz.tixx.tixxbayserver.model.Ticket;

/**
 *
 * @author nkenn
 */
public class CreateChildTicketRequest {
    Ticket ticket;
    int numToCreate;

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public int getNumToCreate() {
        return numToCreate;
    }

    public void setNumToCreate(int numToCreate) {
        this.numToCreate = numToCreate;
    }
    
    
}


