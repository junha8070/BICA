package com.example.bica.mycard;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.bica.CardDao;
import com.example.bica.CardRoomDB;
import com.example.bica.model.Card;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.Date;

public class MyCardModel {
    private String TAG = "MyCardModelTAG";

    private Application application;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private MutableLiveData<ArrayList<Card>> userInfo;
    private ArrayList<Card> arrCard = new ArrayList<>();
    private MutableLiveData<String> cardId;
    private MutableLiveData<Card> updateInfo;
    private MutableLiveData<ArrayList<Card>> delInfo;

    private MyCardFragment mycardfragment;

    private Card card;
//    private CardDao mcardDao;

    public MyCardModel(Application application) {
        this.application = application;

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userInfo = new MutableLiveData<>();
        cardId = new MutableLiveData<>();
        updateInfo = new MutableLiveData<>();
        delInfo = new MutableLiveData<>();

//        CardRoomDB cardRoomDB = Room.databaseBuilder(application.getApplicationContext(), CardRoomDB.class,"CardRoomDB")
//                .fallbackToDestructiveMigration()
//                .allowMainThreadQueries()
//                .build();
//
//        mcardDao = cardRoomDB.cardDao();

    }
    public void delInfo(Card prevCard){
        CollectionReference sfColRef = db.collection("users")
                .document(auth.getCurrentUser().getUid())
                .collection("myCard");

        sfColRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot snapshot : task.getResult()){
                        card=snapshot.toObject(Card.class);
                        String name=snapshot.get("name").toString();

                        if(snapshot.get("company").equals(prevCard.getCompany())){
                            if(snapshot.get("depart").equals(prevCard.getDepart())){
                                if(snapshot.get("email").equals(prevCard.getEmail())){
                                    if(snapshot.get("memo").equals(prevCard.getMemo())){
                                        if(snapshot.get("name").equals(prevCard.getName())){
                                            if(snapshot.get("occupation").equals(prevCard.getOccupation())){
                                                if(snapshot.get("phone").equals(prevCard.getPhone())){
                                                    if(snapshot.get("position").equals(prevCard.getPosition())){
                                                        String doc=snapshot.getId();
                                                        Log.d(TAG,"삭제");

                                                        Task<Void> sfDocRef = db.collection("users")
                                                                .document(auth.getCurrentUser().getUid())
                                                                .collection("myCard").document(doc).delete()
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        Log.d(TAG, "삭제성공");
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {

                                                                    }
                                                                });
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        });
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
//        Log.d(TAG, cardId.getValue());

        CollectionReference sfColRef = db.collection("users")
                .document(auth.getCurrentUser().getUid())
                .collection("myCard");

        sfColRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot snapshot : task.getResult()){
                        card=snapshot.toObject(Card.class);
                        String name=snapshot.get("name").toString();

                            if(snapshot.get("company").equals(prevCard.getCompany())){
                                if(snapshot.get("depart").equals(prevCard.getDepart())){
                                    if(snapshot.get("email").equals(prevCard.getEmail())){
                                        if(snapshot.get("memo").equals(prevCard.getMemo())){
                                            if(snapshot.get("name").equals(prevCard.getName())){
                                                if(snapshot.get("occupation").equals(prevCard.getOccupation())){
                                                    if(snapshot.get("phone").equals(prevCard.getPhone())){
                                                        if(snapshot.get("position").equals(prevCard.getPosition())){
                                                            String doc=snapshot.getId();

                                                            DocumentReference sfDocRef = db.collection("users")
                                                                    .document(auth.getCurrentUser().getUid())
                                                                    .collection("myCard").document(doc);

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
//                mcardDao.setUpdateCard(newCard);    // TODO: Room DB 업데이트 결과 확인
                                                                    Log.d(TAG, "Room DB Update");
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.w(TAG, "Transaction failure.", e);
                                                                }
                                                            });

                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                       }
                    }
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
    public MutableLiveData<ArrayList<Card>> getDelInfo() {
        return delInfo;
    }

}

