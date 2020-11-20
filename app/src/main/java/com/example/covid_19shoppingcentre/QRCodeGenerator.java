package com.example.covid_19shoppingcentre;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRCodeGenerator extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generate_qr_code);
        //CHANGE TO GENERATE USERID AFTER COMBINE
        Intent intent = getIntent();
        String generateWord = intent.getStringExtra("MemberEmail");;
        ImageView qrImage = findViewById(R.id.imgViewQRCode);
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = Math.min(width, height);
        smallerDimension = smallerDimension * 3 / 4;
        QRGEncoder qrgEncode = new QRGEncoder(generateWord, null, QRGContents.Type.TEXT,  smallerDimension);
        qrgEncode.setColorBlack(Color.BLACK);
        qrgEncode.setColorWhite(Color.WHITE);
        try {
            Bitmap qrBits = qrgEncode.getBitmap();
            qrImage.setImageBitmap(qrBits);
        }catch (Exception e){
            e.printStackTrace();;
        }
    }
}
