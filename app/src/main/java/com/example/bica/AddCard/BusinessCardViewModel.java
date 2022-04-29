package com.example.bica.AddCard;

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

public class BusinessCardViewModel extends AndroidViewModel {

    private BusinessCardModel businessCardModel;
    private MutableLiveData<Card> userInfo;
    private MutableLiveData<String> cardId;
    private MutableLiveData<Card> updateInfo;

    public BusinessCardViewModel(@NonNull Application application){
        super(application);

        businessCardModel = new BusinessCardModel(application);
        userInfo = businessCardModel.getUserInfo();
        cardId = businessCardModel.getCardId();
        updateInfo= businessCardModel.getUpdateInfo();
    }

    public void userInfo(){
        businessCardModel.userInfo();
        businessCardModel.getCardId();
    }

    public void changeInfo(Card prevCard, Card newCard){
        businessCardModel.chageInfo(prevCard, newCard);
    }
    public void addBusinessCard(Card cardAccount){
        businessCardModel.addBusinessCard(cardAccount);
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
