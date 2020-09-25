/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.model;

import org.springframework.data.annotation.Id;

/**
 *
 * @author nkenn
 */
public class Discount {
    @Id private String id;
    private double nfcDiscount;
    private double qrDiscount;

    public String getId() {
        return id;
    }

    

    public double getNfcDiscount() {
        return nfcDiscount;
    }

    public void setNfcDiscount(double nfcDiscount) {
        this.nfcDiscount = nfcDiscount;
    }

    public double getQrDiscount() {
        return qrDiscount;
    }

    public void setQrDiscount(double qrDiscount) {
        this.qrDiscount = qrDiscount;
    }
    
    
}



