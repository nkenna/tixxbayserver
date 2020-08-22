/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.controllers;

import com.steinacoz.tixx.tixxbayserver.dao.EventDao;
import com.steinacoz.tixx.tixxbayserver.model.Event;
import com.steinacoz.tixx.tixxbayserver.model.EventKey;
import com.steinacoz.tixx.tixxbayserver.model.Location;
import com.steinacoz.tixx.tixxbayserver.model.User;
import com.steinacoz.tixx.tixxbayserver.repo.EventKeyRepo;
import com.steinacoz.tixx.tixxbayserver.repo.EventRepo;
import com.steinacoz.tixx.tixxbayserver.repo.UserRepo;
import com.steinacoz.tixx.tixxbayserver.request.EventKeyReq;
import com.steinacoz.tixx.tixxbayserver.request.EventUpdateRequest;
import com.steinacoz.tixx.tixxbayserver.request.RemoveImageRequest;
import com.steinacoz.tixx.tixxbayserver.response.EventKeyResponse;
import com.steinacoz.tixx.tixxbayserver.response.EventResponse;
import com.steinacoz.tixx.tixxbayserver.utils.Utils;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import kong.unirest.ContentType;
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
    
    @Autowired
    EventKeyRepo eventkeyRepo;
    
    private DateFormat datetime = new SimpleDateFormat("YY-MM-dd HH:mm:ss");
    
    public HttpResponse<JsonNode> sendSimpleMessage(Event event, User user, String fromEmail, String subject, String content) throws UnirestException {

    	HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/sandboxf0a305f9cb84423c85c4f4f5c03e176e.mailgun.org") //Unirest.post("https://api.mailgun.net/v3/sandbox54745fe7bf41492087ca09fa024aae27.mailgun.org/messages")
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
        
        if(event.getCreatorUsername() == null || event.getCreatorUsername().isEmpty()){
            er.setStatus("failed");
            er.setMessage("Event creator username is required");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(er);
        }
        
        if(event.getCategoryId().isEmpty() || event.getCategoryName().isEmpty()){
            er.setStatus("failed");
            er.setMessage("Event category is required");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(er);
        }
        
        
        if(event.getEventType() == null || event.getEventType().isEmpty()){
            event.setEventType("physical");
        }
        
        try{
            event.setAdminStatus(true);
            event.setStatus(true);
            event.setEventCode(Utils.randomNS(8));
            Event newEvent = eventRepo.save(event);
            
            EventKey key = new EventKey();
            key.setEventId(newEvent.getId());
            key.setArchive(false);
            key.setKey(Utils.randomNS(32));
            
            eventkeyRepo.save(key);
            
            User user = userRepo.findByUsername(event.getCreatorUsername());
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
    @RequestMapping(value = "/edit-event", method = RequestMethod.PUT)
    public ResponseEntity<EventResponse> editEvent(@RequestBody EventUpdateRequest event){
        EventResponse er = new EventResponse();
        
        Event foundEvent = eventRepo.findById(event.getId()).orElseGet(null);
    
        
        if(foundEvent != null){
           try{
               foundEvent.setTitle(event.getTitle());
            foundEvent.setDiscription(event.getDiscription());
            foundEvent.setVenue(event.getVenue());
            foundEvent.setCategoryName(event.getCategoryName());
            foundEvent.setCategoryId(event.getCategoryId());
            foundEvent.setEventType(event.getEventType());
            foundEvent.setLocation(event.getLocation());
            foundEvent.setAvailableTicket(event.getAvailableTicket());
            foundEvent.setStartDate(event.getStartDate());
            foundEvent.setEndDate(event.getEndDate());
            foundEvent.setCreatorUsername(event.getCreatorUsername());
            foundEvent.setVirtualUrl(event.getVirtualUrl());
            foundEvent.setStatus(event.isStatus());
            foundEvent.setAdminStatus(event.isAdminStatus());
            foundEvent.setEventCode(event.getEventCode());
            foundEvent.setCountry(event.getCountry());
            foundEvent.setState(event.getState());
            foundEvent.setLga(event.getLga());
            
            Event newEvent = eventRepo.save(foundEvent);
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
    @RequestMapping(value = "/remove-event-image", method = RequestMethod.PUT)
    public ResponseEntity<EventResponse> removeEventImage(@RequestBody RemoveImageRequest event){
        EventResponse er = new EventResponse();
        Event foundEvent = eventRepo.findById(event.getEventId()).orElseThrow(null);
        if(foundEvent != null){
          
                switch (event.getPosition()) {
                    case 1:
                        foundEvent.setImage1(null);
                        break;
                    case 2:
                        foundEvent.setImage2(null);
                        break;
                    case 3:
                        foundEvent.setImage3(null);
                        break;
                    default:
                        er.setStatus("failed");
                        er.setMessage("Image position not specified.");
                        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(er);
                }
           
        }else{
            er.setStatus("failed");
            er.setMessage("Event not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er);
        }
        
         Event newEvent = eventRepo.save(foundEvent);
        er.setStatus("success");
        er.setEvent(newEvent);
        er.setMessage("Image removed successfully");
        return ResponseEntity.ok().body(er);
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
        List<EventDao> events = eventRepo.aggregateAllEventsByCreator(event.getCreatorUsername());
       er.setMessage("events found: " + String.valueOf(events.size()));
        er.setStatus("success");
        er.setEvents(events);
        return ResponseEntity.ok().body(er);
    }
    
    @CrossOrigin
    @RequestMapping(value = "/all-events-by-country", method = RequestMethod.PUT)
    public ResponseEntity<EventResponse> allEventsByCountry(@RequestBody Event event){
        EventResponse er = new EventResponse();
        List<EventDao> events = eventRepo.aggregateAllEventsByCountry(event.getCountry());
       er.setMessage("events found: " + String.valueOf(events.size()));
        er.setStatus("success");
        er.setEvents(events);
        return ResponseEntity.ok().body(er);
    }
    
    @CrossOrigin
    @RequestMapping(value = "/all-events-by-state", method = RequestMethod.PUT)
    public ResponseEntity<EventResponse> allEventsByState(@RequestBody Event event){
        EventResponse er = new EventResponse();
        List<EventDao> events = eventRepo.aggregateAllEventsByState(event.getState());
       er.setMessage("events found: " + String.valueOf(events.size()));
        er.setStatus("success");
        er.setEvents(events);
        return ResponseEntity.ok().body(er);
    }
    
    @CrossOrigin
    @RequestMapping(value = "/all-events-by-lga", method = RequestMethod.PUT)
    public ResponseEntity<EventResponse> allEventsByLGA(@RequestBody Event event){
        EventResponse er = new EventResponse();
        List<EventDao> events = eventRepo.aggregateAllEventsByLga(event.getLga());
       er.setMessage("events found: " + String.valueOf(events.size()));
        er.setStatus("success");
        er.setEvents(events);
        return ResponseEntity.ok().body(er);
    }
    
    @CrossOrigin
    @RequestMapping(value = "/all-events-by-all", method = RequestMethod.PUT)
    public ResponseEntity<EventResponse> allEventsByAll(@RequestBody Event event){
        EventResponse er = new EventResponse();
        List<EventDao> events = eventRepo.aggregateAllEventsByUserLocation(event.getCountry(), event.getState(), event.getLga());
       er.setMessage("events found: " + String.valueOf(events.size()));
        er.setStatus("success");
        er.setEvents(events);
        return ResponseEntity.ok().body(er);
    }
    
    @CrossOrigin
    @RequestMapping(value = "/get-key-data-by-event", method = RequestMethod.POST)
    public ResponseEntity<EventKeyResponse> getEventKey(@RequestBody EventKeyReq key){
        EventKeyResponse er = new EventKeyResponse();
        EventKey event = eventkeyRepo.findByEventId(key.getEventId());
        if(event != null){
            er.setMessage("data found");
            er.setStatus("success");
        er.setData(event);
        return ResponseEntity.ok().body(er);
        }else{
          er.setMessage("data not found");
            er.setStatus("failed");
        er.setData(event);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er);  
        }
       
    }
    
    @CrossOrigin
    @RequestMapping(value = "/change-event-mode", method = RequestMethod.POST)
    public ResponseEntity<EventResponse> changeEventMode(@RequestBody Event ev){
        EventResponse er = new EventResponse();
        Event event = eventRepo.findById(ev.getId()).orElseGet(null);
        
        if(event != null){
            if(ev.isStatus()){
                event.setStatus(true);
                er.setStatus("success");
                er.setMessage("event status is public");
                return ResponseEntity.ok().body(er);
            }else{
                event.setStatus(false);
                er.setStatus("success");
                er.setMessage("event status is private");
                return ResponseEntity.ok().body(er);
            }
        }else{
          er.setMessage("event not found");
            er.setStatus("failed");
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er);  
        }
    }
    
    
    
}



















































