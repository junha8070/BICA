package com.example.bica.EntireCard;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bica.R;
import com.example.bica.model.Card;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Locale;

public class CardFragment extends Fragment {

    private String TAG = "CardFragmentTAG";

    private RecyclerView mRecyclerView;
    private CardAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    // UI
    MaterialToolbar toolbar;
    SearchView searchView;
    ChipGroup chipGroup;

    // ViewModel
    private CardViewModel cardViewModel;
    private ArrayList<Card> arrCards;

    public ImageView logo_img;
    public EditText find_edt;
    public Spinner spinner_group;
    public TextView edt_tv;
    ArrayList<String> arrayList;
    private ArrayList<Card> dataList = new ArrayList<>();

    public CardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        arrCards = new ArrayList<>();

        cardViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(CardViewModel.class);

        cardViewModel.getCardLiveData().observe(this, new Observer<ArrayList<Card>>() {
            @Override
            public void onChanged(ArrayList<Card> cards) {
                arrCards = cards;
                mAdapter = new CardAdapter(arrCards);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);

        init(view);

        cardViewModel.entireCard();

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.search:
                        if(searchView.getVisibility()==View.GONE){
                            searchView.setVisibility(View.VISIBLE);
                        }else{
                            searchView.setQuery("",true);
                            searchView.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.filter:
                        if(chipGroup.getVisibility()==View.GONE){
                            chipGroup.setVisibility(View.VISIBLE);
                        }else{
                            chipGroup.setVisibility(View.GONE);
                        }
                        break;
                    default:
                        break;
                }

                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return view;
    }

    private void init(View view){
        toolbar = view.findViewById(R.id.topAppBar);
        searchView = view.findViewById(R.id.searchView);
        chipGroup = view.findViewById(R.id.filter_group);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.cardFragment_recyclerview);
    }
    @Override
    public void onPause() {
        super.onPause();
        arrCards.clear();
    }
}