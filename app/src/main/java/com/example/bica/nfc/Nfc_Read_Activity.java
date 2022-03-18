package com.example.bica.nfc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.bica.AddCardActivity;
import com.example.bica.MainActivity;
import com.example.bica.R;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Objects;

public class Nfc_Read_Activity extends AppCompatActivity {

    private final String TAG = "Nfc_Read_Activity";

    private Toolbar tb_nfc_read;
    private TextView tv_nfcResult;

    NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_read);

        // 요소 초기화
        init();

        // Toolbar 활성화
        setSupportActionBar(tb_nfc_read);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 생성
        getSupportActionBar().setTitle(null); // Toolbar 제목 제거

        // NFC isn't available on the device
        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC)){
            tv_nfcResult.setText("NFC를 지원안하는 기기입니다.");
        }else{
            nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        }

    }

    // Toolbar 뒤로가기 버튼 활성화 코드
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    // 요소 초기화 함수
    private void init() {
        tb_nfc_read = (Toolbar) findViewById(R.id.tb_nfc_read);
        tv_nfcResult = (TextView) findViewById(R.id.tv_nfcResult);
    }
}