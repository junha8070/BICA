package com.example.bica.member;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.bica.model.Card;
import com.example.bica.model.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MemberViewModel extends AndroidViewModel {

    private MemberModel memberModel;
    private MutableLiveData<FirebaseUser> userMutableLiveData;
    private MutableLiveData<Boolean> logoutMutableLiveData;
    private MutableLiveData<Boolean> saveUserInfoMutableLiveData;
    private MutableLiveData<Boolean> isSuccessful;
    private MutableLiveData<FirebaseFirestore> cardMutableLiveData;
    private MutableLiveData<String> cardId;
    private MutableLiveData<Card> updateCard;

    public MemberViewModel(@NonNull Application application){
        super(application);

        memberModel = new MemberModel(application);
        userMutableLiveData = memberModel.getUserMutableLiveData();
        logoutMutableLiveData = memberModel.getLogoutMutableLiveData();
        saveUserInfoMutableLiveData = memberModel.getSaveUserInfoMutableLiveData();
        isSuccessful = memberModel.getIsSuccessful();
        cardMutableLiveData = memberModel.getCardMutableLiveData();
        cardId = memberModel.getCardId();
        updateCard = memberModel.getUpdateCard();
    }

    public void register(String email, String password, User userAccount){
        memberModel.register(email, password, userAccount);
    }

    public void registerCard(Card cardAccount){
        memberModel.registerCard(cardAccount);
    }

    public void login(String email, String password){
        memberModel.login(email, password);
    }

    public void isEmailExist(String email){
//        memberModel
    }

//    public void changeInfo(Card prevCard, Card newCard){
//        memberModel.chageInfo(prevCard, newCard);
//    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public MutableLiveData<Boolean> getSaveUserInfoMutableLiveData() {
        return saveUserInfoMutableLiveData;
    }

    public MutableLiveData<Boolean> getIsSuccessful() {
        return isSuccessful;
    }

    public MutableLiveData<FirebaseFirestore> getCardMutableLiveData() {
        return cardMutableLiveData;
    }
    public MutableLiveData<Card> getUpdateInfo() {
        return updateCard;
    }

    public MutableLiveData<String> getCardId() {
        return cardId;
    }

}
