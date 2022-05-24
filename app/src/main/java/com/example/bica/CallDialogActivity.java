package com.example.bica;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bica.EntireCard.CardViewModel;
import com.example.bica.model.Card;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class CallDialogActivity{

    private String TAG = "CardDialogTAG";

    private Context context;

    private TextView tv_company, tv_depart, tv_name, tv_position, tv_Phone, tv_Email, tv_Address, tv_group;

    public CallDialogActivity(Context context) {
        this.context = context;
    }

    ArrayAdapter<String> arrayAdapter;
    static ArrayList<String> arr;


    CardViewModel cardViewModel;

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(Card cardInfo) {
        final Dialog dlg = new Dialog(context);

        tv_company = dlg.findViewById(R.id.tv_company);
        tv_depart = dlg.findViewById(R.id.tv_depart);
        tv_name = dlg.findViewById(R.id.tv_name);
        tv_position = dlg.findViewById(R.id.tv_position);
        tv_Phone = dlg.findViewById(R.id.tv_Phone);
        tv_Email = dlg.findViewById(R.id.tv_Email);
        tv_Address = dlg.findViewById(R.id.tv_Address);
        tv_group = dlg.findViewById(R.id.tv_group);

        tv_company.setText(cardInfo.getCompany());
        tv_depart.setText(cardInfo.getDepart());
        tv_name.setText(cardInfo.getName());
        tv_position.setText(cardInfo.getPosition());
        tv_Phone.setText(cardInfo.getPhone());
        tv_Email.setText(cardInfo.getEmail());
        tv_Address.setText(cardInfo.getAddress());
        tv_group.setText(cardInfo.getGroup());


    }
}