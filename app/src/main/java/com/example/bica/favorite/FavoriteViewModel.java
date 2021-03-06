package com.example.bica.favorite;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.bica.model.Card;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class FavoriteViewModel extends AndroidViewModel {
    private FavoriteModel favoriteModel;
    private FirebaseAuth auth;

    private MutableLiveData<ArrayList<Card>> cardMutableLiveData;

    public FavoriteViewModel(@NonNull Application application) {
        super(application);

        favoriteModel = new FavoriteModel(application);
        cardMutableLiveData = favoriteModel.getCardMutableLiveData();
    }

    public void cardInfo(String email) {
        favoriteModel.cardInfo(email);
    }

    public MutableLiveData<ArrayList<Card>> getCardMutableLiveData() {
        return cardMutableLiveData;
    }
}
