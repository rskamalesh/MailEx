package com.mailex.learning;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import junit.framework.TestCase;

public class JavaMailLearningTest extends TestCase {

    private final String userName = "xxx.xxx@gmail.com"; //change this with your original username
    private String password = "xxxx"; //change this with your original password
    

    public void testShouldSignInToGmailAccountUsingIMAPsService() throws Exception {
        Store store = null;
        try {
            store = validIMAPsStore();
            store.connect(getIMAPHost(), userName, password);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            if ( store != null )
                store.close();
        }
    }
    
    public void testShouldReadMailsFromInboxUsingIMAPSSLService() throws Exception {
        Store store = null;
        try {
            store = validIMAPsStore();
            store.connect(getIMAPHost(), userName, password);
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            assertTrue(inbox.getMessageCount() > 0);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            if ( store != null )
                store.close();
        }
    }
    
    public void testShouldSignInToGmailAccountUsingSMTPSSLService() throws Exception {
        Transport transport = null;
        try {            
            Session session = validSMTPsSession();
            transport = session.getTransport("smtps");
            transport.connect(getSMTPHost(), userName, password);
        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            if(transport != null) 
                transport.close();
        }
    }
    
    public void testShouldSendAnEmailFromTheGmailAccountUsingSMTPsService() throws Exception {
        Transport transport = null;
        try {            
            Session session = validSMTPsSession();
            MimeMessage email = composeMail(session);            
            transport = session.getTransport("smtps");            
            transport.connect(getSMTPHost(), userName, password);
            transport.sendMessage(email, email.getAllRecipients());
        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            if(transport != null) 
                transport.close();
        }
    }

    private MimeMessage composeMail(Session session) throws MessagingException, AddressException {
        String emailAddress = userName;
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(emailAddress));
        email.addRecipient(RecipientType.TO, new InternetAddress(emailAddress));
        email.setSubject("Subject:-Test Mail");
        email.setText("Text: The same old text mail!");
        return email;
    }

    private String getIMAPHost() {
        return "imap.gmail.com";
    }
    
    private String getSMTPHost() {
        return "smtp.gmail.com";
    }

    private Store validIMAPsStore() throws Exception {
        Session session = Session.getInstance(imapProperties(), null);
        Store store = session.getStore("imaps");
        return store;
    }
    
    private Session validSMTPsSession() throws NoSuchProviderException {
        Session session = Session.getInstance(smtpProperties());
        return session;
    }

    private Properties imapProperties() {
        Properties imapProperties = System.getProperties();
        imapProperties.setProperty("mail.store.protocol", "imaps");
        return imapProperties;
    }

    private Properties smtpProperties() {
        Properties smtpProperties = System.getProperties();
        smtpProperties.put("mail.smtps.host", getSMTPHost());
        smtpProperties.put("mail.smtps.auth", "true");
        return smtpProperties;
    }

}
