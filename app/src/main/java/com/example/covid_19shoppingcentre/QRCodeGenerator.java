package com.example.covid_19shoppingcentre;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRCodeGenerator extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generate_qr_code);

        Button buttonX = (Button)findViewById(R.id.button10);

        //CHANGE TO GENERATE USERID AFTER COMBINE
        Intent intent = getIntent();
        String generateWord = intent.getStringExtra("MemberID");;
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
            e.printStackTrace();
        }

        buttonX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                i.putExtra("MemberID", generateWord);
                startActivity(i);
            }
        });
    }
}
