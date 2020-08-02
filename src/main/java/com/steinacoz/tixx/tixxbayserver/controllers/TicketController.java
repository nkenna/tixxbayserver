/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.controllers;

import com.steinacoz.tixx.tixxbayserver.model.ChildTicket;
import com.steinacoz.tixx.tixxbayserver.model.Event;
import com.steinacoz.tixx.tixxbayserver.model.Ticket;
import com.steinacoz.tixx.tixxbayserver.repo.ChildTicketRepo;
import com.steinacoz.tixx.tixxbayserver.repo.EventRepo;
import com.steinacoz.tixx.tixxbayserver.repo.TicketRepo;
import com.steinacoz.tixx.tixxbayserver.request.CreateChildTicketRequest;
import com.steinacoz.tixx.tixxbayserver.response.EventResponse;
import com.steinacoz.tixx.tixxbayserver.response.TicketResponse;
import com.steinacoz.tixx.tixxbayserver.utils.Utils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
          ticket.setTicketCode(event.getEventCode() + "-" + Utils.randomNS(6));  
        }else{
           tr.setStatus("failed");
            tr.setMessage("parent event not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(tr); 
        }
        
        try{
            
            Ticket newTicket = ticketRepo.save(ticket);
            tr.setStatus("success");
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
            ct.setTicketCode(ccr.getTicket().getTicketCode() + "-" + Utils.randomNS(5));
            ct.setCouponId(ccr.getTicket().getCouponId());
            ct.setIndividual(ccr.getTicket().isIndividual());
            ct.setSaleStartDay(ccr.getTicket().getSaleStartDay());
            ct.setSaleEndDay(ccr.getTicket().getSaleEndDay());
            ct.setCreated(LocalDateTime.now());
            ct.setUpdated(LocalDateTime.now());            
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
    
}









