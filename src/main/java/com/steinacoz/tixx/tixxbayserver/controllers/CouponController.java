/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.controllers;

import com.steinacoz.tixx.tixxbayserver.model.Coupon;
import com.steinacoz.tixx.tixxbayserver.model.Event;
import com.steinacoz.tixx.tixxbayserver.model.Ticket;
import com.steinacoz.tixx.tixxbayserver.model.User;
import com.steinacoz.tixx.tixxbayserver.repo.CouponRepo;
import com.steinacoz.tixx.tixxbayserver.repo.EventRepo;
import com.steinacoz.tixx.tixxbayserver.repo.TicketRepo;
import com.steinacoz.tixx.tixxbayserver.repo.UserRepo;
import com.steinacoz.tixx.tixxbayserver.request.CouponRequest;
import com.steinacoz.tixx.tixxbayserver.request.PayoutRequest;
import com.steinacoz.tixx.tixxbayserver.response.CouponResponse;
import com.steinacoz.tixx.tixxbayserver.response.PayoutResponse;
import com.steinacoz.tixx.tixxbayserver.utils.Utils;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nkenn
 */
@RestController
@RequestMapping("/tixxbay/api/coupon/v1")
public class CouponController {
    
    @Autowired
    EventRepo eventRepo;
    
    @Autowired
    UserRepo userRepo;
    
    @Autowired
    CouponRepo couRepo;
    
    @Autowired
    TicketRepo ticketRepo;
    
    @CrossOrigin
    @RequestMapping(value = "/create-coupon", method = RequestMethod.POST)
    public ResponseEntity<CouponResponse> createCoupon(@RequestBody CouponRequest cou){
        CouponResponse cr = new CouponResponse();
        List<Coupon> coupons = new ArrayList<Coupon>();
        if(cou.getQuantity() == 0){
            cr.setStatus("failed");
            cr.setMessage("quantity cannot be zero.");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(cr);
        }
        
        if(cou.getDiscount() == 0){
            cr.setStatus("failed");
            cr.setMessage("discount cannot be zero.");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(cr);
        }
        
        if(cou.getTicketCode() == null || cou.getEventCode() == null){
            cr.setStatus("failed");
            cr.setMessage("ticket code or event code is required");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(cr);
        }
        
        Event event = eventRepo.findByEventCode(cou.getEventCode());
        Ticket ticket = ticketRepo.findByTicketCode(cou.getTicketCode());
        if(event == null || ticket == null){
           cr.setStatus("failed");
            cr.setMessage("ticket or event not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(cr); 
        }
        
        for(int i = 0; i < cou.getQuantity(); i++){
            Coupon coupon = new Coupon();
            coupon.setCreated(LocalDateTime.now());
            coupon.setUpdated(LocalDateTime.now());
            coupon.setCode(Utils.randomNS(8).toUpperCase());
            coupon.setEventCode(cou.getEventCode());
            coupon.setTicketCode(cou.getTicketCode());
            coupon.setUsed(false);
            coupon.setMode(cou.getMode());
            coupon.setDiscount(cou.getDiscount());
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime future = now.plusDays(7);
            
            if(cou.getElapseTime() != null){
               long elaspedTimeStamp = cou.getElapseTime().toEpochSecond(ZoneOffset.UTC); 
               coupon.setExpirationTime(elaspedTimeStamp);
            }else{
               long elaspedTimeStamp = future.toEpochSecond(ZoneOffset.UTC); 
               coupon.setExpirationTime(elaspedTimeStamp); 
            } 
            
            coupons.add(coupon);
        }
        
        try{
            couRepo.insert(coupons);
            List<User> users = userRepo.findAll();
            try{
                Collections.shuffle(users);
                for(int i = 0; i < cou.getQuantity(); i++){
                    if(users.get(i) != null){
                        LocalDateTime els = LocalDateTime.ofEpochSecond(coupons.get(i).getExpirationTime(), 0, ZoneOffset.UTC); 
                        String content = "You can get a discount of " + String.valueOf(coupons.get(i).getDiscount()) +  "% using this coupon " + coupons.get(i).getCode() + " to purchase " +
                                ticket.getTitle() + " ticket." +
                                " This coupon will expiry in " + els.format(DateTimeFormatter.ISO_LOCAL_DATE);
                        Utils.sendOutEmailHtml("TixxBay Event" + event.getTitle() +  "Ticket Coupon", users.get(i).getEmail(), content);
                    }
                }
            }catch(Exception e){
                
            }
            cr.setStatus("success");
            cr.setMessage("coupons created: " + coupons.size());
            cr.setCoupons(coupons);
            return ResponseEntity.ok().body(cr);
        }catch(Exception e){
            cr.setStatus("failed");
            cr.setMessage("error occurred creating coupons: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(cr);
        }
        
    }
}














