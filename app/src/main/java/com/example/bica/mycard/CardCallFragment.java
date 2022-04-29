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
    private TextView tv_mycardname,tv_myPosition,tv_myOccupation,tv_myTeamName,tv_myCompany_Name,tv_myGroupName,tv_myPhoneNum,tv_my_Email,tv_myCompany_Address,tv_myMemo,tv_Pnum;
    private EditText et_mycardname,et_myPosition,et_myOccupation,et_myTeamName,et_myCompany_Name,et_myGroupName,et_myPhoneNum,et_my_Email,et_myMemo,et_myCompany_Address;
    private View view;
    private String Pnum, str_card_info, cardUid;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
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
