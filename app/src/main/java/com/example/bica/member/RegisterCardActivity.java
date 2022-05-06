package com.example.bica.member;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.example.bica.CardDao;
import com.example.bica.CardRoomDB;
import com.example.bica.MainActivity;
import com.example.bica.R;
import com.example.bica.model.Card;
import com.example.bica.mycard.SearchAddressActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class RegisterCardActivity extends AppCompatActivity {

    private static final String TAG = "RegisterCardActivity";
    private CardDao mcardDao;
    Button btn_complete, btn_later;
    ImageView img_face;
    TextView tv_title;
    EditText edt_username, edt_useremail, edt_phonenum, edt_companyname, edt_companyadr, edt_occupation, edt_teamname, edt_position, edt_memo;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private MemberViewModel memberViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_card);
        init();
        CardRoomDB cardRoomDB = Room.databaseBuilder(getApplicationContext(), CardRoomDB.class,"CardRoomDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        mcardDao = cardRoomDB.cardDao();

        edt_companyadr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //주소 검색 웹뷰 화면으로 이동
                Intent intent=new Intent(RegisterCardActivity.this, SearchAddressActivity.class);
                getSearchResult.launch(intent);
            }
        });

        edt_phonenum.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                memberViewModel = new ViewModelProvider(RegisterCardActivity.this).get(MemberViewModel.class);
                //ToDo: 이미지 추가
                String name = edt_username.getText().toString().trim();
                String email = edt_useremail.getText().toString().trim();
                String phone = edt_phonenum.getText().toString().trim();
                String company = edt_companyname.getText().toString().trim();
                String address = edt_companyadr.getText().toString().trim();
                String occupation = edt_occupation.getText().toString().trim();
                String depart = edt_teamname.getText().toString().trim();
                String position = edt_position.getText().toString().trim();
                String memo = edt_memo.getText().toString().trim();

                ProgressDialog mDialog = null;
                if (name.isEmpty() == false && email.isEmpty() == false && phone.isEmpty() == false && company.isEmpty() == false && address.isEmpty() == false && occupation.isEmpty() == false && depart.isEmpty() == false && position.isEmpty() == false) {
                    Log.d(TAG, "완료 버튼");
                    mDialog = new ProgressDialog(RegisterCardActivity.this);
                    mDialog.setMessage("명함입력중입니다.");
                    mDialog.show();

                    if (position.isEmpty()) {
                        position = "";
                    }
                    if (memo.isEmpty()) {
                        memo = "";
                    }

                    Card cardAccount = new Card();
                    cardAccount.setName(name);
                    cardAccount.setEmail(email);
                    cardAccount.setPhone(phone);
                    cardAccount.setCompany(company);
                    cardAccount.setAddress(address);
                    cardAccount.setOccupation(occupation);
                    cardAccount.setDepart(depart);
                    cardAccount.setPosition(position);
                    cardAccount.setMemo(memo);

                    memberViewModel.registerCard(cardAccount);
                    mDialog.dismiss();

                    Intent startMain = new Intent(RegisterCardActivity.this, MainActivity.class);
                    startActivity(startMain);
                    finish();
                }
                //필수정보가 부족할 때
                else {
                    mDialog.dismiss();
                    Toast.makeText(RegisterCardActivity.this, "필수 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

//                String name = edt_username.getText().toString().trim();
//                String email = edt_useremail.getText().toString().trim();
//                String phone = edt_phonenum.getText().toString().trim();
//                String company = edt_companyname.getText().toString().trim();
//                String address = edt_companyadr.getText().toString().trim();
//                String occupation = edt_occupation.getText().toString().trim();
//                String depart = edt_teamname.getText().toString().trim();
//                String position = edt_position.getText().toString().trim();
//                String groupname = edt_groupname.getText().toString().trim();
//                String memo = edt_memo.getText().toString().trim();
//
//                ProgressDialog mDialog = null;
//                if (name.isEmpty() == false && email.isEmpty() == false && phone.isEmpty() == false && company.isEmpty() == false && address.isEmpty() == false && occupation.isEmpty() == false && depart.isEmpty() == false && position.isEmpty() == false) {
//                    Log.d(TAG, "완료 버튼");
//                    mDialog = new ProgressDialog(RegisterCardActivity.this);
//                    mDialog.setMessage("명함입력중입니다.");
//                    mDialog.show();
//
//                    if (position.isEmpty()) {
//                        position = "";
//                    }
//                    if (memo.isEmpty()) {
//                        memo = "";
//                    }
//
//                    Card cardAccount = new Card();
//                    cardAccount.setName(name);
//                    cardAccount.setEmail(email);
//                    cardAccount.setPhone(phone);
//                    cardAccount.setCompany(company);
//                    cardAccount.setAddress(address);
//                    cardAccount.setOccupation(occupation);
//                    cardAccount.setDepart(depart);
//                    cardAccount.setPosition(position);
//                    cardAccount.setGroupname(groupname);
//                    cardAccount.setMemo(memo);
//
//                    //파이어베이스에 신규정보등록
//                    db.collection("cards")
//                            .add(cardAccount)
//                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                @Override
//                                public void onSuccess(DocumentReference documentReference) {
//                                    System.out.println("카드 등록 완료");
//                                }
//                            });
//
//                    //가입이 이루어졌을시 가입 화면을 빠져나감.
//                    mDialog.dismiss();
//                    Intent startMain = new Intent(RegisterCardActivity.this, MainActivity.class);
//                    startActivity(startMain);
//                    finish();
//                    Toast.makeText(RegisterCardActivity.this, "명함 생성을 성공하였습니다.", Toast.LENGTH_SHORT).show();
//
//                }
//                //필수정보가 부족할 때
//                else {
//                    mDialog.dismiss();
//                    Toast.makeText(RegisterCardActivity.this, "필수 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
             }
        });

        btn_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent startMain = new Intent(RegisterCardActivity.this, MainActivity.class);
                startActivity(startMain);
            }
        });
    }

    private final ActivityResultLauncher<Intent> getSearchResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode()==RESULT_OK){
                    if(result.getData()!=null){
                        String data=result.getData().getStringExtra("data");
                        edt_companyadr.setText(data);
                    }
                }
            }
    );

    public void init() {
        btn_complete = findViewById(R.id.btn_complete);
        btn_later = findViewById(R.id.btn_later);
        img_face = findViewById(R.id.previewView);

        tv_title = findViewById(R.id.register_card_title);
        edt_username = findViewById(R.id.user_name);
        edt_useremail = findViewById(R.id.user_email);
        edt_phonenum = findViewById(R.id.user_phonenum);
        edt_companyname = findViewById(R.id.company_name);
        edt_companyadr = findViewById(R.id.company_address);
        edt_occupation = findViewById(R.id.occupation);
        edt_teamname = findViewById(R.id.teamname);
        edt_position = findViewById(R.id.position);
        edt_memo = findViewById(R.id.memo);
    }
}


