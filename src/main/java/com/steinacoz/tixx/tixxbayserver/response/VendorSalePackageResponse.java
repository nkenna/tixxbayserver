/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.response;

import com.steinacoz.tixx.tixxbayserver.dao.VendorSalePackageDao;
import com.steinacoz.tixx.tixxbayserver.model.VendorSalePackage;
import com.steinacoz.tixx.tixxbayserver.model.VendorSellItem;
import java.util.List;

/**
 *
 * @author nkenn
 */
public class VendorSalePackageResponse {
    private String status;
    private String message;
    private List<VendorSalePackageDao> sales;
    private VendorSalePackageDao sale;
    private VendorSellItem item;
    private List<VendorSellItem> items;

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

    public List<VendorSalePackageDao> getSales() {
        return sales;
    }

    public void setSales(List<VendorSalePackageDao> sales) {
        this.sales = sales;
    }

    public VendorSalePackageDao getSale() {
        return sale;
    }

    public void setSale(VendorSalePackageDao sale) {
        this.sale = sale;
    }

    public VendorSellItem getItem() {
        return item;
    }

    public void setItem(VendorSellItem item) {
        this.item = item;
    }

    public List<VendorSellItem> getItems() {
        return items;
    }

    public void setItems(List<VendorSellItem> items) {
        this.items = items;
    }
    
    
}



