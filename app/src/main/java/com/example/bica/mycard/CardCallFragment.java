package com.example.bica.mycard;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.bica.R;
import com.example.bica.model.Card;
import com.google.firebase.auth.FirebaseAuth;

public class CardCallFragment extends Activity {
    private MyCardViewModel myCardViewModel;
    private TextView tv_Pnum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
