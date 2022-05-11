package com.example.bica.favorite;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.bica.model.Card;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FavoriteModel {
    private String TAG = "FavoriteModel";

    private Application application;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private ArrayList<Card> arrCard;
    private MutableLiveData<ArrayList<Card>> cardMutableLiveData;
    private Card card;

    public FavoriteModel(Application application){
        this.application = application;

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        arrCard = new ArrayList<>();
        cardMutableLiveData = new MutableLiveData<>();
    }

    public void cardInfo(String email) {
        Log.d(TAG,"확인");
        firestore.collection("users")
                .document(auth.getCurrentUser().getUid())
                .collection("myCard")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for(DocumentChange dc : value.getDocumentChanges()){
                            switch (dc.getType()){
                                case ADDED:
                                    card = dc.getDocument().toObject(Card.class);
                                    arrCard.add(card);
                                    break;
                                case MODIFIED:
                                    arrCard.remove(dc.getOldIndex());
                                    card = dc.getDocument().toObject(Card.class);
                                    arrCard.add(card);
                                    Log.d(TAG, "Modified city: " + dc.getOldIndex());
                                    break;
                                case REMOVED:
                                    Log.d(TAG, "Removed city: " + dc.getDocument().getData());
                                    break;
                            }
                        }
//                        Log.d(TAG, String.valueOf(arrCard.get(0).getAddress()));
//                        Log.d(TAG, String.valueOf(arrCard.get(1).getAddress()));
                        cardMutableLiveData.postValue(arrCard);
                    }
                });
    }

    public MutableLiveData<ArrayList<Card>> getCardMutableLiveData() {
        return cardMutableLiveData;
    }
}
