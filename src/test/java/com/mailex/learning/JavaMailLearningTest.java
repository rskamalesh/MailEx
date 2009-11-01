package com.mailex.learning;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.Store;

import junit.framework.TestCase;

public class JavaMailLearningTest extends TestCase {

    private final String mailServer = "imap.gmail.com";
    private final String userName = "mailex@gmail.com"; //change this with your original username
    private String password = "xxxxx"; //change this with your original password
    

    public void testShouldBeAbleToSignInToGmailAccountUsingIMAPService() throws Exception {
        Store store = null;
        try {
            Properties mailProperties = System.getProperties();
            mailProperties.setProperty("mail.store.protocol", "imaps");
            Session session = Session.getInstance(mailProperties, null);
            store = session.getStore("imaps");
            store.connect(mailServer, userName, password);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            if ( store != null )
                store.close();
        }
    }
}
