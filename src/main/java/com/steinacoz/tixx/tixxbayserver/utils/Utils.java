/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.utils;

import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Component;

/**
 *
 * @author nkenn
 */
@Component
public class Utils {
    final public static String newcreditTag = "TRANS_NEW_CREDIT_TAG";
	final public static String creditTag = "TRANS_CREDIT_TAG";
	final public static String debitTag = "TRANS_DEBIT_TAG";
	
	final public static String creditWallet = "TRANS_CREDIT_WALLET";
	final public static String debitWallet = "TRANS_DEBIT_WALLET";
	
	final public static String creditAgent = "TRANS_CREDIT_AGENT";
	final public static String creditVendor = "TRANS_CREDIT_Vendor";
	final public static String creditEventManager = "TRANS_CREDIT_EVENT_MANAGER";
        final public static String buyTicket = "TRANS_BUY_TICKET";
	
	final public static String typeAdmin = "ADMIN";
	final public static String typeAgent = "AGENT";
	final public static String typeVendor = "VENDOR";
	final public static String typeEventManager = "EVENT_MANAGER";
        final public static String typeUser = "USER";
        
    
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
   
	
	public static String secretKey = "steintixxbay0987654321";
    public static String salt = "steintixxbay0987654321";
    public static String API_KEY = "key-ac97aa5eaca9fd3c8b4c6fb4d26575b7";//"e2f6d3e4012a9b58b036150f11498b7e-87c34c41-96079a43";
    
    private final static String NUMERIC_STRING = "0123456789abcdefghijklmnoprstuvwxyz";
	private final String NUMERIC = "0123456789";
	private final String STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
    public static String randomNS(int num) {
        StringBuilder builder = new StringBuilder();
        while (num-- != 0) {

            int character = (int) (Math.random() * NUMERIC_STRING.length());

            builder.append(NUMERIC_STRING.charAt(character));

        }
        return builder.toString();
    }
    
    
    public static String decrypt(String strToDecrypt, String secret) {
        try
        {
            byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            IvParameterSpec ivspec = new IvParameterSpec(iv);
             
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
             
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } 
        catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
    
    public static String encrypt(String strToEncrypt, String secret) 
    {
        try
        {
            byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            IvParameterSpec ivspec = new IvParameterSpec(iv);
             
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
             
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } 
        catch (Exception e) 
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }
}






