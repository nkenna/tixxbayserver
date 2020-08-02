/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.controllers;

import com.steinacoz.tixx.tixxbayserver.dao.TixxTagDao;
import com.steinacoz.tixx.tixxbayserver.model.TixxTag;
import com.steinacoz.tixx.tixxbayserver.model.Transaction;
import com.steinacoz.tixx.tixxbayserver.repo.TixxTagRepo;
import com.steinacoz.tixx.tixxbayserver.repo.TransactionRepo;
import com.steinacoz.tixx.tixxbayserver.repo.UserRepo;
import com.steinacoz.tixx.tixxbayserver.repo.WalletRepo;
import com.steinacoz.tixx.tixxbayserver.request.TixxTagSearchRequest;
import com.steinacoz.tixx.tixxbayserver.response.TixxTagResponse;
import com.steinacoz.tixx.tixxbayserver.utils.Utils;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import java.math.BigDecimal;
import java.util.List;
/**
 *
 * @author nkenn
 */
@RestController
@RequestMapping("/tixxbay/api/tag/v1")
public class TixxTagController {
    @Autowired
    UserRepo userRepo;
    
    @Autowired
    WalletRepo walletRepo;
    
    @Autowired
    TixxTagRepo tixxRepo;
    
    @Autowired
    TransactionRepo transRepo;
	
    
    private DateFormat datetime = new SimpleDateFormat("YY-MM-dd HH:mm:ss");
    @CrossOrigin
    @RequestMapping(value = "/add-band", method = RequestMethod.POST)
    public ResponseEntity<TixxTagResponse> createTixxBand(@RequestBody TixxTag tband){
		TixxTagResponse tbr = new TixxTagResponse();
		
		//check if uuid is empty
		if(tband.getTagUuid() == null || tband.getTagUuid().isEmpty()) {
			tbr.setStatus("failed");
			tbr.setMessage(" tag uuid is required to add band");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(tbr);
		}
		
		//check if addedbyId is empty
		if(tband.getAddedById() == null || tband.getAddedById().isEmpty()) {
			tbr.setStatus("failed");
			tbr.setMessage("a ghost cannot added a band. added by Id required");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(tbr);
		}
		
		//check if tag have been added before
		TixxTag tixxBand = tixxRepo.findByTaguuid(tband.getTagUuid());
		if(tixxBand != null) {
			tbr.setMessage("this band already exist");
			tbr.setStatus("failed");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(tbr);
		}	
		
		tixxBand = new TixxTag();
		BeanUtils.copyProperties(tband, tixxBand);
		
		tixxBand.setActive(true);
		tixxBand.setFlag(false);
		tixxBand.setAvailableAmount(new BigDecimal(0.0));
		tixxBand.setPreviousAmount(new BigDecimal(0.0));
		tixxBand.setDefaultAmount(new BigDecimal(0.0));
		tixxBand.setCreated(LocalDateTime.now());
		tixxBand.setUpdated(LocalDateTime.now());
		tixxBand.setRole(tband.getRole());
		
		try {
			tixxRepo.save(tixxBand);
			tbr.setMessage("band added successfully");
			tbr.setStatus("success");
			return ResponseEntity.ok().body(tbr);
		}catch(Exception e) {
			tbr.setMessage("error occurred adding band");
			tbr.setStatus("failed");
			return ResponseEntity.status(500).body(tbr);
		}
	}
    
    
    @CrossOrigin
	@RequestMapping(value = "/all-bands", method = RequestMethod.GET)
	public ResponseEntity<TixxTagResponse> allBands(){
            
            
		TixxTagResponse tbr = new TixxTagResponse();
		List<TixxTagDao> tixxs = tixxRepo.aggregateAlltags();
		tbr.setMessage("bands found: " + String.valueOf(tixxs.size()));
		tbr.setStatus("success");
		tbr.setTbsdao(tixxs);
		return ResponseEntity.ok().body(tbr);
		
	}
        
        
        @RequestMapping(value = "/band-by-uuid", method = RequestMethod.POST)
	public ResponseEntity<TixxTagResponse> bandByUuid(@RequestBody TixxTagSearchRequest tbsr){
		TixxTagResponse tbr = new TixxTagResponse();
		TixxTagDao tixx = tixxRepo.aggrefindByTagUuid(tbsr.getTaguuid());
		if(tixx != null) {
			tbr.setMessage("band found");
			tbr.setStatus("success");
			tbr.setTbdao(tixx);
			return ResponseEntity.ok().body(tbr);
		}else {
			tbr.setStatus("failed");
			tbr.setMessage("band not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(tbr);
		}
	} 
        
        
        @RequestMapping(value = "/flag-band", method = RequestMethod.POST)
	public ResponseEntity<TixxTagResponse> flagBand(@RequestBody TixxTagSearchRequest tbsr){
		TixxTagResponse tbr = new TixxTagResponse();	
                System.out.println(tbsr.getTaguuid());
		
		TixxTag tixx = tixxRepo.findByTaguuid(tbsr.getTaguuid());
		
		
		if(tixx != null) {
			if(tbsr.isFlag()) {
				tixx.setFlag(true);
				tixxRepo.save(tixx);
				tbr.setStatus("success");
				tbr.setMessage("band have been flagged");
				return ResponseEntity.ok().body(tbr);
			}else {
				tixx.setFlag(false);
				tixxRepo.save(tixx);
				tbr.setStatus("success");
				tbr.setMessage("band have been unflagged");
				return ResponseEntity.ok().body(tbr);
			}
		}else {
			tbr.setStatus("failed");
			tbr.setMessage("band not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(tbr);
		}
	}
        
        
        @RequestMapping(value = "/new-credit-band", method = RequestMethod.POST)
	public ResponseEntity<TixxTagResponse> newCreditBand(@RequestBody TixxTagSearchRequest tbsr){
		TixxTagResponse tbr = new TixxTagResponse();
		
		if(tbsr.getUpdatedById() == null || tbsr.getUpdatedById().isEmpty()) {
			tbr.setStatus("failed");
			tbr.setMessage("a ghost cannot credit a band. updated by Id required");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(tbr);
		}
                
                if(tbsr.getTaguuid() == null || tbsr.getTaguuid().isEmpty()) {
			tbr.setStatus("failed");
			tbr.setMessage("Tag uuid is required");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(tbr);
		}
		
		TixxTag tixx = tixxRepo.findByTaguuid(tbsr.getTaguuid());
		
		if(tixx != null) {
			//get previous previous amt
			tixx.setPreviousAmount(tbsr.getAmount());
			tixx.setAvailableAmount(tbsr.getAmount());
			tixx.setDefaultAmount(tbsr.getAmount());
			tixx.setUpdated(LocalDateTime.now());
			tixx.setUpdatedById(tbsr.getUpdatedById());
			
			try {
				tixxRepo.save(tixx);
				
				//build transcation data
				Transaction trans = new Transaction();
				trans.setAmount(tbsr.getAmount());
				trans.setTransRef(Utils.randomNS(16).toLowerCase());
				trans.setTransDate(LocalDateTime.now());
				trans.setTransType(Utils.newcreditTag);
				trans.setWalletId(tbsr.getWalletId());
				trans.setTagUuid(tbsr.getTaguuid());
				trans.setOriginId(tbsr.getUpdatedById());
				if(tbsr.getNarration() == null || tbsr.getNarration().isEmpty()) {
					trans.setNarration("new tixx band credited.");
				}else {
					trans.setNarration(tbsr.getNarration());
				}
				
				transRepo.save(trans);
				tbr.setStatus("success");
				tbr.setMessage("Band credited successfully");
				tbr.setAmount(trans.getAmount());
				return ResponseEntity.ok().body(tbr);
			}catch(Exception e) {
				tbr.setStatus("failed");
				tbr.setMessage("error occurred crediting band");
				return ResponseEntity.status(500).body(tbr);
			}			
			
			
		}else {
			tbr.setStatus("failed");
			tbr.setMessage("Band not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(tbr);
		}
	}
        
        @RequestMapping(value = "/credit-band", method = RequestMethod.POST)
	public ResponseEntity<TixxTagResponse> creditBand(@RequestBody TixxTagSearchRequest tbsr){
		TixxTagResponse tbr = new TixxTagResponse();
		TixxTag tixx = tixxRepo.findByTaguuid(tbsr.getTaguuid());
		
		// check if this creditor is an admin  or agent
		
		
		if(tixx != null) {
			// check if the updater is from our side
			//get previous previous amt
			BigDecimal prevAmt = tixx.getPreviousAmount();
			BigDecimal availAmt = tixx.getAvailableAmount();
			BigDecimal defAmt = tixx.getDefaultAmount();
			
			/**
			 * check if available AMt == 0
			 * if true, then check default Amt to know the default amt b4
			 * if not true, then check for previous amt
			**/
			
			if(tixx.getAvailableAmount().equals(new BigDecimal(0.0))) {
				// available balance is zero, check for default amt
				System.out.println("balance is zero");
				if(tbsr.getDefaultAmount().equals(tixx.getDefaultAmount())) {
					// default amt is the same. Band is good
					System.out.println("balance is zero, ava: " + String.valueOf(tixx.getAvailableAmount()));
					System.out.println("balance is zero, def: " + String.valueOf(tixx.getDefaultAmount()));
					System.out.println("balance is zero, prev: " + String.valueOf(tixx.getPreviousAmount()));
					tixx.setDefaultAmount(tbsr.getAmount());
					tixx.setAvailableAmount(tbsr.getAmount());
					tixx.setPreviousAmount(tbsr.getAmount());
				}else {
					// default amt is not equal. Band have been tampered with, flag it.
                                        TixxTagDao tdao = new TixxTagDao();
                                        
					tixx.setFlag(true);
					tixxRepo.save(tixx);
                                        BeanUtils.copyProperties(tixx, tdao);
					tbr.setStatus("failed");
					tbr.setMessage("fraud detected");
					tbr.setTbdao(tdao);
					tbr.setAmount(tbsr.getAmount());
					return ResponseEntity.status(HttpStatus.LOCKED).body(tbr);
				}
			}else {
				// band available balance is not zero.
				System.out.println("balance is not zero");
				
				if(tbsr.getPreviousAmount().equals(tixx.getPreviousAmount())) {
					// band previous balance is ok. Band is good
					System.out.println("balance is not zero, ava: " + String.valueOf(tixx.getAvailableAmount()));
					System.out.println("balance is not zero, def: " + String.valueOf(tixx.getDefaultAmount()));
					System.out.println("balance is not zero, prev: " + String.valueOf(tixx.getPreviousAmount()));
					tixx.setPreviousAmount(tixx.getAvailableAmount());
					BigDecimal result = tixx.getAvailableAmount().add(tbsr.getAmount());
					tixx.setAvailableAmount(result);
					
					System.out.println("balance is not zero2, ava: " + String.valueOf(tixx.getAvailableAmount()));
					System.out.println("balance is not zero2, def: " + String.valueOf(tixx.getDefaultAmount()));
					System.out.println("balance is not zero2, prev: " + String.valueOf(tixx.getPreviousAmount()));
					
				}else {
					// band previous balance is not ok. Band have been tampered with, flag it.
					TixxTagDao tdao = new TixxTagDao();
                                        
                                        tixx.setFlag(true);
					tixxRepo.save(tixx);
                                        BeanUtils.copyProperties(tixx, tdao);
					tbr.setStatus("failed");
					tbr.setMessage("fraud detected");
					tbr.setTbdao(tdao);
					tbr.setAmount(tbsr.getAmount());
					return ResponseEntity.status(HttpStatus.LOCKED).body(tbr);
				}
			}
			
			
			tixx.setUpdated(LocalDateTime.now());
			tixx.setUpdatedById(tbsr.getUpdatedById());
			try {
				tixxRepo.save(tixx);
				
				//build transcation data
				Transaction trans = new Transaction();
				trans.setAmount(tbsr.getAmount());
				trans.setTransRef(Utils.randomNS(16).toLowerCase());
				trans.setTransDate(LocalDateTime.now());
				trans.setTransType(Utils.creditTag);
				trans.setWalletId(tbsr.getWalletId());
				trans.setTagUuid(tbsr.getTaguuid());
				trans.setOriginId(tbsr.getUpdatedById());
				if(tbsr.getNarration() == null || tbsr.getNarration().isEmpty()) {
					trans.setNarration("tixx band credited.");
				}else {
					trans.setNarration(tbsr.getNarration());
				}
				
				transRepo.save(trans);
				tbr.setStatus("success");
				tbr.setMessage("Band credited successfully");
				tbr.setAmount(trans.getAmount());
				return ResponseEntity.ok().body(tbr);
			}catch(Exception e) {
				tbr.setStatus("failed");
				tbr.setMessage("error occurred crediting band");
				return ResponseEntity.status(500).body(tbr);
			}			
			
			
		}else {
			tbr.setStatus("failed");
			tbr.setMessage("Band not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(tbr);
		}
	}
        
        
        @RequestMapping(value = "/debit-band", method = RequestMethod.POST)
	public ResponseEntity<TixxTagResponse> debitBand(@RequestBody TixxTagSearchRequest tbsr){
		TixxTagResponse tbr = new TixxTagResponse();
                TixxTagDao tdao = new TixxTagDao();
		TixxTag tixx = tixxRepo.findByTaguuid(tbsr.getTaguuid());
		
		// check if this creditor is an admin  or agent
		
		
		if(tixx != null) {
			// check if the updater is from our side
			//get previous previous amt
			BigDecimal prevAmt = tixx.getPreviousAmount();
			BigDecimal availAmt = tixx.getAvailableAmount();
			BigDecimal defAmt = tixx.getDefaultAmount();
			
			/**
			 * check if available AMt == 0
			 * if true, then check default Amt to know the default amt b4
			 * if not true, then check for previous amt
			**/
			
			if(tixx.getAvailableAmount().equals(new BigDecimal(0.0))) {
				// available balance is zero, check for default amt
				System.out.println("balance is zero");
				System.out.println("balance is zero, ava: " + String.valueOf(tixx.getAvailableAmount()));
				System.out.println("balance is zero, def: " + String.valueOf(tixx.getDefaultAmount()));
				System.out.println("balance is zero, prev: " + String.valueOf(tixx.getPreviousAmount()));
				
				// Band balance is zero. insufficient balance
                                
                                BeanUtils.copyProperties(tixx, tdao);
				tbr.setStatus("failed");
				tbr.setMessage("Insufficient balance");
				tbr.setTbdao(tdao);
				tbr.setAmount(tbsr.getAmount());
				return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(tbr);
				
			}else {
				// band available balance is not zero.
				System.out.println("balance is not zero");
				
				// check if amount is less than band available balance
				if(tbsr.getAmount().compareTo(tixx.getAvailableAmount()) == -1 || tbsr.getAmount().compareTo(tixx.getAvailableAmount()) == 0) {
					// amount is within available balance
					// deduct balance
					if(tbsr.getPreviousAmount().equals(tixx.getPreviousAmount())) {
						// band previous balance is ok. Band is good
						System.out.println("balance is not zero1, ava: " + String.valueOf(tixx.getAvailableAmount()));
						System.out.println("balance is not zero1, def: " + String.valueOf(tixx.getDefaultAmount()));
						System.out.println("balance is not zero1, prev: " + String.valueOf(tixx.getPreviousAmount()));						
						
					}else {
						// band previous balance is not ok. Band have been tampered with, flag it.
						tixx.setFlag(true);
						// Band balance is zero. insufficient balance
						tixxRepo.save(tixx);
                                                BeanUtils.copyProperties(tixx, tdao);
						tbr.setStatus("failed");
						tbr.setMessage("fraud detected");
						tbr.setTbdao(tdao);
						tbr.setAmount(tbsr.getAmount());
						return ResponseEntity.status(HttpStatus.LOCKED).body(tbr);
					}
					
					if(tbsr.getDefaultAmount().equals(tixx.getDefaultAmount())) {
						// band default balance is ok. Band is good
						System.out.println("balance is not zero2, ava: " + String.valueOf(tixx.getAvailableAmount()));
						System.out.println("balance is not zero2, def: " + String.valueOf(tixx.getDefaultAmount()));
						System.out.println("balance is not zero2, prev: " + String.valueOf(tixx.getPreviousAmount()));						
						
					}else {
						// band default balance is not ok. Band have been tampered with, flag it.
						tixx.setFlag(true);
						tixxRepo.save(tixx);
                                                BeanUtils.copyProperties(tixx, tdao);
						tbr.setStatus("failed");
						tbr.setMessage("fraud detected");
						tbr.setTbdao(tdao);
						tbr.setAmount(tbsr.getAmount());
						return ResponseEntity.status(HttpStatus.LOCKED).body(tbr);
					}
					
					
					if(tbsr.getAvailableAmount().equals(tixx.getAvailableAmount())) {
						// band available balance is ok. Band is good
						System.out.println("balance is not zero3, ava: " + String.valueOf(tixx.getAvailableAmount()));
						System.out.println("balance is not zero3, def: " + String.valueOf(tixx.getDefaultAmount()));
						System.out.println("balance is not zero3, prev: " + String.valueOf(tixx.getPreviousAmount()));						
						
					}else {
						// band available balance is not ok. Band have been tampered with, flag it.
						tixx.setFlag(true);
						tixxRepo.save(tixx);
                                                BeanUtils.copyProperties(tixx, tdao);
						tbr.setStatus("failed");
						tbr.setMessage("fraud detected");
						tbr.setTbdao(tdao);
						tbr.setAmount(tbsr.getAmount());
						return ResponseEntity.status(HttpStatus.LOCKED).body(tbr);
					}
					
					
					BigDecimal result = tixx.getAvailableAmount().subtract(tbsr.getAmount());
					tixx.setPreviousAmount(tixx.getAvailableAmount());
					tixx.setAvailableAmount(result);
				}else {
					// amount is greater than available
					//throw insufficient balance
                                        BeanUtils.copyProperties(tixx, tdao);
					tbr.setStatus("failed");
					tbr.setMessage("Insufficient balance");
					tbr.setTbdao(tdao);
					tbr.setAmount(tbsr.getAmount());
					return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(tbr);
					
				}
				
				
			}
			
			
			tixx.setUpdated(LocalDateTime.now());
			tixx.setUpdatedById(tbsr.getUpdatedById());
			try {
				tixxRepo.save(tixx);
				
				//build transcation data
				Transaction trans = new Transaction();
				trans.setAmount(tbsr.getAmount());
				trans.setTransRef(Utils.randomNS(16).toLowerCase());
				trans.setTransDate(LocalDateTime.now());
				trans.setTransType(Utils.creditTag);
				trans.setWalletId(tbsr.getWalletId());
				trans.setTagUuid(tbsr.getTaguuid());
				trans.setOriginId(tbsr.getUpdatedById());
				if(tbsr.getNarration() == null || tbsr.getNarration().isEmpty()) {
					trans.setNarration("tixx band credited.");
				}else {
					trans.setNarration(tbsr.getNarration());
				}
				
				transRepo.save(trans);
                                BeanUtils.copyProperties(tixx, tdao);
				tbr.setStatus("success");
				tbr.setMessage("Band debited successfully");
                                tbr.setTbdao(tdao);
				tbr.setAmount(trans.getAmount());
				return ResponseEntity.ok().body(tbr);
			}catch(Exception e) {
				tbr.setStatus("failed");
				tbr.setMessage("error occurred crediting band");
				return ResponseEntity.status(500).body(tbr);
			}			
			
			
		}else {
			tbr.setStatus("failed");
			tbr.setMessage("Band not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(tbr);
		}
	}
	

	
	
	
    
}

















