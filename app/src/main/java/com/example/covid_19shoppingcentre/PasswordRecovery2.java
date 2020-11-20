package com.example.covid_19shoppingcentre;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class PasswordRecovery2 extends AppCompatActivity {

    private String subject,eMessage,code, sEmail,sPassword;
    //private String email;
    private Button resend, next;
    private EditText codeEntered;
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_recovery_2);
        Intent intent = getIntent();
        String emailReceived = intent.getStringExtra("MemberEmail");


        final Random randomCode = new Random();
        resend = findViewById(R.id.btnResend);
        next = findViewById(R.id.btnPassRecOK);
        sEmail = "fyptesting984@gmail.com";
        sPassword = "123456fyp";

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties properties = new Properties();
                properties.put("mail.smtp.host", "smtp.gmail.com");
                properties.put("mail.smtp.starttls.enable","true");
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.port", "587");
                properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
                code = String.valueOf(randomCode.nextInt(9999));
                //email = "wolflee4399@gmail.com";
                subject = "Validation Code For Reset Password";
                eMessage = "Your verification code for resetting your password is   "+ code;

               Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator(){
                   @Override
                    protected PasswordAuthentication getPasswordAuthentication(){
                        return new PasswordAuthentication(sEmail, sPassword);
                    }
                });

                try{
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(sEmail));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailReceived));
                    message.setSubject(subject);
                    message.setText(eMessage);
                    new SendMail().execute(message);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                codeEntered = findViewById(R.id.txtVerificationCode);
                if(code == codeEntered.getText().toString()){
                    Toast.makeText(getApplicationContext(),"Code Entered Successfully", Toast.LENGTH_SHORT);
                    //GO TO PASSWORD3
                    Intent i = new Intent(PasswordRecovery2.this, PasswordRecovery3.class);
                    i.putExtra("MemberEmail",emailReceived);
                    startActivity(i);
            }else{
                    Toast.makeText(getApplicationContext(),"Code Entered Wrong", Toast.LENGTH_SHORT);
                }
            }
        } );
    }

    private class SendMail extends AsyncTask<Message, String,String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(PasswordRecovery2.this, "Please Wait", "Sending Mail...", true, false);
        }

        @Override
        protected String doInBackground(Message... messages){
            try{
                Transport.send(messages[0]);
                return "Success";
            }catch (MessagingException e){
                e.printStackTrace();
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            progressDialog.dismiss();
            if(s.equals("Success")){

                AlertDialog.Builder builder = new AlertDialog.Builder(PasswordRecovery2.this);
                builder.setCancelable(false);
                builder.setTitle(Html.fromHtml("<font color='#500324'>Success</font>"));
                builder.setMessage("Mail send successfully");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int which){
                        dialog.dismiss();
                    }
                });
                builder.show();
            }else{
                Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_SHORT);
            }
        }
    }
}
