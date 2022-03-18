package com.example.bica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class AddCardActivity extends AppCompatActivity {
String TAG = "Nfc_Read_result";
    TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        Log.d(TAG, "수신됨");

        init();

        Intent passedIntent = getIntent();

        if(passedIntent != null){
            onNewIntent(passedIntent);
        }
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String s = "";

        Parcelable[] data = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        if (data != null) {
            try {
                for (int i = 0; i < data.length; i++) {
                    NdefRecord[] recs = ((NdefMessage) data[i]).getRecords();

                    for (int j = 0; j < recs.length; j++) {
                        if (recs[j].getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(recs[j].getType(),
                                NdefRecord.RTD_TEXT)) {

                            byte[] payload = recs[j].getPayload();

                            String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
                            int langCodeLen = payload[0] & 0077;

                            s += ("\n" + new String(payload, langCodeLen + 1, payload.length - langCodeLen - 1, textEncoding));
                        }
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            tv_result.setText(s);
        }
    }

    public void init(){
        tv_result = (TextView) findViewById(R.id.tv_result);
    }
}