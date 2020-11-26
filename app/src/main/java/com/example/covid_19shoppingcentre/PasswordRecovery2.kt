package com.example.covid_19shoppingcentre

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.password_recovery_2.*
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class PasswordRecovery2 : AppCompatActivity() {
    private var subject: String? = null
    private var eMessage: String? = null
    private var code: String? = null
    private var sEmail: String? = null
    private var sPassword: String? = null
    private var memberid:String?=null
    //private String email;
   // private var resend: Button? = null
    //private var next: Button? = null
   // private var codeEntered: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.password_recovery_2)
        val intent = intent
        val emailReceived = intent.getStringExtra("MemberEmail")
        memberid = intent.getStringExtra("MemberId")
        val randomCode = Random()
        sEmail = "fyptesting984@gmail.com"
        sPassword = "123456fyp"

        btnResend.setOnClickListener{
            val properties = Properties()
            properties["mail.smtp.host"] = "smtp.gmail.com"
            properties["mail.smtp.starttls.enable"] = "true"
            properties["mail.smtp.auth"] = "true"
            properties["mail.smtp.port"] = "587"
            properties["mail.smtp.ssl.trust"] = "smtp.gmail.com"
            code = randomCode.nextInt(9999).toString()
            //email = "wolflee4399@gmail.com";
            subject = "Validation Code For Reset Password"
            eMessage = "Your verification code for resetting your password is   $code"
            Log.d("testing", code)
            val session = Session.getDefaultInstance(
                properties,
                object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(sEmail, sPassword)
                    }
                })
            try {
                val message: Message = MimeMessage(session)
                message.setFrom(InternetAddress(sEmail))
                message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(emailReceived)
                )
                message.subject = subject
                message.setText(eMessage)
                Transport.send(message)
            } catch (e: MessagingException) {
                throw RuntimeException(e)
            }
        }
        val policy =
            StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)



        btnPassRecOK12.setOnClickListener{
            val generatecode = code
            if (generatecode == txtVerificationCode.getText().toString()) {
                Toast.makeText(
                    applicationContext,
                    "Code Entered Successfully",
                    Toast.LENGTH_SHORT
                )
                //GO TO PASSWORD3
                val i = Intent(this@PasswordRecovery2, PasswordRecovery3::class.java)
                i.putExtra("MemberEmail", emailReceived)
                i.putExtra("memberId",memberid)
                startActivity(i)
            } else {
                Toast.makeText(
                    applicationContext,
                    "Code Entered Wrong",
                    Toast.LENGTH_SHORT
                )
            }
        }

        btnBack.setOnClickListener{
            val i = Intent(this@PasswordRecovery2, PasswordRecovery1::class.java)
            startActivity(i)
        }
    } /*private class SendMail extends AsyncTask<Message, String,String> {
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
    }*/
}