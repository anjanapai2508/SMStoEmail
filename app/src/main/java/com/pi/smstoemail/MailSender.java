package com.pi.smstoemail;

import java.util.Calendar;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

public class MailSender {
    private String toList;
    private String subject;
    final private static String SMTP_SERVER = "smtp.gmail.com";
    private String from;
    private String txtBody;
    private boolean authenticationRequired = false;

    public MailSender(String from, String toList, String subject, String txtBody) {
        this.txtBody = txtBody;
        this.subject = subject;
        this.from = from;
        this.toList = toList;
        this.authenticationRequired = true;
    }
    /**
     * Send an e-mail
     *
     * @throws MessagingException
     * @throws AddressException
     */
    public void send() throws AddressException, MessagingException {
        Properties props = new Properties();
        // set the host smtp address
        props.put("mail.smtp.host", SMTP_SERVER);
        props.put("mail.user", from);
        props.put("mail.smtp.starttls.enable", "true");  // needed for gmail
        props.put("mail.smtp.auth", "true"); // needed for gmail
        props.put("mail.smtp.port", "587");  // gmail smtp port 587 default gmail
        Session session;
        if (authenticationRequired) {
            Authenticator auth = new SMTPAuthenticator();
            props.put("mail.smtp.auth", "true");
            session = Session.getDefaultInstance(props, auth);
        } else {
            session = Session.getDefaultInstance(props, null);
        }
        // get the default session
        session.setDebug(true);
        // create message
        Message msg = new javax.mail.internet.MimeMessage(session);
        // set from and to address
        try {
            msg.setFrom(new InternetAddress(from, from));
        } catch (Exception e) {
            msg.setFrom(new InternetAddress(from));
        }
        // set send date
        msg.setSentDate(Calendar.getInstance().getTime());
        // parse the recipients TO address
        java.util.StringTokenizer st = new java.util.StringTokenizer(toList, ",");
        int numberOfRecipients = st.countTokens();
        javax.mail.internet.InternetAddress[] addressTo = new javax.mail.internet.InternetAddress[numberOfRecipients];
        int i = 0;
        while (st.hasMoreTokens()) {
            addressTo[i++] = new javax.mail.internet.InternetAddress(st
                    .nextToken());
        }
        msg.setRecipients(javax.mail.Message.RecipientType.TO, addressTo);
        msg.setSubject(subject);
        Multipart mp = new MimeMultipart("related");
        // set body message
        MimeBodyPart bodyMsg = new MimeBodyPart();
        bodyMsg.setText(txtBody, "iso-8859-1");
        mp.addBodyPart(bodyMsg);
        msg.setContent(mp, "text");
        // send it
        try {
            javax.mail.Transport.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * SimpleAuthenticator is used to do simple authentication when the SMTP
     * server requires it.
     */
    private static class SMTPAuthenticator extends javax.mail.Authenticator {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            // TODO : replace username and password with senders email id and password
            String username = "sender@someemail.com";
            String password = "email-account-password";
            return new PasswordAuthentication(username, password);
        }
    }

    public String getToList() {
        return toList;
    }

    public void setToList(String toList) {
        this.toList = toList;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTxtBody(String body) {
        this.txtBody = body;
    }

    public boolean isAuthenticationRequired() {
        return authenticationRequired;
    }
    public void setAuthenticationRequired(boolean authenticationRequired) {
        this.authenticationRequired = authenticationRequired;
    }
}
