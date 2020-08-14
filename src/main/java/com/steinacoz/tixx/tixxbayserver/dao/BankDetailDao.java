/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.dao;

import com.steinacoz.tixx.tixxbayserver.model.User;
import java.time.LocalDateTime;

/**
 *
 * @author nkenn
 */
public class BankDetailDao {
    private String id;
    private String accountName;
    private String accountNumber;
    private String bankCode;
    private int bankId;
    private String ownerId;
    private String ownerUsername;
    private boolean flagged; //true or false
    private LocalDateTime created;
    private LocalDateTime updated;
    private User user;
    
}


