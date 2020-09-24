/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.controllers;

import com.steinacoz.tixx.tixxbayserver.dao.SaleTransactionDao;
import com.steinacoz.tixx.tixxbayserver.dao.TicketDao;
import com.steinacoz.tixx.tixxbayserver.dao.TicketSaleTransactionDao;
import com.steinacoz.tixx.tixxbayserver.repo.SaleTransactionRepo;
import com.steinacoz.tixx.tixxbayserver.repo.TicketSaleTransactionRepo;
import com.steinacoz.tixx.tixxbayserver.repo.TransactionRepo;
import com.steinacoz.tixx.tixxbayserver.request.SaleByMonthRequest;
import com.steinacoz.tixx.tixxbayserver.request.TicketSaleByMonthRequest;
import com.steinacoz.tixx.tixxbayserver.response.SalesByMonthResponse;
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
   
   @Autowired
   SaleTransactionRepo stRepo;
   
   
    @CrossOrigin
    @RequestMapping(value = "/all-ticket-sales-month", method = RequestMethod.POST)
    public ResponseEntity<TicketSalesByMonthResponse> allTicketsSalesByMonth(@RequestBody TicketSaleByMonthRequest tsbm){
        TicketSalesByMonthResponse er = new TicketSalesByMonthResponse();
        List<TicketSaleTransactionDao> week1 = new ArrayList<>();
        List<TicketSaleTransactionDao> week2 = new ArrayList<>();
        List<TicketSaleTransactionDao> week3 = new ArrayList<>();
        List<TicketSaleTransactionDao> week4 = new ArrayList<>();
        
         double week1Total = 0.0;
        double week2Total = 0.0;
        double week3Total = 0.0;
        double week4Total = 0.0;
              
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
        
        
        for(TicketSaleTransactionDao td: week1){
            System.out.println(td.getTotalAmount().doubleValue());
            week1Total =+ td.getTotalAmount().doubleValue(); 
        }
        
        for(TicketSaleTransactionDao td: week2){
            System.out.println(td.getTotalAmount().doubleValue());
            week2Total =+ td.getTotalAmount().doubleValue();
        }
        
        for(TicketSaleTransactionDao td: week3){
            System.out.println(td.getTotalAmount().doubleValue());
            week3Total =+ td.getTotalAmount().doubleValue();
        }
        
        for(TicketSaleTransactionDao td: week4){
            System.out.println(td.getTotalAmount().doubleValue());
            week4Total =+ td.getTotalAmount().doubleValue();
        }
        
        System.out.println(week1Total);
        System.out.println(week2Total);
        System.out.println(week3Total);
        System.out.println(week4Total);
        
       
        
        er.setMessage("data found: " + String.valueOf(data.size()));
        er.setStatus("success");
        er.setWeek1(new BigDecimal(week1Total));
        er.setWeek2(new BigDecimal(week2Total));
        er.setWeek3(new BigDecimal(week3Total));
        er.setWeek4(new BigDecimal(week4Total));
        
        er.setWeek1data(week1);
        er.setWeek2data(week2);
        er.setWeek3data(week3);
        er.setWeek4data(week4);
        
        er.setTicketSales(data);
        return ResponseEntity.ok().body(er);
    }
    
    @CrossOrigin
    @RequestMapping(value = "/all-sales", method = RequestMethod.GET)
    public ResponseEntity<SalesByMonthResponse> allSales(){
        SalesByMonthResponse er = new SalesByMonthResponse();
        List<SaleTransactionDao> sales = stRepo.getAllSaleTrans();
        er.setSales(sales);
        er.setMessage("sales found: " + String.valueOf(sales.size()));
        er.setStatus("success");
        return ResponseEntity.ok().body(er);
    }
    
    @CrossOrigin
    @RequestMapping(value = "/all-ticket-sales", method = RequestMethod.GET)
    public ResponseEntity<TicketSalesByMonthResponse> allTicketSales(){
        TicketSalesByMonthResponse er = new TicketSalesByMonthResponse();
        List<TicketSaleTransactionDao> sales = ttRepo.getAllTicketSaleTrans();
        er.setTicketSales(sales);
        er.setMessage("sales found: " + String.valueOf(sales.size()));
        er.setStatus("success");
        return ResponseEntity.ok().body(er);
    }
    
    @CrossOrigin
    @RequestMapping(value = "/get-ticket-sales-by-eventcode", method = RequestMethod.PUT)
    public ResponseEntity<TicketSalesByMonthResponse> allTicketSalesByEventCode(@RequestBody TicketSaleByMonthRequest tsbm){
        TicketSalesByMonthResponse er = new TicketSalesByMonthResponse();
        List<TicketSaleTransactionDao> sales = ttRepo.getAllTicketSaleTransByEventCode(tsbm.getEventCode());
        er.setTicketSales(sales);
        er.setMessage("sales found: " + String.valueOf(sales.size()));
        er.setStatus("success");
        return ResponseEntity.ok().body(er);
    }
    
    @CrossOrigin
    @RequestMapping(value = "/all-vendor-sales-month", method = RequestMethod.POST)
    public ResponseEntity<SalesByMonthResponse> allSalesByMonth(@RequestBody SaleByMonthRequest tsbm){
        SalesByMonthResponse er = new SalesByMonthResponse();
        List<SaleTransactionDao> week1 = new ArrayList<>();
        List<SaleTransactionDao> week2 = new ArrayList<>();
        List<SaleTransactionDao> week3 = new ArrayList<>();
        List<SaleTransactionDao> week4 = new ArrayList<>();
        
         double week1Total = 0.0;
        double week2Total = 0.0;
        double week3Total = 0.0;
        double week4Total = 0.0;
              
        List<SaleTransactionDao> data = stRepo.getAllSaleTransByMonth(tsbm.getYearMonth(), tsbm.getEventCode());
        
        for(SaleTransactionDao tstd : data){
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
        
        
        for(SaleTransactionDao td: week1){
            System.out.println(td.getTotalAmount().doubleValue());
            week1Total =+ td.getTotalAmount().doubleValue(); 
        }
        
        for(SaleTransactionDao td: week2){
            System.out.println(td.getTotalAmount().doubleValue());
            week2Total =+ td.getTotalAmount().doubleValue();
        }
        
        for(SaleTransactionDao td: week3){
            System.out.println(td.getTotalAmount().doubleValue());
            week3Total =+ td.getTotalAmount().doubleValue();
        }
        
        for(SaleTransactionDao td: week4){
            System.out.println(td.getTotalAmount().doubleValue());
            week4Total =+ td.getTotalAmount().doubleValue();
        }
        
        System.out.println(week1Total);
        System.out.println(week2Total);
        System.out.println(week3Total);
        System.out.println(week4Total);
        
       
        
        er.setMessage("data found: " + String.valueOf(data.size()));
        er.setStatus("success");
        er.setWeek1(new BigDecimal(week1Total));
        er.setWeek2(new BigDecimal(week2Total));
        er.setWeek3(new BigDecimal(week3Total));
        er.setWeek4(new BigDecimal(week4Total));
        
        er.setWeek1data(week1);
        er.setWeek2data(week2);
        er.setWeek3data(week3);
        er.setWeek4data(week4);
        
        er.setSales(data);
        return ResponseEntity.ok().body(er);
    }
    
   @CrossOrigin
    @RequestMapping(value = "/get-vendor-sales-by-eventcode", method = RequestMethod.PUT)
    public ResponseEntity<SalesByMonthResponse> allVendorSalesByEventCode(@RequestBody SaleByMonthRequest tsbm){
        SalesByMonthResponse er = new SalesByMonthResponse();;
        List<SaleTransactionDao> sales = stRepo.getAllTicketSaleByEventCode(tsbm.getEventCode());
        er.setSales(sales);
        er.setMessage("sales found: " + String.valueOf(sales.size()));
        er.setStatus("success");
        return ResponseEntity.ok().body(er);
    }
    
}








































