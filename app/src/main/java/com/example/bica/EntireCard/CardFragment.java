package com.example.bica.EntireCard;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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

import com.example.bica.R;
import com.example.bica.model.Card;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

public class CardFragment extends Fragment {

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
                Log.d("CardFragment", "arrCards.get(0).getEmail()");
                Log.d("CardFragment", arrCards.get(0).getEmail());
                mAdapter = new CardAdapter(arrCards);
                mRecyclerView.setAdapter(mAdapter);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // cardFragment_recyclerview
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





//        mRecyclerView = (RecyclerView) view.findViewById(R.id.cardFragment_recyclerview);
//        mLayoutManager = new LinearLayoutManager(getActivity());
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mAdapter = new CardAdapter(dataList);
//        mRecyclerView.setAdapter(mAdapter);
//
//        ItemTouchHelper helper;
//
////        edt_tv = (TextView) view.findViewById(R.id.edt_tv);
////        logo_img = (ImageView) view.findViewById(R.id.logo_img);
////        find_edt = (EditText) view.findViewById(R.id.find_edt);
////        spinner_group = (Spinner) view.findViewById(R.id.spinner_group);
//        arrayList = new ArrayList<>();
//
//        // 내 uid밑의 arrayList를 가져와 arrayList에 담고 spinner에 보이도록
//        arrayList.add("미분류");
//        arrayList.add("test1");
//        arrayList.add("test2");
//        arrayList.add("test3");
//        arrayList.add("test4");
//        arrayList.add("test5");
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, arrayList);
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner_group.setAdapter(arrayAdapter);
//
//        // Create ItemTouchHelperListener Interface
//        helper = new ItemTouchHelper(new ItemTouchHelperCallback(mAdapter));
//        helper.attachToRecyclerView(mRecyclerView);

        // editText에 글자 채워질 때 반응


//        // 편집 글자 눌렀을 때 반응
//        edt_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        return view;
    }

    private void init(View view){
        toolbar = view.findViewById(R.id.topAppBar);
        searchView = view.findViewById(R.id.searchView);
        chipGroup = view.findViewById(R.id.filter_group);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.cardFragment_recyclerview);
    }
}