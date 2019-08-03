package com.pi.smstoemail;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import javax.mail.MessagingException;

public class MainActivity extends AppCompatActivity implements MessageListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Regiser SMS listener
        MessageReceiver.bindListner(this);
    }

    @Override
    public void messageReceived(String message)  {
        Toast.makeText(this, "New Message Received: " + message, Toast.LENGTH_SHORT).show();
        AsyncSendEmail sendEmail = new AsyncSendEmail();
        sendEmail.execute(message);

    }
    private class AsyncSendEmail extends AsyncTask<String,String, String>
    {
        @Override
        protected String doInBackground(String... strings) {
            //TODO : insert valid email addresses in from and toList
            MailSender mailSender = new MailSender("sender@some-email.com","recepient1@some-email.com,recepient2@some-email.com","Message received on Phone",  strings[0]);
            try {
                mailSender.send();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
           System.out.println("sent email successfully");
        }
    }
}
