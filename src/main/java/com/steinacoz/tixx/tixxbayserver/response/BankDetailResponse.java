/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.response;
import com.steinacoz.tixx.tixxbayserver.dao.BankDetailDao;
import com.steinacoz.tixx.tixxbayserver.model.BankDetail;
import java.util.List;

/**
 *
 * @author nkenn
 */
public class BankDetailResponse {
    private String status;
    private String message;
    private BankDetailDao bankDetail;
    private List<BankDetailDao> bankDetails;

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

    public BankDetailDao getBankDetail() {
        return bankDetail;
    }

    public void setBankDetail(BankDetailDao bankDetail) {
        this.bankDetail = bankDetail;
    }

    public List<BankDetailDao> getBankDetails() {
        return bankDetails;
    }

    public void setBankDetails(List<BankDetailDao> bankDetails) {
        this.bankDetails = bankDetails;
    }

    
    
    
}



