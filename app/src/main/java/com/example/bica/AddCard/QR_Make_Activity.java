package com.example.bica.AddCard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.bica.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Hashtable;

public class QR_Make_Activity extends AppCompatActivity {
    String TAG = "QR_Make_Activity";

    ImageView iv_qrcode;
    private String str_card;
    QRCodeWriter writer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_make);

        // 요소 초기화
        init();

        Intent receiveCardId = getIntent();
        str_card = receiveCardId.getStringExtra("cardId");
        Log.d(TAG, "아이디"+str_card);

//        str_card = "https://www.naver.com";

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try{
            Hashtable hints = new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix matrix = writer.encode(str_card, BarcodeFormat.QR_CODE, 500, 500, hints);

            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(matrix);
            iv_qrcode.setImageBitmap(bitmap);
        }catch (Exception e){}


    }

    private void init(){
        iv_qrcode = findViewById(R.id.iv_qrcode);
        writer = new QRCodeWriter();
    }
}