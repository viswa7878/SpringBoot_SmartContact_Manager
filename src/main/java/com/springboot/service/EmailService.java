package com.springboot.service;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public boolean sendEmail(String subject, String message, String to) {
        
    	boolean res=false;
        // Set the SMTP server details
        String host = "smtp.gmail.com";
        String from = "kommanaviswa@karunya.edu.in";  // Update with your email

        // Set mail properties
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");  // Use port 587 for TLS
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        // Get the session object
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // Use secure method for storing credentials like environment variables
                return new PasswordAuthentication("kommanaviswa@karunya.edu.in","viswa@8179");
            }
        });
        
        // Compose the message
        try {
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(from));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message);
            mimeMessage.setContent(message,"text/html");
            // Send the message
            Transport.send(mimeMessage);
            System.out.println("Email sent successfully.");
            res=true;
        } catch (MessagingException e) {
            e.printStackTrace();
        }
		return res;
    }
}
