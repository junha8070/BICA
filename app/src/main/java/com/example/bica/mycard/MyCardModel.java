package com.example.bica.mycard;

import static android.content.ContentValues.TAG;


import android.app.AlertDialog;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.bica.R;
import com.example.bica.model.Card;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;

public class MyCardModel {
    private String TAG = "MyCardModel";

    private Application application;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private MutableLiveData<ArrayList<Card>> userInfo;
    private ArrayList<Card> arrCard = new ArrayList<>();
    private MutableLiveData<String> cardId;
    private MutableLiveData<Card> updateInfo;
    private String Pnum;

    private MyCardFragment mycardfragment;

    private Card card;

    public MyCardModel(Application application) {
        this.application = application;

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userInfo = new MutableLiveData<>();
        cardId = new MutableLiveData<>();
        updateInfo = new MutableLiveData<>();

    }

    public void userInfo() {
        db.collection("users")
                .document(auth.getCurrentUser()
                        .getUid())
                .collection("myCard")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    card = dc.getDocument().toObject(Card.class);
                                    Log.d(TAG, "Add city: " + card.getEmail());
                                    arrCard.add(card);
                                    userInfo.postValue(arrCard);
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
                    }
                });
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
////                for(QueryDocumentSnapshot querySnapshot : task.getResult()){
////                    Card card = querySnapshot.toObject(Card.class);
////                    arrCard.add(card);
////                    userInfo.postValue(arrCard);
////                    cardId.postValue(querySnapshot.getId());
////                    Pnum=card.getPhone();
////                }
//            }
//        });
    }

    public void chageInfo(Card prevCard, Card newCard) {
        Log.d(TAG, cardId.getValue());
        DocumentReference sfDocRef = db.collection("users")
                .document(auth.getCurrentUser().getUid())
                .collection("myCard")
                .document(cardId.getValue());

        Log.d(TAG, prevCard.getName() + "||" + newCard.getName());

        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                if (!prevCard.getName().equals(newCard.getName())) {
                    transaction.update(sfDocRef, "name", newCard.getName());
                }

                if (!prevCard.getPosition().equals(newCard.getPosition())) {
                    transaction.update(sfDocRef, "position", newCard.getPosition());
                }
                if (!prevCard.getOccupation().equals(newCard.getOccupation())) {
                    transaction.update(sfDocRef, "occupation", newCard.getOccupation());
                }
                if (!prevCard.getDepart().equals(newCard.getDepart())) {
                    transaction.update(sfDocRef, "depart", newCard.getDepart());
                }
                if (!prevCard.getCompany().equals(newCard.getCompany())) {
                    transaction.update(sfDocRef, "company", newCard.getCompany());
                }
//                if (!prevCard.getGroupname().equals(newCard.getGroupname())) {
//                    transaction.update(sfDocRef, "groupname", newCard.getGroupname());
//                }
                if (!prevCard.getPhone().equals(newCard.getPhone())) {
                    transaction.update(sfDocRef, "phone", newCard.getPhone());
                }
                if (!prevCard.getEmail().equals(newCard.getEmail())) {
                    transaction.update(sfDocRef, "email", newCard.getEmail());
                }


                transaction.update(sfDocRef, "address", newCard.getAddress());


                if (!prevCard.getMemo().equals(newCard.getMemo())) {
                    transaction.update(sfDocRef, "memo", newCard.getMemo());
                }

                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Transaction success!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Transaction failure.", e);
            }
        });
    }


    public MutableLiveData<ArrayList<Card>> getUserInfo() {
        return userInfo;
    }

    public MutableLiveData<String> getCardId() {
        return cardId;
    }

    public MutableLiveData<Card> getUpdateInfo() {
        return updateInfo;
    }


}
