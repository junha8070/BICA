package com.example.bica.EntireCard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bica.model.Card;

import java.util.ArrayList;

public class CardViewModel extends AndroidViewModel {
    private CardModel cardModel;

    private LiveData<ArrayList<Card>> cardLiveData;

    public CardViewModel(@NonNull Application application) {
        super(application);
        cardModel = new CardModel(application);
        cardLiveData = cardModel.getCardLiveData();
    }

    public void entireCard(){
        cardModel.entireCard();
    }

    public LiveData<ArrayList<Card>> getCardLiveData() {
        return cardLiveData;
    }
}
