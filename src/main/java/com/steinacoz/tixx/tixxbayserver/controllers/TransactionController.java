/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.controllers;

import com.steinacoz.tixx.tixxbayserver.dao.TicketDao;
import com.steinacoz.tixx.tixxbayserver.dao.TicketSaleTransactionDao;
import com.steinacoz.tixx.tixxbayserver.repo.TicketSaleTransactionRepo;
import com.steinacoz.tixx.tixxbayserver.repo.TransactionRepo;
import com.steinacoz.tixx.tixxbayserver.request.TicketSaleByMonthRequest;
import com.steinacoz.tixx.tixxbayserver.response.TicketResponse;
import com.steinacoz.tixx.tixxbayserver.response.TicketSalesByMonthResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nkenn
 */
@RestController
@RequestMapping("/tixxbay/api/trans/v1")
public class TransactionController {
    @Autowired
    TransactionRepo transRepo;
    
   @Autowired
   TicketSaleTransactionRepo ttRepo;
   
   
   @CrossOrigin
    @RequestMapping(value = "/all-ticket-sales-month", method = RequestMethod.POST)
    public ResponseEntity<TicketSalesByMonthResponse> allTicketsSalesByMonth(@RequestBody TicketSaleByMonthRequest tsbm){
        TicketSalesByMonthResponse er = new TicketSalesByMonthResponse();
        List<TicketSaleTransactionDao> week1 = new ArrayList<>();
        List<TicketSaleTransactionDao> week2 = new ArrayList<>();
        List<TicketSaleTransactionDao> week3 = new ArrayList<>();
        List<TicketSaleTransactionDao> week4 = new ArrayList<>();
        
              
        List<TicketSaleTransactionDao> data = ttRepo.getAllTicketSaleTransByMonth(tsbm.getYearMonth(), tsbm.getEventCode());
        
        for(TicketSaleTransactionDao tstd : data){
            if(tstd.getTransDate().getDayOfMonth() > 1 && tstd.getTransDate().getDayOfMonth() < 7){
                week1.add(tstd);
            }else if(tstd.getTransDate().getDayOfMonth() > 7 && tstd.getTransDate().getDayOfMonth() < 14){
                week2.add(tstd);
            }else if(tstd.getTransDate().getDayOfMonth() > 14 && tstd.getTransDate().getDayOfMonth() < 28){
                week3.add(tstd);
            }else if(tstd.getTransDate().getDayOfMonth() > 28){
                week4.add(tstd);
            }
        }
        
        //double sum = items.stream().mapToDouble(Item::getPrice).sum();
        
        double week1Total = week1.stream().mapToDouble(TicketSaleTransactionDao::getTotalAmount).sum();
        double week2Total = week2.stream().mapToDouble(TicketSaleTransactionDao::getTotalAmount).sum();
        double week3Total = week3.stream().mapToDouble(TicketSaleTransactionDao::getTotalAmount).sum();
        double week4Total = week4.stream().mapToDouble(TicketSaleTransactionDao::getTotalAmount).sum();
        
                
        er.setMessage("data found: " + String.valueOf(data.size()));
        er.setStatus("success");
        er.setWeek1(week1Total);
        er.setWeek2(week2Total);
        er.setWeek3(week3Total);
        er.setWeek4(week4Total);
        
        er.setWeek1data(week1);
        er.setWeek2data(week2);
        er.setWeek3data(week3);
        er.setWeek4data(week4);
        
        er.setTicketSales(data);
        return ResponseEntity.ok().body(er);
    }
    
    @CrossOrigin
    @RequestMapping(value = "/all-ticket-sales", method = RequestMethod.GET)
    public ResponseEntity<TicketSalesByMonthResponse> allTicketsSales(){
        TicketSalesByMonthResponse er = new TicketSalesByMonthResponse();
        List<TicketSaleTransactionDao> sales = ttRepo.getAllTicketSaleTrans();
        er.setTicketSales(sales);
        er.setMessage("sales found: " + String.valueOf(sales.size()));
        er.setStatus("success");
        return ResponseEntity.ok().body(er);
    }
    
}






















