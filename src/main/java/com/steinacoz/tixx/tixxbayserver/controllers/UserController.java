/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.controllers;

import com.steinacoz.tixx.tixxbayserver.config.jwtservice.JwtTokenUtil;
import com.steinacoz.tixx.tixxbayserver.dao.UserDao;
import com.steinacoz.tixx.tixxbayserver.model.User;
import com.steinacoz.tixx.tixxbayserver.model.VerifyCode;
import com.steinacoz.tixx.tixxbayserver.model.Wallet;
import com.steinacoz.tixx.tixxbayserver.repo.UserRepo;
import com.steinacoz.tixx.tixxbayserver.repo.VerifyCodeRepo;
import com.steinacoz.tixx.tixxbayserver.repo.WalletRepo;
import com.steinacoz.tixx.tixxbayserver.request.UserLoginRequest;
import com.steinacoz.tixx.tixxbayserver.response.UserResponse;
import com.steinacoz.tixx.tixxbayserver.utils.Utils;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author nkenn
 */
@RestController
@RequestMapping("/tixxbay/api/user/v1")
public class UserController {
    
    @Autowired
    UserRepo userRepo;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    

    
    @Autowired
    WalletRepo walletRepo;
    
    @Autowired
    VerifyCodeRepo vcRepo;
    
    @Autowired
    AuthenticationManager authenticationManager;
    
 
    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;
	
    
	
    private DateFormat datetime = new SimpleDateFormat("YY-MM-dd HH:mm:ss");
	
    public HttpResponse<JsonNode> sendSimpleMessage(User user, String fromEmail, String subject, String content) throws UnirestException {

    	HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/sandbox54745fe7bf41492087ca09fa024aae27.mailgun.org/messages")
	            .basicAuth("api", Utils.API_KEY)
	            .field("from", fromEmail)
	            .field("to", user.getEmail())
	            .field("subject", subject)
	            .field("text", content)
	            .asJson();

        return request;
    }
    
    
    @RequestMapping(value = "/create-user", method = RequestMethod.POST)
	public ResponseEntity<UserResponse> createAgent(@RequestBody User user) {
		
		UserResponse ar = new UserResponse();
		User foundUser;
		
		//search for duplicate email
		foundUser = userRepo.findByEmail(user.getEmail());
		if(foundUser != null) {
			ar.setMessage("user with email already exist");
			ar.setStatus("failed");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ar);
		}
		
		//generate username and check for it exist before
		String gen_username = Utils.randomNS(6);
		boolean username_check = false;
		while(username_check) {
			User us = userRepo.findByUsername(gen_username);
			if(us == null) {
                            username_check = false;
			}
			gen_username = Utils.randomNS(6);
		}
		
		System.out.println(gen_username);
		
		//encyprt paswword
		String enc_password = bCryptPasswordEncoder.encode(user.getPassword());
		user.setUsername(gen_username);
		user.setPassword(enc_password);
		//agent.setUserType(ag);
		user.setVerified(false);
		user.setActive(true);
		user.setCreated(LocalDateTime.now());
		user.setUpdated(LocalDateTime.now());
                user.setFlag(false);
                
                                    		
		User newUser = null;
		
		try {
                                      
                  newUser = userRepo.save(user);
                    //create wallet for user
                    Wallet wallet = new Wallet();
                    wallet.setOwnerid(newUser.getId());
                    wallet.setBalance(BigDecimal.ZERO);
                    wallet.setCreateddate(LocalDateTime.now());
                    wallet.setUpdateddate(LocalDateTime.now());
                    wallet.setStatus(true);
                    wallet.setWalletid(Utils.randomNS(12));
                    
                    walletRepo.save(wallet);
                    
                   
			
		}catch(Exception e) {
			ar.setMessage("error occurred saving user: " + e.getMessage());
			ar.setStatus("failed");
			return ResponseEntity.status(500).body(ar);
		}
		
	
			String enc_aid = Utils.randomNS(15);
                        
                        VerifyCode vc = new VerifyCode();
                        vc.setCode(enc_aid);
                        vc.setUsed(false);
                        vc.setUser(newUser.getId());
                        
                        vcRepo.save(vc);
                        
