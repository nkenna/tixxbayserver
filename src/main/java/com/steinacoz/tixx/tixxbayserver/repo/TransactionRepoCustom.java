/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.dao.TransactionDao;
import java.util.List;

/**
 *
 * @author nkenn
 */
public interface TransactionRepoCustom {
    List<TransactionDao> aggregateAll();
    List<TransactionDao> aggregateTransRef(String transRef);
    List<TransactionDao> getTransByMonth(String transRef);
}



