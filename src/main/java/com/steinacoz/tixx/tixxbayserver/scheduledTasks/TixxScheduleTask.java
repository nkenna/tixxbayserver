/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.scheduledTasks;


import com.steinacoz.tixx.tixxbayserver.config.jwtservice.JwtTokenUtil;
import com.steinacoz.tixx.tixxbayserver.model.UserPoint;
import com.steinacoz.tixx.tixxbayserver.repo.BankDetailRepo;
import com.steinacoz.tixx.tixxbayserver.repo.RoleRepo;
import com.steinacoz.tixx.tixxbayserver.repo.UserPointRepo;
import com.steinacoz.tixx.tixxbayserver.repo.UserRepo;
import com.steinacoz.tixx.tixxbayserver.repo.VerifyCodeRepo;
import com.steinacoz.tixx.tixxbayserver.repo.WalletRepo;
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
    
    @Scheduled(fixedRate = 300000000)
    public void checkUserPoints(){
        
        //get all user points
        List<UserPoint> ups = upRepo.findByPointsGreaterThanQuery(0.1);
        System.out.println("length of resolved data" +  String.valueOf(ups.size()));
        tasklog.info("length of resolved data: {}", String.valueOf(ups.size()));
    }
}











