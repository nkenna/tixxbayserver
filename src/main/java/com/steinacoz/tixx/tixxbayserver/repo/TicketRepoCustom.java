/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.dao.TicketDao;
import com.steinacoz.tixx.tixxbayserver.model.Ticket;
import java.util.List;

/**
 *
 * @author nkenn
 */
public interface TicketRepoCustom {
    List<TicketDao> aggregateAllTicketCategories();
    List<TicketDao> aggregateAllTicketCategoriesByEvent(String ticketCode);
    TicketDao getTicketCategory(String ticketCode);
    TicketDao getTicketCategoryByChildTicket(String ticketCode);
    List<TicketDao> findAllTicketsABoutToStart();
    List<Ticket> findAllExpiredTickets();
}









