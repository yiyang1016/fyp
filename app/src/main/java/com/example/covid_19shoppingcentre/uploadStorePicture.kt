package com.example.covid_19shoppingcentre

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.list_layout.view.*
import kotlinx.android.synthetic.main.upload_store_picture.*
import java.lang.reflect.Array.get

class uploadStorePicture : AppCompatActivity() {

    lateinit var alertDialog: AlertDialog
    lateinit var storageReference: StorageReference


    lateinit var passURL: String

    companion object{
        private val PICK_IMAGE_CODE = 1000
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_CODE){
            alertDialog.show()
            val uploadTask = storageReference!!.putFile(data!!.data!!)
            val task = uploadTask.continueWithTask {
                task ->
                if (!task.isSuccessful) {
                    Toast.makeText(this@uploadStorePicture, "Failed", Toast.LENGTH_SHORT).show()
                }
                storageReference!!.downloadUrl
            }.addOnCompleteListener{task ->
                if(task.isSuccessful){
                    val downloadUri = task.result
                    val url = downloadUri!!.toString().substring(0, downloadUri.toString().indexOf("&token"))
                    Log.d("DIRECTLINK", url)
                    alertDialog.dismiss()
                    Picasso.with(this@uploadStorePicture).load(url).into(ivStorePic)
                    passURL = url
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.upload_store_picture)

        val builder = AlertDialog.Builder(this)
        alertDialog = builder.create()
        alertDialog.show()

        val intent = intent
        val name = intent.getStringExtra("regisStoreName")

        storageReference = FirebaseStorage.getInstance().getReference(name)

        btnUploadComfirm.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_CODE)
        }

        btnBackRes.setOnClickListener{
            val i = Intent(
                this@uploadStorePicture,
                storeRegistration::class.java
            )
            i.putExtra("PictureURL", passURL)
            startActivity(i)
        }
    }
}