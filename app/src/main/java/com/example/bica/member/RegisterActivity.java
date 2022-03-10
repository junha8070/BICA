package com.example.bica.member;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.bica.MainActivity;
import com.example.bica.R;

public class RegisterActivity extends AppCompatActivity {

    Button btn_next, btn_check;
    EditText edt_username, edt_useremail, edt_pw, edt_pw_check, edt_phonenum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startMain = new Intent(RegisterActivity.this, RegisterCardActivity.class);
                startActivity(startMain);
            }
        });

        // TODO: 회원가입 구현
    }

    public void init(){
        edt_username = findViewById(R.id.user_name);
        edt_useremail = findViewById(R.id.user_email);
        edt_pw = findViewById(R.id.user_pw);
        edt_pw_check = findViewById(R.id.user_pw_check);
        edt_phonenum = findViewById(R.id.user_phonenum);
        btn_next = findViewById(R.id.next_regist2);
        btn_check = findViewById(R.id.email_check);
    }
}