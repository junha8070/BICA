package com.example.bica.AddCard;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.bica.model.Card;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class QRRepository {
    private Application application;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    private MutableLiveData<Boolean> SuccessMutableLiveData;

    public QRRepository(Application application) {
        this.application = application;

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    public void AddCard(String str_card_info){
        String[] info = SeparateInfo(str_card_info);
        Card card = new Card();

        card.setName(info[0]);
        card.setPosition(info[1]);
        card.setOccupation(info[2]);
        card.setDepart(info[3]);
        card.setCompany(info[4]);
        card.setGroupname(info[5]);
        card.setPhone(info[6]);
        card.setEmail(info[7]);
        card.setCompany(info[8]);
        card.setMemo(info[9]);

        firestore.collection("business")
                .document(auth.getCurrentUser().getUid())
                .collection("cards").document(info[10]).set(card).addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(application, "Upload Success", Toast.LENGTH_SHORT).show();
                    SuccessMutableLiveData.postValue(true);
                }else{
                    Toast.makeText(application, "Upload Failed : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public String[] SeparateInfo(String str_card_info){
        String[] array = str_card_info.split("///");
        return array;
    }

    public MutableLiveData<Boolean> getSuccessMutableLiveData() {
        return SuccessMutableLiveData;
    }
}
