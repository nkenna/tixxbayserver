/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.controllers;

import com.steinacoz.tixx.tixxbayserver.dao.EventDao;
import com.steinacoz.tixx.tixxbayserver.model.Event;
import com.steinacoz.tixx.tixxbayserver.model.User;
import com.steinacoz.tixx.tixxbayserver.repo.EventRepo;
import com.steinacoz.tixx.tixxbayserver.repo.UserRepo;
import com.steinacoz.tixx.tixxbayserver.response.EventResponse;
import com.steinacoz.tixx.tixxbayserver.utils.Utils;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
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
@RequestMapping("/tixxbay/api/event/v1")
public class EventController {
    @Autowired
    EventRepo eventRepo;
    
    @Autowired
    UserRepo userRepo;
    
    private DateFormat datetime = new SimpleDateFormat("YY-MM-dd HH:mm:ss");
    
    public HttpResponse<JsonNode> sendSimpleMessage(Event event, User user, String fromEmail, String subject, String content) throws UnirestException {

    	HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/sandbox54745fe7bf41492087ca09fa024aae27.mailgun.org/messages")
	            .basicAuth("api", Utils.API_KEY)
	            .field("from", fromEmail)
	            .field("to", user.getEmail())
	            .field("subject", subject)
	            .field("text", content)
	            .asJson();

        return request;
    }

