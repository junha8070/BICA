package com.example.bica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.bica.member.FindIDActivity;
import com.example.bica.member.FoundIDActivity;
import com.example.bica.member.RegisterActivity;
import com.example.bica.member.RegisterCardActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class EditInfoActivity extends AppCompatActivity {

    private Toolbar tb_editInfo;
    private EditText edt_newPW, edt_newcheckPW;
    private Button btn_newPW;
    private static final String TAG = "EditInfoActivity";
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        init();

        btn_newPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                String newPW = edt_newPW.getText().toString().trim();
                String newcheckPW = edt_newcheckPW.getText().toString().trim();

                if(newPW.isEmpty() == false && newcheckPW.isEmpty() == false){
                    if(newPW.equals(newcheckPW)){

                        user.updatePassword(newPW)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Log.d(TAG, "비밀번호 변경 성공");
                                            // 성공적으로 변경되었을 시 설정화면으로 돌아감
                                            finish();
                                            Toast.makeText(EditInfoActivity.this, "비밀번호 변경 성공", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "오류가 발생하였습니다", e);
                                    }
                                });

                    }
                    // 비밀번호가 일치하지 않을 때
                    else{
                        Toast.makeText(EditInfoActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                // 빈칸이 존재할 때
                else{
                    Toast.makeText(EditInfoActivity.this, "빈칸이 존재합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }


            }
        });

        // Toolbar 활성화
        setSupportActionBar(tb_editInfo);
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
        tb_editInfo = findViewById(R.id.tb_editInfo);
        edt_newcheckPW = findViewById(R.id.edt_newcheckPW);
        edt_newPW = findViewById(R.id.edt_newPW);
        btn_newPW = findViewById(R.id.btn_newPW);
    }
}