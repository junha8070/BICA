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
import com.google.firebase.auth.ActionCodeSettings;
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
//        edt_phonenum.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //?????? ?????? ????????????
                final String email = edt_useremail.getText().toString().trim();
                String pwd = edt_pw.getText().toString().trim();
                String pwdcheck = edt_pw_check.getText().toString().trim();
                String username = edt_username.getText().toString().trim();
                String phonenum = edt_phonenum.getText().toString().trim();

                if (email.isEmpty() || pwd.isEmpty() || pwdcheck.isEmpty() || username.isEmpty() || phonenum.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "??? ?????? ???????????????", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    if(pwd.equals(pwdcheck)){

                        User userAccount = new User();
                        userAccount.setEmail(email);
                        userAccount.setUsername(username);
                        userAccount.setPhonenum(phonenum);

                        final ProgressDialog mDialog = new ProgressDialog(RegisterActivity.this);

                        memberViewModel.register(email, pwd, userAccount);
                        mDialog.setMessage("??????????????????");
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
                                    Toast.makeText(RegisterActivity.this, "?????? ???????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, "??????????????? ???????????????. ?????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }



//                //?????? ?????? ????????????
//                final String email = edt_useremail.getText().toString().trim();
//                String pwd = edt_pw.getText().toString().trim();
//                String pwdcheck = edt_pw_check.getText().toString().trim();
//                String username = edt_username.getText().toString().trim();
//                String phonenum = edt_phonenum.getText().toString().trim();
//
//                if(username.isEmpty()==false && email.isEmpty()==false && pwd.isEmpty()==false && pwdcheck.isEmpty()==false && phonenum.isEmpty()==false){
//                    if(pwd.equals(pwdcheck)){
//                        Log.d(TAG, "?????? ?????? " + email + " , " + pwd);
//                        final ProgressDialog mDialog = new ProgressDialog(RegisterActivity.this);
//                        mDialog.setMessage("??????????????????");
//                        mDialog.show();
//
//                        //????????????????????? ???????????? ????????????
//                        firebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                //?????? ?????????
//                                if(task.isSuccessful()){
//                                    mDialog.dismiss();
//
//                                    FirebaseUser user = firebaseAuth.getCurrentUser();
//                                    String email = user.getEmail();
//                                    String uid = user.getUid();
//                                    String name = edt_username.getText().toString().trim();
//                                    String phonenum = edt_phonenum.getText().toString().trim();
//
//                                    //????????? ???????????? ?????????????????? ????????????????????? ??????
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
//                                                    Log.d(TAG, "??????????????? ?????????????????????");
//                                                }
//                                            })
//                                            .addOnFailureListener(new OnFailureListener() {
//                                                @Override
//                                                public void onFailure(@NonNull Exception e) {
//                                                    Log.w(TAG, "????????? ?????????????????????", e);
//                                                }
//                                            });
//
//                                    //????????? ?????????????????? ?????? ????????? ????????????.
//                                    Intent startMain = new Intent(RegisterActivity.this, RegisterCardActivity.class);
//                                    startActivity(startMain);
//                                    finish();
//                                    Toast.makeText(RegisterActivity.this, "??????????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
//
//                                }
//                                //?????? ??????
//                                else{
//                                    mDialog.dismiss();
//                                    Toast.makeText(RegisterActivity.this, "?????? ???????????? ??????????????????.", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                            }
//                        });
//                    }
//                    //???????????? ?????????
//                    else{
//                        Toast.makeText(RegisterActivity.this, "??????????????? ???????????????. ?????? ??????????????????.", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                }
//                //????????? ?????????
//                else{
//                    Toast.makeText(RegisterActivity.this, "????????? ???????????????. ??????????????????.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
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