    @CrossOrigin
    @RequestMapping(value = "/add-event", method = RequestMethod.POST)
    public ResponseEntity<EventResponse> createEvent(@RequestBody Event event){
        EventResponse er = new EventResponse();
        
        if(event.getTitle() == null || event.getTitle().isEmpty()){
            er.setStatus("failed");
            er.setMessage("Event title is required");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(er);
        }
        
        if(event.getVenue() == null || event.getVenue().isEmpty()){
            er.setStatus("failed");
            er.setMessage("Event venue is required");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(er);
        }
        
        if(event.getEventType().equalsIgnoreCase("virtual") && event.getVirtualUrl().isEmpty()){
            er.setStatus("failed");
            er.setMessage("Event type is virtual. Virtual url is required");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(er);
        }
        
        if(event.getStartDate() == null || event.getEndDate() == null){
            er.setStatus("failed");
            er.setMessage("Event start and end date is required");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(er);
        }
        
        if(event.getCreatorId() == null || event.getCreatorId().isEmpty()){
            er.setStatus("failed");
            er.setMessage("Event creator ID is required");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(er);
        }
        
        if(event.getEventCategory() == null || event.getEventCategory().isEmpty()){
            event.setEventCategory("others");
        }
        
        if(event.getEventType() == null || event.getEventType().isEmpty()){
            event.setEventType("physical");
        }
        
        try{
            event.setAdminStatus(true);
            event.setStatus(true);
            event.setEventCode(Utils.randomNS(8));
            Event newEvent = eventRepo.save(event);
            
            User user = userRepo.findById(newEvent.getCreatorId()).orElseThrow(null);
            if(user != null){
                this.sendSimpleMessage(event, user, "info@tixxbay.com", "New Event created",
                    "Your new Event have been successfully created. The event details is available on your dashboard."
                );
            }            
            er.setStatus("success");
            er.setMessage("event created successfully");
            return ResponseEntity.ok().body(er);
        }catch(Exception e){
            System.out.println(e.getMessage());
            er.setStatus("failed");
            er.setMessage("error creating event: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }
    
    
    @CrossOrigin
    @RequestMapping(value = "/edit-event", method = RequestMethod.POST)
    public ResponseEntity<EventResponse> editEvent(@RequestBody Event event){
        EventResponse er = new EventResponse();
        
        Event foundEvent = eventRepo.findById(event.getId()).orElseGet(null);
        
        if(foundEvent != null){
           try{
            Event newEvent = eventRepo.save(event);
            er.setEvent(newEvent);
            er.setStatus("success");
            er.setMessage("Event updated succesfully");
            return ResponseEntity.ok().body(er);
        }catch(Exception e){
           er.setStatus("failed");
           er.setMessage("error occurred updating event: " + e.getMessage());
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er); 
        } 
        }else{
           er.setStatus("failed");
            er.setMessage("Event not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er); 
        } 
    }
    
    @CrossOrigin
    @RequestMapping(value = "/add-event-photo", method = RequestMethod.POST)
    public ResponseEntity<EventResponse> addPhoto(@RequestParam("image") MultipartFile image, @RequestParam("position") int position, @RequestParam("eventId") String eventId){
        EventResponse er = new EventResponse();
        Event foundEvent = eventRepo.findById(eventId).orElseThrow(null);
        
        if(foundEvent != null){
            try{
                switch (position) {
                    case 1:
                        foundEvent.setImage1(new Binary(BsonBinarySubType.BINARY, image.getBytes()));
                        break;
                    case 2:
                        foundEvent.setImage2(new Binary(BsonBinarySubType.BINARY, image.getBytes()));
                        break;
                    case 3:
                        foundEvent.setImage3(new Binary(BsonBinarySubType.BINARY, image.getBytes()));
                        break;
                    default:
                        er.setStatus("failed");
                        er.setMessage("Image position not specified.");
                        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(er);
                }
            }catch(IOException e){
                er.setStatus("failed");
                er.setMessage("Image position not specified.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
            }
        }else{
            er.setStatus("failed");
            er.setMessage("Event not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er);
        }
        
        Event event = eventRepo.save(foundEvent);
        er.setStatus("success");
        er.setEvent(event);
        er.setMessage("Image uploaded successfully");
        return ResponseEntity.ok().body(er);
    }
    
    @CrossOrigin
    @RequestMapping(value = "/remove-event-user", method = RequestMethod.POST)
    public ResponseEntity<EventResponse> removeEventUser(@RequestBody Event event){
        EventResponse er = new EventResponse();
        
        Event foundEvent = eventRepo.findById(event.getId()).orElseThrow(null);
        
        if(foundEvent != null){
            if(event.isStatus()){
                foundEvent.setStatus(true);
                Event savedEvent = eventRepo.save(event);
                er.setEvent(event);
                er.setStatus("success");
                er.setMessage("event active");
                return ResponseEntity.ok().body(er);
            }else{
               foundEvent.setStatus(false); 
               Event savedEvent = eventRepo.save(event);
                er.setEvent(event);
                er.setStatus("success");
                er.setMessage("event inactive");
                return ResponseEntity.ok().body(er);
            }
            
            
        }else{
            er.setStatus("failed");
            er.setMessage("event not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er);
        }
    }
    
    @CrossOrigin
    @RequestMapping(value = "/remove-event-admin", method = RequestMethod.POST)
    public ResponseEntity<EventResponse> removeEventAdmin(@RequestBody Event event){
        EventResponse er = new EventResponse();
        
        Event foundEvent = eventRepo.findById(event.getId()).orElseThrow(null);
        
        if(foundEvent != null){
            if(event.isAdminStatus()){
                foundEvent.setAdminStatus(true);
                Event savedEvent = eventRepo.save(event);
                er.setEvent(event);
                er.setStatus("success");
                er.setMessage("event active");
                return ResponseEntity.ok().body(er);
            }else{
               foundEvent.setAdminStatus(false); 
               Event savedEvent = eventRepo.save(event);
                er.setEvent(event);
                er.setStatus("success");
                er.setMessage("event inactive");
                return ResponseEntity.ok().body(er);
            }
            
            
        }else{
            er.setStatus("failed");
            er.setMessage("event not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er);
        }
    }
    
    @CrossOrigin
    @RequestMapping(value = "/all-events", method = RequestMethod.GET)
    public ResponseEntity<EventResponse> allEvents(){
        EventResponse er = new EventResponse();
        List<EventDao> events = eventRepo.aggregateAllEvents();
        er.setMessage("events found: " + String.valueOf(events.size()));
        er.setStatus("success");
        er.setEvents(events);
        return ResponseEntity.ok().body(er);
    }
    
    @CrossOrigin
    @RequestMapping(value = "/all-events-by-creator", method = RequestMethod.PUT)
    public ResponseEntity<EventResponse> allEventsByCreator(@RequestBody Event event){
        EventResponse er = new EventResponse();
        List<EventDao> events = eventRepo.aggregateAllEventsByCreator(event.getCreatorId());
        er.setMessage("events found: " + String.valueOf(events.size()));
        er.setStatus("success");
        er.setEvents(events);
        return ResponseEntity.ok().body(er);
    }
}
























