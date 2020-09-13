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
	final public static String creditVendor = "TRANS_CREDIT_Vendor";
	final public static String creditEventManager = "TRANS_CREDIT_EVENT_MANAGER";
        final public static String buyTicket = "TRANS_BUY_TICKET";
	
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
    private static byte[] key;
    
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
 

}




























