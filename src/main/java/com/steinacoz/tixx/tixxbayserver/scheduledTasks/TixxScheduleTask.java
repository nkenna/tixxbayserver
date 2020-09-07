/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.scheduledTasks;


import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.steinacoz.tixx.tixxbayserver.config.jwtservice.JwtTokenUtil;
import com.steinacoz.tixx.tixxbayserver.model.User;
import com.steinacoz.tixx.tixxbayserver.model.UserPoint;
import com.steinacoz.tixx.tixxbayserver.model.Wallet;
import com.steinacoz.tixx.tixxbayserver.model.WalletTransaction;
import com.steinacoz.tixx.tixxbayserver.repo.BankDetailRepo;
import com.steinacoz.tixx.tixxbayserver.repo.RoleRepo;
import com.steinacoz.tixx.tixxbayserver.repo.UserPointRepo;
import com.steinacoz.tixx.tixxbayserver.repo.UserRepo;
import com.steinacoz.tixx.tixxbayserver.repo.VerifyCodeRepo;
import com.steinacoz.tixx.tixxbayserver.repo.WalletRepo;
import com.steinacoz.tixx.tixxbayserver.repo.WalletTransactionRepo;
import com.steinacoz.tixx.tixxbayserver.utils.Utils;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author nkenn
 */
@Component
public class TixxScheduleTask {
    private static final Logger tasklog = LoggerFactory.getLogger(TixxScheduleTask.class);
    
    @Autowired
    UserRepo userRepo;    
        
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
    WalletTransactionRepo walletTransRepo;
    
    @Scheduled(fixedRate = 21600000)
    public void checkUserPoints(){
        
        //get all user points
        List<UserPoint> ups = upRepo.findByPointsGreaterThanQuery(20.0);
        System.out.println("length of resolved data" +  String.valueOf(ups.size()));
        tasklog.info("length of resolved data: {}", String.valueOf(ups.size()));
        
        for(UserPoint up: ups){
            //get user wallet
            User user = userRepo.findByUsername(up.getUsername());
            if(user != null){
                Wallet wallet = walletRepo.findByWalletid(user.getWalletId());
                if(wallet != null){
                    // update wallet balance
                    BigDecimal add1000 = wallet.getBalance().add(new BigDecimal(1000.0));
                    wallet.setBalance(add1000);
                    wallet.setUpdateddate(LocalDateTime.now());
                    
                    // deduct points
                    up.setPoints(up.getPoints() - 20.0);
                    
                    //save all updated date
                    upRepo.save(up);
                    walletRepo.save(wallet);
                    
                    //build wallet transaction record
                    WalletTransaction wt = new WalletTransaction();
                    wt.setTransRef("TIXX" + Utils.randomNS(12));
                    wt.setTransDate(LocalDateTime.now());
                    wt.setTotalAmount(new BigDecimal(1000.0));
                    wt.setTransType(Utils.creditWallet);
                    //wt.setBoughtBy(user);
                    wt.setWalletId(wallet.getWalletid());
                    wt.setWalletOwnerUsername(wallet.getOwnerUsername());
                    //wt.setEventCode(event.getEventCode());
                    wt.setNarration("wallet was credited for tixx coins bonus");
                    //wt.setLocation(str.getLocation());
                    //wt.setTicketDiscount(charge);
                    //wt.setTicketCodes(tCodes);
                    walletTransRepo.save(wt);
                    
                    //send out email to notify user
                    Email from = new Email("support@tixxbay.com");
                    String subject = "Tixx wallet update";
                    Email to = new Email(user.getEmail());
                    Content content = new Content("text/plain", "Your tixx coin is up to 20 coins. NGN 1000 have been credited to your wallet.\n Continue using tixxbay products to keep getting free credits like this.");
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
                         tasklog.error("error occured sending email: {}", ex.getMessage()); 
                    }
                }
            }
        }
    }
}






















