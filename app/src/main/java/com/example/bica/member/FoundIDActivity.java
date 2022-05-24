package com.example.bica.member;

import androidx.appcompat.app.AppCompatActivity;
import com.example.bica.R;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class FoundIDActivity extends AppCompatActivity {

    private TextView tv_foundID;
    private Button btn_move;
    private Toolbar tb_foundID;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_id);

        init();

        Intent intent = getIntent();
        tv_foundID.setText(intent.getStringExtra("email"));

        btn_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startMain = new Intent(FoundIDActivity.this, LoginActivity.class);
                startActivity(startMain);
                finish();
            }
        });

        // Toolbar 활성화
        setSupportActionBar(tb_foundID);
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
        tb_foundID = findViewById(R.id.tb_foundID);
        tv_foundID = findViewById(R.id.tv_foundID);
        btn_move = findViewById(R.id.btn_move);
    }
}
