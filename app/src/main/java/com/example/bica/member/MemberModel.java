package com.example.bica.member;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.bica.CardDao;
import com.example.bica.CardRoomDB;
import com.example.bica.model.Card;
import com.example.bica.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MemberModel {

    private String TAG = "MemberModel";

    private CardDao mcardDao;
    private Application application;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private MutableLiveData<FirebaseUser> userMutableLiveData;
    private MutableLiveData<Boolean> isSuccessful;
    private MutableLiveData<Boolean> logoutMutableLiveData;
    private MutableLiveData<Boolean> saveUserInfoMutableLiveData;
    private MutableLiveData<FirebaseFirestore> userInfoMutableLiveData;
    private MutableLiveData<FirebaseFirestore> cardMutableLiveData;

    public MemberModel(Application application) {
        this.application = application;

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userMutableLiveData = new MutableLiveData<>();
        logoutMutableLiveData = new MutableLiveData<>();
        saveUserInfoMutableLiveData = new MutableLiveData<>();
        isSuccessful = new MutableLiveData<>();
        cardMutableLiveData = new MutableLiveData<>();

        CardRoomDB cardRoomDB = Room.databaseBuilder(application.getApplicationContext(), CardRoomDB.class,"CardRoomDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        mcardDao = cardRoomDB.cardDao();

        if (firebaseAuth.getCurrentUser() != null) {
            userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
            logoutMutableLiveData.postValue(false);
        }
    }

    public void register(String email, String password, User userAccount) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userMutableLiveData.postValue(firebaseAuth.getCurrentUser());   // 현재 로그인된 사용자 정보 저장
                            firestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).set(userAccount).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        saveUserInfoMutableLiveData.postValue(true);
                                        Log.d(TAG, "회원정보 저장 성공");
                                    } else {
                                        saveUserInfoMutableLiveData.postValue(false);
                                        Log.w(TAG, "회원정보 저장 오류");
                                    }
                                }
                            });
                        } else {
                            saveUserInfoMutableLiveData.postValue(false);
                            Toast.makeText(application, "회원가입 오류" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void registerCard(Card cardAccount) {
        firestore.collection("users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("myCard")
                .add(cardAccount)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Card cardRoom = new Card();
                        cardRoom.setEmail(firebaseAuth.getCurrentUser().getEmail());
                        mcardDao.setInsertCard(cardAccount);
                        System.out.println("카드 등록 완료");

                    }
                });
    }

    public void login(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
                            firestore.collection("users")
                                    .document(firebaseAuth.getCurrentUser().getUid())
                                    .collection("myCard")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()){
                                                Card saveCard = new Card();
                                                for(QueryDocumentSnapshot document : task.getResult()){
                                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                                    saveCard.setName(document.get("name").toString());
                                                    saveCard.setEmail(document.get("email").toString());
                                                    saveCard.setPhone(document.get("phone").toString());
                                                    saveCard.setCompany(document.get("company").toString());
                                                    saveCard.setAddress(document.get("address").toString());
                                                    saveCard.setOccupation(document.get("occupation").toString());
                                                    saveCard.setDepart(document.get("depart").toString());
                                                    saveCard.setPosition(document.get("position").toString());
                                                    saveCard.setMemo(document.get("memo").toString());
                                                    //saveCard.setImage(document.get("image").toString());
                                                    mcardDao.setInsertCard(saveCard);
                                                }
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(application, "로그인 오류" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void logout() {
        firebaseAuth.signOut();
        logoutMutableLiveData.postValue(true);
    }

    public void saveUserInfo(String uid, User userAccount) {

    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public MutableLiveData<Boolean> getLogoutMutableLiveData() {
        return logoutMutableLiveData;
    }

    public MutableLiveData<Boolean> getSaveUserInfoMutableLiveData() {
        return saveUserInfoMutableLiveData;
    }

    public MutableLiveData<FirebaseFirestore> getUserInfoMutableLiveData() {
        return userInfoMutableLiveData;
    }

    public MutableLiveData<Boolean> getIsSuccessful() {
        return isSuccessful;
    }

    public MutableLiveData<FirebaseFirestore> getCardMutableLiveData() {
        return cardMutableLiveData;
    }
}
