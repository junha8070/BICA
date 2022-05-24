package com.example.bica.EntireCard;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bica.model.Card;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CardModel {

    private String TAG = "CardModelTAG";

    private Application application;

    // Firebase
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    // Values
    private ArrayList<Card> arrCard;
    private MutableLiveData<ArrayList<Card>> cardMutableLiveData;

    // State
    private MutableLiveData<Boolean> del_state;

    public CardModel(Application application) {
        this.application = application;

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        arrCard = new ArrayList<>();
        cardMutableLiveData = new MutableLiveData<>();
        del_state = new MutableLiveData<Boolean>();
    }

    public void delFavorite(Card cardInfo) {
        CollectionReference colRef = firestore.collection("users").document(auth.getCurrentUser().getUid()).collection("FavoriteCard");
        colRef.whereEqualTo("image", cardInfo.getImage()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        colRef.document(task.getResult().getDocuments().get(0).getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(application.getApplicationContext(), "즐겨찾기에서 해제되었습니다.", Toast.LENGTH_SHORT).show();
                                    del_state.postValue(task.isSuccessful());
                                } else {
                                    Log.e(TAG, "삭제과정에서 오류" + task.getException().getMessage());
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    public void entireCard() {
        firestore.collection("users").document(auth.getCurrentUser().getUid())
                .collection("BusinessCard").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.isEmpty()){
                    Toast.makeText(application, "값이 없습니다.", Toast.LENGTH_SHORT).show();
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            Card card = dc.getDocument().toObject(Card.class);
                            arrCard.add(card);
                            Log.d(TAG, "현재 arrCard 사이즈 : "+arrCard.size());
                            cardMutableLiveData.postValue(arrCard);
                            break;

                        case MODIFIED:
//                            arrCard.remove(dc.getOldIndex());
//                            card = dc.getDocument().toObject(Card.class);
//                            arrCard.add(card);
                            break;

                        case REMOVED:
                            arrCard.remove(dc.getOldIndex());
                            Log.d(TAG, "현재 arrCard 사이즈 : "+arrCard.size());
                            cardMutableLiveData.postValue(arrCard);
                            break;

                        default:
                            break;
                    }

                }
            }
        });
    }

    public LiveData<ArrayList<Card>> getCardLiveData() {
        return cardMutableLiveData;
    }

    public LiveData<Boolean> getDel_state() {
        return del_state;
    }
}
