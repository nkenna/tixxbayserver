/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.steinacoz.tixx.tixxbayserver.dao.ChildTicketDao;
import com.steinacoz.tixx.tixxbayserver.dao.TicketDao;
import com.steinacoz.tixx.tixxbayserver.dao.UserDao;
import com.steinacoz.tixx.tixxbayserver.model.ChildTicket;
import com.steinacoz.tixx.tixxbayserver.model.Event;
import com.steinacoz.tixx.tixxbayserver.model.IssuedTicket;
import com.steinacoz.tixx.tixxbayserver.model.Location;
import com.steinacoz.tixx.tixxbayserver.model.Ticket;
import com.steinacoz.tixx.tixxbayserver.model.TicketSaleTransaction;
import com.steinacoz.tixx.tixxbayserver.model.Transaction;
import com.steinacoz.tixx.tixxbayserver.model.User;
import com.steinacoz.tixx.tixxbayserver.model.Wallet;
import com.steinacoz.tixx.tixxbayserver.model.WalletTransaction;
import com.steinacoz.tixx.tixxbayserver.repo.ChildTicketRepo;
import com.steinacoz.tixx.tixxbayserver.repo.EventRepo;
import com.steinacoz.tixx.tixxbayserver.repo.IssuedTicketRepo;
import com.steinacoz.tixx.tixxbayserver.repo.TicketRepo;
import com.steinacoz.tixx.tixxbayserver.repo.TicketSaleTransactionRepo;
import com.steinacoz.tixx.tixxbayserver.repo.UserRepo;
import com.steinacoz.tixx.tixxbayserver.repo.WalletRepo;
import com.steinacoz.tixx.tixxbayserver.repo.WalletTransactionRepo;
import com.steinacoz.tixx.tixxbayserver.request.CheckinTicketRequest;
import com.steinacoz.tixx.tixxbayserver.request.CreateChildTicketRequest;
import com.steinacoz.tixx.tixxbayserver.request.IssueTicketRequest;
import com.steinacoz.tixx.tixxbayserver.request.SellTicketReqest;
import com.steinacoz.tixx.tixxbayserver.response.EventResponse;
import com.steinacoz.tixx.tixxbayserver.response.InitializeVerifyResponse;
import com.steinacoz.tixx.tixxbayserver.response.TicketResponse;
import com.steinacoz.tixx.tixxbayserver.utils.Utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import kong.unirest.ContentType;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author nkenn
 */
@RestController
@RequestMapping("/tixxbay/api/ticket/v1")
public class TicketController {
    @Autowired
    UserRepo userRepo;
    
    @Autowired
    TicketRepo ticketRepo;
    
    @Autowired
    EventRepo eventRepo;
    
    @Autowired
    ChildTicketRepo ctRepo;
    
    @Autowired
    TicketSaleTransactionRepo ttRepo;
    
    @Autowired
    IssuedTicketRepo istRepo;
    
    @Autowired
    WalletRepo walletRepo;
    
    @Autowired
    WalletTransactionRepo walletTransRepo;
    
    private DateFormat datetime = new SimpleDateFormat("YY-MM-dd HH:mm:ss");
    
