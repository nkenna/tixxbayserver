/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.controllers;

import com.steinacoz.tixx.tixxbayserver.model.Event;
import com.steinacoz.tixx.tixxbayserver.model.EventTeam;
import com.steinacoz.tixx.tixxbayserver.model.Ticket;
import com.steinacoz.tixx.tixxbayserver.model.User;
import com.steinacoz.tixx.tixxbayserver.repo.EventRepo;
import com.steinacoz.tixx.tixxbayserver.repo.EventTeamRepo;
import com.steinacoz.tixx.tixxbayserver.repo.UserRepo;
import com.steinacoz.tixx.tixxbayserver.request.AddTeamMemberRequest;
import com.steinacoz.tixx.tixxbayserver.request.CreateTeamRequest;
import com.steinacoz.tixx.tixxbayserver.response.EventTeamResponse;
import com.steinacoz.tixx.tixxbayserver.response.TicketResponse;
import com.steinacoz.tixx.tixxbayserver.utils.Utils;
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
@RequestMapping("/tixxbay/api/team/v1")
public class EventTeamController {
    @Autowired
    UserRepo userRepo;
    
    @Autowired
    EventRepo eventRepo;
    
    @Autowired
    EventTeamRepo teamRepo;
    
    @CrossOrigin
    @RequestMapping(value = "/create-team", method = RequestMethod.POST)
    public ResponseEntity<EventTeamResponse> addTeamMember(@RequestBody CreateTeamRequest ctr){
        EventTeamResponse etr = new EventTeamResponse();
        
        if(ctr.getEventCode() == null || ctr.getEventCode().isEmpty()){
            etr.setStatus("failed");
            etr.setMessage("event code is required");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(etr);
        }
        
        Event event = eventRepo.findByEventCode(ctr.getEventCode());
        
        if(event == null){
           etr.setStatus("failed");
            etr.setMessage("event not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(etr); 
        }
                
        
        String gen_ref = Utils.randomNS(12);
	boolean ref_check = true;
	while(ref_check) {
            EventTeam team = teamRepo.findByTeamRef(gen_ref);
            if(team == null) {
                ref_check = false;
            }
            gen_ref = Utils.randomNS(12);
        }
        
        EventTeam eventTeam = new EventTeam();
        eventTeam.setTeamRef(gen_ref);
        eventTeam.setEventCode(event.getEventCode());
        eventTeam.setEventId(event.getId());
        eventTeam.setCreated(LocalDateTime.now());
        eventTeam.setUpdated(LocalDateTime.now());
        
        try{
            EventTeam newTeam = teamRepo.save(eventTeam);
            etr.setStatus("success");
            etr.setTeam(newTeam);
            etr.setMessage("team created successfully");
            return ResponseEntity.ok().body(etr); 
        }catch(Exception e){
            etr.setStatus("failed");
            etr.setMessage("error creating team: " + e.getMessage());
            return ResponseEntity.ok().body(etr);
        }
    }
    
    @CrossOrigin
    @RequestMapping(value = "/add-team-member", method = RequestMethod.POST)
    public ResponseEntity<EventTeamResponse> addTeamMemeber(@RequestBody AddTeamMemberRequest atmr){
        EventTeamResponse etr = new EventTeamResponse();
        
        //find user first
        User user = userRepo.findByUsernameOrEmail(atmr.getUsername(), atmr.getUserEmail());
        
        if(user == null){
            etr.setStatus("failed");
            etr.setMessage("user not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(etr);
        }
        if(user.isFlag()){
            etr.setStatus("failed");
            etr.setMessage("user account is flagged");
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(etr);
        }
        
        EventTeam team = teamRepo.findByTeamRef(atmr.getTeamRef());
        
        if(team == null){
           etr.setStatus("failed");
            etr.setMessage("team not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(etr); 
        }else{
            List<User> users = new ArrayList<>();
            
            if(team.getMembers() != null){
                users.addAll(team.getMembers());
            }
            
            boolean isMember = false;
            for(int i = 0; i < users.size(); i++){
                if(users.get(i).getUsername().equalsIgnoreCase(user.getUsername())){
                    isMember = true;
                    break;
                }
            }
            
            if(isMember){
              etr.setStatus("failed");
              etr.setMessage("user already in team");
              return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(etr);  
            }
            
            user.setUserType(atmr.getRole());
            users.add(user);
            team.setMembers(users);
            team.setUpdated(LocalDateTime.now());
            EventTeam tt = teamRepo.save(team);
            etr.setTeam(tt);
            etr.setStatus("success");
            etr.setMessage("team member added successfully");
            return ResponseEntity.ok().body(etr); 
        }
    }
    
    @CrossOrigin
    @RequestMapping(value = "/remove-team-member", method = RequestMethod.POST)
    public ResponseEntity<EventTeamResponse> removeTeamMemeber(@RequestBody AddTeamMemberRequest atmr){
       EventTeamResponse etr = new EventTeamResponse();
        
        //find user first
        User user = userRepo.findByUsernameOrEmail(atmr.getUsername(), atmr.getUserEmail());
        
        if(user == null){
            etr.setStatus("failed");
            etr.setMessage("user not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(etr);
        } 
        
        EventTeam team = teamRepo.findByTeamRef(atmr.getTeamRef());
        
        if(team == null){
           etr.setStatus("failed");
            etr.setMessage("team not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(etr); 
        }else{
           List<User> users = team.getMembers();
           boolean isMember = false; 
           
           for(int i = 0; i < users.size(); i++){
                if(users.get(i).getUsername().equalsIgnoreCase(user.getUsername())){
                    team.getMembers().remove(users.get(i));
                    isMember = true;
                    break;
                }
            }
           
           if(!isMember){
              etr.setStatus("failed");
              etr.setMessage("user not a member of this team");
              return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(etr);  
            }
           
           users.clear();
           users = team.getMembers();
           
           team.setMembers(users);
            team.setUpdated(LocalDateTime.now());
            EventTeam tt = teamRepo.save(team);
            etr.setTeam(tt);
            etr.setStatus("success");
            etr.setMessage("team member remove successfully");
            return ResponseEntity.ok().body(etr);
           
           
        }
        
        
    }
    
    @CrossOrigin
    @RequestMapping(value = "/event-team", method = RequestMethod.POST)
    public ResponseEntity<EventTeamResponse> getEventTeam(@RequestBody AddTeamMemberRequest atmr){
        EventTeamResponse etr = new EventTeamResponse();
        
        EventTeam team = teamRepo.findByTeamRef(atmr.getTeamRef());
        
        if(team != null){
            etr.setTeam(team);
            etr.setStatus("success");
            etr.setMessage("team retrieved successfully");
            return ResponseEntity.ok().body(etr);
        }else{
           etr.setStatus("success");
            etr.setMessage("team not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(etr); 
        }
    }
}

















