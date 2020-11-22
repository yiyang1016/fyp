package com.example.covid_19shoppingcentre

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.member_view_record_menu.*

class member_menu :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.member_view_record_menu)

        /////////////////////////////////////////
        val intent1: Intent = intent
        var memberId = intent1.getStringExtra("MemberID")
        ////////////////////////////////////////

        btn_visitHistory.setOnClickListener {
            val intent = Intent(this, VisitHistory::class.java)
            intent.putExtra("MemberID", memberId)
            startActivity(intent)
        }
        btn_bodyTemperature.setOnClickListener{
            val intent = Intent(this, BodyTemperature::class.java)
            intent.putExtra("MemberID", memberId)
            startActivity(intent)
        }
    }
}