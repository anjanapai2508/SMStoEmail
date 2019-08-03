package com.pi.smstoemail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.mail.MessagingException;

public class MessageReceiver extends BroadcastReceiver {

    public static MessageListener messageListener;
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();
        Object [] protocolDataUnits = (Object[])data.get("pdus");
        for(int i =0;i<protocolDataUnits.length;i++) {
            SmsMessage message = SmsMessage.createFromPdu((byte[]) protocolDataUnits[i]);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            String dateString = formatter.format(new Date(message.getTimestampMillis()));
            String formattedMessage = "Sender : " + message.getDisplayOriginatingAddress() + "\n" +
                    "Message : " + message.getDisplayMessageBody()  + "\n" +
                    "Received at : " + dateString ;
            try {
                messageListener.messageReceived(formattedMessage);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }
    public static void bindListner(MessageListener listener)
    {
        messageListener = listener;
    }
}