			System.out.println(enc_aid);
			HttpResponse<JsonNode> jn = sendSimpleMessage(
					newUser, 
					"welcome@tixxbay.ng", 
					"Welcome to Tixxbay. Activate your account.",
					"http://127.0.0.1:8090/tixxbay/api/user/v1/verify-user/" + enc_aid
			);
                        
                UserDao usdao = new UserDao();
                BeanUtils.copyProperties(newUser, usdao);		
		
		ar.setStatus("success");
		ar.setMessage("user saved successfully");
                ar.setUser(usdao);
		return ResponseEntity.ok().body(ar);
		
	}
        
        @CrossOrigin
        @RequestMapping(value = "/verify-user/{enc_aid}", method = RequestMethod.GET)
	public ResponseEntity<UserResponse>  verifyUser(@PathVariable String enc_aid) {
		UserResponse cur = new UserResponse();
		System.out.println(enc_aid);
		//find user code
                VerifyCode vc = vcRepo.findByCode(enc_aid);
                
                if(vc == null){
                    cur.setStatus("failed");
                    cur.setMessage("Verification code is invalid");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(cur);
                }
                else if(vc != null && vc.isUsed() == true){
                    cur.setStatus("failed");
                    cur.setMessage("Verification code have been used");
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(cur);
                }
                
                User user = userRepo.findById(vc.getUser()).orElseGet(null);
                if(user != null){
                    if(user.isVerified()){
                        cur.setStatus("failed");
                        cur.setMessage("user already verified");
                        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(cur);
                    }
                    
                    user.setVerified(true);
                    user.setUpdated(LocalDateTime.now());
                    userRepo.save(user);
                    vc.setUsed(true);
                    vcRepo.save(vc);
                    cur.setStatus("success");
                    cur.setMessage("user verified successfully");
                    return ResponseEntity.ok().body(cur);
                }else{
                   cur.setStatus("failed");
                   cur.setMessage("user not found");
                   return ResponseEntity.status(HttpStatus.NOT_FOUND).body(cur); 
                }
           	
	}
        
        
        @CrossOrigin
    @RequestMapping(value = "auth/login-user", method = RequestMethod.POST)
    public ResponseEntity<UserResponse>  loginUser(@RequestBody UserLoginRequest alr){
    	UserResponse cur = new UserResponse();
        System.out.println(111);
        System.out.println(alr.getUsername());
        System.out.println(alr.getPassword());
        System.out.println(222);
		
    	User user = userRepo.findByUsername(alr.getUsername());
    	if(user == null) {
    		cur.setStatus("failed");
		cur.setMessage("user not found");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(cur);
    	}
    	
    	if(!user.isVerified()) {
            cur.setStatus("failed");
            cur.setMessage("user not validated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(cur);
    	}
    	
    	if(!user.isActive()) {
            cur.setStatus("failed");
            cur.setMessage("account not active");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(cur);
    	}
        
        try{
            System.out.println(alr.getUsername());
            System.out.println(alr.getPassword());
            
            final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        alr.getUsername(),
                        alr.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
      

                
                User usernew = userRepo.findByUsername(alr.getUsername());
               final String token = jwtTokenUtil.generateToken(user);
                UserDao aDao = new UserDao();
    		BeanUtils.copyProperties(usernew, aDao);
                cur.setUser(aDao);
    		cur.setStatus("success");
                cur.setToken(token);
		cur.setMessage("user logged in successfully");
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(cur);
                
                } catch (AuthenticationException e) {
                    cur.setStatus("failed");
                
		cur.setMessage("Invalid email/password supplied");
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(cur);
           // throw new BadCredentialsException("Invalid email/password supplied");
        }
    	
    	
    }
    
    @RequestMapping(value = "/all-users", method = RequestMethod.GET)
    public ResponseEntity<UserResponse> allUsers(){
            
		UserResponse ar = new UserResponse();
		List<UserDao> users = userRepo.aggregateAllUsers();
		ar.setMessage("users found: " + String.valueOf(users.size()));
		ar.setStatus("success");
		ar.setUsers(users);
		return ResponseEntity.ok().body(ar);
		
    }
    

         
        
    
}







































