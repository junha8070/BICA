package com.example.bica.AddCard;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class QRViewmodel extends AndroidViewModel {

    private QRRepository qrRepository;
    private MutableLiveData<Boolean> SuccessMutableLiveData;

    public QRViewmodel(@NonNull Application application){
        super(application);

        qrRepository = new QRRepository(application);
        SuccessMutableLiveData = qrRepository.getSuccessMutableLiveData();
    }

    public void AddCard(String str_card_info){
        qrRepository.AddCard(str_card_info);
    }

    public MutableLiveData<Boolean> getSuccessMutableLiveData() {
        return SuccessMutableLiveData;
    }
}
