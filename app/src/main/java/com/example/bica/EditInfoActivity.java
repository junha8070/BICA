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
                                            Log.d(TAG, "???????????? ?????? ??????");
                                            // ??????????????? ??????????????? ??? ?????????????????? ?????????
                                            finish();
                                            Toast.makeText(EditInfoActivity.this, "???????????? ?????? ??????", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "????????? ?????????????????????", e);
                                    }
                                });

                    }
                    // ??????????????? ???????????? ?????? ???
                    else{
                        Toast.makeText(EditInfoActivity.this, "??????????????? ???????????? ????????????.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                // ????????? ????????? ???
                else{
                    Toast.makeText(EditInfoActivity.this, "????????? ???????????????.", Toast.LENGTH_SHORT).show();
                    return;
                }


            }
        });

        // Toolbar ?????????
        setSupportActionBar(tb_editInfo);
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
        tb_editInfo = findViewById(R.id.tb_editInfo);
        edt_newcheckPW = findViewById(R.id.edt_newcheckPW);
        edt_newPW = findViewById(R.id.edt_newPW);
        btn_newPW = findViewById(R.id.btn_newPW);
    }
}