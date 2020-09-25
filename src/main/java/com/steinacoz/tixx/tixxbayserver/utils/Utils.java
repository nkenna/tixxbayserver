/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.steinacoz.tixx.tixxbayserver.scheduledTasks.TixxScheduleTask;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.Arrays;
//import java.util.Base64;

import java.util.Iterator;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author nkenn
 */
@Component
public class Utils {
    private static final Logger utillog = LoggerFactory.getLogger(Utils.class);
    final public static String newcreditTag = "TRANS_NEW_CREDIT_TAG";
	final public static String creditTag = "TRANS_CREDIT_TAG";
	final public static String debitTag = "TRANS_DEBIT_TAG";
	
	final public static String creditWallet = "TRANS_CREDIT_WALLET";
	final public static String debitWallet = "TRANS_DEBIT_WALLET";
	
	final public static String creditAgent = "TRANS_CREDIT_AGENT";
	final public static String creditVendor = "TRANS_CREDIT_VENDOR";
	final public static String creditEventManager = "TRANS_CREDIT_EVENT_MANAGER";
        final public static String buyTicket = "TRANS_BUY_TICKET";
        final public static String vendorSell = "TRANS_VENDOR_SELL";
	
	final public static String typeAdmin = "ADMIN";
	final public static String typeAgent = "AGENT";
	final public static String typeVendor = "VENDOR";
	final public static String typeEventManager = "EVENT_MANAGER";
        final public static String typeUser = "USER";
        
        final public static String payPaid = "PAID";
        final public static String payPending = "PENDING";
        final public static String payDeclined = "DECLINED";
        final public static String payTypeVendor = "VENDOR PAY";
        final public static String payTypeTicket = "TICKET PAY";
        
    
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
   
	
	public static String secretKey = "steintixxbay0987654321";
    public static String salt = "steintixxbay0987654321";

    private final static String NUMERIC_STRING = "0123456789abcdefghijklmnoprstuvwxyz";
	private final String NUMERIC = "0123456789";
	private final String STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        
       // private static SecretKeySpec secretKey;
   // private static byte[] key;
    
    //private static SecretKeySpec secretKey;
    private static byte[] key;
 
