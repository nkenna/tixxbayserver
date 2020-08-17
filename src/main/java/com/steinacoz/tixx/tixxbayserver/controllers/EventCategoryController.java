/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.controllers;

import com.steinacoz.tixx.tixxbayserver.dao.EventCategoryDao;
import com.steinacoz.tixx.tixxbayserver.model.Event;
import com.steinacoz.tixx.tixxbayserver.model.EventCategory;
import com.steinacoz.tixx.tixxbayserver.repo.EventCategoryRepo;
import com.steinacoz.tixx.tixxbayserver.repo.EventRepo;
import com.steinacoz.tixx.tixxbayserver.response.EventCategoryResponse;
import com.steinacoz.tixx.tixxbayserver.response.EventResponse;
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
@RequestMapping("/tixxbay/api/category/v1")
public class EventCategoryController {
    @Autowired
    EventCategoryRepo eventCategoryRepo;
    
    @CrossOrigin
    @RequestMapping(value = "/add-category", method = RequestMethod.POST)
    public ResponseEntity<EventCategoryResponse> createEventCategory(@RequestBody EventCategory ec){
        EventCategoryResponse ecr = new EventCategoryResponse();
        if(ec.getCategory() == null || ec.getCategory().isEmpty()){
            ecr.setStatus("failed");
            ecr.setMessage("cateory name is required");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ecr);
        }
        
        try{
             EventCategory newEC = eventCategoryRepo.save(ec);
             EventCategoryDao ecdao = new EventCategoryDao();
             BeanUtils.copyProperties(newEC, ecdao);
             ecr.setEventCategory(ecdao);
            ecr.setStatus("success");
            ecr.setMessage("cateory created successfully");
            return ResponseEntity.ok().body(ecr);
        }catch(Exception e){
            ecr.setStatus("failed");
            ecr.setMessage("error occurred while saving category: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ecr);
        }
    }
}




