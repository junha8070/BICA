package com.example.bica.favorite;

import android.animation.ArgbEvaluator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bica.R;
import com.example.bica.model.Card;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {

    String TAG = "FavoriteFragment";

    ViewPager2 viewPager2;
    FavoriteAdapter adapter;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private FavoriteViewModel favoriteViewModel;
    private Card card;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        favoriteViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(FavoriteViewModel.class);

        final Observer<ArrayList<Card>> cardObserver = new Observer<ArrayList<Card>>() {
            @Override
            public void onChanged(ArrayList<Card> cards) {
                adapter = new FavoriteAdapter(cards, viewPager2);
                viewPager2.setAdapter(adapter);
            }
        };
        favoriteViewModel.getCardMutableLiveData().observe(this, cardObserver);

//        favoriteViewModel.getCardMutableLiveData().observe(this, new Observer<ArrayList<Card>>() {
//            @Override
//            public void onChanged(ArrayList<Card> cards) {
//                adapter = new FavoriteAdapter(cards, viewPager2);
//                viewPager2.setAdapter(adapter);
//            }
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        viewPager2 = view.findViewById(R.id.vp_favorite);
        favoriteViewModel.cardInfo(firebaseAuth.getCurrentUser().getEmail());

        return view;
    }
}