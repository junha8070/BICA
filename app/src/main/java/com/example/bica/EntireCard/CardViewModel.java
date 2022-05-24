package com.example.bica.EntireCard;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bica.model.Card;
import com.example.bica.mycard.MyCardModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class  CardViewModel extends AndroidViewModel {
    private CardModel cardModel;
    private MyCardModel myCardModel;
    private LiveData<ArrayList<Card>> cardLiveData;

    private LiveData<Boolean> del_state;

    public CardViewModel(@NonNull Application application) {
        super(application);
        cardModel = new CardModel(application);
        cardLiveData = cardModel.getCardLiveData();
        del_state = cardModel.getDel_state();
    }

    public void entireCard(){
        cardModel.entireCard();
    }

    public void delFavorite(Card cardInfo){ cardModel.delFavorite(cardInfo);
        Log.d("CardViewModel", cardInfo.getEmail()); }

    public void delInfo(Card prevCard){
        cardModel.delInfo(prevCard);
    }

    public LiveData<ArrayList<Card>> getCardLiveData() {
        return cardLiveData;
    }

    public LiveData<Boolean> getDel_state() {
        return del_state;
    }
}
