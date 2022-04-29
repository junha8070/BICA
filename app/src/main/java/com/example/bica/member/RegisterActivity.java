package com.example.bica.member;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bica.MainActivity;
import com.example.bica.R;
import com.example.bica.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    Button btn_next, btn_check;
    EditText edt_username, edt_useremail, edt_pw, edt_pw_check, edt_phonenum;
    private static final String TAG = "RegisterActivity";
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private MemberViewModel memberViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        memberViewModel = new ViewModelProvider(this).get(MemberViewModel.class);

        init();
        edt_phonenum.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        //Todo: 한 전화번호당 한 아이디 제한걸기

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //가입 정보 가져오기
                final String email = edt_useremail.getText().toString().trim();
                String pwd = edt_pw.getText().toString().trim();
                String pwdcheck = edt_pw_check.getText().toString().trim();
                String username = edt_username.getText().toString().trim();
                String phonenum = edt_phonenum.getText().toString().trim();

                if (email.isEmpty() || pwd.isEmpty() || pwdcheck.isEmpty() || username.isEmpty() || phonenum.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "빈 칸을 채워주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                User userAccount = new User();
                userAccount.setEmail(email);
                userAccount.setUsername(username);
                userAccount.setPhonenum(phonenum);

                final ProgressDialog mDialog = new ProgressDialog(RegisterActivity.this);

                memberViewModel.register(email, pwd, userAccount);
                mDialog.setMessage("가입중입니다");
                mDialog.show();
                memberViewModel.getSaveUserInfoMutableLiveData().observe(RegisterActivity.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if (aBoolean) {
                            mDialog.dismiss();
                            Intent intent = new Intent(RegisterActivity.this, RegisterCardActivity.class);
                            startActivity(intent);
                        }else{
                            mDialog.dismiss();
                        }
                    }
                });


//                //가입 정보 가져오기
//                final String email = edt_useremail.getText().toString().trim();
//                String pwd = edt_pw.getText().toString().trim();
//                String pwdcheck = edt_pw_check.getText().toString().trim();
//                String username = edt_username.getText().toString().trim();
//                String phonenum = edt_phonenum.getText().toString().trim();
//
//                if(username.isEmpty()==false && email.isEmpty()==false && pwd.isEmpty()==false && pwdcheck.isEmpty()==false && phonenum.isEmpty()==false){
//                    if(pwd.equals(pwdcheck)){
//                        Log.d(TAG, "등록 버튼 " + email + " , " + pwd);
//                        final ProgressDialog mDialog = new ProgressDialog(RegisterActivity.this);
//                        mDialog.setMessage("가입중입니다");
//                        mDialog.show();
//
//                        //파이어베이스에 신규계정 등록하기
//                        firebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                //가입 성공시
//                                if(task.isSuccessful()){
//                                    mDialog.dismiss();
//
//                                    FirebaseUser user = firebaseAuth.getCurrentUser();
//                                    String email = user.getEmail();
//                                    String uid = user.getUid();
//                                    String name = edt_username.getText().toString().trim();
//                                    String phonenum = edt_phonenum.getText().toString().trim();
//
//                                    //해쉬맵 테이블을 파이어베이스 데이터베이스에 저장
//                                    HashMap<Object, String> hashMap = new HashMap<>();
//
//                                    hashMap.put("uid",uid);
//                                    hashMap.put("email", email.replace(".", ">"));
//                                    hashMap.put("name", name);
//                                    hashMap.put("phonenum", phonenum);
//
//                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
//                                    DatabaseReference reference = database.getReference("Users");
//                                    reference.child(uid).setValue(hashMap);
//
//                                    User userAccount = new User();
//                                    userAccount.setEmail(email);
//                                    userAccount.setUsername(name);
//                                    userAccount.setPhonenum(phonenum);
//
//                                    Map<Object, String> users = new HashMap<>();
//                                    users.put("email", email);
//                                    users.put("name", name);
//                                    users.put("phonenum", phonenum);
//
//                                    firestore.collection("users").document(uid)
//                                            .set(users)
//                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                @Override
//                                                public void onSuccess(Void unused) {
//                                                    Log.d(TAG, "성공적으로 입력되었습니다");
//                                                }
//                                            })
//                                            .addOnFailureListener(new OnFailureListener() {
//                                                @Override
//                                                public void onFailure(@NonNull Exception e) {
//                                                    Log.w(TAG, "오류가 발생하였습니다", e);
//                                                }
//                                            });
//
//                                    //가입이 이루어졌을시 가입 화면을 빠져나감.
//                                    Intent startMain = new Intent(RegisterActivity.this, RegisterCardActivity.class);
//                                    startActivity(startMain);
//                                    finish();
//                                    Toast.makeText(RegisterActivity.this, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
//
//                                }
//                                //가입 실패
//                                else{
//                                    mDialog.dismiss();
//                                    Toast.makeText(RegisterActivity.this, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                            }
//                        });
//                    }
//                    //비밀번호 오류시
//                    else{
//                        Toast.makeText(RegisterActivity.this, "비밀번호가 틀렸습니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                }
//                //빈칸이 있을시
//                else{
//                    Toast.makeText(RegisterActivity.this, "빈칸이 존재합니다. 입력해주세요.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
            }
        });

    }

    public void init() {
        edt_username = findViewById(R.id.user_name);
        edt_useremail = findViewById(R.id.user_email);
        edt_pw = findViewById(R.id.user_pw);
        edt_pw_check = findViewById(R.id.user_pw_check);
        edt_phonenum = findViewById(R.id.user_phonenum);
        btn_next = findViewById(R.id.next_regist2);
        btn_check = findViewById(R.id.email_check);
    }
}