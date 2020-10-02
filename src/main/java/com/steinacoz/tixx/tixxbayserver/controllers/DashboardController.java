/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.controllers;

import com.steinacoz.tixx.tixxbayserver.model.DashboardData;
import com.steinacoz.tixx.tixxbayserver.model.Event;
import com.steinacoz.tixx.tixxbayserver.model.EventTeam;
import com.steinacoz.tixx.tixxbayserver.model.SaleTransaction;
import com.steinacoz.tixx.tixxbayserver.model.TicketSaleTransaction;
import com.steinacoz.tixx.tixxbayserver.model.User;
import com.steinacoz.tixx.tixxbayserver.model.WalletTransaction;
import com.steinacoz.tixx.tixxbayserver.repo.EventRepo;
import com.steinacoz.tixx.tixxbayserver.repo.EventTeamRepo;
import com.steinacoz.tixx.tixxbayserver.repo.SaleTransactionRepo;
import com.steinacoz.tixx.tixxbayserver.repo.TicketSaleTransactionRepo;
import com.steinacoz.tixx.tixxbayserver.repo.UserRepo;
import com.steinacoz.tixx.tixxbayserver.repo.WalletTransactionRepo;
import com.steinacoz.tixx.tixxbayserver.response.DashboardDataResponse;
import com.steinacoz.tixx.tixxbayserver.response.TicketSalesByMonthResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nkenn
 */
@RestController
@RequestMapping("/tixxbay/api/dashboard/v1")
public class DashboardController {
    @Autowired
    UserRepo userRepo;
    
    @Autowired
    EventRepo eventRepo;
    
    @Autowired
    EventTeamRepo eventTeamRepo;
    
    @Autowired
   TicketSaleTransactionRepo ttRepo;
   
   @Autowired
   SaleTransactionRepo stRepo;
   
   @Autowired
   WalletTransactionRepo wtRepo;
   
   @CrossOrigin
   @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/all-dashboard-data", method = RequestMethod.GET)
    public ResponseEntity<DashboardDataResponse> allDashboardData(){
        DashboardDataResponse ddr = new DashboardDataResponse();
        List<User> users = userRepo.getUsersLast30days();
        List<Event> events = eventRepo.getEventsCreatedBy3wks();
        List<EventTeam> teams = eventTeamRepo.getTeamsBy3wks();
        List<SaleTransaction> saleTrans = stRepo.getSTCreatedBy3wks();
        List<TicketSaleTransaction> ticketTrans = ttRepo.getTTCreatedBy3wks();
        List<WalletTransaction> walletTrans = wtRepo.getWTCreatedBy3wks();
        
        DashboardData dd = new DashboardData();
        dd.setTeams(teams);
        dd.setUsers(users);
        dd.setEvent(events);
        dd.setTicketTrans(ticketTrans);
        dd.setSaleTrans(saleTrans);
        dd.setWalletTrans(walletTrans);
        
        ddr.setData(dd);
        ddr.setStatus("success");
        ddr.setMessage("data retrieved");
        return ResponseEntity.ok().body(ddr);
        
    }
}














