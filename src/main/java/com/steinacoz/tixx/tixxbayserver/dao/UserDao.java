/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.dao;

import com.steinacoz.tixx.tixxbayserver.model.Location;
import com.steinacoz.tixx.tixxbayserver.model.TixxTag;
import com.steinacoz.tixx.tixxbayserver.model.Transaction;
import com.steinacoz.tixx.tixxbayserver.model.Wallet;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author nkenn
 */
public class UserDao {
    
    private String id;
	private String firstName;
	private String middleName;
	private String lastName;
	private String mobileNumber;
	private String email;
	private String username;
	private String dob;
	private String state;
	private String lga;
	private String address;
	private Location location;
	private LocalDateTime created;
	private LocalDateTime updated;
	private String userType;
	private boolean verified;
	private boolean active;
        private boolean flag;
        private String walletId;
    private String taguuid;
    private String eventId;
        private TixxTag tixxtag;
        private List<Wallet> wallet;
        private List<Transaction> donetrans;

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

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
        
        

    public List<Transaction> getDonetrans() {
        return donetrans;
    }

    public void setDonetrans(List<Transaction> donetrans) {
        this.donetrans = donetrans;
    }

    public List<Wallet> getWallet() {
        return wallet;
    }

    public void setWallet(List<Wallet> wallet) {
        this.wallet = wallet;
    }
        
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLga() {
        return lga;
    }

    public void setLga(String lga) {
        this.lga = lga;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public TixxTag getTixxtag() {
        return tixxtag;
    }

    public void setTixxtag(TixxTag tixxtag) {
        this.tixxtag = tixxtag;
    }
        
        
        
    
}








