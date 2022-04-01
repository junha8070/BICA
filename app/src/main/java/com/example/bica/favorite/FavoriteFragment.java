package com.example.bica.favorite;

import android.animation.ArgbEvaluator;
import android.os.Bundle;

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

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {

    ViewPager2 viewPager2;
    FavoriteAdapter adapter;
    List<Card> models;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private ViewModelProvider.AndroidViewModelFactory viewModelFactory;
    private FavoriteViewModel favoriteViewModel;
    private Card card;

    public FavoriteFragment() {
    }

    public static FavoriteFragment newInstance() {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        favoriteViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(FavoriteViewModel.class);
        favoriteViewModel.getCardMutableLiveData().observe(this, new Observer<DocumentSnapshot>() {
            @Override
            public void onChanged(DocumentSnapshot documentSnapshot) {
                models = new ArrayList<>();
                card = documentSnapshot.toObject(Card.class);
                Log.d("Favorite",card.getCompany());

                models.add(card);

                adapter = new FavoriteAdapter(models, viewPager2);
                viewPager2.setAdapter(adapter);
//                Toast.makeText(getContext(), card.getEmail(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        viewPager2 = view.findViewById(R.id.vp_favorite);


        favoriteViewModel.cardInfo(firebaseAuth.getCurrentUser().getEmail());

//        models.add(card);

//        models.add(new Card(R.drawable.bica, "강남대학교", "EASY", "황준하", "개발자", "010-8070-1071", "junha5021301@kakao.com", "경기도 용인시 기흥구 구갈동", "",""));
//        models.add(new Card(R.drawable.bica, "강남대학교", "WWW", "황진영", "개발자", "010-2850-3725", "510cara@naver.com", "경기도 용인시 기흥구 구갈동","", ""));
//        models.add(new Card(R.drawable.bica, "강남대학교", "KIS", "강보현", "개발자", "010-4101-5869", "bhyn9785@naver.com", "경기도 안양시 범계역 2번출구","", ""));
//        models.add(new Card(R.drawable.bica, "강남대학교", "WWW", "김다은", "개발자", "010-6607-7935", "ekqhddl@naver.com", "인천광역시 미추홀구","", ""));






        return view;
    }
}