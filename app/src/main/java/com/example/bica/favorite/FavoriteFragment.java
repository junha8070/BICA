package com.example.bica.favorite;

import android.animation.ArgbEvaluator;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bica.EntireCard.CardViewModel;
import com.example.bica.R;
import com.example.bica.model.Card;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {

    String TAG = "FavoriteFragment";

    FragmentManager fragmentManager;

    ViewPager2 viewPager2;
    FavoriteAdapter adapter;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    MaterialCardView default_card;
    RecyclerViewEmptySupport recyclerViewEmptySupport;


    private FavoriteViewModel favoriteViewModel;
    private CardViewModel cardViewModel;
    private ArrayList<Card> arrCards;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        favoriteViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(FavoriteViewModel.class);
        cardViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(CardViewModel.class);

        final Observer<ArrayList<Card>> cardObserver = new Observer<ArrayList<Card>>() {
            @Override
            public void onChanged(ArrayList<Card> cards) {
                if (cards.isEmpty()) {
                    arrCards = cards;
                    viewPager2.setVisibility(View.GONE);
                    recyclerViewEmptySupport = new RecyclerViewEmptySupport(getContext());
                    recyclerViewEmptySupport.setEmptyView(default_card);
                } else {
                    arrCards = new ArrayList<>();
                    viewPager2.setVisibility(View.VISIBLE);
                    default_card.setVisibility(View.GONE);
                    arrCards = cards;
                    adapter = new FavoriteAdapter(arrCards, viewPager2, getActivity().getApplication());
                    viewPager2.setAdapter(adapter);
                }

            }
        };
        favoriteViewModel.getCardMutableLiveData().observe(this, cardObserver);

//        final Observer<Boolean> delObserver = new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean aBoolean) {
//                Log.d(TAG, "삭제현황"+aBoolean);
//            }
//        };
//        cardViewModel.getDel_state().observe(this, delObserver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        viewPager2 = view.findViewById(R.id.vp_favorite);
        default_card = view.findViewById(R.id.default_card);
        favoriteViewModel.cardInfo(firebaseAuth.getCurrentUser().getEmail());
        fragmentManager = getActivity().getSupportFragmentManager();

        return view;
    }


//    @Override
//    public void onPause() {
//        super.onPause();
////        arrCards = new ArrayList<>();
//    }
//
    @Override
    public void onStop() {
        super.onStop();
        arrCards.clear();
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        arrCards.clear();
//    }


//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        if(!arrCards.isEmpty()){
//            arrCards.clear();
//        }
//    }


//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        arrCards.clear();
//    }
}