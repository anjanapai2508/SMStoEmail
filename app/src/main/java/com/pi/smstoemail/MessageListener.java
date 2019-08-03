package com.pi.smstoemail;

import javax.mail.MessagingException;

interface MessageListener {

    void messageReceived(String message) throws MessagingException;
}
