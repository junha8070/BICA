package com.example.bica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bica.member.LoginActivity;
import com.example.bica.member.RegisterActivity;

public class InitialActivity extends AppCompatActivity {

    private Button btn_login, btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        init();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startLoginActivity = new Intent(InitialActivity.this, LoginActivity.class);
                startActivity(startLoginActivity);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startRegisterActivity = new Intent(InitialActivity.this, RegisterActivity.class);
                startActivity(startRegisterActivity);
            }
        });

    }

    public void init(){
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
    }
}