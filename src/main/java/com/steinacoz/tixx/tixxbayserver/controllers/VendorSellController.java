/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.controllers;

import com.steinacoz.tixx.tixxbayserver.dao.UserDao;
import com.steinacoz.tixx.tixxbayserver.model.Event;
import com.steinacoz.tixx.tixxbayserver.model.Location;
import com.steinacoz.tixx.tixxbayserver.model.SaleTransaction;
import com.steinacoz.tixx.tixxbayserver.model.TixxTag;
import com.steinacoz.tixx.tixxbayserver.model.User;
import com.steinacoz.tixx.tixxbayserver.model.UserPoint;
import com.steinacoz.tixx.tixxbayserver.model.VendorSalePackage;
import com.steinacoz.tixx.tixxbayserver.model.VendorSellItem;
import com.steinacoz.tixx.tixxbayserver.model.Wallet;
import com.steinacoz.tixx.tixxbayserver.model.WalletTransaction;
import com.steinacoz.tixx.tixxbayserver.repo.SaleTransactionRepo;
import com.steinacoz.tixx.tixxbayserver.repo.TixxTagRepo;
import com.steinacoz.tixx.tixxbayserver.repo.UserPointRepo;
import com.steinacoz.tixx.tixxbayserver.repo.UserRepo;
import com.steinacoz.tixx.tixxbayserver.repo.VendorSalePackageRepo;
import com.steinacoz.tixx.tixxbayserver.repo.VendorSellItemRepo;
import com.steinacoz.tixx.tixxbayserver.repo.WalletRepo;
import com.steinacoz.tixx.tixxbayserver.repo.WalletTransactionRepo;
import com.steinacoz.tixx.tixxbayserver.response.EventResponse;
import com.steinacoz.tixx.tixxbayserver.response.VendorSalePackageResponse;
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
@RequestMapping("/tixxbay/api/vendor/v1")
public class VendorSellController {
    
    @Autowired
    VendorSalePackageRepo vsprRepo;
    
    @Autowired
    TixxTagRepo tixxRepo;
    
    @Autowired
    UserRepo userRepo;
    
    @Autowired
    WalletRepo walletRepo;
    
    @Autowired
    UserPointRepo upRepo;
    
    @Autowired
    WalletTransactionRepo walletTransRepo;
    
    @Autowired
    SaleTransactionRepo stRepo;
    
    @Autowired
    VendorSellItemRepo vsiRepo;
    
