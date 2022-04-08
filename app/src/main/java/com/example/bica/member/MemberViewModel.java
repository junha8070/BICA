package com.example.bica.member;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.bica.model.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MemberViewModel extends AndroidViewModel {

    private MemberModel memberModel;
    private MutableLiveData<FirebaseUser> userMutableLiveData;
    private MutableLiveData<Boolean> logoutMutableLiveData;
    private MutableLiveData<Boolean> saveUserInfoMutableLiveData;

    public MemberViewModel(@NonNull Application application){
        super(application);

        memberModel = new MemberModel(application);
        userMutableLiveData = memberModel.getUserMutableLiveData();
        logoutMutableLiveData = memberModel.getLogoutMutableLiveData();
        saveUserInfoMutableLiveData = memberModel.getSaveUserInfoMutableLiveData();
    }

    public void register(String email, String password, User userAccount){
        memberModel.register(email, password, userAccount);
    }

    public void login(String email, String password){
        memberModel.login(email, password);
    }

    public void isEmailExist(String email){
//        memberModel
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public MutableLiveData<Boolean> getSaveUserInfoMutableLiveData() {
        return saveUserInfoMutableLiveData;
    }
}
