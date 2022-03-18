package com.example.bica.member;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bica.MainActivity;
import com.example.bica.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    Button btn_login;
    EditText edt_ID,edt_PW;
    TextView tv_Find_ID, tv_Find_PW;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        firebaseAuth = FirebaseAuth.getInstance();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = edt_ID.getText().toString().trim();
                String pw = edt_PW.getText().toString().trim();

                firebaseAuth.signInWithEmailAndPassword(id, pw)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                                    Intent startMain = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(startMain);
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, "로그인 오류", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        tv_Find_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startMain = new Intent(LoginActivity.this, FindIDActivity.class);
                startActivity(startMain);
            }
        });

        tv_Find_PW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startMain = new Intent(LoginActivity.this, FindPWActivity.class);
                startActivity(startMain);
            }
        });

    }

    public void init(){
        edt_ID = findViewById(R.id.edt_id);
        edt_PW = findViewById(R.id.edt_pw);
        tv_Find_ID = findViewById(R.id.tv_Find_ID);
        tv_Find_PW = findViewById(R.id.tv_Find_PW);
        btn_login = findViewById(R.id.btn_login);
    }
}