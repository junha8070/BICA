package com.example.bica.AddCard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.bica.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanQR extends AppCompatActivity {

    private IntentIntegrator qrScan;
    private QRViewmodel qrViewmodel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanqr);

        qrScan = new IntentIntegrator(this);
        qrScan.setOrientationLocked(false); // default가 세로모드인데 휴대폰 방향에 따라 가로, 세로로 자동 변경됩니다.
        qrScan.initiateScan();

        qrViewmodel = new ViewModelProvider(this).get(QRViewmodel.class);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                qrViewmodel.AddCard(result.getContents());

                qrViewmodel.getSuccessMutableLiveData().observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if(aBoolean){
                            Toast.makeText(ScanQR.this, "Success", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(ScanQR.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }
                });
                // todo
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}