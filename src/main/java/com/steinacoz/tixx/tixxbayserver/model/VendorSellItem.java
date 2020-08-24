/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.model;

import java.math.BigDecimal;
import org.springframework.data.annotation.Id;

/**
 *
 * @author nkenn
 */
public class VendorSellItem {
    @Id private String id;
    private String name;
    private BigDecimal amount;
    private int quantity;
}


