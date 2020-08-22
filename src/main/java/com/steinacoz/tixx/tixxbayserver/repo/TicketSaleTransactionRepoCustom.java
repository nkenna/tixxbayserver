/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.dao.TicketSaleTransactionDao;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 *
 * @author nkenn
 */
public interface TicketSaleTransactionRepoCustom {
    List<TicketSaleTransactionDao> getAllTicketSaleTrans();
    List<TicketSaleTransactionDao> getAllTicketSaleTransByMonth(LocalDate date, String eventCode);
    TicketSaleTransactionDao getTicketSaleTrans(String transRef);
}