    public static void setKey(String myKey) 
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); 
            secretKey =  Base64.encodeBase64String(new SecretKeySpec(key, "AES").getEncoded());
        } 
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } 
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    
    public static String encrypt(String strToEncrypt, String secret) 
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            byte[] decodedKey = Base64.decodeBase64(secretKey);
            SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES"); 
            cipher.init(Cipher.ENCRYPT_MODE, originalKey);
            return Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } 
        catch (Exception e) 
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }
    
    public static String decrypt(String strToDecrypt, String secret) 
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            byte[] decodedKey = Base64.decodeBase64(secretKey);
            SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES"); 
            cipher.init(Cipher.DECRYPT_MODE, originalKey);
            //cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)));
        } 
        catch (Exception e) 
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
    
    public static String randomNS(int num) {
        StringBuilder builder = new StringBuilder();
        while (num-- != 0) {

            int character = (int) (Math.random() * NUMERIC_STRING.length());

            builder.append(NUMERIC_STRING.charAt(character));

        }
        return builder.toString();
    }
    
    public static BufferedImage generateQRCodeImage(String barcodeText) throws Exception {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = 
        barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 300, 300); 
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
    
    public static BufferedImage drawTextOnImage(String text, BufferedImage image, int space) {
        if(image != null){
            if(text == null || text.isEmpty()){
                text = "Tixxbay Ticket";
            }else if(text.length() > 15){
                text = text.substring(0, 14);
            }
            BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight() + space, BufferedImage.OPAQUE);
            Graphics2D g2d = (Graphics2D) bi.createGraphics();
            g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
            g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
            g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON));

            g2d.drawImage(image, 0, 0, null);

            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Calibri", Font.PLAIN, 14));
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(text);

            //center text at bottom of image in the new space
            g2d.drawString(text, (bi.getWidth() / 2) - textWidth / 2, bi.getHeight() - 10);

            g2d.dispose();
            return bi;
        }else {
            return image;
        }
    
}
    
    
 
   
    
    public static void addImageWatermark(File watermark, String type, File source, File destination) throws IOException {
        BufferedImage image = ImageIO.read(source);
        BufferedImage overlay = resize(ImageIO.read(watermark), 150, 150);

        // determine image type and handle correct transparency
        int imageType = "png".equalsIgnoreCase(type) ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        BufferedImage watermarked = new BufferedImage(image.getWidth(), image.getHeight(), imageType);

        // initializes necessary graphic properties
        Graphics2D w = (Graphics2D) watermarked.getGraphics();
        w.drawImage(image, 0, 0, null);
        AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);
        w.setComposite(alphaChannel);

        // calculates the coordinate where the String is painted
        int centerX = image.getWidth() / 2;
        int centerY = image.getHeight() / 2;

        // add text watermark to the image
        w.drawImage(overlay, centerX, centerY, null);
        ImageIO.write(watermarked, type, destination);
        w.dispose();
    }

    private static BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }
    
    
    public static File compressImage(File input, File output) throws IOException {

        //File input = new File("/tmp/duke.jpg");
        BufferedImage image = ImageIO.read(input);

        //File output = new File("/tmp/duke-compressed-005.jpg");
        OutputStream out = new FileOutputStream(output);

        ImageWriter writer =  ImageIO.getImageWritersByFormatName("png").next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(out);
        writer.setOutput(ios);

        ImageWriteParam param = writer.getDefaultWriteParam();
        if (param.canWriteCompressed()){
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.05f);
        }

        writer.write(null, new IIOImage(image, null, null), param);

        out.close();
        ios.close();
        writer.dispose();
        
        return output;

    }
    
    public static void sendOutEmailText(String _subject, 
                                    String _to, 
                                    String _type,
                                    String _content){
        //send out email to notify user
                    Email from = new Email("support@tixxbay.com");
                    String subject = _subject;
                    Email to = new Email(_to);
                    Content content = new Content(_type, _content);
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
                         utillog.error("error occured sending email: {}", ex.getMessage()); 
                    }
    }
    
    
    public static void sendOutEmailHtml(String _subject, 
                                    String _to, 
                                    String _content){
        //send out email to notify user
                    Email from = new Email("support@tixxbay.com");
                    String subject = _subject;
                    Email to = new Email(_to);
                    Content content = new Content("text/html", Utils.sendHtmlEmailNewEvent(_content));
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
                         utillog.error("error occured sending email: {}", ex.getMessage()); 
                    }
    }
    
    
    public static String sendHtmlEmailNewEvent(String content){
        StringBuilder sb = new StringBuilder();
        sb.append("<table bgcolor=\"#fd0a5d\" cellpadding=\"0\" cellspacing=\"0\" class=\"nl-container\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; min-width: 320px; Margin: 0 auto; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #fd0a5d; width: 100%;\" valign=\"top\" width=\"100%\">\n" +
"<tbody>\n" +
"<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
"<td style=\"word-break: break-word; vertical-align: top;\" valign=\"top\">\n" +
"\n" +
"<div style=\"background-color:#002767;\">\n" +
"<div class=\"block-grid\" style=\"Margin: 0 auto; min-width: 320px; max-width: 650px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
"<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
"\n" +
"<div class=\"col num12\" style=\"min-width: 320px; max-width: 650px; display: table-cell; vertical-align: top; width: 650px;\">\n" +
"<div style=\"width:100% !important;\">\n" +
"\n" +
"<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
"\n" +
"<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\" width=\"100%\">\n" +
"<tbody>\n" +
"<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
"<td class=\"divider_inner\" style=\"word-break: break-word; vertical-align: top; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; padding-top: 10px; padding-right: 10px; padding-bottom: 10px; padding-left: 10px;\" valign=\"top\">\n" +
"<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider_content\" height=\"0\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-top: 0px solid transparent; height: 0px; width: 100%;\" valign=\"top\" width=\"100%\">\n" +
"<tbody>\n" +
"<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
"<td height=\"0\" style=\"word-break: break-word; vertical-align: top; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\"><span></span></td>\n" +
"</tr>\n" +
"</tbody>\n" +
"</table>\n" +
"</td>\n" +
"</tr>\n" +
"</tbody>\n" +
"</table>\n" +
"\n" +
"</div>\n" +
"\n" +
"</div>\n" +
"</div>\n" +
"\n" +
"</div>\n" +
"</div>\n" +
"</div>\n" +
"<div style=\"background-color:#002767;\">\n" +
"<div class=\"block-grid mixed-two-up\" style=\"Margin: 0 auto; min-width: 320px; max-width: 650px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
"<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
"\n" +
"<div class=\"col num3\" style=\"display: table-cell; vertical-align: top; max-width: 320px; min-width: 162px; width: 162px;\">\n" +
"<div style=\"width:100% !important;\">\n" +
"\n" +
"<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
"\n" +
"<div align=\"center\" class=\"img-container center autowidth\" style=\"padding-right: 0px;padding-left: 0px;\">\n" +
"<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr style=\"line-height:0px\"><td style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\"><![endif]--><a href=\"http://www.example.com/\" style=\"outline:none\" tabindex=\"-1\" target=\"_blank\"> <img align=\"center\" alt=\"Logo\" border=\"0\" class=\"center autowidth\" src=\"https://firebasestorage.googleapis.com/v0/b/tixxbay-app.appspot.com/o/EMAIL_Logo4x.png?alt=media&token=bc1cdffa-8e0f-425b-a8ee-54e6ef451ad1\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; height: auto; border: 0; width: 100%; max-width: 162px; display: block;\" title=\"Logo\" width=\"162\"/></a>\n" +
"<!--[if mso]></td></tr></table><![endif]-->\n" +
"</div>\n" +
"<!--[if (!mso)&(!IE)]><!-->\n" +
"</div>\n" +
"<!--<![endif]-->\n" +
"</div>\n" +
"</div>\n" +
"<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
"<!--[if (mso)|(IE)]></td><td align=\"center\" width=\"487\" style=\"background-color:transparent;width:487px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 0px; padding-left: 0px; padding-top:15px; padding-bottom:10px;\"><![endif]-->\n" +
"<div class=\"col num9\" style=\"display: table-cell; vertical-align: top; min-width: 320px; max-width: 486px; width: 487px;\">\n" +
"<div style=\"width:100% !important;\">\n" +
"<!--[if (!mso)&(!IE)]><!-->\n" +
"<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:15px; padding-bottom:10px; padding-right: 0px; padding-left: 0px;\">\n" +
"<!--<![endif]-->\n" +
"<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 10px; padding-left: 10px; padding-top: 10px; padding-bottom: 10px; font-family: Arial, sans-serif\"><![endif]-->\n" +
"<div style=\"color:#ffffff;font-family:'Helvetica Neue', Helvetica, Arial, sans-serif;line-height:1.2;padding-top:10px;padding-right:10px;padding-bottom:10px;padding-left:10px;\">\n" +
"<div style=\"line-height: 1.2; font-size: 12px; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; color: #ffffff; mso-line-height-alt: 14px;\">\n" +
"<p style=\"font-size: 16px; line-height: 1.2; word-break: break-word; text-align: center; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; mso-line-height-alt: 19px; margin: 0;\"><span style=\"font-size: 12px;\"><a href=\"http://www.example.com/\" rel=\"noopener\" style=\"text-decoration: none; color: #ffffff;\" target=\"_blank\">BUY TICKETS</a>    <a href=\"http://www.example.com/\" rel=\"noopener\" style=\"text-decoration: none; color: #ffffff;\" target=\"_blank\">SUPPORT</a></span></p>\n" +
"</div>\n" +
"</div>\n" +
"<!--[if mso]></td></tr></table><![endif]-->\n" +
"<!--[if (!mso)&(!IE)]><!-->\n" +
"</div>\n" +
"<!--<![endif]-->\n" +
"</div>\n" +
"</div>\n" +
"<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
"<!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n" +
"</div>\n" +
"</div>\n" +
"</div>\n" +
"<div style=\"background-color:#002767;\">\n" +
"<div class=\"block-grid\" style=\"Margin: 0 auto; min-width: 320px; max-width: 650px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
"<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
"<!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"background-color:#002767;\"><tr><td align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:650px\"><tr class=\"layout-full-width\" style=\"background-color:transparent\"><![endif]-->\n" +
"<!--[if (mso)|(IE)]><td align=\"center\" width=\"650\" style=\"background-color:transparent;width:650px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 0px; padding-left: 0px; padding-top:5px; padding-bottom:5px;\"><![endif]-->\n" +
"<div class=\"col num12\" style=\"min-width: 320px; max-width: 650px; display: table-cell; vertical-align: top; width: 650px;\">\n" +
"<div style=\"width:100% !important;\">\n" +
"<!--[if (!mso)&(!IE)]><!-->\n" +
"<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
"<!--<![endif]-->\n" +
"<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\" width=\"100%\">\n" +
"<tbody>\n" +
"<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
"<td class=\"divider_inner\" style=\"word-break: break-word; vertical-align: top; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; padding-top: 10px; padding-right: 10px; padding-bottom: 10px; padding-left: 10px;\" valign=\"top\">\n" +
"<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider_content\" height=\"0\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-top: 0px solid transparent; height: 0px; width: 100%;\" valign=\"top\" width=\"100%\">\n" +
"<tbody>\n" +
"<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
"<td height=\"0\" style=\"word-break: break-word; vertical-align: top; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\"><span></span></td>\n" +
"</tr>\n" +
"</tbody>\n" +
"</table>\n" +
"</td>\n" +
"</tr>\n" +
"</tbody>\n" +
"</table>\n" +
"<!--[if (!mso)&(!IE)]><!-->\n" +
"</div>\n" +
"<!--<![endif]-->\n" +
"</div>\n" +
"</div>\n" +
"<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
"<!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n" +
"</div>\n" +
"</div>\n" +
"</div>\n" +
"<div style=\"background-color:transparent;\">\n" +
"<div class=\"block-grid\" style=\"Margin: 0 auto; min-width: 320px; max-width: 650px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
"<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
"<!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"background-color:transparent;\"><tr><td align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:650px\"><tr class=\"layout-full-width\" style=\"background-color:transparent\"><![endif]-->\n" +
"<!--[if (mso)|(IE)]><td align=\"center\" width=\"650\" style=\"background-color:transparent;width:650px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 0px; padding-left: 0px; padding-top:35px; padding-bottom:0px;\"><![endif]-->\n" +
"<div class=\"col num12\" style=\"min-width: 320px; max-width: 650px; display: table-cell; vertical-align: top; width: 650px;\">\n" +
"<div style=\"width:100% !important;\">\n" +
"<!--[if (!mso)&(!IE)]><!-->\n" +
"<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:35px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
"<!--<![endif]-->\n" +
"<div align=\"center\" class=\"img-container center autowidth\" style=\"padding-right: 0px;padding-left: 0px;\">\n" +
"<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr style=\"line-height:0px\"><td style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\"><![endif]--><img align=\"center\" border=\"0\" class=\"center autowidth\" src=\"https://firebasestorage.googleapis.com/v0/b/tixxbay-app.appspot.com/o/hello_tixxs4x-100.jpg?alt=media&token=0918ffa3-028d-443c-bdce-7768ce2095b3\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; height: auto; border: 0; width: 100%; max-width: 650px; display: block;\" width=\"650\"/>\n" +
"<!--[if mso]></td></tr></table><![endif]-->\n" +
"</div>\n" +
"<!--[if (!mso)&(!IE)]><!-->\n" +
"</div>\n" +
"<!--<![endif]-->\n" +
"</div>\n" +
"</div>\n" +
"<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
"<!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n" +
"</div>\n" +
"</div>\n" +
"</div>\n" +
"<div style=\"background-color:transparent;\">\n" +
"<div class=\"block-grid\" style=\"Margin: 0 auto; min-width: 320px; max-width: 650px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
"<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
"<!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"background-color:transparent;\"><tr><td align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:650px\"><tr class=\"layout-full-width\" style=\"background-color:transparent\"><![endif]-->\n" +
"<!--[if (mso)|(IE)]><td align=\"center\" width=\"650\" style=\"background-color:transparent;width:650px; border-top: 0px solid transparent; border-left: 4px solid #57366E; border-bottom: 0px solid transparent; border-right: 4px solid #57366E;\" valign=\"top\"><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 0px; padding-left: 0px; padding-top:55px; padding-bottom:60px;\"><![endif]-->\n" +
"<div class=\"col num12\" style=\"min-width: 320px; max-width: 650px; display: table-cell; vertical-align: top; width: 642px;\">\n" +
"<div style=\"width:100% !important;\">\n" +
"<!--[if (!mso)&(!IE)]><!-->\n" +
"<div style=\"border-top:0px solid transparent; border-left:4px solid #57366E; border-bottom:0px solid transparent; border-right:4px solid #57366E; padding-top:55px; padding-bottom:60px; padding-right: 0px; padding-left: 0px; color: #FFFFFF\">\n" +
"\n" +
"<!--<![endif]-->\n" +
"<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 10px; padding-left: 10px; padding-top: 60px; padding-bottom: 60px; font-family: Arial, sans-serif\"><![endif]-->\n" +
"<!--[if mso]></td></tr></table><![endif]-->\n" +
"<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 50px; padding-left: 50px; padding-top: 10px; padding-bottom: 10px; font-family: Arial, sans-serif\"><![endif]-->\n" +
"<!--[if mso]></td></tr></table><![endif]-->\n" +
"<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 30px; padding-left: 30px; padding-top: 60px; padding-bottom: 60px; font-family: Arial, sans-serif\"><![endif]-->\n" +
"<!--[if mso]></td></tr></table><![endif]-->\n" +
"<!--[if (!mso)&(!IE)]><!-->\n" +
"<p>" + content  + "</p>\n" +
"</div>\n" +
"<!--<![endif]-->\n" +
"</div>\n" +
"</div>\n" +
"<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
"<!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n" +
"</div>\n" +
"</div>\n" +
"</div>\n" +
"<div style=\"background-color:transparent;\">\n" +
"<div class=\"block-grid\" style=\"Margin: 0 auto; min-width: 320px; max-width: 650px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: #002767;\">\n" +
"<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:#002767;\">\n" +
"<!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"background-color:transparent;\"><tr><td align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:650px\"><tr class=\"layout-full-width\" style=\"background-color:#002767\"><![endif]-->\n" +
"<!--[if (mso)|(IE)]><td align=\"center\" width=\"650\" style=\"background-color:#002767;width:650px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 30px; padding-left: 30px; padding-top:55px; padding-bottom:55px;\"><![endif]-->\n" +
"<div class=\"col num12\" style=\"min-width: 320px; max-width: 650px; display: table-cell; vertical-align: top; width: 650px;\">\n" +
"<div style=\"width:100% !important;\">\n" +
"<!--[if (!mso)&(!IE)]><!-->\n" +
"<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:55px; padding-bottom:55px; padding-right: 30px; padding-left: 30px;\">\n" +
"<!--<![endif]-->\n" +
"<div align=\"center\" class=\"button-container\" style=\"padding-top:10px;padding-right:10px;padding-bottom:10px;padding-left:10px;\">\n" +
"<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-spacing: 0; border-collapse: collapse; mso-table-lspace:0pt; mso-table-rspace:0pt;\"><tr><td style=\"padding-top: 10px; padding-right: 10px; padding-bottom: 10px; padding-left: 10px\" align=\"center\"><v:roundrect xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" href=\"http://www.example.com/\" style=\"height:45.75pt; width:267pt; v-text-anchor:middle;\" arcsize=\"50%\" strokeweight=\"1.5pt\" strokecolor=\"#4C76BB\" fillcolor=\"#011b47\"><w:anchorlock/><v:textbox inset=\"0,0,0,0\"><center style=\"color:#ffffff; font-family:Arial, sans-serif; font-size:18px\"><![endif]--><a href=\"http://www.example.com/\" style=\"-webkit-text-size-adjust: none; text-decoration: none; display: inline-block; color: #ffffff; background-color: #011b47; border-radius: 30px; -webkit-border-radius: 30px; -moz-border-radius: 30px; width: auto; width: auto; border-top: 2px solid #4C76BB; border-right: 2px solid #4C76BB; border-bottom: 2px solid #4C76BB; border-left: 2px solid #4C76BB; padding-top: 18px; padding-bottom: 18px; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; text-align: center; mso-border-alt: none; word-break: keep-all;\" target=\"_blank\"><span style=\"padding-left:60px;padding-right:60px;font-size:18px;display:inline-block;\"><span style=\"font-size: 16px; line-height: 1.2; word-break: break-word; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; mso-line-height-alt: 19px;\"><span data-mce-style=\"font-size: 18px; line-height: 21px;\" style=\"font-size: 18px; line-height: 21px;\">Privacy Policy</span></span></span></a>\n" +
"<!--[if mso]></center></v:textbox></v:roundrect></td></tr></table><![endif]-->\n" +
"</div>\n" +
"<div align=\"center\" class=\"button-container\" style=\"padding-top:10px;padding-right:10px;padding-bottom:10px;padding-left:10px;\">\n" +
"<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-spacing: 0; border-collapse: collapse; mso-table-lspace:0pt; mso-table-rspace:0pt;\"><tr><td style=\"padding-top: 10px; padding-right: 10px; padding-bottom: 10px; padding-left: 10px\" align=\"center\"><v:roundrect xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" href=\"http://www.example.com/\" style=\"height:45.75pt; width:225pt; v-text-anchor:middle;\" arcsize=\"50%\" strokeweight=\"1.5pt\" strokecolor=\"#4C76BB\" fillcolor=\"#011b47\"><w:anchorlock/><v:textbox inset=\"0,0,0,0\"><center style=\"color:#ffffff; font-family:Arial, sans-serif; font-size:18px\"><![endif]--><a href=\"http://www.example.com/\" style=\"-webkit-text-size-adjust: none; text-decoration: none; display: inline-block; color: #ffffff; background-color: #011b47; border-radius: 30px; -webkit-border-radius: 30px; -moz-border-radius: 30px; width: auto; width: auto; border-top: 2px solid #4C76BB; border-right: 2px solid #4C76BB; border-bottom: 2px solid #4C76BB; border-left: 2px solid #4C76BB; padding-top: 18px; padding-bottom: 18px; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; text-align: center; mso-border-alt: none; word-break: keep-all;\" target=\"_blank\"><span style=\"padding-left:35px;padding-right:35px;font-size:18px;display:inline-block;\"><span style=\"font-size: 16px; line-height: 1.2; word-break: break-word; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; mso-line-height-alt: 19px;\"><span data-mce-style=\"font-size: 18px; line-height: 21px;\" style=\"font-size: 18px; line-height: 21px;\">Terms &amp; Conditions</span></span></span></a>\n" +
"<!--[if mso]></center></v:textbox></v:roundrect></td></tr></table><![endif]-->\n" +
"</div>\n" +
"<div align=\"center\" class=\"button-container\" style=\"padding-top:10px;padding-right:10px;padding-bottom:10px;padding-left:10px;\">\n" +
"<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-spacing: 0; border-collapse: collapse; mso-table-lspace:0pt; mso-table-rspace:0pt;\"><tr><td style=\"padding-top: 10px; padding-right: 10px; padding-bottom: 10px; padding-left: 10px\" align=\"center\"><v:roundrect xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" href=\"http://www.example.com/\" style=\"height:45.75pt; width:265.5pt; v-text-anchor:middle;\" arcsize=\"50%\" strokeweight=\"1.5pt\" strokecolor=\"#4C76BB\" fillcolor=\"#011b47\"><w:anchorlock/><v:textbox inset=\"0,0,0,0\"><center style=\"color:#ffffff; font-family:Arial, sans-serif; font-size:18px\"><![endif]--><a href=\"http://www.example.com/\" style=\"-webkit-text-size-adjust: none; text-decoration: none; display: inline-block; color: #ffffff; background-color: #011b47; border-radius: 30px; -webkit-border-radius: 30px; -moz-border-radius: 30px; width: auto; width: auto; border-top: 2px solid #4C76BB; border-right: 2px solid #4C76BB; border-bottom: 2px solid #4C76BB; border-left: 2px solid #4C76BB; padding-top: 18px; padding-bottom: 18px; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; text-align: center; mso-border-alt: none; word-break: keep-all;\" target=\"_blank\"><span style=\"padding-left:60px;padding-right:60px;font-size:18px;display:inline-block;\"><span style=\"font-size: 16px; line-height: 1.2; word-break: break-word; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; mso-line-height-alt: 19px;\"><span data-mce-style=\"font-size: 18px; line-height: 21px;\" style=\"font-size: 18px; line-height: 21px;\">Cookie Policy</span></span></span></a>\n" +
"<!--[if mso]></center></v:textbox></v:roundrect></td></tr></table><![endif]-->\n" +
"</div>\n" +
"<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 25px; padding-left: 25px; padding-top: 20px; padding-bottom: 10px; font-family: Arial, sans-serif\"><![endif]-->\n" +
"<div style=\"color:#ffffff;font-family:'Helvetica Neue', Helvetica, Arial, sans-serif;line-height:1.5;padding-top:20px;padding-right:25px;padding-bottom:10px;padding-left:25px;\">\n" +
"<div style=\"line-height: 1.5; font-size: 12px; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; color: #ffffff; mso-line-height-alt: 18px;\">\n" +
"<p style=\"font-size: 16px; line-height: 1.5; word-break: break-word; text-align: center; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; mso-line-height-alt: 24px; margin: 0;\"><span style=\"font-size: 16px;\">If you have any questions do not hesitate to <a href=\"support@tixxbay.com\" style=\"text-decoration: underline; color: #ffffff;\" title=\"reach\">reach out to us</a></span></p>\n" +
"</div>\n" +
"</div>\n" +
"<!--[if mso]></td></tr></table><![endif]-->\n" +
"<!--[if (!mso)&(!IE)]><!-->\n" +
"</div>\n" +
"<!--<![endif]-->\n" +
"</div>\n" +
"</div>\n" +
"<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
"<!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n" +
"</div>\n" +
"</div>\n" +
"</div>\n" +
"<div style=\"background-color:transparent;\">\n" +
"<div class=\"block-grid\" style=\"Margin: 0 auto; min-width: 320px; max-width: 650px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
"<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
"<!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"background-color:transparent;\"><tr><td align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:650px\"><tr class=\"layout-full-width\" style=\"background-color:transparent\"><![endif]-->\n" +
"<!--[if (mso)|(IE)]><td align=\"center\" width=\"650\" style=\"background-color:transparent;width:650px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 0px; padding-left: 0px; padding-top:0px; padding-bottom:0px;\"><![endif]-->\n" +
"<div class=\"col num12\" style=\"min-width: 320px; max-width: 650px; display: table-cell; vertical-align: top; width: 650px;\">\n" +
"<div style=\"width:100% !important;\">\n" +
"<!--[if (!mso)&(!IE)]><!-->\n" +
"<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
"<!--<![endif]-->\n" +
"<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 10px; padding-left: 10px; padding-top: 15px; padding-bottom: 20px; font-family: Arial, sans-serif\"><![endif]-->\n" +
"<div style=\"color:#ffffff;font-family:'Helvetica Neue', Helvetica, Arial, sans-serif;line-height:1.5;padding-top:15px;padding-right:10px;padding-bottom:20px;padding-left:10px;\">\n" +
"<div style=\"line-height: 1.5; font-size: 12px; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; color: #ffffff; mso-line-height-alt: 18px;\">\n" +
"<p style=\"font-size: 12px; line-height: 1.5; word-break: break-word; text-align: center; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; mso-line-height-alt: 18px; margin: 0;\"><span style=\"font-size: 12px;\">Lifecamp FCT Abuja, 900001, Nigeria</span></p>\n" +
"<p style=\"font-size: 12px; line-height: 1.5; word-break: break-word; text-align: center; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; mso-line-height-alt: 18px; margin: 0;\"><span style=\"font-size: 12px;\">© Copyright 2020 tixxbay.com</span></p>\n" +
"<p style=\"font-size: 12px; line-height: 1.5; word-break: break-word; text-align: center; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; mso-line-height-alt: 18px; margin: 0;\"><span style=\"font-size: 12px;\"><a href=\"http://www.example.com/\" rel=\"noopener\" style=\"text-decoration: none; color: #ffffff;\" target=\"_blank\">Manage Preferences</a> | <a href=\"http://www.example.com/\" rel=\"noopener\" style=\"text-decoration: none; color: #ffffff;\" target=\"_blank\">Unsubscribe</a></span></p>\n" +
"</div>\n" +
"</div>\n" +
"<!--[if mso]></td></tr></table><![endif]-->\n" +
"<!--[if (!mso)&(!IE)]><!-->\n" +
"</div>\n" +
"<!--<![endif]-->\n" +
"</div>\n" +
"</div>\n" +
"<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
"<!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n" +
"</div>\n" +
"</div>\n" +
"</div>\n" +
"<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
"</td>\n" +
"</tr>\n" +
"</tbody>\n" +
"</table>");
        return sb.toString();
    }
 

}






