    @CrossOrigin
    @RequestMapping(value = "/add-ticket", method = RequestMethod.POST)
    public ResponseEntity<TicketResponse> createTicket(@RequestBody Ticket ticket){
        TicketResponse tr = new TicketResponse();
        ticket.setCreated(LocalDateTime.now());
        ticket.setUpdated(LocalDateTime.now());
        
        
        Event event = eventRepo.findById(ticket.getEventId()).orElseThrow(null);
        
        if(ticket.getAvailableTickets() > 0){
            int avt = event.getAvailableTicket() + ticket.getAvailableTickets();
            event.setAvailableTicket(avt);
            event.setUpdated(LocalDateTime.now());
            try{
                eventRepo.save(event);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        
        if(event != null){
          ticket.setTicketCode(event.getEventCode() + Utils.randomNS(4));  
        }else{
           tr.setStatus("failed");
            tr.setMessage("parent event not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(tr); 
        }
        
        try{
            
            Ticket newTicket = ticketRepo.save(ticket);
            TicketDao td = new TicketDao();
                BeanUtils.copyProperties(newTicket, td);
            tr.setStatus("success");
            tr.setTicket(td);
            tr.setMessage("Ticket created successful");
            return ResponseEntity.ok().body(tr);
        }catch(Exception e){
            tr.setStatus("failed");
            tr.setMessage("error occurred creating ticket: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(tr);
        }
    }
    
    @CrossOrigin
    @RequestMapping(value = "/add-child-ticket", method = RequestMethod.POST)
    public ResponseEntity<TicketResponse> createChildTicket(@RequestBody CreateChildTicketRequest ccr){
        TicketResponse tr = new TicketResponse();
        List<ChildTicket> cts = new ArrayList<>();       
   
        
        for(int i = 0; i < ccr.getNumToCreate(); i++){
            ChildTicket ct = new ChildTicket();
            ct.setTitle(ccr.getTicket().getTitle());
            ct.setDescription(ccr.getTicket().getDescription());
            ct.setTicketCategory(ccr.getTicket().getTicketCategory());
            ct.setPaidTicket(ccr.getTicket().isPaidTicket());
            ct.setTicketAmount(ccr.getTicket().getTicketAmount());
            ct.setEventId(ccr.getTicket().getEventId());
            ct.setTicketType(ccr.getTicket().getTicketType());
            ct.setTicketCode(ccr.getTicket().getTicketCode() + Utils.randomNS(8));
            ct.setParentTicketCode(ccr.getTicket().getTicketCode());
            ct.setEventCode(ccr.getTicket().getEventCode());
            ct.setCouponId(ccr.getTicket().getCouponId());
            ct.setIndividual(ccr.getTicket().isIndividual());
            ct.setSaleStartDay(ccr.getTicket().getSaleStartDay());
            ct.setSaleEndDay(ccr.getTicket().getSaleEndDay());
            ct.setCreated(LocalDateTime.now());
            ct.setUpdated(LocalDateTime.now()); 
            ct.setParentTicketId(ccr.getTicket().getId());
            cts.add(ct);
        }          
        
        
        try{            
            List<ChildTicket> newCTs = ctRepo.insert(cts);
            tr.setChildTickets(newCTs);
            tr.setStatus("success");
            tr.setMessage("Child Tickets created successful");
            return ResponseEntity.ok().body(tr);
        }catch(Exception e){
            tr.setStatus("failed");
            tr.setMessage("error occurred creating tickets: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(tr);
        }
    }
    
    @CrossOrigin
    @RequestMapping(value = "/issue-ticket", method = RequestMethod.POST)
    public ResponseEntity<TicketResponse> issueChildTicket(@RequestBody IssueTicketRequest itr){
        TicketResponse tr = new TicketResponse();
        List<ChildTicket> cts = new ArrayList<>();
        IssuedTicket it = new IssuedTicket();
      
        List<String> codes = new ArrayList<>();
        
              
       Ticket parentTicket = ticketRepo.findByTicketCode(itr.getTicketCode());
       Event event = eventRepo.findById(itr.getEventId()).orElseThrow(null);
       
              
       if(parentTicket != null && event != null){
          for(int i = 0; i < itr.getQuantity(); i++){
            ChildTicket ct = new ChildTicket();
            ct.setTitle(parentTicket.getTitle());
            ct.setDescription(parentTicket.getDescription());
            ct.setTicketCategory(parentTicket.getTicketCategory());
            ct.setPaidTicket(parentTicket.isPaidTicket());
            ct.setTicketAmount(parentTicket.getTicketAmount());
            ct.setEventId(parentTicket.getEventId());
            ct.setTicketType(parentTicket.getTicketType());
            ct.setTicketCode(parentTicket.getTicketCode() + Utils.randomNS(8));
            ct.setParentTicketCode(parentTicket.getTicketCode());
            ct.setEventCode(parentTicket.getEventCode());
            ct.setCouponId(parentTicket.getCouponId());
            ct.setIndividual(parentTicket.isIndividual());
            ct.setSaleStartDay(parentTicket.getSaleStartDay());
            ct.setSaleEndDay(parentTicket.getSaleEndDay());
            ct.setCreated(LocalDateTime.now());
            ct.setUpdated(LocalDateTime.now()); 
            ct.setParentTicketId(parentTicket.getId());
            cts.add(ct);
            
            codes.add(ct.getTicketCode());
        } 
          
          User issuer = userRepo.findByUsername(itr.getIssuerUsername());
          User issuedTo = userRepo.findByUsername(itr.getIssuedToUsername());
      
          
          
          
          it.setTicketTitle(parentTicket.getTitle());
          it.setTicketCode(codes);
          it.setEventCode(itr.getEventCode());
          it.setIssuedDate(LocalDateTime.now());
          it.setQuantity(itr.getQuantity());
          it.setTotalAmount(new BigDecimal(itr.getQuantity() * parentTicket.getTicketAmount().doubleValue()));
          it.setUnitAmunt(new BigDecimal(it.getTotalAmount().doubleValue() / itr.getQuantity()));
          it.setIssuer(issuer);
          it.setIssuedTo(issuedTo);
          
       }else{
           tr.setStatus("failed");
            tr.setMessage("parent ticket or event not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(tr);
       }
        
        try{            
            istRepo.save(it);
            List<ChildTicket> newCTs = ctRepo.insert(cts);
            tr.setChildTickets(newCTs);
            tr.setStatus("success");
            tr.setMessage("Child Tickets issued successful");
            return ResponseEntity.ok().body(tr);
        }catch(Exception e){
            tr.setStatus("failed");
            tr.setMessage("error occurred creating tickets: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(tr);
        }
    }
    
    
    @CrossOrigin
    @RequestMapping(value = "/edit-ticket-category", method = RequestMethod.POST)
    public ResponseEntity<TicketResponse> editEventCategoryTicket(@RequestBody Ticket ticket){
        TicketResponse tr = new TicketResponse();
        
        
        Ticket foundTicket =  ticketRepo.findById(ticket.getId()).orElseGet(null);
           
        if(foundTicket != null){
            foundTicket.setTitle(ticket.getTitle());
            foundTicket.setDescription(ticket.getDescription());
            foundTicket.setTicketCategory(ticket.getTicketCategory());
            foundTicket.setTicketAmount(ticket.getTicketAmount());
            foundTicket.setPaidTicket(ticket.isPaidTicket());
            foundTicket.setEventId(ticket.getEventId());
            foundTicket.setEventCode(ticket.getEventCode());
            foundTicket.setTicketType(ticket.getTicketType());
            foundTicket.setTicketCode(ticket.getTicketCode());
            foundTicket.setCouponId(ticket.getCouponId());
            foundTicket.setIndividual(ticket.isIndividual());
            foundTicket.setSaleStartDay(ticket.getSaleStartDay());
            foundTicket.setSaleEndDay(ticket.getSaleEndDay());
            foundTicket.setUpdated(LocalDateTime.now());
            
            try{
                Ticket newTicket = ticketRepo.save(foundTicket);
                TicketDao td = new TicketDao();
                BeanUtils.copyProperties(newTicket, td);
                tr.setStatus("success");
                tr.setMessage("ticket category updated successfully");
                tr.setTicket(td);
                return ResponseEntity.ok().body(tr);
            }catch(Exception e){
                tr.setStatus("failed");
                tr.setMessage("ticket category updated failed: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(tr);
            }
            
        }else{
          tr.setStatus("failed");
          tr.setMessage("ticket category not found");
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(tr);  
        }
        
    }

    @CrossOrigin
    @RequestMapping(value = "/all-ticket-categories", method = RequestMethod.GET)
    public ResponseEntity<TicketResponse> allTickets(){
        TicketResponse er = new TicketResponse();
        List<TicketDao> tickets = ticketRepo.aggregateAllTicketCategories();
        er.setMessage("tickets found: " + String.valueOf(tickets.size()));
        er.setStatus("success");
        er.setTickets(tickets);
        return ResponseEntity.ok().body(er);
    }
    
    @CrossOrigin
    @RequestMapping(value = "/all-ticket-categories-by-event", method = RequestMethod.PUT)
    public ResponseEntity<TicketResponse> allTicketsCategoriesByEvent(@RequestBody Ticket ticket){
        TicketResponse er = new TicketResponse();
        List<TicketDao> tickets = ticketRepo.aggregateAllTicketCategoriesByEvent(ticket.getEventCode());
        er.setMessage("tickets found: " + String.valueOf(tickets.size()));
        er.setStatus("success");
        er.setTickets(tickets);
        return ResponseEntity.ok().body(er);
    }
    
    @CrossOrigin
    @RequestMapping(value = "/ticket-category", method = RequestMethod.PUT)
    public ResponseEntity<TicketResponse> getTicketCategory(@RequestBody Ticket ticket){
        TicketResponse er = new TicketResponse();
        TicketDao foundTicket = ticketRepo.getTicketCategory(ticket.getTicketCode());
        if(foundTicket != null){
          er.setMessage("ticket retrieved successfully found");
            er.setStatus("success");
            er.setTicket(foundTicket);
            return ResponseEntity.ok().body(er);  
        }else{
           er.setMessage("ticket not found");
            er.setStatus("failed");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er);  
        }
        
    }
    
    @CrossOrigin
    @RequestMapping(value = "/ticket-category-by-child-ticket", method = RequestMethod.PUT)
    public ResponseEntity<TicketResponse> getTicketCategoryByChildTicket(@RequestBody ChildTicket ticket){
        TicketResponse er = new TicketResponse();
        TicketDao foundTicket = ticketRepo.getTicketCategoryByChildTicket(ticket.getParentTicketCode());
        if(foundTicket != null){
          er.setMessage("ticket retrieved successfully found");
            er.setStatus("success");
            er.setTicket(foundTicket);
            return ResponseEntity.ok().body(er);  
        }else{
           er.setMessage("ticket not found");
            er.setStatus("failed");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er);  
        }
        
    }
    
    @CrossOrigin
    @RequestMapping(value = "/make-ticket-category-private-public", method = RequestMethod.PUT)
    public ResponseEntity<TicketResponse> ticketCategoryPrivatePublic (@RequestBody Ticket ticket){
        TicketResponse er = new TicketResponse();
        Ticket foundTicket = ticketRepo.findById(ticket.getId()).orElseGet(null);
        if(foundTicket != null){
            if(ticket.getTicketCategory().toLowerCase().equalsIgnoreCase("public")){
                foundTicket.setTicketCategory("public");
            }else if(ticket.getTicketCategory().toLowerCase().equalsIgnoreCase("private")){
               foundTicket.setTicketCategory("private"); 
            }else{
                er.setMessage("invalid parameter was provided");
                er.setStatus("failed");
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(er);   
            }
            Ticket newTicket = ticketRepo.save(foundTicket);
            TicketDao td = new TicketDao();
            BeanUtils.copyProperties(newTicket, td);
            er.setMessage("ticket category changed successfully: " + newTicket.getTicketCategory());
            er.setStatus("success");
            er.setTicket(td);
            return ResponseEntity.ok().body(er);  
        }else{
           er.setMessage("ticket not found");
            er.setStatus("failed");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er);  
        }
        
    }
    
    
            
    @CrossOrigin
    @RequestMapping(value = "/sell-ticket", method = RequestMethod.POST)
    public ResponseEntity<TicketResponse> sellTicket (@RequestBody SellTicketReqest str){
        TicketResponse tr = new TicketResponse();
        
        if(str.getEventCode() == null || str.getEventCode().isEmpty()){
            tr.setStatus("failed");
            tr.setMessage("event code is required");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(tr);
        }else if(str.getParentTicketCode() == null || str.getParentTicketCode().isEmpty()){
            tr.setStatus("failed");
            tr.setMessage("parent ticket code is required");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(tr);
        }else if(str.getQuantity() == 0){
            tr.setStatus("failed");
            tr.setMessage("ticket quantity cannot be zero");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(tr);
        }
        
        Ticket parentTicket = ticketRepo.findByTicketCode(str.getParentTicketCode());
        
        List<ChildTicket> cts = new ArrayList<>();       
   
        
        for(int i = 0; i < str.getQuantity(); i++){
            ChildTicket ct = new ChildTicket();
            ct.setTitle(parentTicket.getTitle());
            ct.setDescription(parentTicket.getDescription());
            ct.setTicketCategory(parentTicket.getTicketCategory());
            ct.setPaidTicket(parentTicket.isPaidTicket());
            ct.setTicketAmount(parentTicket.getTicketAmount());
            ct.setEventId(parentTicket.getEventId());
            ct.setTicketType(parentTicket.getTicketType());
            ct.setTicketCode(parentTicket.getTicketCode() + Utils.randomNS(8));
            ct.setParentTicketCode(parentTicket.getTicketCode());
            ct.setEventCode(parentTicket.getEventCode());
            ct.setCouponId(parentTicket.getCouponId());
            ct.setIndividual(parentTicket.isIndividual());
            ct.setSaleStartDay(parentTicket.getSaleStartDay());
            ct.setSaleEndDay(parentTicket.getSaleEndDay());
            ct.setCreated(LocalDateTime.now());
            ct.setUpdated(LocalDateTime.now()); 
            ct.setParentTicketId(parentTicket.getId());
            cts.add(ct);
        }
        
        
        
        try{            
            List<ChildTicket> newCTs = ctRepo.insert(cts);
            tr.setChildTickets(newCTs);
            tr.setStatus("success");
            tr.setMessage("Child Tickets created successful");
            return ResponseEntity.ok().body(tr);
        }catch(Exception e){
            tr.setStatus("failed");
            tr.setMessage("error occurred creating tickets: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(tr);
        }
        
        
    }
    
    
    @CrossOrigin
    @RequestMapping(value = "/sell-ticket-by-verify", method = RequestMethod.POST)
    public ResponseEntity<TicketResponse> sellTicketByRef (@RequestBody SellTicketReqest str){
        TicketResponse tr = new TicketResponse();
        System.out.println(str.toString());
        System.out.println(str.getBoughtByEmail());
        InitializeVerifyResponse initializeVerifyResponse = new InitializeVerifyResponse();
         StringBuilder result = new StringBuilder();
         int statusCode = 0;
        
        if(str.getEventCode() == null || str.getEventCode().isEmpty()){
            tr.setStatus("failed");
            tr.setMessage("event code is required");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(tr);
        }else if(str.getParentTicketCode() == null || str.getParentTicketCode().isEmpty()){
            tr.setStatus("failed");
            tr.setMessage("parent ticket code is required");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(tr);
        }else if(str.getQuantity() == 0){
            tr.setStatus("failed");
            tr.setMessage("ticket quantity cannot be zero");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(tr);
        }else if(str.getReference() == null || str.getReference().isEmpty()){
            tr.setStatus("failed");
            tr.setMessage("payment reference is needed");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(tr);
        }
        
        try {
	            // convert transaction to json then use it as a body to post json
	            Gson gson = new Gson();
	            // add paystack chrges to the amount
	            //StringEntity postingString = new StringEntity(gson.toJson(request));
	            HttpClient client = HttpClientBuilder.create().build();
	            HttpGet getReq = new HttpGet("https://api.paystack.co/transaction/verify/"+ str.getReference());
	            //HttpPost post = new HttpPost("https://api.paystack.co/transaction/initialize");
	            //post.setEntity(postingString);
	            getReq.addHeader("Content-type", "text/plain");
	            getReq.addHeader("Authorization", "Bearer sk_test_36c529556d2463dac5aa8258522be46456013ee2");
	           
	            org.apache.http.HttpResponse response = client.execute(getReq);
                    statusCode = response.getStatusLine().getStatusCode();
	            if (response.getStatusLine().getStatusCode() == 200) {
	                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

	                String line;
	                while ((line = rd.readLine()) != null) {
	                    result.append(line);
	                }

	            } else if (response.getStatusLine().getStatusCode() == 500) {
	            	initializeVerifyResponse.setMessage("error");
	            }
	            else {
                        tr.setStatus("failed");
                        tr.setMessage("error verifying transaction");
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(tr);
	                
	            }
	            ObjectMapper mapper = new ObjectMapper();

	            initializeVerifyResponse = mapper.readValue(result.toString(), InitializeVerifyResponse.class);
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            initializeVerifyResponse.setMessage("error: 500");
	           // throw new Exception("Failure initializaing paystack transaction");
	        }
        
        //request.getBody().;
        System.out.println(result);
        List<ChildTicket> cts = new ArrayList<>(); 
        List<String> tCodes = new ArrayList<>(); 
       
  
         
        if(initializeVerifyResponse.getStatus() && statusCode == 200){
            if(initializeVerifyResponse.getData().getStatus().toLowerCase().equalsIgnoreCase("success")){
                // create transcation data
                TicketSaleTransaction trans = new TicketSaleTransaction();
                trans.setTransRef("TIXX" + Utils.randomNS(10));
                trans.setPayRef(str.getReference());
                trans.setPayId(String.valueOf(initializeVerifyResponse.getData().getId()));
                trans.setTransDate(LocalDateTime.now());
                trans.setTransStatus(true);
                trans.setPayStatus(initializeVerifyResponse.getData().getStatus());
                trans.setTotalAmount(new BigDecimal(initializeVerifyResponse.getData().getAmount()/100));
                trans.setUnitAmount(new BigDecimal((initializeVerifyResponse.getData().getAmount()/100) / str.getQuantity()));
                trans.setTransType(Utils.buyTicket);
                trans.setQuantity(str.getQuantity());              
                
                
                User user = userRepo.findByEmail(str.getBoughtByEmail());
                UserDao udao = new UserDao();
                BeanUtils.copyProperties(user, udao);
                trans.setBoughtBy(udao);
                trans.setEventCode(str.getEventCode());
                trans.setNarration("user bought ticket");
                trans.setLocation(str.getLocation());
                Ticket parentTicket = ticketRepo.findByTicketCode(str.getParentTicketCode());
                int pavt = parentTicket.getAvailableTickets() - str.getQuantity();
                parentTicket.setAvailableTickets(pavt);
                ticketRepo.save(parentTicket);
               // System.out.println(parentTicket.getTitle());
               
                if(parentTicket != null){ 
                    for(int i = 0; i < str.getQuantity(); i++){
                        ChildTicket ct = new ChildTicket();
                        ct.setTitle(parentTicket.getTitle());
                        ct.setDescription(parentTicket.getDescription());
                        ct.setTicketCategory(parentTicket.getTicketCategory());
                        ct.setPaidTicket(parentTicket.isPaidTicket());
                        ct.setTicketAmount(parentTicket.getTicketAmount());
                        ct.setEventId(parentTicket.getEventId());
                        ct.setTicketType(parentTicket.getTicketType());
                        ct.setTicketCode(parentTicket.getTicketCode() + Utils.randomNS(8));
                        ct.setParentTicketCode(parentTicket.getTicketCode());
                        ct.setEventCode(parentTicket.getEventCode());
                        ct.setCouponId(parentTicket.getCouponId());
                        ct.setIndividual(parentTicket.isIndividual());
                        ct.setSaleStartDay(parentTicket.getSaleStartDay());
                        ct.setSaleEndDay(parentTicket.getSaleEndDay());
                        ct.setCreated(LocalDateTime.now());
                        ct.setUpdated(LocalDateTime.now()); 
                        ct.setParentTicketId(parentTicket.getId());
                        tCodes.add(ct.getTicketCode());
                        cts.add(ct);
                    }
                    trans.setTicketTitle(parentTicket.getTitle());
                    trans.setTicketCodes(tCodes);
                    
                    
                    // credit event manager wallet
                    Event event = eventRepo.findByEventCode(str.getEventCode());
                    if(event != null){
                        int avt = event.getAvailableTicket() - str.getQuantity();
                        event.setAvailableTicket(avt);
                        try{
                            eventRepo.save(event);
                        }catch(Exception e){
                            System.out.println(e.getMessage());
                        }
                       User manager = userRepo.findByUsername(event.getCreatorUsername());
                       if(manager != null){
                           Wallet wallet = walletRepo.findByWalletid(manager.getWalletId());
                            if(wallet != null){
                                double charge = 9; //normal charge is 9%
                                if(parentTicket.getTicketType().equalsIgnoreCase("QR")){
                                     charge =  7.5; //charge 7.5% if ticket is qr code
                                     
                                }
                                double newBalance = trans.getTotalAmount().doubleValue() - ((charge/100) * trans.getTotalAmount().doubleValue());
                                wallet.setBalance(wallet.getBalance().add(new BigDecimal(newBalance)));
                                wallet.setUpdateddate(LocalDateTime.now());
                                Wallet nWallet = walletRepo.save(wallet);
               
                                
                                //build wallet transaction record
                                WalletTransaction wt = new WalletTransaction();
                                wt.setTransRef("TIXX" + Utils.randomNS(12));
                                wt.setTransDate(LocalDateTime.now());
                                wt.setTotalAmount(nWallet.getBalance());
                                wt.setTransType(Utils.creditWallet);
                                wt.setBoughtBy(udao);
                                wt.setWalletId(nWallet.getWalletid());
                                wt.setWalletOwnerUsername(nWallet.getOwnerUsername());
                                wt.setEventCode(event.getEventCode());
                                wt.setNarration(trans.getNarration());
                                wt.setLocation(str.getLocation());
                                wt.setTicketDiscount(charge);
                                wt.setTicketCodes(tCodes);
                                walletTransRepo.save(wt);
                            }else{
                               tr.setStatus("failed");
                                tr.setMessage("wallet not found");
                                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(tr); 
                            }
                       } else{
                         tr.setStatus("failed");
                        tr.setMessage("manager not found");
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(tr);  
                       } 
                    }else{
                        tr.setStatus("failed");
                        tr.setMessage("event not found");
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(tr);
                    }
                    
        
        
                }else{
                    tr.setStatus("failed");
                    tr.setMessage("parent ticket not found");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(tr);
                }
          
        try{            
            List<ChildTicket> newCTs = ctRepo.insert(cts);
            ttRepo.save(trans); 
            tr.setChildTickets(newCTs);
            tr.setStatus("success");
            tr.setMessage("Child Tickets created successful");
            return ResponseEntity.ok().body(tr);
        }catch(Exception e){
            tr.setStatus("failed");
            tr.setMessage("error occurred creating tickets: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(tr);
        }
            }else{
                // status is not success
                tr.setStatus("failed");
                tr.setMessage("payment seems not be successful");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(tr);
            }
        }else{
            //status not verify
            tr.setStatus("failed");
            tr.setMessage("payment not verified");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(tr);
        }     
        
        
        
    }
    
    @CrossOrigin
    @RequestMapping(value = "/checkin-ticket", method = RequestMethod.POST)
    public ResponseEntity<TicketResponse> sellTicket (@RequestBody CheckinTicketRequest ctr){
        TicketResponse tr = new TicketResponse();
        ChildTicket ct = ctRepo.findByTicketCode(ctr.getTicketCode());
        
                
        if(ct != null){
            if(ct.isCheckedIn()){
                tr.setStatus("failed");
                tr.setMessage("ticket have been checked in before");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(tr);
            }
            
            ct.setCheckedIn(true);
            ct.setCheckedinTime(LocalDateTime.now());
            ctRepo.save(ct);
            
            ChildTicketDao ctDao = ctRepo.getChildTicketByTicketCode(ct.getTicketCode());
            tr.setTicketCheckin(ctDao);
            tr.setStatus("success");
            tr.setMessage("ticket checked in successfully");
            return ResponseEntity.ok().body(tr);
        }else{
            tr.setStatus("failed");
            tr.setMessage("ticket not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(tr);
        }
        
        
    }
    
    @CrossOrigin
    @RequestMapping(value = "/get-child-ticket", method = RequestMethod.POST)
    public ResponseEntity<TicketResponse> getChildTicket (@RequestBody CheckinTicketRequest ctr){
        TicketResponse tr = new TicketResponse();
        ChildTicket ct = ctRepo.findByTicketCode(ctr.getTicketCode());
        
                
        if(ct != null){
            if(ct.isCheckedIn()){
                tr.setStatus("failed");
                tr.setMessage("ticket have been checked in before");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(tr);
            } 
            
            ct.setCheckedIn(true);
            ct.setCheckedinTime(LocalDateTime.now());
            ctRepo.save(ct);
            
            Event event = eventRepo.findByEventCode(ct.getEventCode());
            if(event != null){
                System.out.println(event.getCategoryName());
                System.out.println(event.getDiscription());
                event.setCheckedInTicket(event.getCheckedInTicket() + 1);
            }
            
            ChildTicketDao ctDao = ctRepo.getChildTicketByTicketCode(ct.getTicketCode());
            tr.setTicketCheckin(ctDao);
            tr.setStatus("success");
            tr.setMessage("ticket retrieved");
            return ResponseEntity.ok().body(tr);
        }else{
            tr.setStatus("failed");
            tr.setMessage("ticket not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(tr);
        }
        
        
    }
    
    @CrossOrigin
    @RequestMapping(value = "/send-qr-email", method = RequestMethod.POST)
    public ResponseEntity<EventResponse> sendQRImageToEmail(@RequestParam("image") MultipartFile image, @RequestParam("email") String email, @RequestParam("eventId") String eventId) throws IOException{
        EventResponse er = new EventResponse();
        Event event = eventRepo.findById(eventId).orElseThrow(null);
        
        if(event != null){
          
                Email from = new Email("support@tixxbay.com");
                String subject = "QR ticket image for " + event.getTitle();
                Email to = new Email(email);
                Content content = new Content("text/plain", "You recently purchased a ticket for " + event.getTitle());
                Mail mail = new Mail(from, subject, to, content);
                System.out.println(mail.from.getEmail());
    
    byte[] filedata= image.getBytes();   
    
    Base64 x = new Base64();
    String imageDataString = x.encodeAsString(filedata);
    Attachments attachments3 = new Attachments();
       attachments3.setContent(imageDataString);
       attachments3.setType("image/png");//"application/pdf"
       attachments3.setFilename("tixxbay-access-" + Utils.randomNS(6) + ".png");
       attachments3.setDisposition("attachment");
       attachments3.setContentId("Banner");
       mail.addAttachments(attachments3);
       
    
    SendGrid sg = new SendGrid(System.getenv("SENDGRID_API")); 
    Request request = new Request();
    try {
      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());
      Response response = sg.api(request);
      System.out.println(response.getStatusCode());
      System.out.println(response.getBody());
      System.out.println(response.getHeaders());
      er.setStatus("success");
                    er.setMessage("image successfully sent to email");
                    return ResponseEntity.ok().body(er);
    } catch (IOException ex) {
      er.setStatus("fail");
                    er.setMessage("email sending failed");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
    }
              
                        //.field("image", image.getBytes(), ContentType.IMAGE_PNG , "tixxbay-access-" + Utils.randomNS(6) + ".png")
                   
            
                /**if(request.isSuccess()){
                    er.setStatus("success");
                    er.setMessage("image successfully sent to email");
                    return ResponseEntity.ok().body(er);
                }else{
                    er.setStatus("fail");
                    er.setMessage("email sending failed");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
                }**/
           /** } catch (IOException ex) {
                er.setStatus("fail");
                er.setMessage("error reading email");
                Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
            }        **/
        }else{
            er.setStatus("fail");
            er.setMessage("event not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er);
        }
    }
    
    
}





















































































































