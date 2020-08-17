/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.controllers;

import com.steinacoz.tixx.tixxbayserver.repo.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author nkenn
 */
@RequestMapping("/tixxbay/api/trans/v1")
public class TransactionController {
    @Autowired
    TransactionRepo transRepo;
    
    //YearMonth month = YearMonth.rom(date);
    //LocalDate start = month.atDay(1);
    //LocalDate end   = month.atEndOfMonth();
}



