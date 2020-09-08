/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.controllers;

import com.steinacoz.tixx.tixxbayserver.model.Event;
import com.steinacoz.tixx.tixxbayserver.model.MailingList;
import com.steinacoz.tixx.tixxbayserver.repo.MailingListRepo;
import com.steinacoz.tixx.tixxbayserver.repo.StateRepo;
import com.steinacoz.tixx.tixxbayserver.request.MailingRequest;
import com.steinacoz.tixx.tixxbayserver.response.EventResponse;
import com.steinacoz.tixx.tixxbayserver.response.MailingListResponse;
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
@RequestMapping("/tixxbay/api/mailing/v1")
public class MailingListController {
    @Autowired
    MailingListRepo mlRepo;
    
    @CrossOrigin
    @RequestMapping(value = "/add-email-list", method = RequestMethod.POST)
    public ResponseEntity<MailingListResponse> addEmail(@RequestBody MailingRequest mlr){
        MailingListResponse ml = new MailingListResponse();
        
        if(mlr.getEmail() == null || mlr.getEmail().isEmpty()){
            ml.setStatus("failed");
            ml.setMessage("email is required");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ml);
        }
        
        if(mlRepo.findByEmail(mlr.getEmail()) != null){
            ml.setStatus("failed");
            ml.setMessage("email is already subscribed");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ml);
        }
        
        try{
            MailingList mal = new MailingList();
            mal.setEmail(mlr.getEmail());
            mal =  mlRepo.save(mal);
            ml.setStatus("success");
            ml.setMessage("email successfully subscribed");
            return ResponseEntity.ok().body(ml);
        }catch(Exception e){
           ml.setStatus("failed");
            ml.setMessage("error occurred saving data: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ml); 
        }
    }
    
    @CrossOrigin
    @RequestMapping(value = "/remove-email-list", method = RequestMethod.POST)
    public ResponseEntity<MailingListResponse> removeEmail(@RequestBody MailingRequest mlr){
        MailingListResponse ml = new MailingListResponse();
        
        MailingList mal = mlRepo.findByEmail(mlr.getEmail());
        
        if(mal == null){
            ml.setStatus("failed");
            ml.setMessage("email is have already been subscribed");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ml);
        }else{
            mlRepo.deleteById(mal.getId());
            ml.setStatus("success");
            ml.setMessage("email successfully removed");
            return ResponseEntity.ok().body(ml);
        }
        
        
    }
    
    @CrossOrigin
    @RequestMapping(value = "/all-email-list", method = RequestMethod.GET)
    public ResponseEntity<MailingListResponse> allEmail(){
        MailingListResponse ml = new MailingListResponse();
        List<MailingList> mls =  mlRepo.findAll();
        ml.setStatus("success");
        ml.setMessage("number of emails in mailing list: " + String.valueOf(mls.size()));
        return ResponseEntity.ok().body(ml);
    }
}









