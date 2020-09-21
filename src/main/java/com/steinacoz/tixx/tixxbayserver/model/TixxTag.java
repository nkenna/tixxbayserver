/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;

/**
 *
 * @author nkenn
 */
public class TixxTag {
    @Id private String id;
	private String taguuid;//-
	private String content;
	private String addedById;
	private String updatedById;
	private String wornedById; //access control
	private String wornedByName; //access control
	private String wornedByPosition; //access control
	private LocalDateTime created;
	private LocalDateTime updated;
	private BigDecimal defaultAmount;
	private BigDecimal previousAmount; //payment
	private BigDecimal availableAmount; //payment
	private boolean flag;
	private boolean active;
	private Location location;
	private String role;
	private String eventCode; //event management
	private String eventId;
        

    public String getId() {
        return id;
    }

   

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddedById() {
        return addedById;
    }

    public void setAddedById(String addedById) {
        this.addedById = addedById;
    }

    public String getUpdatedById() {
        return updatedById;
    }

    public void setUpdatedById(String updatedById) {
        this.updatedById = updatedById;
    }

    public String getWornedById() {
        return wornedById;
    }

    public void setWornedById(String wornedById) {
        this.wornedById = wornedById;
    }

    public String getWornedByName() {
        return wornedByName;
    }

    public void setWornedByName(String wornedByName) {
        this.wornedByName = wornedByName;
    }

    public String getWornedByPosition() {
        return wornedByPosition;
    }

    public void setWornedByPosition(String wornedByPosition) {
        this.wornedByPosition = wornedByPosition;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public BigDecimal getDefaultAmount() {
        return defaultAmount;
    }

    public void setDefaultAmount(BigDecimal defaultAmount) {
        this.defaultAmount = defaultAmount;
    }

    public BigDecimal getPreviousAmount() {
        return previousAmount;
    }

    public void setPreviousAmount(BigDecimal previousAmount) {
        this.previousAmount = previousAmount;
    }

    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(BigDecimal availableAmount) {
        this.availableAmount = availableAmount;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTaguuid() {
        return taguuid;
    }

    public void setTaguuid(String taguuid) {
        this.taguuid = taguuid;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

   

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
        
        
}






