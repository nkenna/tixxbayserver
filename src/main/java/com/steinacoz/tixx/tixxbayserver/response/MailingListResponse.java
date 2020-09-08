/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.response;

import com.steinacoz.tixx.tixxbayserver.model.MailingList;
import java.util.List;

/**
 *
 * @author nkenn
 */
public class MailingListResponse {
    private String status;
    private String message;
    private MailingList mailItem;
    private List<MailingList> mailItems;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MailingList getMailItem() {
        return mailItem;
    }

    public void setMailItem(MailingList mailItem) {
        this.mailItem = mailItem;
    }

    public List<MailingList> getMailItems() {
        return mailItems;
    }

    public void setMailItems(List<MailingList> mailItems) {
        this.mailItems = mailItems;
    }
    
    
}


