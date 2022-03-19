package com.example.bica.favorite;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bica.MainActivity;
import com.example.bica.model.Card;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class FavoriteViewModel extends ViewModel {
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    private MutableLiveData<List<Card>> card;

    public LiveData<List<Card>> getCard() {
        if(card == null){
            card = new MutableLiveData<>();
            firebase();

        }
        return card;
    }

    private void loadCard(){
        // Do an asynchronous operation to fetch users.

        firestore.collection("cards").document(auth.getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
//                    card.setValue();
                }
            }
        });
    }

    private void firebase(){
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }
}
