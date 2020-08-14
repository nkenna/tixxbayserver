/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.dao.BankDetailDao;
import java.util.List;

/**
 *
 * @author nkenn
 */
public interface BankDetailRepoCustom {
    List<BankDetailDao> getAllBankDetails();
    List<BankDetailDao> getAllBankDetailsByOwnerId(String ownerid);
    List<BankDetailDao> getAllBankDetailsByOwnerUsername(String username);
    List<BankDetailDao> getAllBankDetailsByAccountNumber(String number);
    List<BankDetailDao> getAllBankDetailsByAccountName(String name);
}


