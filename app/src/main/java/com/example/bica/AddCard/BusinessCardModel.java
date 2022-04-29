package com.example.bica.AddCard;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.bica.model.Card;
import com.example.bica.mycard.MyCardFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

public class BusinessCardModel {

    private String TAG = "BusinessCardModel";

    private Application application;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private MutableLiveData<Card> userInfo;
    private MutableLiveData<String> cardId;
    private MutableLiveData<Card> updateInfo;
    private String Pnum;

    public BusinessCardModel(Application application) {
        this.application = application;
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userInfo = new MutableLiveData<>();
        cardId = new MutableLiveData<>();
        updateInfo = new MutableLiveData<>();

    }

    public void userInfo() {
        firestore.collection("users")
                .document(auth.getCurrentUser()
                        .getUid())
                .collection("BusinessCard")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot querySnapshot : task.getResult()){
                    Card card = querySnapshot.toObject(Card.class);
                    userInfo.postValue(card);
                    cardId.postValue(querySnapshot.getId());
                    Pnum=card.getPhone();
                }
            }
        });
    }

    public void addBusinessCard(Card cardAccount) {
        firestore.collection("users")
                .document(auth.getCurrentUser().getUid())
                .collection("BusinessCard")
                .add(cardAccount)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        System.out.println("카드 등록 완료");
                    }
                });
    }


    public void chageInfo(Card prevCard, Card newCard){
        Log.d(TAG, cardId.getValue());
        DocumentReference sfDocRef= firestore.collection("users")
                .document(auth.getCurrentUser().getUid())
                .collection("BusinessCard")
                .document(cardId.getValue());

        Log.d(TAG, prevCard.getName()+"||"+newCard.getName());

        firestore.runTransaction(new Transaction.Function<Void>() {
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




    public MutableLiveData<Card> getUserInfo() {
        return userInfo;
    }

    public MutableLiveData<String> getCardId() {
        return cardId;
    }

    public MutableLiveData<Card> getUpdateInfo() {
        return updateInfo;
    }



}
