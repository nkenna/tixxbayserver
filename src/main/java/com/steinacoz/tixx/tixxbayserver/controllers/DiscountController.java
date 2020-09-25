/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.controllers;

import com.steinacoz.tixx.tixxbayserver.model.Discount;
import com.steinacoz.tixx.tixxbayserver.model.Ticket;
import com.steinacoz.tixx.tixxbayserver.repo.DiscountRepo;
import com.steinacoz.tixx.tixxbayserver.repo.UserRepo;
import com.steinacoz.tixx.tixxbayserver.response.DiscountResponse;
import com.steinacoz.tixx.tixxbayserver.response.TicketResponse;
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
@RequestMapping("/tixxbay/api/discount/v1")
public class DiscountController {
    @Autowired
    DiscountRepo discountRepo;
    
    @CrossOrigin
    @RequestMapping(value = "/add-discount", method = RequestMethod.POST)
    public ResponseEntity<DiscountResponse> createDiscount(@RequestBody Discount disc){
        DiscountResponse dr = new DiscountResponse();
        
        if(disc.getNfcDiscount() == 0.0 || disc.getQrDiscount() == 0.0){
            dr.setMessage("discount cannot be zero");
            dr.setStatus("failed");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(dr);
        }
        
        try {
            Discount discount = discountRepo.save(disc);
            dr.setMessage("discount created");
            dr.setStatus("success");
            return ResponseEntity.ok().body(dr);
        }catch(Exception e){
            dr.setMessage("error occurred adding discount: " + e.getMessage());
            dr.setStatus("failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dr);
        }
    }
        
        
    @CrossOrigin
    @RequestMapping(value = "/edit-discount", method = RequestMethod.POST)
    public ResponseEntity<DiscountResponse> editDiscount(@RequestBody Discount disc){
        DiscountResponse dr = new DiscountResponse();
        
        if(disc.getNfcDiscount() == 0.0 || disc.getQrDiscount() == 0.0){
            dr.setMessage("discount cannot be zero");
            dr.setStatus("failed");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(dr);
        }
        
        Discount discount = discountRepo.findById(disc.getId()).orElseGet(null);
        
        if(discount != null){
            discount.setNfcDiscount(disc.getNfcDiscount());
            discount.setQrDiscount(disc.getQrDiscount());
            discount = discountRepo.save(discount);
            dr.setMessage("discount updated successfully");
            dr.setStatus("success");
            return ResponseEntity.ok().body(dr);
        }else{
           dr.setMessage("discount data not found");
            dr.setStatus("failed");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dr); 
        }
    }
    
    
}





