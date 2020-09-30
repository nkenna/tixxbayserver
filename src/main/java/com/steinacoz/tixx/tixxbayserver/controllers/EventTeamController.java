/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.controllers;

import com.steinacoz.tixx.tixxbayserver.model.Event;
import com.steinacoz.tixx.tixxbayserver.model.EventTeam;
import com.steinacoz.tixx.tixxbayserver.model.Role;
import com.steinacoz.tixx.tixxbayserver.model.Ticket;
import com.steinacoz.tixx.tixxbayserver.model.User;
import com.steinacoz.tixx.tixxbayserver.repo.EventRepo;
import com.steinacoz.tixx.tixxbayserver.repo.EventTeamRepo;
import com.steinacoz.tixx.tixxbayserver.repo.RoleRepo;
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
    
    @Autowired
    RoleRepo roleRepo;
    
    @CrossOrigin
    @RequestMapping(value = "/create-team", method = RequestMethod.POST)
    public ResponseEntity<EventTeamResponse> createTeam(@RequestBody CreateTeamRequest ctr){
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
        
        if(atmr.getRole() == null || atmr.getRole().isEmpty()){
            etr.setStatus("failed");
            etr.setMessage("user role in the team is required");
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
            //typeEventManager
            if(atmr.getRole().equalsIgnoreCase(Utils.typeAgent)){
                
                
                    Role userRole = roleRepo.findByName("ROLE_USER");
                    if(userRole  == null){
                        throw( new RuntimeException("Error: user role not found."));
                    }
                if(user.getRoles().contains(userRole)){
                    user.getRoles().add(userRole);
                }
            }
            
            if(atmr.getRole().equalsIgnoreCase(Utils.typeVendor)){
                Role vendorRole = roleRepo.findByName("ROLE_VENDOR");
                    if(vendorRole  == null){
                        throw( new RuntimeException("Error: vendor role not found."));
                    }

                if(user.getRoles().contains(vendorRole) == false){
                    user.getRoles().add(vendorRole);
                }
            }
            user.setUserType(atmr.getRole());
            List<String> linkedEvents = new ArrayList<>();
            if(user.getLinkedEvents() != null){
                linkedEvents.addAll(linkedEvents);
            }
            boolean isLinked = false;
            for(int j = 0; j < linkedEvents.size(); j++){
                if(linkedEvents.get(j).equalsIgnoreCase(team.getEventCode())){
                    isLinked = true;
                    break;
                }
            }
            
            if(!isLinked){
                linkedEvents.add(team.getEventCode());
                user.setLinkedEvents(linkedEvents);
                user.setUpdated(LocalDateTime.now());
                userRepo.save(user);
            }
                            
            
            users.add(user);
            team.setMembers(users);
            team.setUpdated(LocalDateTime.now());
            EventTeam tt = teamRepo.save(team);
            Utils.sendOutEmailHtml(
                    "New event team access",
                    user.getEmail(),
                    "You have been added to an Event team as a " + atmr.getRole()  + ". Open your dashboard to get more details.");
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
           
           if(user.getLinkedEvents() != null){
               List<String> linkedEvents = user.getLinkedEvents();
                boolean isLinked = false;
                 for(int j = 0; j < linkedEvents.size(); j++){
                     if(linkedEvents.get(j).equalsIgnoreCase(team.getEventCode())){
                         user.getLinkedEvents().remove(linkedEvents.get(j));

                         user.setLinkedEvents(user.getLinkedEvents());
                         user.setUpdated(LocalDateTime.now());
                         userRepo.save(user);
                         isLinked = true;
                         break;
                     }
                 }
           }
           
           users.clear();
           users = team.getMembers();
           
           team.setMembers(users);
            team.setUpdated(LocalDateTime.now());
            EventTeam tt = teamRepo.save(team);
            Utils.sendOutEmailHtml(
                    "Event team access revoked",
                    user.getEmail(),
                    "You have been removed from an event team and your access revoked. If you feel that this is a mistake. Please contact the event manager."
                    );
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
    
    @CrossOrigin
    @RequestMapping(value = "/event-team-by-memeber", method = RequestMethod.POST)
    public ResponseEntity<EventTeamResponse> getEventTeamByMemberId(@RequestBody AddTeamMemberRequest atmr){
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
    
    @CrossOrigin
    @RequestMapping(value = "/all-teams", method = RequestMethod.GET)
    public ResponseEntity<EventTeamResponse> getAllTeams(){
        EventTeamResponse etr = new EventTeamResponse();
        List<EventTeam> teams = teamRepo.findAll();
        
        etr.setStatus("success");
        etr.setMessage("teams found: " + String.valueOf(teams.size()));
        etr.setTeams(teams);
        return ResponseEntity.ok().body(etr);
    }
}


































