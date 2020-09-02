/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.controllers;

import com.steinacoz.tixx.tixxbayserver.dao.WalletDao;
import com.steinacoz.tixx.tixxbayserver.model.User;
import com.steinacoz.tixx.tixxbayserver.model.Wallet;
import com.steinacoz.tixx.tixxbayserver.repo.TixxTagRepo;
import com.steinacoz.tixx.tixxbayserver.repo.TransactionRepo;
import com.steinacoz.tixx.tixxbayserver.repo.UserRepo;
import com.steinacoz.tixx.tixxbayserver.repo.WalletRepo;
import com.steinacoz.tixx.tixxbayserver.response.UserResponse;
import com.steinacoz.tixx.tixxbayserver.response.WalletResponse;
import com.steinacoz.tixx.tixxbayserver.utils.Utils;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nkenn
 */
@RestController
@RequestMapping("/tixxbay/api/wallet/v1")
public class WalletController {
    @Autowired
    UserRepo userRepo;
    
    @Autowired
    WalletRepo walletRepo;
    
    @Autowired
    TixxTagRepo tixxRepo;
    
    @Autowired
    TransactionRepo transRepo;
    
    @CrossOrigin
    @RequestMapping(value = "/create-wallet", method = RequestMethod.POST)
    public ResponseEntity<WalletResponse> createWallet(@RequestBody Wallet walletReq){
        WalletResponse wr = new WalletResponse();
        WalletDao wdao = new WalletDao();
        
        //generate username and check for it exist before
	String gen_wallet_id = Utils.randomNS(12);
        boolean wallet_check = false;
	while(wallet_check) {
            Wallet us = walletRepo.findByWalletid(gen_wallet_id);
            if(us == null) {
                wallet_check = false;
            }
            gen_wallet_id = Utils.randomNS(12);
	}
        System.out.println(gen_wallet_id);
        
        Wallet wallet = new Wallet();
        wallet.setOwnerid(walletReq.getOwnerid());
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setCreateddate(LocalDateTime.now());
        wallet.setUpdateddate(LocalDateTime.now());
        wallet.setStatus(true);
        wallet.setWalletid(Utils.randomNS(12));
        
        try{
            walletRepo.save(wallet);
            BeanUtils.copyProperties(wallet, wdao);
            wr.setBalance(wallet.getBalance());
            wr.setStatus("success");
            wr.setMessage("Wallet successfully created");
            wr.setWallet(wdao);
            return ResponseEntity.ok().body(wr);
        }catch(Exception e){
            wr.setStatus("failed");
            wr.setMessage("creating wallet failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(wr);
        }
        
        
        
        
    }
    
    @CrossOrigin
    @RequestMapping(value = "/get-user-wallet", method = RequestMethod.PUT)
    public ResponseEntity<WalletResponse> getUserWallet(@RequestBody Wallet wal){
        WalletResponse wr = new WalletResponse();
        WalletDao wdao = new WalletDao();       
        
        
        Wallet wallet = walletRepo.findByWalletid(wal.getWalletid());
        
        if(wallet != null){
            BeanUtils.copyProperties(wallet, wdao);
            wr.setStatus("success");
            wr.setWallet(wdao);
            wr.setMessage("wallet found");
            return ResponseEntity.ok().body(wr);
        }else{
          wr.setStatus("failed");
            wr.setMessage("wallet not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(wr);  
        }
    }
}






