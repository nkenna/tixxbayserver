/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.dao.SaleTransactionDao;
import com.steinacoz.tixx.tixxbayserver.dao.TicketSaleTransactionDao;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author nkenn
 */
public interface SaleTransactionRepoCustom {
    List<SaleTransactionDao> getAllSaleTrans();
    List<SaleTransactionDao> getAllSaleTransByMonth(LocalDate date, String eventCode);
    SaleTransactionDao getSaleTrans(String transRef);
    List<SaleTransactionDao> getAllTicketSaleByEventCode(String eventCode);
}



