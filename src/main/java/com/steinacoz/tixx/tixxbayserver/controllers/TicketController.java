/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.steinacoz.tixx.tixxbayserver.dao.ChildTicketDao;
import com.steinacoz.tixx.tixxbayserver.dao.TicketDao;
import com.steinacoz.tixx.tixxbayserver.model.ChildTicket;
import com.steinacoz.tixx.tixxbayserver.model.Event;
import com.steinacoz.tixx.tixxbayserver.model.Ticket;
import com.steinacoz.tixx.tixxbayserver.repo.ChildTicketRepo;
import com.steinacoz.tixx.tixxbayserver.repo.EventRepo;
import com.steinacoz.tixx.tixxbayserver.repo.TicketRepo;
import com.steinacoz.tixx.tixxbayserver.request.CheckinTicketRequest;
import com.steinacoz.tixx.tixxbayserver.request.CreateChildTicketRequest;
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
    TicketRepo ticketRepo;
    
    @Autowired
    EventRepo eventRepo;
    
    @Autowired
    ChildTicketRepo ctRepo;
    
    private DateFormat datetime = new SimpleDateFormat("YY-MM-dd HH:mm:ss");
    
    @CrossOrigin
    @RequestMapping(value = "/add-ticket", method = RequestMethod.POST)
    public ResponseEntity<TicketResponse> createTicket(@RequestBody Ticket ticket){
        TicketResponse tr = new TicketResponse();
        ticket.setCreated(LocalDateTime.now());
        ticket.setUpdated(LocalDateTime.now());
        
        
        Event event = eventRepo.findById(ticket.getEventId()).orElseThrow(null);
        
        if(event != null){
          ticket.setTicketCode(event.getEventCode() + Utils.randomNS(8));  
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
         
        if(initializeVerifyResponse.getStatus() && statusCode == 200){
            if(initializeVerifyResponse.getData().getStatus().toLowerCase().equalsIgnoreCase("success")){
                Ticket parentTicket = ticketRepo.findByTicketCode(str.getParentTicketCode());
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
            cts.add(ct);
        }
        
                }else{
                    tr.setStatus("failed");
                tr.setMessage("parent ticket not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(tr);
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
    @RequestMapping(value = "/send-qr-email", method = RequestMethod.POST)
    public ResponseEntity<EventResponse> sendQRImageToEmail(@RequestParam("image") MultipartFile image, @RequestParam("email") String email, @RequestParam("eventId") String eventId){
        EventResponse er = new EventResponse();
        Event event = eventRepo.findById(eventId).orElseThrow(null);
        
        if(event != null){
            try {
                HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/sandboxf0a305f9cb84423c85c4f4f5c03e176e.mailgun.org") //Unirest.post("https://api.mailgun.net/v3/sandbox54745fe7bf41492087ca09fa024aae27.mailgun.org/messages")
                        .basicAuth("api", Utils.API_KEY)
                        .field("from", "info@tixxbay.com")
                        .field("to", email)
                        .field("subject", "QR ticket image for " + event.getTitle())
                        .field("text", "You recently purchased a ticket for " + event.getTitle())
                        .field("image", image.getBytes(), ContentType.IMAGE_PNG , "tixxbay-access-" + Utils.randomNS(6) + ".png")
                        .asJson();
                
                System.out.println(request.getBody());
                if(request.isSuccess()){
                    er.setStatus("success");
                    er.setMessage("image successfully sent to email");
                    return ResponseEntity.ok().body(er);
                }else{
                    er.setStatus("fail");
                    er.setMessage("email sending failed");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
                }
            } catch (IOException ex) {
                er.setStatus("fail");
                er.setMessage("error reading email");
                Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
            }        
        }else{
            er.setStatus("fail");
            er.setMessage("event not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er);
        }
    }
    
    
}















































