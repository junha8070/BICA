package com.example.bica.member;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.bica.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class FindPWActivity extends AppCompatActivity {

    private EditText edt_username, edt_phonenum, edt_email;
    private Button btn_email;
    private Toolbar tb_findPW;
    private static final String TAG = "FindPWActivity";
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private boolean checkInfo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

        init();

        edt_phonenum.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        btn_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String useremail = edt_email.getText().toString().trim();
                String username = edt_username.getText().toString().trim();
                String phonenum = edt_phonenum.getText().toString().trim();

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(username.isEmpty() || phonenum.isEmpty() || useremail.isEmpty()){
                    Toast.makeText(FindPWActivity.this, "빈칸을 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else {
                    firestore.collection("users")
                            .whereEqualTo("username", username)
                            .whereEqualTo("phonenum", phonenum)
                            .whereEqualTo("email", useremail)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        Log.d(TAG, "회원정보 일치");
                                        firebaseAuth.sendPasswordResetEmail(useremail)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Log.d(TAG, "이메일 전송 성공");
                                                            Toast.makeText(FindPWActivity.this, "이메일을 확인해주세요.", Toast.LENGTH_SHORT).show();
                                                            Intent starLogin = new Intent(FindPWActivity.this, LoginActivity.class);
                                                            startActivity(starLogin);
                                                            finish();
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.e(TAG,"이메일 전송 실패", e);
                                                        Toast.makeText(FindPWActivity.this, "이메일 전송에 실패하였습니다. 다시 시도해주세요.",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "일치하는 회원정보 없음");
                                    Toast.makeText(FindPWActivity.this, "일치하는 회원정보가 없습니다. 다시 입력해주세요", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        // Toolbar 활성화
        setSupportActionBar(tb_findPW);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 생성
        getSupportActionBar().setTitle(null); // Toolbar 제목 제거
    }

    // Toolbar 뒤로가기 버튼 활성화 코드
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void init() {
        tb_findPW = findViewById(R.id.findPW);
        edt_username = findViewById(R.id.user_name);
        edt_phonenum = findViewById(R.id.user_phonenum);
        edt_email = findViewById(R.id.user_email);
        btn_email = findViewById(R.id.email_check);
    }
}