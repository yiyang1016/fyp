package com.example.covid_19shoppingcentre

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.dashboard_main.*

class DashboardMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_main)

        logoImgDashBoard.setImageDrawable(resources.getDrawable(R.drawable.logo))

        btnPiechartMaleFemale.setOnClickListener{
            val intent = Intent(this, PieChartMaleFemale::class.java).apply {
                putExtra("EXTRA_MESSAGE", "message")
            }
            startActivity(intent)
        }
    }
}