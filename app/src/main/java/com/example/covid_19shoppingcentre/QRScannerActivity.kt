package com.example.covid_19shoppingcentre

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import github.nisrulz.qreader.QRDataListener
import github.nisrulz.qreader.QREader
import kotlinx.android.synthetic.main.sc_member_check_in_scanner.*

class QRScannerActivity :AppCompatActivity() {

    private var qrEader: QREader?=null;
    private lateinit var mRunnable: Runnable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sc_member_check_in_scanner)

        Dexter.withActivity(this@QRScannerActivity)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object:PermissionListener{
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    setupCamera()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    Toast.makeText(this@QRScannerActivity, "You must enable this permission", Toast.LENGTH_SHORT).show()
                }

            }).check()
    }

    private fun setupCamera() {
        btn_enable_disable.setOnClickListener{
            if(qrEader!!.isCameraRunning)
            {
                btn_enable_disable.text = "Start"
                qrEader!!.stop()
            }
            else
            {
                btn_enable_disable.text = "Stop"
                qrEader!!.start()
            }
        }

        setupQREADER()
    }

    private fun setupQREADER() {

        qrEader = QREader.Builder(this@QRScannerActivity,camera_view, QRDataListener { data ->
            info_code.post{ startActivity(Intent(this, MemberInformationActivity::class.java).apply {
                putExtra("EXTRA_MESSAGE", data) })}
        }).facing(QREader.BACK_CAM)
            .enableAutofocus(true)
            .height(camera_view.height)
            .width(camera_view.width)
            .build()
    }

    override fun onResume(){
        super.onResume()
        if(qrEader != null)
            qrEader!!.initAndStart(camera_view)
    }

    override fun onPause(){
        super.onPause()
        if(qrEader != null)
            qrEader!!.releaseAndCleanup()
    }
}