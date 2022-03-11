package com.example.bica.member;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bica.MainActivity;
import com.example.bica.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class RegisterCardActivity extends AppCompatActivity {

    Button btn_complete, btn_later;
    ImageView img_face;
    TextView tv_title;
    EditText edt_username, edt_useremail, edt_phonenum, edt_companyname, edt_companyadr, edt_occupation, edt_teamname, edt_position, edt_groupname, edt_memo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_card);

        init();

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startMain = new Intent(RegisterCardActivity.this, MainActivity.class);
                startActivity(startMain);
            }
        });

        btn_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startMain = new Intent(RegisterCardActivity.this, MainActivity.class);
                startActivity(startMain);
            }
        });
    }
    public void init(){
        btn_complete = findViewById(R.id.btn_complete);
        btn_later = findViewById(R.id.btn_later);
        img_face = findViewById(R.id.img_face);
        tv_title = findViewById(R.id.register_card_title);
        edt_username = findViewById(R.id.user_name);
        edt_useremail = findViewById(R.id.user_email);
        edt_phonenum = findViewById(R.id.user_phonenum);
        edt_companyname = findViewById(R.id.company_name);
        edt_companyadr = findViewById(R.id.company_address);
        edt_occupation = findViewById(R.id.occupation);
        edt_teamname = findViewById(R.id.teamname);
        edt_position = findViewById(R.id.position);
        edt_groupname = findViewById(R.id.groupname);
        edt_memo = findViewById(R.id.memo);
    }
}


