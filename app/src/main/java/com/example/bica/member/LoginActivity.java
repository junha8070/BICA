package com.example.bica.member;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bica.MainActivity;
import com.example.bica.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private EditText edt_ID, edt_PW;
    private TextView tv_Find_ID, tv_Find_PW;
    private FirebaseAuth firebaseAuth;

    private MemberViewModel memberViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        memberViewModel = new ViewModelProvider(this).get(MemberViewModel.class);
        memberViewModel.getUserMutableLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                Intent startMain = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(startMain);
            }
        });

        // 요소 초기화
        init();
        firebaseAuth = FirebaseAuth.getInstance();  // 파이어베이스 auth 인스턴스 생성

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = edt_ID.getText().toString().trim();
                String pw = edt_PW.getText().toString().trim();

                // 아이디 입력 안했으면
                if (id.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 아이디가 이메일 형식을 벗어낫을경우
                if (!id.contains("@")) {
                    Toast.makeText(LoginActivity.this, "아이디를 이메일 형식으로 작성해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 비밀번호 입력 안했으면
                if (pw.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                memberViewModel.login(id, pw);

            }
        });

        // 아이디 찾기
        tv_Find_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startMain = new Intent(LoginActivity.this, FindIDActivity.class);
                startActivity(startMain);
            }
        });

        // 비밀번호 찾기
        tv_Find_PW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startMain = new Intent(LoginActivity.this, FindPWActivity.class);
                startActivity(startMain);
            }
        });
    }

    // 요소 초기화
    public void init() {
        edt_ID = findViewById(R.id.edt_id);
        edt_PW = findViewById(R.id.edt_pw);
        tv_Find_ID = findViewById(R.id.tv_Find_ID);
        tv_Find_PW = findViewById(R.id.tv_Find_PW);
        btn_login = findViewById(R.id.btn_login);
    }
}