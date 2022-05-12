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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bica.CardRepository;
import com.example.bica.member.MemberModel;
import com.example.bica.model.Card;

import java.util.ArrayList;
import java.util.List;

public class MyCardViewModel extends AndroidViewModel {

    private MyCardModel myCardModel;
    private MutableLiveData<ArrayList<Card>> userInfo;
    private MutableLiveData<String> cardId;
    private MutableLiveData<Card> updateInfo;
    private MutableLiveData<ArrayList<Card>> delInfo;
    private CardRepository cardRepository;
    private LiveData<List<Card>> allCards;


    public MyCardViewModel(@NonNull Application application){
        super(application);
        cardRepository = new CardRepository(application);
        myCardModel = new MyCardModel(application);
        userInfo = myCardModel.getUserInfo();
        cardId = myCardModel.getCardId();
        updateInfo= myCardModel.getUpdateInfo();
        delInfo=myCardModel.getDelInfo();
        allCards = cardRepository.getAllCards();
    }

    public void userInfo(){
        myCardModel.userInfo();
        myCardModel.getCardId();
    }

    public void changeInfo(Card prevCard, Card newCard){
        myCardModel.chageInfo(prevCard, newCard);
    }

    public void delInfo(Card prevCard){
        myCardModel.delInfo(prevCard);
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

    public LiveData<List<Card>>getAllCards(){
        return allCards;
    }
}
