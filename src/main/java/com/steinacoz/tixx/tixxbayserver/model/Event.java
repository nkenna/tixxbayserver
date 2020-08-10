/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;

/**
 *
 * @author nkenn
 */
public class Event {
    @Id private String id;
    private String title; //
    private String discription;
    private String venue; //
    private String eventCategory; // birthday, wedding, conference, religious, workshop & training, others
    private String eventType; //virtual or physical
    private Location location;
    private int availableTicket;
    private LocalDateTime startDate; //
    private LocalDateTime endDate; //
    private String creatorId; //
    private String virtualUrl;
    private boolean status;
    private boolean adminStatus;
    private String eventCode;
    private Binary image1;
    private Binary image2;
    private Binary image3;

    public Binary getImage1() {
        return image1;
    }

    public void setImage1(Binary image1) {
        this.image1 = image1;
    }

    public Binary getImage2() {
        return image2;
    }

    public void setImage2(Binary image2) {
        this.image2 = image2;
    }

    public Binary getImage3() {
        return image3;
    }

    public void setImage3(Binary image3) {
        this.image3 = image3;
    }
    
    

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }
    
    

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(boolean adminStatus) {
        this.adminStatus = adminStatus;
    }
    
    

    public String getVirtualUrl() {
        return virtualUrl;
    }

    public void setVirtualUrl(String virtualUrl) {
        this.virtualUrl = virtualUrl;
    }
    
    

    public String getId() {
        return id;
    }

    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getAvailableTicket() {
        return availableTicket;
    }

    public void setAvailableTicket(int availableTicket) {
        this.availableTicket = availableTicket;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }
    
    
}










