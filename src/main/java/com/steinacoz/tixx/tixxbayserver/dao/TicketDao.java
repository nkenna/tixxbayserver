/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.dao;

import com.steinacoz.tixx.tixxbayserver.model.ChildTicket;
import com.steinacoz.tixx.tixxbayserver.model.Coupon;
import com.steinacoz.tixx.tixxbayserver.model.Event;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.annotation.Id;

/**
 *
 * @author nkenn
 */
public class TicketDao {
    private String id;
    private String title;
    private String description;
    private String ticketCategory; //public or private
    private boolean paidTicket; //true = paid, false = free
    private BigDecimal ticketAmount;
    private String eventId;
    private String eventCode;
    private String ticketType; //electronic, nfc tag or qr code
    private String ticketCode;
    private String couponId;
    private boolean individual;
    private LocalDateTime saleStartDay;
    private LocalDateTime saleEndDay;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Event event;
    private List<ChildTicket> childTickets;
    private List<Coupon> coupons;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTicketCategory() {
        return ticketCategory;
    }

    public void setTicketCategory(String ticketCategory) {
        this.ticketCategory = ticketCategory;
    }

    public boolean isPaidTicket() {
        return paidTicket;
    }

    public void setPaidTicket(boolean paidTicket) {
        this.paidTicket = paidTicket;
    }

    public BigDecimal getTicketAmount() {
        return ticketAmount;
    }

    public void setTicketAmount(BigDecimal ticketAmount) {
        this.ticketAmount = ticketAmount;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public boolean isIndividual() {
        return individual;
    }

    public void setIndividual(boolean individual) {
        this.individual = individual;
    }

    public LocalDateTime getSaleStartDay() {
        return saleStartDay;
    }

    public void setSaleStartDay(LocalDateTime saleStartDay) {
        this.saleStartDay = saleStartDay;
    }

    public LocalDateTime getSaleEndDay() {
        return saleEndDay;
    }

    public void setSaleEndDay(LocalDateTime saleEndDay) {
        this.saleEndDay = saleEndDay;
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

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<ChildTicket> getChildTickets() {
        return childTickets;
    }

    public void setChildTickets(List<ChildTicket> childTickets) {
        this.childTickets = childTickets;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }
    
    
}




