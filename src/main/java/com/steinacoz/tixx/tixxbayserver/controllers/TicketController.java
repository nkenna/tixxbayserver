/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.controllers;

import com.steinacoz.tixx.tixxbayserver.dao.TicketDao;
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
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
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
}

























