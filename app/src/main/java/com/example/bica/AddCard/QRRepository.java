package com.example.bica.AddCard;

import android.app.Application;
import android.os.Build;
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
        SuccessMutableLiveData = new MutableLiveData<>();
        SuccessMutableLiveData.setValue(false);
    }

    public void AddCard(String str_card_info){
        String[] info = SeparateInfo(str_card_info);
        Card card = new Card();

        card.setName(info[0]);
        card.setEmail(info[1]);
        card.setCompany(info[2]);
        card.setAddress(info[3]);
        card.setPhone(info[4]);
        card.setOccupation(info[5]);
        card.setDepart(info[6]);
        card.setPosition(info[7]);
        card.setMemo(info[8]);
        card.setImage(info[9]);
        //card.setGroupname(info[5]);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            firestore.collection("users")
                    .document(auth.getCurrentUser().getUid())
                    .collection("BusinessCard")
                    .add(card)
                    .addOnCompleteListener(application.getMainExecutor(), task -> {
                        if(task.isSuccessful()){
                            Toast.makeText(application, "Upload Success", Toast.LENGTH_SHORT).show();
                            SuccessMutableLiveData.postValue(true);
                        }else{
                            Toast.makeText(application, "Upload Failed : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            firestore.collection("users")
                    .document(auth.getCurrentUser().getUid())
                    .collection("BusinessCard")
                    .add(card)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Toast.makeText(application, "Upload Success", Toast.LENGTH_SHORT).show();
                            SuccessMutableLiveData.postValue(true);
                        }else{
                            Toast.makeText(application, "Upload Failed : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }



    }

    public String[] SeparateInfo(String str_card_info){
        String[] array = str_card_info.split("///");
        return array;
    }

    public MutableLiveData<Boolean> getSuccessMutableLiveData() {
        return SuccessMutableLiveData;
    }
}
