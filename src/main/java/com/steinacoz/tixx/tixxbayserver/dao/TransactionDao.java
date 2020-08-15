/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.dao;

import com.steinacoz.tixx.tixxbayserver.model.ChildTicket;
import com.steinacoz.tixx.tixxbayserver.model.Coupon;
import com.steinacoz.tixx.tixxbayserver.model.Location;
import com.steinacoz.tixx.tixxbayserver.model.TixxTag;
import com.steinacoz.tixx.tixxbayserver.model.User;
import com.steinacoz.tixx.tixxbayserver.model.Wallet;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author nkenn
 */
public class TransactionDao {
    private String id;
    private String transRef;
    private String payRef;
    private String payId;
    private LocalDateTime transDate;
    private BigDecimal amount;
    private String transType;
    private String walletId;
    private String taguuid;
    private String usernameTo;
    private String userIdTo;
    private String usernameFrom;
    private String userIdFrom;
    private String narration;
    private Location location;
    private String couponCode;
    private String ticketCode;
    private List<ChildTicket> childTickets;
    private List<Coupon> coupons;
    private User userFrom;
    private User userTo;
    private List<TixxTag> tixxTags;
    private Wallet wallet;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransRef() {
        return transRef;
    }

    public void setTransRef(String transRef) {
        this.transRef = transRef;
    }

    public String getPayRef() {
        return payRef;
    }

    public void setPayRef(String payRef) {
        this.payRef = payRef;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public LocalDateTime getTransDate() {
        return transDate;
    }

    public void setTransDate(LocalDateTime transDate) {
        this.transDate = transDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getTaguuid() {
        return taguuid;
    }

    public void setTaguuid(String taguuid) {
        this.taguuid = taguuid;
    }

    public String getUsernameTo() {
        return usernameTo;
    }

    public void setUsernameTo(String usernameTo) {
        this.usernameTo = usernameTo;
    }

    public String getUserIdTo() {
        return userIdTo;
    }

    public void setUserIdTo(String userIdTo) {
        this.userIdTo = userIdTo;
    }

    public String getUsernameFrom() {
        return usernameFrom;
    }

    public void setUsernameFrom(String usernameFrom) {
        this.usernameFrom = usernameFrom;
    }

    public String getUserIdFrom() {
        return userIdFrom;
    }

    public void setUserIdFrom(String userIdFrom) {
        this.userIdFrom = userIdFrom;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
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

    public User getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(User userFrom) {
        this.userFrom = userFrom;
    }

    public User getUserTo() {
        return userTo;
    }

    public void setUserTo(User userTo) {
        this.userTo = userTo;
    }

    public List<TixxTag> getTixxTags() {
        return tixxTags;
    }

    public void setTixxTags(List<TixxTag> tixxTags) {
        this.tixxTags = tixxTags;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }
    
    

    
}




