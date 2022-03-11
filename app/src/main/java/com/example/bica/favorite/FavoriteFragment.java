package com.example.bica.favorite;

import android.animation.ArgbEvaluator;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bica.R;
import com.example.bica.model.Card;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {

    ViewPager2 viewPager2;
    FavoriteAdapter adapter;
    List<Card> models;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        models = new ArrayList<>();
        models.add(new Card(R.drawable.bica, "강남대학교", "EASY", "황준하", "개발자", "010-8070-1071", "junha5021301@kakao.com", "경기도 용인시 기흥구 구갈동"));
        models.add(new Card(R.drawable.bica, "강남대학교", "WWW", "황진영", "개발자", "010-2850-3725", "510cara@naver.com", "경기도 용인시 기흥구 구갈동"));
        models.add(new Card(R.drawable.bica, "강남대학교", "KIS", "강보현", "개발자", "010-4101-5869", "bhyn9785@naver.com", "경기도 안양시 범계역 2번출구"));
        models.add(new Card(R.drawable.bica, "강남대학교", "WWW", "김다은", "개발자", "010-6607-7935", "ekqhddl@naver.com", "인천광역시 미추홀구"));

        adapter = new FavoriteAdapter(models, this.viewPager2);

        viewPager2 = view.findViewById(R.id.vp_favorite);
        viewPager2.setAdapter(adapter);




        return view;
    }
}