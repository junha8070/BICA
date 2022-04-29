package com.example.bica.mycard;

import static androidx.camera.core.CameraXThreads.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.nfc.Tag;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.bica.member.MemberModel;
import com.example.bica.model.Card;

public class MyCardViewModel extends AndroidViewModel {

    private MyCardModel myCardModel;
    private MutableLiveData<Card> userInfo;
    private MutableLiveData<String> cardId;
    private MutableLiveData<Card> updateInfo;

    public MyCardViewModel(@NonNull Application application){
        super(application);

        myCardModel = new MyCardModel(application);
        userInfo = myCardModel.getUserInfo();
        cardId = myCardModel.getCardId();
        updateInfo= myCardModel.getUpdateInfo();
    }

    public void userInfo(){
        myCardModel.userInfo();
        myCardModel.getCardId();
    }

    public void changeInfo(Card prevCard, Card newCard){
        myCardModel.chageInfo(prevCard, newCard);
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
