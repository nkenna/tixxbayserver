/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.response;

import com.steinacoz.tixx.tixxbayserver.dao.TixxTagDao;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author nkenn
 */
public class TixxTagResponse {
    private String status;
    private String message;
    private BigDecimal amount;
    private TixxTagDao tbdao;
    private List<TixxTagDao> tbsdao;

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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TixxTagDao getTbdao() {
        return tbdao;
    }

    public void setTbdao(TixxTagDao tbdao) {
        this.tbdao = tbdao;
    }

    public List<TixxTagDao> getTbsdao() {
        return tbsdao;
    }

    public void setTbsdao(List<TixxTagDao> tbsdao) {
        this.tbsdao = tbsdao;
    }
    
    
}


