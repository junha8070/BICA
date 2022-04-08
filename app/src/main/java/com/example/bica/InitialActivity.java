package com.example.bica;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.example.bica.member.LoginActivity;
import com.example.bica.member.MemberViewModel;
import com.example.bica.member.RegisterActivity;
import com.google.firebase.auth.FirebaseUser;

public class InitialActivity extends AppCompatActivity {

    private Button btn_login, btn_register;
    private TextView tv_first, tv_second, tv_third;
    private Animation fade_in;

    private MemberViewModel memberViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        memberViewModel = new ViewModelProvider(this).get(MemberViewModel.class);
        memberViewModel.getUserMutableLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                Intent startMain = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(startMain);
            }
        });

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