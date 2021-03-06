/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.steinacoz.tixx.tixxbayserver.config.jwtservice.JwtTokenUtil;
import com.steinacoz.tixx.tixxbayserver.dao.BankDetailDao;
import com.steinacoz.tixx.tixxbayserver.dao.UserDao;
import com.steinacoz.tixx.tixxbayserver.errormodels.NotFoundException;
import com.steinacoz.tixx.tixxbayserver.model.BankDetail;
import com.steinacoz.tixx.tixxbayserver.model.ERole;
import com.steinacoz.tixx.tixxbayserver.model.Location;
import com.steinacoz.tixx.tixxbayserver.model.Role;
import com.steinacoz.tixx.tixxbayserver.model.User;
import com.steinacoz.tixx.tixxbayserver.model.UserPoint;
import com.steinacoz.tixx.tixxbayserver.model.VerifyCode;
import com.steinacoz.tixx.tixxbayserver.model.Wallet;
import com.steinacoz.tixx.tixxbayserver.repo.BankDetailRepo;
import com.steinacoz.tixx.tixxbayserver.repo.RoleRepo;
import com.steinacoz.tixx.tixxbayserver.repo.UserPointRepo;
import com.steinacoz.tixx.tixxbayserver.repo.UserRepo;
import com.steinacoz.tixx.tixxbayserver.repo.VerifyCodeRepo;
import com.steinacoz.tixx.tixxbayserver.repo.WalletRepo;
import com.steinacoz.tixx.tixxbayserver.request.ChangeUserPasswordRequest;
import com.steinacoz.tixx.tixxbayserver.request.CreateUserRequest;
import com.steinacoz.tixx.tixxbayserver.request.ResolveBankRequest;
import com.steinacoz.tixx.tixxbayserver.request.RestUserPasswordRequest;
import com.steinacoz.tixx.tixxbayserver.request.RoleRequest;
import com.steinacoz.tixx.tixxbayserver.request.UpdateUserRequest;
import com.steinacoz.tixx.tixxbayserver.request.UserFlagRequest;
import com.steinacoz.tixx.tixxbayserver.request.UserLoginRequest;
import com.steinacoz.tixx.tixxbayserver.response.BankDetailResponse;
import com.steinacoz.tixx.tixxbayserver.response.InitializeVerifyResponse;
import com.steinacoz.tixx.tixxbayserver.response.ResolveBankResponse;
import com.steinacoz.tixx.tixxbayserver.response.UserResponse;
import com.steinacoz.tixx.tixxbayserver.utils.Utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    BankDetailRepo bankDetailsRepo;
    
    @Autowired
    RoleRepo roleRepo;
    
    @Autowired
    UserPointRepo upRepo;
    
    @Autowired
    AuthenticationManager authenticationManager;
    
 
    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;
	
    private DateFormat datetime = new SimpleDateFormat("YY-MM-dd HH:mm:ss");
	
   
    
    
    @RequestMapping(value = "/create-user", method = RequestMethod.POST)
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest user) {
		
		UserResponse ar = new UserResponse();
		User foundUser;
                
                if(user.getEmail() == null || user.getEmail().isEmpty()){
                    ar.setMessage("user email is required");
			ar.setStatus("failed");
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ar);
                }
                
                if(user.getPassword() == null || user.getPassword().isEmpty()){
                    ar.setMessage("user password is required");
                    ar.setStatus("failed");
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ar);
                }
                
                if(user.getPassword().length() < 8){
                    ar.setMessage("user password must be more than 8 characters");
                    ar.setStatus("failed");
                    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(ar);
                }
		
		//search for duplicate email
		foundUser = userRepo.findByEmail(user.getEmail());
		if(foundUser != null) {
			ar.setMessage("user with email already exist");
			ar.setStatus("failed");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ar);
		}
                
                User newUser = new User();
		
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
		newUser.setUsername(gen_username);
                newUser.setEmail(user.getEmail());
                newUser.setMobileNumber(user.getMobileNumber());
		newUser.setPassword(enc_password);
		//agent.setUserType(ag);
		newUser.setVerified(false);
		newUser.setActive(true);
		newUser.setCreated(LocalDateTime.now());
		newUser.setUpdated(LocalDateTime.now());
                newUser.setFlag(false);
                
                //set user roles
                Set<String> strRoles = user.getRoles();
		Set<Role> roles = new HashSet<>();
                
                if (strRoles == null) {
			Role userRole = roleRepo.findByName("ROLE_USER");
			if(userRole != null){
                            roles.add(userRole);
                        }else{
                            ar.setMessage("role not found");
                            ar.setStatus("failed");
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ar);
                        }		
			
		}else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepo.findByName("ROLE_ADMIN");
                                        if(adminRole == null){
                                            throw( new RuntimeException("Error: admin Role is not found."));
                                        }
					
					roles.add(adminRole);

					break;
				case "vendor":
					Role vendorRole = roleRepo.findByName("ROLE_VENDOR");
					if(vendorRole  == null){
                                            throw( new RuntimeException("Error: vendor Role not found."));
                                        }
					roles.add(vendorRole);

					break;
                                case "superadmin":
					Role superAdminRole = roleRepo.findByName("ROLE_SUPERADMIN");
					if(superAdminRole  == null){
                                            throw( new RuntimeException("Error: super adminRole not found."));
                                        }
					roles.add(superAdminRole);

					break;
                                case "eventmanager":
					Role eventmanagerRole = roleRepo.findByName("ROLE_EVENTMANAGER");
					if(eventmanagerRole  == null){
                                            throw( new RuntimeException("Error: event manager Role  not found."));
                                        }
					roles.add(eventmanagerRole);
					break;        
				default:
					Role userRole = roleRepo.findByName("ROLE_USER");
					if(userRole  == null){
                                            throw( new RuntimeException("Error: user Role2 not found."));
                                        }
					roles.add(userRole);
				}
			});
		}
                
                newUser.setRoles(roles);
		
		try {
                                      
                  newUser = userRepo.save(newUser);
                    //create wallet for user
                    Wallet wallet = new Wallet();
                    wallet.setOwnerid(newUser.getId());
                    wallet.setBalance(BigDecimal.ZERO);
                    wallet.setCreateddate(LocalDateTime.now());
                    wallet.setUpdateddate(LocalDateTime.now());
                    wallet.setStatus(true);
                    wallet.setWalletid(Utils.randomNS(12));
                    
                    Wallet nWallet = walletRepo.save(wallet);
                    
                    newUser.setWalletId(nWallet.getWalletid());
                    
                    //create user point
                    UserPoint up = new UserPoint();
                    up.setUsername(newUser.getUsername());
                    up.setPoints(0.1); // free points for creating account
                    upRepo.save(up);
                    
                    newUser = userRepo.save(newUser);                   
                    
                   
			
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
                        Email from = new Email("support@tixxbay.com");
                        String subject = "Welcome to Tixxbay. Verify your account.";
                        Email to = new Email(newUser.getEmail());
                        Content content = new Content("text/html", Utils.sendHtmlEmailNewEvent("https://tixxbayserver.herokuapp.com/tixxbay/api/user/v1/verify-user/" + enc_aid));
                        Mail mail = new Mail(from, subject, to, content);
                        System.out.println(mail.from.getEmail());
                        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API")); 
                        Request request = new Request();
                        try {
                          request.setMethod(Method.POST);
                          request.setEndpoint("mail/send");
                          request.setBody(mail.build());
                          Response response = sg.api(request);
                          System.out.println(response.getStatusCode());
                          System.out.println(response.getBody());
                          System.out.println(response.getHeaders());
                          
                        } catch (IOException ex) {
                          
                        }
			
                UserDao usdao = new UserDao();
                BeanUtils.copyProperties(newUser, usdao);		
		
		ar.setStatus("success");
		ar.setMessage("user saved successfully");
                ar.setUser(usdao);
		return ResponseEntity.ok().body(ar);
		
	}
    
    @RequestMapping(value = "/create-user-admin", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> createUserAdmin(@RequestBody CreateUserRequest user) {
		
		UserResponse ar = new UserResponse();
		User foundUser;
                
                if(user.getEmail() == null || user.getEmail().isEmpty()){
                    ar.setMessage("user email is required");
			ar.setStatus("failed");
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ar);
                }
                
                if(user.getPassword() == null || user.getPassword().isEmpty()){
                    ar.setMessage("user password is required");
                    ar.setStatus("failed");
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ar);
                }
                
                if(user.getPassword().length() < 8){
                    ar.setMessage("user password must be more than 8 characters");
                    ar.setStatus("failed");
                    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(ar);
                }
		
		//search for duplicate email
		foundUser = userRepo.findByEmail(user.getEmail());
		if(foundUser != null) {
			ar.setMessage("user with email already exist");
			ar.setStatus("failed");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ar);
		}
                
                User newUser = new User();
		
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
		newUser.setUsername(gen_username);
                newUser.setEmail(user.getEmail());
                newUser.setMobileNumber(user.getMobileNumber());
		newUser.setPassword(enc_password);
		//agent.setUserType(ag);
		newUser.setVerified(false);
		newUser.setActive(true);
		newUser.setCreated(LocalDateTime.now());
		newUser.setUpdated(LocalDateTime.now());
                newUser.setFlag(false);
                
                //set user roles
                Set<String> strRoles = user.getRoles();
		Set<Role> roles = new HashSet<>();
                
                if (strRoles == null) {
			Role userRole = roleRepo.findByName("ROLE_USER");
			if(userRole != null){
                            roles.add(userRole);
                        }else{
                            ar.setMessage("role not found");
                            ar.setStatus("failed");
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ar);
                        }		
			
		}else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepo.findByName("ROLE_ADMIN");
                                        if(adminRole == null){
                                            throw( new RuntimeException("Error: admin Role is not found."));
                                        }
					
					roles.add(adminRole);

					break;
				case "vendor":
					Role vendorRole = roleRepo.findByName("ROLE_VENDOR");
					if(vendorRole  == null){
                                            throw( new RuntimeException("Error: vendor Role not found."));
                                        }
					roles.add(vendorRole);

					break;
                                case "superadmin":
					Role superAdminRole = roleRepo.findByName("ROLE_SUPERADMIN");
					if(superAdminRole  == null){
                                            throw( new RuntimeException("Error: super adminRole not found."));
                                        }
					roles.add(superAdminRole);

					break;
                                case "eventmanager":
					Role eventmanagerRole = roleRepo.findByName("ROLE_EVENTMANAGER");
					if(eventmanagerRole  == null){
                                            throw( new RuntimeException("Error: event manager Role  not found."));
                                        }
					roles.add(eventmanagerRole);
					break;        
				default:
					Role userRole = roleRepo.findByName("ROLE_USER");
					if(userRole  == null){
                                            throw( new RuntimeException("Error: user Role2 not found."));
                                        }
					roles.add(userRole);
				}
			});
		}
                
                newUser.setRoles(roles);
		
		try {
                                      
                  newUser = userRepo.save(newUser);
                    //create wallet for user
                    Wallet wallet = new Wallet();
                    wallet.setOwnerid(newUser.getId());
                    wallet.setBalance(BigDecimal.ZERO);
                    wallet.setCreateddate(LocalDateTime.now());
                    wallet.setUpdateddate(LocalDateTime.now());
                    wallet.setStatus(true);
                    wallet.setWalletid(Utils.randomNS(12));
                    
                    Wallet nWallet = walletRepo.save(wallet);
                    
                    newUser.setWalletId(nWallet.getWalletid());
                    
                    //create user point
                    UserPoint up = new UserPoint();
                    up.setUsername(newUser.getUsername());
                    up.setPoints(0.1); // free points for creating account
                    upRepo.save(up);
                    
                    newUser = userRepo.save(newUser);                   
                    
                   
			
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
                        Email from = new Email("support@tixxbay.com");
                        String subject = "Your Admin account is ready.";
                        Email to = new Email(newUser.getEmail());
                        Content content = new Content("text/plain", "https://tixxbayserver.herokuapp.com/tixxbay/api/user/v1/verify-user/" + enc_aid);
                        Mail mail = new Mail(from, subject, to, content);
                        System.out.println(mail.from.getEmail());
                        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API")); 
                        Request request = new Request();
                        try {
                          request.setMethod(Method.POST);
                          request.setEndpoint("mail/send");
                          request.setBody(mail.build());
                          Response response = sg.api(request);
                          System.out.println(response.getStatusCode());
                          System.out.println(response.getBody());
                          System.out.println(response.getHeaders());
                          
                        } catch (IOException ex) {
                          
                        }
			
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
    
    @RequestMapping(value = "/resend-email", method = RequestMethod.PUT)
    public ResponseEntity<UserResponse> resendUser(@RequestBody User uur){
        UserResponse ur = new UserResponse();
        
        User user = userRepo.findByEmail(uur.getEmail());
        if(user != null){
            VerifyCode vc = vcRepo.findByUser(user.getId());
            if(vc != null){
                if(vc.isUsed() == false){
                    Email from = new Email("support@tixxbay.com");
                        String subject = "Welcome to Tixxbay. Verify your account.";
                        Email to = new Email(user.getEmail());
                        Content content = new Content("text/html", Utils.sendHtmlEmailNewEvent("https://tixxbayserver.herokuapp.com/tixxbay/api/user/v1/verify-user/" + vc.getCode()));
                        Mail mail = new Mail(from, subject, to, content);
                        System.out.println(mail.from.getEmail());
                        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API")); 
                        Request request = new Request();
                        try {
                          request.setMethod(Method.POST);
                          request.setEndpoint("mail/send");
                          request.setBody(mail.build());
                          Response response = sg.api(request);
                          System.out.println(response.getStatusCode());
                          System.out.println(response.getBody());
                          System.out.println(response.getHeaders());
                          
                          
                        } catch (Exception ex) {
                          //return null;
                        }
                        
                        ur.setStatus("success");
                            ur.setMessage("user verification code already used");
                            return ResponseEntity.ok().body(ur);
                }else{
                   ur.setStatus("failed");
                   ur.setMessage("user verification code already used");
                   return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ur);  
                }
            }else{
               ur.setStatus("failed");
               ur.setMessage("user verification code not found");
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ur); 
            }
            
        }else{
            ur.setStatus("failed");
            ur.setMessage("user not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ur);
        }
    }
    
    @RequestMapping(value = "/update-user", method = RequestMethod.PUT)
    public ResponseEntity<UserResponse> updateUser(@RequestBody UpdateUserRequest uur){
		UserResponse ar = new UserResponse();
		UserDao adao = new UserDao();
                
		
		User user = userRepo.findById(uur.getId()).orElseThrow(() -> new NotFoundException("user with this Id does not exist"));//.orElseGet(null);
		if(user != null) {
                    user.setUpdated(LocalDateTime.now());
                    user.setFirstName(uur.getFirstName());
                    user.setLastName(uur.getLastName());
                    user.setMiddleName(uur.getMiddleName());
                    user.setMobileNumber(uur.getMobileNumber());
                    user.setDob(uur.getDob());
                    user.setState(uur.getState());
                    user.setLga(uur.getLga());
                    user.setTaguuid(uur.getTaguuid());
                    user.setAddress(uur.getAddress());
                    user.setCountry(uur.getCountry());
                    user.setLocation(uur.getLocation());
                    //user.setUserType(uur.getUserType());
                    
                    userRepo.save(user);
                    ar.setMessage("user updated successfully");
                    ar.setStatus("success");
                    BeanUtils.copyProperties(user, adao);
                    ar.setUser(adao);
                    return ResponseEntity.ok().body(ar);
			
		}else {
			ar.setMessage("user updated failed. agent not found");
			ar.setStatus("failed");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ar);
		}
		
	}
    
    @CrossOrigin
    @RequestMapping(value = "/flag-user", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> flagUser(@RequestBody UserFlagRequest  ufr){
		UserResponse ar = new UserResponse();
		UserDao adao = new UserDao();
		System.out.println(ufr.getId());
		
		if(ufr.getId() == null || ufr.getId().isEmpty()) {
			ar.setStatus("failed");
			ar.setMessage("user ID is required");
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ar);
		}
		
		
		User user = userRepo.findById(ufr.getId()).orElseGet(null);
		
		if(user != null) {
			if(ufr.isActive()) {
				user.setActive(true);
                                User updatedUser = userRepo.save(user);
				BeanUtils.copyProperties(updatedUser, adao);
				ar.setUser(adao);
				ar.setStatus("success");
				ar.setMessage("user activated successfully");
				return ResponseEntity.ok().body(ar);
			}else {
				user.setActive(false);
                                User updatedUser = userRepo.save(user);
				BeanUtils.copyProperties(updatedUser, adao);
				
				ar.setUser(adao);
				ar.setStatus("success");
				ar.setMessage("user deactivated successfully");
				return ResponseEntity.ok().body(ar);
			}
		}else {
			ar.setStatus("failed");
			ar.setMessage("user not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ar);
		}
		
	}
	
        
        
    
    @RequestMapping(value = "auth/login-user", method = RequestMethod.POST)
    public ResponseEntity<UserResponse>  loginUser(@RequestBody UserLoginRequest alr){
    	UserResponse cur = new UserResponse();
        System.out.println(111);
        System.out.println(alr.getEmail());
        System.out.println(alr.getPassword());
        System.out.println(222);
		
    	User user = userRepo.findByEmail(alr.getEmail());
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
            System.out.println(alr.getEmail());
            System.out.println(alr.getPassword());
            
            final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        alr.getEmail(),
                        alr.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        List<String> roles = authentication.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
        
      

                
                User usernew = userRepo.findByEmail(alr.getEmail());
               final String token = jwtTokenUtil.generateToken(user);
                UserDao aDao = new UserDao();
    		BeanUtils.copyProperties(usernew, aDao);
                cur.setUser(aDao);
    		cur.setStatus("success");
                cur.setToken(token);
                cur.setRoles(roles);
		cur.setMessage("user logged in successfully");
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(cur);
                
                } catch (AuthenticationException e) {
                    cur.setStatus("failed");
                
		cur.setMessage("Invalid email/password supplied");
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(cur);
           // throw new BadCredentialsException("Invalid email/password supplied");
        }
    	
    	
    }
    
    @CrossOrigin
    @RequestMapping(value = "/user-by-email", method = RequestMethod.PUT)
    public ResponseEntity<UserResponse> getUserByEmail(@RequestBody User user){
        UserResponse er = new UserResponse();
        UserDao udao= userRepo.getUserByEmail(user.getEmail());
        if(udao != null){
           er.setMessage("user found");
        er.setStatus("success");
        er.setUser(udao);
        return ResponseEntity.ok().body(er); 
        }else{
           er.setMessage("user not found");
        er.setStatus("failed");        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er);
        }
        
    }
    
    @CrossOrigin
    @RequestMapping(value = "/user-by-number", method = RequestMethod.PUT)
    public ResponseEntity<UserResponse> getUserByPhoneNumber(@RequestBody User user){
        UserResponse er = new UserResponse();
        UserDao udao= userRepo.getUserByPhoneNumber(user.getMobileNumber());
        if(udao != null){
           er.setMessage("user found");
        er.setStatus("success");
        er.setUser(udao);
        return ResponseEntity.ok().body(er); 
        }else{
           er.setMessage("user not found");
        er.setStatus("failed");        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er);
        }
        
    }
    
    
    @CrossOrigin
    @RequestMapping(value = "auth/change-user-password", method = RequestMethod.POST)
    public ResponseEntity<UserResponse>  changeUserPassword(@RequestBody ChangeUserPasswordRequest cup){
        UserResponse ur = new UserResponse();
        
        User user = userRepo.findById(cup.getUserId()).orElseGet(null);
        
        if(user != null){
            if(bCryptPasswordEncoder.matches( cup.getOldPassword(), user.getPassword() )){
                String nPwd = bCryptPasswordEncoder.encode(cup.getNewPassword());
                user.setPassword(nPwd);
                userRepo.save(user);
                ur.setStatus("success");
                ur.setMessage("password changed successfully");
                return ResponseEntity.ok().body(ur);
            }else{
               ur.setStatus("failed");
                ur.setMessage("password cannot be matched");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(ur); 
            }
        }else{
            ur.setStatus("failed");
            ur.setMessage("user account not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ur);
        }
    }
    
    @CrossOrigin
    @RequestMapping(value = "auth/reset-user-password-1", method = RequestMethod.POST)
    public ResponseEntity<UserResponse>  resetUserPassword(@RequestBody RestUserPasswordRequest rpr){
        UserResponse ur = new UserResponse();
        User user = userRepo.findByEmail(rpr.getEmail());
        
        if(user != null){
            String enc_aid = Utils.randomNS(15);
            VerifyCode vc = new VerifyCode();
            vc.setCode(enc_aid);
            vc.setUsed(false);
            vc.setUser(user.getId());
            vcRepo.save(vc);
            Email from = new Email("support@tixxbay.com");
                        String subject = "Password reset request";
                        Email to = new Email(user.getEmail());
                        Content content = new Content("text/plain", "https://tixxbayserver.herokuapp.com/tixxbay/api/user/v1/auth/reset-user-password-2/" + enc_aid);
                        Mail mail = new Mail(from, subject, to, content);
                        System.out.println(mail.from.getEmail());
                        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API")); 
                        Request request = new Request();
                        try {
                          request.setMethod(Method.POST);
                          request.setEndpoint("mail/send");
                          request.setBody(mail.build());
                          Response response = sg.api(request);
                          System.out.println(response.getStatusCode());
                          System.out.println(response.getBody());
                          System.out.println(response.getHeaders());
                          
                        } catch (IOException ex) {
                          ur.setStatus("failed");
                            ur.setMessage("error occurred performing operation: " + ex.getMessage());
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ur);
                        }
          
            //System.out.println(jn.isSuccess());
            ur.setStatus("success");
            ur.setMessage("check your email to continue password reset");
            return ResponseEntity.ok().body(ur);
        }else{
            ur.setStatus("failed");
            ur.setMessage("user account not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ur);
        }
    }
    
    
    @CrossOrigin
    @RequestMapping(value = "auth/reset-user-password-2/{enc_aid}", method = RequestMethod.GET)
    public ResponseEntity<UserResponse>  resetUserPassword2(@PathVariable String enc_aid){
        UserResponse ur = new UserResponse();
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
            String newPwd = Utils.randomNS(15);
            user.setPassword(bCryptPasswordEncoder.encode(newPwd));             
            ur.setStatus("success");
            ur.setMessage(newPwd);
            return ResponseEntity.ok().body(ur);
        }else{
            ur.setStatus("failed");
            ur.setMessage("user account not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ur);
        }
    }
    
    
    @RequestMapping(value = "/all-users", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> allUsers(){
            
		UserResponse ar = new UserResponse();
		List<UserDao> users = userRepo.aggregateAllUsers();
		ar.setMessage("users found: " + String.valueOf(users.size()));
		ar.setStatus("success");
		ar.setUsers(users);
		return ResponseEntity.ok().body(ar);
		
    }
    
    @CrossOrigin
    @RequestMapping(value = "/add-bank-details", method = RequestMethod.POST)
    public ResponseEntity<BankDetailResponse> addBankDetails(@RequestBody BankDetail bd){
        BankDetailResponse bdr = new BankDetailResponse();
        BankDetail foundBD =  bankDetailsRepo.findByAccountNumber(bd.getAccountNumber());
        
        if(foundBD == null){
            try{
         
                BankDetail newBankDetail = new BankDetail();
                newBankDetail.setAccountName(bd.getAccountName());
                newBankDetail.setAccountNumber(bd.getAccountNumber());
                newBankDetail.setBankCode(bd.getBankCode());
                newBankDetail.setBankName(bd.getBankName());
                newBankDetail.setBankId(bd.getBankId());
                newBankDetail.setOwnerId(bd.getOwnerId());
                newBankDetail.setOwnerUsername(bd.getOwnerUsername());
                newBankDetail.setFlagged(false);
                newBankDetail.setCreated(LocalDateTime.now());
                newBankDetail.setUpdated(LocalDateTime.now());
                
                BankDetail newBd = bankDetailsRepo.save(newBankDetail);
                BankDetailDao bdao = new BankDetailDao();
                BeanUtils.copyProperties(newBd, bdao);
                bdr.setBankDetail(bdao);
                bdr.setStatus("success");
                bdr.setMessage("bank detail saved successfully");
                return ResponseEntity.ok().body(bdr);
            }catch(Exception e){
                bdr.setStatus("failed");
                bdr.setMessage("error occurred saving bank details: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(bdr);
            }
        }else{
            bdr.setStatus("failed");
            bdr.setMessage("This account details already exist");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(bdr); //ResponseEntity.status(HttpStatus.NOT_FOUND).body(ur)
        }
    }
    
    @CrossOrigin
    @RequestMapping(value = "/edit-bank-details", method = RequestMethod.POST)
    public ResponseEntity<BankDetailResponse> editBankDetails(@RequestBody BankDetail bd){
        BankDetailResponse bdr = new BankDetailResponse();
        BankDetail newBankDetail =  bankDetailsRepo.findByAccountNumber(bd.getId());
        
        
        
        if(newBankDetail != null){
            try{
                if(newBankDetail.isFlagged()){
           bdr.setStatus("failed");
                bdr.setMessage("you are not allowed to perform this operation");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(bdr); 
        }
         
                newBankDetail.setAccountName(bd.getAccountName());
                newBankDetail.setAccountNumber(bd.getAccountNumber());
                newBankDetail.setBankCode(bd.getBankCode());
                newBankDetail.setBankName(bd.getBankName());
                newBankDetail.setBankId(bd.getBankId());
                newBankDetail.setUpdated(LocalDateTime.now());
                
                BankDetail newBd = bankDetailsRepo.save(newBankDetail);
                
                BankDetailDao bdao = new BankDetailDao();
                BeanUtils.copyProperties(newBd, bdao);
                bdr.setBankDetail(bdao);
                
                bdr.setStatus("success");
                bdr.setMessage("bank detail updated successfully");
                return ResponseEntity.ok().body(bdr);
            }catch(Exception e){
                bdr.setStatus("failed");
                bdr.setMessage("error occurred saving bank details: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(bdr);
            }
        }else{
            bdr.setStatus("failed");
            bdr.setMessage("account details not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(bdr); //ResponseEntity.status(HttpStatus.NOT_FOUND).body(ur)
        }
    }
    
    @CrossOrigin
    @RequestMapping(value = "/flag-unflag-bank-details", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BankDetailResponse> flagBankDetails(@RequestBody BankDetail bd){
        BankDetailResponse bdr = new BankDetailResponse();
        BankDetail newBankDetail =  bankDetailsRepo.findByAccountNumber(bd.getId());
        
        
        
        if(newBankDetail != null){
            if(bd.isFlagged()){
                newBankDetail.setFlagged(true);
                newBankDetail.setUpdated(LocalDateTime.now());
                
                BankDetail newBd = bankDetailsRepo.save(newBankDetail);
                
                BankDetailDao bdao = new BankDetailDao();
                BeanUtils.copyProperties(newBd, bdao);
                bdr.setBankDetail(bdao);
            
            
                bdr.setStatus("success");
                bdr.setMessage("bank detail flagged successfully");
                return ResponseEntity.ok().body(bdr);
            }else if(!bd.isFlagged()){
               newBankDetail.setFlagged(false); 
               newBankDetail.setUpdated(LocalDateTime.now());
               
               BankDetail newBd = bankDetailsRepo.save(newBankDetail);
                
                BankDetailDao bdao = new BankDetailDao();
                BeanUtils.copyProperties(newBd, bdao);
                bdr.setBankDetail(bdao);
            
                bdr.setStatus("success");
                bdr.setMessage("bank detail unflasgged successfully");
               return ResponseEntity.ok().body(bdr);
            }else{
                bdr.setStatus("failed");
                bdr.setMessage("invalid parameter supplied");
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(bdr);
            }
            
            
            
        }else{
            bdr.setStatus("failed");
            bdr.setMessage("account details not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(bdr); //ResponseEntity.status(HttpStatus.NOT_FOUND).body(ur)
        }
    }
    
    @RequestMapping(value = "/all-bank-details", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BankDetailResponse> allBankDetails(){
            
		BankDetailResponse br = new BankDetailResponse();
		List<BankDetailDao> bdaos = bankDetailsRepo.getAllBankDetails();
		br.setMessage("bank details found: " + String.valueOf(bdaos.size()));
		br.setStatus("success");
		br.setBankDetails(bdaos);
		return ResponseEntity.ok().body(br);
		
    }
    
    @CrossOrigin
    @RequestMapping(value = "/all-bank-details-by-ownerid", method = RequestMethod.PUT)
    public ResponseEntity<BankDetailResponse> allBankDetailsByOwnerId(@RequestBody BankDetail bd){
            
		BankDetailResponse br = new BankDetailResponse();
		List<BankDetailDao> bdaos = bankDetailsRepo.getAllBankDetailsByOwnerId(bd.getOwnerId());
		br.setMessage("bank details found: " + String.valueOf(bdaos.size()));
		br.setStatus("success");
		br.setBankDetails(bdaos);
		return ResponseEntity.ok().body(br);
		
    }
    
    @CrossOrigin
    @RequestMapping(value = "/all-bank-details-by-username", method = RequestMethod.PUT)
    public ResponseEntity<BankDetailResponse> allBankDetailsByOwnerUsername(@RequestBody BankDetail bd){
            
		BankDetailResponse br = new BankDetailResponse();
		List<BankDetailDao> bdaos = bankDetailsRepo.getAllBankDetailsByOwnerUsername(bd.getOwnerUsername());
		br.setMessage("bank details found: " + String.valueOf(bdaos.size()));
		br.setStatus("success");
		br.setBankDetails(bdaos);
		return ResponseEntity.ok().body(br);
		
    }
    
    @RequestMapping(value = "/all-bank-details-by-number", method = RequestMethod.PUT)
    public ResponseEntity<BankDetailResponse> allBankDetailsByAcctNumber(@RequestBody BankDetail bd){
            
		BankDetailResponse br = new BankDetailResponse();
		List<BankDetailDao> bdaos = bankDetailsRepo.getAllBankDetailsByAccountNumber(bd.getAccountNumber());
		br.setMessage("bank details found: " + String.valueOf(bdaos.size()));
		br.setStatus("success");
		br.setBankDetails(bdaos);
		return ResponseEntity.ok().body(br);
		
    }
    
    
    @RequestMapping(value = "/all-bank-details-by-name", method = RequestMethod.PUT)
    public ResponseEntity<BankDetailResponse> allBankDetailsByAcctName(@RequestBody BankDetail bd){
            
		BankDetailResponse br = new BankDetailResponse();
		List<BankDetailDao> bdaos = bankDetailsRepo.getAllBankDetailsByAccountName(bd.getAccountName());
		br.setMessage("bank details found: " + String.valueOf(bdaos.size()));
		br.setStatus("success");
		br.setBankDetails(bdaos);
		return ResponseEntity.ok().body(br);
		
    }
    
    @RequestMapping(value = "/create-role", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity createRole(@RequestBody RoleRequest rr){
        if(rr.getName() != null){
            Role role = roleRepo.findByName(rr.getName());
            
            if(role == null){
                Role newRole = new Role();
                newRole.setName(rr.getName());
                roleRepo.save(newRole);
                return ResponseEntity.ok(role);
            }else{
                return ResponseEntity.badRequest().body(role);
            }
        }else{
            return ResponseEntity.badRequest().body("no data");
        }
    }
    
    
    @RequestMapping(value = "/resolve-bank", method = RequestMethod.POST)
    public ResponseEntity<ResolveBankResponse> resolveBank(@RequestBody ResolveBankRequest rr){
        ResolveBankResponse rbb = new ResolveBankResponse();
        ResolveBankResponse rbbp = new ResolveBankResponse();
        int statusCode = 0;
        StringBuilder result = new StringBuilder();
        
        try {
	            // convert transaction to json then use it as a body to post json
	            Gson gson = new Gson();
	            // add paystack chrges to the amount
	            //StringEntity postingString = new StringEntity(gson.toJson(request));
	            HttpClient client = HttpClientBuilder.create().build();
                    //https://api.paystack.co/bank/resolve?account_number=0044815978&bank_code=044
	            HttpGet getReq = new HttpGet("https://api.paystack.co/bank/resolve?account_number="+ rr.getAccount_number() + "&bank_code=" + rr.getBank_code() );
	            //HttpPost post = new HttpPost("https://api.paystack.co/transaction/initialize");
	            //post.setEntity(postingString);
	            getReq.addHeader("Content-type", "text/plain");
	            getReq.addHeader("Authorization", "Bearer sk_test_36c529556d2463dac5aa8258522be46456013ee2");
	           
	            org.apache.http.HttpResponse response = client.execute(getReq);
                    statusCode = response.getStatusLine().getStatusCode();
	            if (response.getStatusLine().getStatusCode() == 200) {
	                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

	                String line;
	                while ((line = rd.readLine()) != null) {
	                    result.append(line);
	                }

	            } else if (response.getStatusLine().getStatusCode() == 500) {
	            	 rbbp.setMessage("error");
                         rbbp.setStatus(false);
	            }
	            else {
                        rbbp.setStatus(false);
                        rbbp.setMessage("error resolving bank details");
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(rbbp);
	                
	            }
	            ObjectMapper mapper = new ObjectMapper();

	            rbb = mapper.readValue(result.toString(), ResolveBankResponse.class);
                    rbbp = mapper.readValue(result.toString(), ResolveBankResponse.class);
                    
                    
                    return ResponseEntity.ok().body(rbbp);
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            rbbp.setStatus(false);
                        rbbp.setMessage("error resolving bank details");
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(rbbp);
	        }
    }
    
   
}


































































































































