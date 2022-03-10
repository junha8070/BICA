package com.example.bica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.example.bica.member.LoginActivity;
import com.example.bica.member.RegisterActivity;

public class InitialActivity extends AppCompatActivity {

    private Button btn_login, btn_register;
    private TextView tv_first, tv_second, tv_third;
    private Animation fade_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        // 초기화
        init();

        tv_first.setVisibility(View.VISIBLE);
        tv_first.startAnimation(fade_in);
        tv_second.setVisibility(View.VISIBLE);
        tv_second.startAnimation(fade_in);
        tv_third.setVisibility(View.VISIBLE);
        tv_third.startAnimation(fade_in);

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
        // 애니메이션 초기화
        fade_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

        // 요소 초기화
        tv_first = findViewById(R.id.tv_welcome_first);
        tv_second = findViewById(R.id.tv_welcome_second);
        tv_third = findViewById(R.id.tv_welcome_third);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
    }
}