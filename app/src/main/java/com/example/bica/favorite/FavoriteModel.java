package com.example.bica.favorite;

import android.app.Application;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FavoriteModel {

    private Application application;
    private FirebaseFirestore firestore;
    private MutableLiveData<DocumentSnapshot> cardMutableLiveData;

    private int image;
    private String company;
    private String depart;
    private String name;
    private String position;
    private String phone;
    private String email;
    private String address;
    private String occupation;
    private String groupname;
    private String memo;

    public FavoriteModel(Application application){
        this.application = application;

        firestore = FirebaseFirestore.getInstance();
        cardMutableLiveData = new MutableLiveData<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void cardInfo(String email){
        firestore.collection("cards").document(email).get().addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    cardMutableLiveData.setValue(task.getResult());
                }
                else {
                    Toast.makeText(application, "Failed: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public MutableLiveData<DocumentSnapshot> getCardMutableLiveData() {
        return cardMutableLiveData;
    }
}