    @CrossOrigin
    @RequestMapping(value = "/add-vendor-sell", method = RequestMethod.POST)
    public ResponseEntity<VendorSalePackageResponse> createVendorSellItem(@RequestBody VendorSalePackage vsp){
        VendorSalePackageResponse vspr = new VendorSalePackageResponse();
        
        //generate ref and check for it exist before
		String gen_ref = "TIXX" + Utils.randomNS(12) + "VEN";
		boolean ref_check = false;
		while(ref_check) {
                    VendorSalePackage new_vs = vsprRepo.findByTransRef(gen_ref);
			
			if(new_vs == null) {
                            ref_check = false;
			}
			gen_ref = "TIXX" + Utils.randomNS(12) + "VEN";
		}
		
		System.out.println(gen_ref);
                
                if(vsp.getTaguuid() == null || vsp.getTaguuid().isEmpty()){
                    vspr.setStatus("failed");
                    vspr.setMessage("Tag ID is required");
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(vspr);
                }
                
                if(vsp.getVendorUsername() == null || vsp.getVendorUsername().isEmpty()){
                    vspr.setStatus("failed");
                    vspr.setMessage("Vendor username is required");
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(vspr);
                }
                
                User vendor = userRepo.findByUsername(vsp.getVendorUsername());
                
                if(vendor == null){
                    vspr.setStatus("failed");
                    vspr.setMessage("Vendor data not found");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(vspr);
                }
                
                Wallet wallet = walletRepo.findByWalletid(vendor.getWalletId());
                if(wallet == null){
                    vspr.setStatus("failed");
                    vspr.setMessage("wallet data not found");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(vspr);
                }
                
                if(vsp.getEventCode() == null || vsp.getEventCode().isEmpty()){
                    vspr.setStatus("failed");
                    vspr.setMessage("Event code is required");
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(vspr);
                }
                
                if(vsp.getAvailableAmount() == null || vsp.getEventCode() == null || vsp.getEventCode() == null || vsp.getTotalAmount() == null){
                    vspr.setStatus("failed");
                    vspr.setMessage("Tag available amount, previous amount, total amount or default amount is required");
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(vspr);
                }
                
                TixxTag tag = tixxRepo.findByTaguuid(vsp.getTaguuid());
                
                if(tag == null){
                   vspr.setStatus("failed");
                   vspr.setMessage("Tag data not found");
                   return ResponseEntity.status(HttpStatus.NOT_FOUND).body(vspr); 
                }
                
                double defaultAmt = tag.getDefaultAmount().doubleValue();
                double availableAmt = tag.getAvailableAmount().doubleValue();
                double previousAmt = tag.getPreviousAmount().doubleValue();
                double totalAmt = vsp.getTotalAmount().doubleValue();
                
                // check for sufficient balance
                if(availableAmt < totalAmt){
                    vspr.setStatus("failed");
                    vspr.setMessage("Insufficient balance. Fund this tag");
                    return ResponseEntity.status(HttpStatus.UPGRADE_REQUIRED).body(vspr);
                }
                
                // deduct money from Tag
                double newTagBalance = availableAmt - totalAmt;
                
                // update tag data
                tag.setPreviousAmount(tag.getAvailableAmount());
                tag.setAvailableAmount(new BigDecimal(newTagBalance));
                tag.setUpdated(LocalDateTime.now());
                TixxTag updatedTag = tixxRepo.save(tag);
                
                // update vendor wallet
                wallet.setBalance(wallet.getBalance().add(new BigDecimal(totalAmt)));
                wallet.setUpdateddate(LocalDateTime.now());
                Wallet nWallet = walletRepo.save(wallet);
                
                // update user points for tag owner
                User tagUser = userRepo.findByTaguuid(updatedTag.getTaguuid());
                UserPoint upTagOwner = upRepo.findByTaguuid(tagUser.getTaguuid());
                    if(upTagOwner != null){
                        if(totalAmt < 5000.0){
                            upTagOwner.setPoints(upTagOwner.getPoints() + 0.3);
                        }else if(totalAmt > 5000.0 && totalAmt < 25000.0){
                            upTagOwner.setPoints(upTagOwner.getPoints() + 0.4);
                        }else if(totalAmt> 25000.0 && totalAmt < 50000.0){
                            upTagOwner.setPoints(upTagOwner.getPoints() + 0.5);
                        }else if(totalAmt > 50000.0 ){
                            upTagOwner.setPoints(upTagOwner.getPoints() + 0.75);
                        }
                        upRepo.save(upTagOwner);
                    }
                    
                // update user points for vendor
                UserPoint upVenOwner = upRepo.findByUsername(vendor.getUsername());
                    if(upVenOwner != null){
                        if(totalAmt < 5000.0){
                            upVenOwner.setPoints(upVenOwner.getPoints() + 0.3);
                        }else if(totalAmt > 5000.0 && totalAmt < 25000.0){
                            upVenOwner.setPoints(upVenOwner.getPoints() + 0.4);
                        }else if(totalAmt> 25000.0 && totalAmt < 50000.0){
                            upVenOwner.setPoints(upVenOwner.getPoints() + 0.5);
                        }else if(totalAmt > 50000.0 ){
                            upVenOwner.setPoints(upVenOwner.getPoints() + 0.75);
                        }
                        upRepo.save(upVenOwner);
                    } 
                    
                // add item to db
                for(VendorSellItem vss : vsp.getVendorSellItems()){
                    vss.setEventCode(vsp.getEventCode());
                    vss.setVendorUsername(vendor.getUsername());
                    vsiRepo.save(vss);                    
                }
               
                // create sales Transaction data 1
                                
                SaleTransaction st = new SaleTransaction();
                st.setTransRef(gen_ref);
                st.setTransDate(LocalDateTime.now());
                st.setTotalAmount(new BigDecimal(totalAmt));
                User payee = userRepo.findByTaguuid(tag.getTaguuid());
                UserDao daoPayee = new UserDao();
                BeanUtils.copyProperties(payee, daoPayee);
                st.setBoughtBy(daoPayee);
                
                UserDao daoVendor = new UserDao();
                BeanUtils.copyProperties(vendor, daoVendor);
                st.setSoldBy(daoVendor);
                
                st.setEventCode(vsp.getEventCode());
                st.setNarration("Vendor Wallet was credit");
                st.setLocation(vsp.getLocation());
                st.setTaguuid(tag.getTaguuid());
                stRepo.save(st);
                
                //build wallet transaction record
                WalletTransaction wt = new WalletTransaction();
                wt.setTransRef("TIXX" + Utils.randomNS(12));
                wt.setTransDate(LocalDateTime.now());
                wt.setTotalAmount(nWallet.getBalance());
                wt.setTransType(Utils.creditWallet);
                wt.setWalletId(nWallet.getWalletid());
                wt.setWalletOwnerUsername(nWallet.getOwnerUsername());
                wt.setEventCode(vsp.getEventCode());
                wt.setNarration("Vendor Wallet was credit");
                wt.setLocation(vsp.getLocation());
                walletTransRepo.save(wt);
                
                
                // populate sell package data
                vsp.setTransDate(LocalDateTime.now());
                VendorSalePackage newVS = vsprRepo.save(vsp);
                vspr.setMessage("item(s) sales was successful");
                vspr.setStatus("success");
                return ResponseEntity.ok().body(vspr);
                
    }
}


























