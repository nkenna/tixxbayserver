/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.controllers;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.steinacoz.tixx.tixxbayserver.model.Event;
import com.steinacoz.tixx.tixxbayserver.model.Payout;
import com.steinacoz.tixx.tixxbayserver.model.User;
import com.steinacoz.tixx.tixxbayserver.model.Wallet;
import com.steinacoz.tixx.tixxbayserver.repo.EventRepo;
import com.steinacoz.tixx.tixxbayserver.repo.PayoutRepo;
import com.steinacoz.tixx.tixxbayserver.repo.UserRepo;
import com.steinacoz.tixx.tixxbayserver.repo.WalletRepo;
import com.steinacoz.tixx.tixxbayserver.request.PayoutRequest;
import com.steinacoz.tixx.tixxbayserver.response.EventResponse;
import com.steinacoz.tixx.tixxbayserver.response.PayoutResponse;
import com.steinacoz.tixx.tixxbayserver.utils.Utils;
import java.time.LocalDateTime;
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
@RequestMapping("/tixxbay/api/payout/v1")
public class PayoutController {
    @Autowired
    EventRepo eventRepo;
    
    @Autowired
    UserRepo userRepo;
    
    @Autowired
    PayoutRepo payoutRepo;
    
    @Autowired
    WalletRepo walletRepo;
    
    @CrossOrigin
    @RequestMapping(value = "/request-payout", method = RequestMethod.POST)
    public ResponseEntity<PayoutResponse> requestPayout(@RequestBody PayoutRequest payReq){
        PayoutResponse pr = new PayoutResponse();
        if(payReq.getUsername() == null || payReq.getUsername().isEmpty()){
            pr.setStatus("failed");
            pr.setMessage("username is required");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(pr);
        }
        
        if(payReq.getPayType() == null || payReq.getPayType().isEmpty()){
            pr.setStatus("failed");
            pr.setMessage("pay type is required. Pay type can either be TICKET PAY or VENDOR PAY");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(pr);
        }
        
        if(payReq.getPayType().equalsIgnoreCase("TICKET PAY") == false || payReq.getPayType().equalsIgnoreCase("VENDOR PAY") == false){
            pr.setStatus("failed");
            pr.setMessage("invalid pay type. Pay type can either be TICKET PAY or VENDOR PAY");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(pr);
        }
        
        if(payReq.getEventCode() == null || payReq.getEventCode().isEmpty()){
            pr.setStatus("failed");
            pr.setMessage("event code is required");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(pr);
        } 
        
        if(payReq.getWalletId() == null || payReq.getWalletId().isEmpty()){
            pr.setStatus("failed");
            pr.setMessage("wallet id is required");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(pr);
        }  
        
        User user = userRepo.findByUsername(payReq.getUsername());
        if(user == null){
            pr.setStatus("failed");
            pr.setMessage("user not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pr);
        } 
        
        Wallet wallet = walletRepo.findByWalletid(payReq.getWalletId());
        if(wallet == null){
            pr.setStatus("failed");
            pr.setMessage("user wallet not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pr);
        } 
        
        Event event = eventRepo.findByEventCode(payReq.getEventCode());
        if(event == null){
            pr.setStatus("failed");
            pr.setMessage("event not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pr);
        } 
       
        //every check is fine
        Payout payout = new Payout();
        payout.setCreated(LocalDateTime.now());
        payout.setUpdated(LocalDateTime.now());
        payout.setUsername(payReq.getUsername());
        payout.setWalletId(payReq.getWalletId());
        payout.setEventCode(payReq.getEventCode());
        payout.setStatus(Utils.payPending);
        payout.setPayType(payReq.getPayType());
        
        try{
            Payout newPayout = payoutRepo.save(payout);
            
            //send Email
            Email from = new Email("support@tixxbay.com");
            String subject = "TixxBay payout request";
            Email to = new Email(user.getEmail());
            Content content = new Content("text/plain", "You requested a payout. Your payout is being processed");
            Mail mail = new Mail(from, subject, to, content);
            System.out.println(mail.from.getEmail());
            SendGrid sg = new SendGrid(System.getenv("SENDGRID_API")); 
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());  
            
            
            //send Email
            from = new Email("support@tixxbay.com");
            subject = "TixxBay payout request";
            to = new Email("support@tixxbay.com");
            content = new Content("text/plain", "A payout have been requested. Open dashboard to fulfil it");
            mail = new Mail(from, subject, to, content);
            System.out.println(mail.from.getEmail());
            sg = new SendGrid(System.getenv("SENDGRID_API")); 
            request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
                          
            pr.setPayout(newPayout);
            pr.setMessage("payout requested successfully");
            pr.setStatus("success");
            return ResponseEntity.ok().body(pr);
        }catch(Exception e){
            pr.setStatus("failed");
            pr.setMessage("error occurred saving payout: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pr);
        }
        
    }
    
    
    @CrossOrigin
    @RequestMapping(value = "/update-payout-status", method = RequestMethod.POST)
    public ResponseEntity<PayoutResponse> createEvent(@RequestBody PayoutRequest payReq){
        PayoutResponse pr = new PayoutResponse();
        if(!payReq.getStatus().equalsIgnoreCase(Utils.payPaid) || !payReq.getStatus().equalsIgnoreCase(Utils.payPending) || !payReq.getStatus().equalsIgnoreCase(Utils.payDeclined)){
            pr.setStatus("failed");
            pr.setMessage("invalid pay status.");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(pr);
        }
        
        Payout payout = payoutRepo.findById(payReq.getPayoutId()).orElseGet(null);
        
        if(payout != null){
            User user = userRepo.findByUsername(payout.getUsername());
            if(user == null){
                pr.setStatus("failed");
                pr.setMessage("user not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pr);
            }
            payout.setStatus(payReq.getStatus());
            payout.setUpdated(LocalDateTime.now());
            
            try{
                Payout updatedPayout = payoutRepo.save(payout);
            
                //send out email to both admin and user
                //send Email
                Email from = new Email("support@tixxbay.com");
                String subject = "TixxBay payout request";
                Email to = new Email(user.getEmail());
                Content content = new Content();
                if(payReq.getStatus().equalsIgnoreCase(Utils.payPaid)){
                    content.setType("text/plain");
                    content.setValue("Your requested payout have been processed and posted. Expect your funds soon.");
                }else if(payReq.getStatus().equalsIgnoreCase(Utils.payDeclined)){
                    content.setType("text/plain");
                    content.setValue("Your requested payout have been declined. Please contact support for rectification.");
                }else if(payReq.getStatus().equalsIgnoreCase(Utils.payDeclined)){
                    content.setType("text/plain");
                    content.setValue("Your requested payout is been processed");
                }
                //Content content = new Content("text/plain", "You requested a payout. Your payout is being processed");
                Mail mail = new Mail(from, subject, to, content);
                System.out.println(mail.from.getEmail());
                SendGrid sg = new SendGrid(System.getenv("SENDGRID_API")); 
                Request request = new Request();
                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");
                request.setBody(mail.build());
                Response response = sg.api(request);
                System.out.println(response.getStatusCode());
                System.out.println(response.getBody());
                System.out.println(response.getHeaders());  

                pr.setPayout(updatedPayout);
                pr.setMessage("payout updated successfully");
                pr.setStatus("success");
                return ResponseEntity.ok().body(pr);
            }catch(Exception e){
                pr.setStatus("failed");
                pr.setMessage("error occurred updating payout: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pr);
            }
            
        }else{
           pr.setStatus("failed");
           pr.setMessage("payout not found");
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pr); 
        }
        
    }
}







