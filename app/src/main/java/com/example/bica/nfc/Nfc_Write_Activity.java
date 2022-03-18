package com.example.bica.nfc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bica.R;

public class Nfc_Write_Activity extends AppCompatActivity {

    NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_write);

        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC)){
            Toast.makeText(this, "NFC를 지원안하는 기기입니다.", Toast.LENGTH_SHORT).show();
        }else{
            nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        }
    }
}