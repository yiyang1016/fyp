package com.example.covid_19shoppingcentre

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_yiyang.*

class Yiyang : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yiyang)

        distanceTrackingBtn.setOnClickListener {
            val intent = Intent(this, distance_tracking::class.java)
            startActivity(intent)
        }

        nearestHostpitalBtn.setOnClickListener {
            val intent = Intent(this, nearby_hospital::class.java)
            startActivity(intent)
        }
    }
}