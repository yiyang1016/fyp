package com.example.covid_19shoppingcentre

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.customer_review.view.*
import kotlinx.android.synthetic.main.customer_review_submission.view.*

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val serviceClass = checkService::class.java
        val intent1 = Intent(applicationContext, serviceClass)
        intent1.putExtra("MemberId", "M00001")

        if (!isServiceRunning(serviceClass)) {
            // Start the service
            startService(intent1)
        } else {
            Toast.makeText(applicationContext,"service already running",Toast.LENGTH_SHORT).show()
        }

        StaffMainPage.setOnClickListener {
            stopService(intent1)
            val intent = Intent(this, StaffMainActivity::class.java)
            startActivity(intent)
        }

        reserveBtn.setOnClickListener {
            val intent = Intent(this, ReserveStore_List::class.java)
            startActivity(intent)
        }

        reserveListBtn.setOnClickListener{
            val intent = Intent(this, Reservation_List::class.java)
            startActivity(intent)
        }

        store_list.setOnClickListener {
            val intent = Intent(this, Store_List::class.java)
            startActivity(intent)
        }

        sdchBtn.setOnClickListener {
            val intent = Intent(this, social_distance_score_history::class.java)
            startActivity(intent)
        }
        btnDistance_tracking.setOnClickListener {
            val intent = Intent(this, distance_tracking::class.java)
            startActivity(intent)
        }

        btnNearbyHospital.setOnClickListener {
            val intent = Intent(this, nearby_hospital::class.java)
            startActivity(intent)
        }

        btnStoreLogin.setOnClickListener {
            val intent = Intent(this, tabletStoreLoginVarification::class.java)
            startActivity(intent)
        }

        review.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this@MainActivity, R.style.CustomAlertDialog)

            val view = layoutInflater.inflate(R.layout.customer_review, null)
            dialogBuilder.setView(view)
            val alertDialog = dialogBuilder.create()
            alertDialog.show()

            val submitButton = view.btnSubmit
            submitButton.setOnClickListener {
                if (view.review1.rating.toInt() == 0 || view.review2.rating.toInt() == 0 ||
                    view.review3.rating.toInt() == 0 || view.review4.rating.toInt() == 0
                ) {
                    val errordialogBuilder =
                        AlertDialog.Builder(this@MainActivity, R.style.CustomAlertDialog)
                    val view1 = layoutInflater.inflate(R.layout.customer_review_empty_field, null)
                    errordialogBuilder.setView(view1)
                    val alertDialog1 = errordialogBuilder.create()
                    alertDialog1.show()
                    view1.okbtn.setOnClickListener {
                        alertDialog1.cancel()
                    }
                } else {
                    val ref = FirebaseDatabase.getInstance().getReference("Review")
                    var reviewId = ""
                    val refSearch =
                        FirebaseDatabase.getInstance().getReference("Review").orderByKey()
                            .limitToLast(1)

                    refSearch.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            val text = "Connection Failed"
                            Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.exists()) {
                                for (p0 in p0.children) {
                                    val reviewsId = p0.getValue(ReviewIdClass::class.java)
                                    reviewId = reviewsId.toString()
                                }

                                val cal = ((reviewId.substring(2, 7)).toInt()) + 1
                                val num = 100000 + cal
                                val newId = "RV" + num.toString().substring(1,6)

                                val editText = view.review5
                                if (editText.text.toString() != null) {
                                    val showString = editText.text.toString()

                                    var feedback = ""
                                    feedback = view.review5.text.toString()

                                    val data = Review(
                                        newId,
                                        view.review1.rating.toInt(),
                                        view.review2.rating.toInt(),
                                        view.review3.rating.toInt(),
                                        view.review4.rating.toInt(),
                                        feedback
                                    )

                                    ref.child(newId).setValue(data).addOnCompleteListener {
                                        val submissiondialogBuilder = AlertDialog.Builder(
                                            this@MainActivity,
                                            R.style.CustomAlertDialog
                                        )
                                        alertDialog.cancel()
                                        val view2 = layoutInflater.inflate(
                                            R.layout.customer_review_submission,
                                            null
                                        )
                                        submissiondialogBuilder.setView(view2)
                                        val alertDialog = submissiondialogBuilder.create()
                                        alertDialog.show()
                                        view2.okbtn.setOnClickListener {
                                            alertDialog.cancel()
                                        }
                                    }

                                }
                            }

                        }
                    })

                }

            }

        }

    }

    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        // Loop through the running services
        for (service in activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                // If the service is running then return true
                return true
            }
        }
        return false
    }
}

