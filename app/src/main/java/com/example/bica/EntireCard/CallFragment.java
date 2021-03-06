package com.example.bica.EntireCard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.bica.R;

public class CallFragment extends Activity {
    private TextView tv_Pnum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_card_call);

        //UI 객체생성
        tv_Pnum = (TextView)findViewById(R.id.tv_Pnum);

        //데이터 가져오기
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        tv_Pnum.setText(data);
    }
}
