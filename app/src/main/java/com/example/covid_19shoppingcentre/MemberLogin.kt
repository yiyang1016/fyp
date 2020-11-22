package com.example.covid_19shoppingcentre

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.member_login.*

class MemberLogin : AppCompatActivity() {
    //firebase
    private var userDatabase = FirebaseDatabase.getInstance().getReference("Member")

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.member_login)

        btnLogin.setOnClickListener{
            val userID = txtUserid.text.toString()
            val userPassword = txtPassword.text.toString()
//            lblWelcome.setText(userID)
            //getData
            var checkUser:Query = userDatabase.orderByKey().equalTo(userID)

            checkUser.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "Error",Toast.LENGTH_SHORT).show()
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        var pass: String = snapshot.child(userID).child("Password").value.toString()
                        var role = snapshot.child(userID).child("Role").value.toString()

                        if(pass == userPassword){
                            Toast.makeText(
                                applicationContext,
                                "Login Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            val i = Intent(this@MemberLogin, questionMobile::class.java)
                            i.putExtra("MemberID", userID)
                            startActivity(i)
                        }else
                            Toast.makeText(applicationContext, "wrong password",Toast.LENGTH_SHORT).show()
                    }else
                        Toast.makeText(applicationContext, "Member ID doesn't exists $userID",Toast.LENGTH_SHORT).show()
                }
            })
        }
        btnPasswordRecover.setOnClickListener{
            val i = Intent(this@MemberLogin, PasswordRecovery1::class.java)
            startActivity(i)
        }
        btnRegister.setOnClickListener{
            val i = Intent(this@MemberLogin, MemberRegister::class.java)
            startActivity(i)
        }

    }

}