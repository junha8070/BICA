package com.example.bica.member;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.bica.R;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.util.Objects;

public class FindPWActivity extends AppCompatActivity {

    private EditText edt_username, edt_phonenum, edt_email;
    private Button btn_phonenum, btn_email;
    private Toolbar tb_findPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

        init();

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
        btn_phonenum = findViewById(R.id.phonenum_check);
        btn_email = findViewById(R.id.email_check);
    }
}