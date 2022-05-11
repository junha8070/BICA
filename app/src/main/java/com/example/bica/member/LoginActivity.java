package com.example.bica.member;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bica.CardDao;
import com.example.bica.CardRoomDB;
import com.example.bica.MainActivity;
import com.example.bica.R;
import com.example.bica.model.Card;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.nio.file.FileStore;

public class LoginActivity extends AppCompatActivity {

    private String TAG = "LoginActivity";

    private Button btn_login;
    private EditText edt_ID, edt_PW;
    private TextView tv_Find_ID, tv_Find_PW;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private MemberViewModel memberViewModel;

    private CardDao mcardDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 요소 초기화
        init();

        CardRoomDB cardRoomDB = Room.databaseBuilder(getApplicationContext(), CardRoomDB.class,"CardRoomDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        mcardDao = cardRoomDB.cardDao();

        memberViewModel = new ViewModelProvider(this).get(MemberViewModel.class);
        memberViewModel.getUserMutableLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                firestore.collection("users")
                        .document(firebaseAuth.getCurrentUser().getUid())
                        .collection("myCard")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    Card saveCard = new Card();
                                    for(QueryDocumentSnapshot document : task.getResult()){
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        saveCard.setName(document.get("name").toString());
                                        saveCard.setEmail(document.get("email").toString());
                                        saveCard.setPhone(document.get("phone").toString());
                                        saveCard.setCompany(document.get("company").toString());
                                        saveCard.setAddress(document.get("address").toString());
                                        saveCard.setOccupation(document.get("occupation").toString());
                                        saveCard.setDepart(document.get("depart").toString());
                                        saveCard.setPosition(document.get("position").toString());
                                        saveCard.setMemo(document.get("memo").toString());
                                        //saveCard.setImage(document.get("image").toString());
                                        mcardDao.setUpdateCard(saveCard);
                                    }
                                }
                            }
                        });

                Intent startMain = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(startMain);

                finish();
            }
        });



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

        firebaseAuth = FirebaseAuth.getInstance();  // 파이어베이스 auth 인스턴스 생성
        firestore = FirebaseFirestore.getInstance();

    }
}