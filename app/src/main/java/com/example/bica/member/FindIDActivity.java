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
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
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

public class FindIDActivity extends AppCompatActivity {

    private EditText edt_username, edt_phonenum;
    private Button btn_check;
    private Toolbar tb_findID;
    private static final String TAG = "FindIDActivity";
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public String foundId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);

        init();

//        edt_phonenum.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                String name = edt_username.getText().toString().trim();
                String phonenum = edt_phonenum.getText().toString().trim();

                if(name.isEmpty()||phonenum.isEmpty()){
                    Toast.makeText(FindIDActivity.this, "??? ?????? ???????????????", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{

                    firestore.collection("users")
                            .whereEqualTo("username", name)
                            .whereEqualTo("phonenum", phonenum)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        System.out.println("test FindIDActivity");
                                        for(QueryDocumentSnapshot document : task.getResult()){
                                            Log.d(TAG, document.getId() + " => " + document.getData());
                                            foundId = document.get("email").toString();
                                            Log.d(TAG,"?????????"+document.get("email"));
                                            System.out.println("test FindIDActivity ID "+ document.get("email").toString());
                                            System.out.println("test FindIDActivity foundID "+ foundId);

                                        }
                                        if(foundId == null){
                                            Log.d(TAG, "???????????? ??????????????? ????????????.");
                                            Toast.makeText(FindIDActivity.this, "???????????? ??????????????? ????????????.", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        else{
                                            Intent startMain = new Intent(FindIDActivity.this, FoundIDActivity.class);
                                            startMain.putExtra("email", foundId);
                                            startActivity(startMain);
                                            finish();
                                            Toast.makeText(FindIDActivity.this, "????????? ?????? ??????", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                    else{
                                        Log.d(TAG, "???????????? ??????????????? ????????????.");
                                        Toast.makeText(FindIDActivity.this, "???????????? ??????????????? ????????????.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "???????????? ??????????????? ????????????.");
                                    Toast.makeText(FindIDActivity.this, "???????????? ??????????????? ????????????.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            });
                }
            }
        });

        // Toolbar ?????????
        setSupportActionBar(tb_findID);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // ???????????? ?????? ??????
        getSupportActionBar().setTitle(null); // Toolbar ?????? ??????
    }

    // Toolbar ???????????? ?????? ????????? ??????
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar??? back??? ????????? ??? ??????
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void init() {
        tb_findID = findViewById(R.id.tb_findID);
        edt_username = findViewById(R.id.user_name);
        edt_phonenum = findViewById(R.id.user_phonenum);
        btn_check = findViewById(R.id.phonenum_check);
    }
}