package com.example.bica.EntireCard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bica.ChipData;
import com.example.bica.CardRepository;
import com.example.bica.R;
import com.example.bica.favorite.FavoriteFragment;
import com.example.bica.model.Card;
import com.example.bica.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.example.bica.mycard.MyCardViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.api.Distribution;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CardFragment extends Fragment {

    private String TAG = "CardFragmentTAG";
    private ChipData mChipData;

    private RecyclerView mRecyclerView;
    private CardAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CardRepository cardRepository;

    FragmentManager fragmentManager;

    // UI
    MaterialToolbar toolbar;
    SearchView searchView;
    LinearLayout linear, tv_linear;
    ChipGroup chipGroup;
    Chip chip;
    HorizontalScrollView filter_group_view;

    // ViewModel
    private CardViewModel cardViewModel;
    private MyCardViewModel myCardViewModel;
    private ArrayList<Card> arrCards;

    public ImageView logo_img;
    public EditText find_edt, edt_group;
    public MaterialButton btn_add_group;
    public Spinner spinner_group;
    public TextView edt_tv, tv_group;
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
        myCardViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(MyCardViewModel.class);

        cardViewModel.getCardLiveData().observe(this, new Observer<ArrayList<Card>>() {
            @Override
            public void onChanged(ArrayList<Card> cards) {
                arrCards = cards;
                mAdapter = new CardAdapter(arrCards);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            }
        });

        myCardViewModel.getAllCards().observe(this, new Observer<List<Card>>() {
            @Override
            public void onChanged(List<Card> cards) {
//                Toast.makeText(getContext(), cards.get(0).getName(), Toast.LENGTH_SHORT).show();
                if (cards.size() != 0) {
                    Log.d("CardFragmentTag", cards.get(0).getName());
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);

        init(view);
        fragmentManager = getActivity().getSupportFragmentManager();

        cardViewModel.entireCard();

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.search:
                        if (searchView.getVisibility() == View.GONE) {
                            searchView.setVisibility(View.VISIBLE);
                            filter_group_view.setVisibility(View.GONE);
                            linear.setVisibility(View.GONE);

                        } else {
                            searchView.setQuery("", true);
                            searchView.setVisibility(View.GONE);
                        }
                        break;

                    case R.id.filter:
                        if (filter_group_view.getVisibility() == View.GONE) {
                            filter_group_view.setVisibility(View.VISIBLE);
                            linear.setVisibility(View.VISIBLE);
                            searchView.setVisibility(View.GONE);
                            searchView.setQuery("", true);
                        } else {
                            filter_group_view.setVisibility(View.GONE);
                            linear.setVisibility(View.GONE);
                            cardViewModel.getCardLiveData().observe(getActivity(), new Observer<ArrayList<Card>>() {
                                @Override
                                public void onChanged(ArrayList<Card> cards) {
                                    arrCards = cards;
                                    mAdapter = new CardAdapter(arrCards);
                                    mRecyclerView.setAdapter(mAdapter);
                                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                                }
                            });
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        if (searchView.getVisibility() == View.VISIBLE) {
            System.out.println("test test test ");

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    mAdapter.getFilter().filter(newText);
                    System.out.println("test test test "+newText);

                    return false;
                }
            });
        }


        ArrayList<String> chipArr = new ArrayList<>();
        ArrayList<String> tempArr = new ArrayList<>();

        ChipData chipData = new ChipData();
        chipData.setUserEmail(auth.getCurrentUser().getEmail());

        db.collection("users").document(auth.getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
//                            System.out.println("chip group docu " + document.get("group").toString());

                            if (document.exists()) {
                                List<Object> list = (List<Object>) document.get("group");

                                //Iterate through the list to get the desired values
                                for (Object item : list) {
                                    System.out.println("chip group object item " + item);
                                    System.out.println("chip group object item to String " + item.toString());
                                    chipArr.add(item.toString());
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                        System.out.println("chip group tempArr 0 " + tempArr);
                        System.out.println("chip group chipArr 0 " + chipArr);

                        for (String item : chipArr) {
                            System.out.println("chip data item " + item);
                            System.out.println("chip data test ");
                            Chip chip = new Chip(getContext());
                            chip.setText(item);
                            chip.setCheckable(true);
                            chip.setCloseIconVisible(true);
                            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    chipGroup.removeView(chip);
                                    if(chipArr.contains(chip.getText().toString())){
                                        chipArr.remove(chip.getText().toString());
                                    }
                                    System.out.println("chipArr removed "+ chipArr);
                                    CollectionReference sfColRef = FirebaseFirestore.getInstance().collection("users")
                                            .document(auth.getCurrentUser().getUid()).collection("BusinessCard");
                                    FirebaseFirestore.getInstance().collection("users")
                                            .document(auth.getCurrentUser().getUid()).update("group", chipArr);
                                    sfColRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for(QueryDocumentSnapshot item : queryDocumentSnapshots){
                                                mChipData = item.toObject(ChipData.class);
                                                if(item.getData().get("group")!= null){
                                                    if(item.getData().get("group").toString().contains(chip.getText().toString())){
                                                        System.out.println("remove chip " + item.getId());
                                                        sfColRef.document(item.getId()).update("group", null);
                                                        reset();

                                                    }
                                                }
                                                else{
                                                    System.out.println("remove chip null");

                                                }

                                            }
                                        }
                                    });
                                }
                            });
                            chipGroup.addView(chip);
                            System.out.println("chip data item " + item);
                        }

//                        System.out.println("chip data test " + chipGroup.getChildCount());
//                        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
//                            @Override
//                            public void onCheckedChanged(ChipGroup group, int checkedId) {
//                                Chip chip = chipGroup.findViewById(checkedId);
//                                System.out.println("chip data test 1 "+ chip.getText().toString());
//                                System.out.println("chip data test 2 "+ chip.getId());
//                                System.out.println("chip data test 3 "+ chip.isChecked());
//
//                            }
//                        });
                    }
                });


        btn_add_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // editText의 값 받아와서
                String str_group_name = edt_group.getText().toString();
                if (str_group_name.isEmpty()) {
                    // editText 빈칸일 경우
                    System.out.println("chip editText is empty ");
                } else {
                    // editText 빈칸이 아닐 경우
                    // Firestore의 group(arraylist) 에 포함되지 않거나, tempArr에 포함되지 않은 경우
                    if (!chipArr.contains(str_group_name)) {
                        chipArr.add(str_group_name);
                        edt_group.setText("");
                        chipData.setChip(chipArr);
                        System.out.println("chipArr "+ chipArr);

                        db.collection("users").document(auth.getCurrentUser().getUid()).update("group", chipData.getChip());

                        db.collection("users").document(auth.getCurrentUser().getUid()).get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            System.out.println("chip data in fs 2 " + task.getResult().get("group"));
//                                            for (String item : chipArr) {
//                                                System.out.println("chip data item " + item);
//                                                System.out.println("chip data test ");
//                                                Chip chip = new Chip(getContext());
//                                                chip.setText(item);
//                                                chipGroup.addView(chip);
//                                                System.out.println("chip data item " + item);
//                                            }
                                            Chip chip = new Chip(getContext());
                                            chip.setText(str_group_name);
                                            chip.setCheckable(true);
                                            chip.setCloseIconVisible(true);
                                            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    chipGroup.removeView(chip);
                                                    if(chipArr.contains(chip.getText().toString())){
                                                        chipArr.remove(chip.getText().toString());
                                                    }
                                                    System.out.println("chipArr removed "+ chipArr);
                                                    CollectionReference sfColRef = FirebaseFirestore.getInstance().collection("users")
                                                            .document(auth.getCurrentUser().getUid()).collection("BusinessCard");
                                                    FirebaseFirestore.getInstance().collection("users")
                                                            .document(auth.getCurrentUser().getUid()).update("group", chipArr);
                                                    sfColRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                            for(QueryDocumentSnapshot item : queryDocumentSnapshots){
                                                                mChipData = item.toObject(ChipData.class);
                                                                if(item.getData().get("group")!= null){
                                                                    if(item.getData().get("group").toString().contains(chip.getText().toString())){
                                                                        System.out.println("remove chip " + item.getId());
                                                                        sfColRef.document(item.getId()).update("group", null);
                                                                        reset();

                                                                    }
                                                                }
                                                                else{
                                                                    System.out.println("remove chip null");

                                                                }

                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                            chipGroup.addView(chip);
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(getContext(), "이미 존재하는 그룹명 입니다", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        System.out.println("chip group tempArr 3 " + tempArr);
        System.out.println("chip group chipArr 3 " + chipArr);

        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {

                Chip chip = chipGroup.findViewById(checkedId);

                System.out.println("test group " + checkedId);

                if(checkedId != -1){
                    mAdapter.getCardGroup().filter(chip.getText().toString());
                }
                if(checkedId == -1){
                    System.out.println("test group = " + checkedId);
                    mAdapter.getCardGroup().filter("");
                }
            }



        });


        System.out.println("chip group tempArr 3 " + tempArr);
        System.out.println("chip group chipArr 3 " + chipArr);

        return view;
    }

    private void init(View view) {
        toolbar = view.findViewById(R.id.topAppBar);
        searchView = view.findViewById(R.id.searchView);
        chipGroup = view.findViewById(R.id.chip_group);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.cardFragment_recyclerview);
        linear = (LinearLayout) view.findViewById(R.id.linear);
        edt_group = (EditText) view.findViewById(R.id.edt_group);
        btn_add_group = (MaterialButton) view.findViewById(R.id.btn_add_group);
        tv_group = (TextView) view.findViewById(R.id.tv_group);
        filter_group_view = (HorizontalScrollView) view.findViewById(R.id.filter_group_view);
    }

    public void reset(){
        Intent intent = ((Activity)getContext()).getIntent();
        ((Activity)getContext()).finish(); //현재 액티비티 종료 실시
        ((Activity)getContext()).overridePendingTransition(0, 0); //효과 없애기
        ((Activity)getContext()).startActivity(intent); //현재 액티비티 재실행 실시
        ((Activity)getContext()).overridePendingTransition(0, 0); //효과 없애기
    }

    @Override
    public void onPause() {
        super.onPause();
        arrCards.clear();
//        fragmentManager.beginTransaction()
//                .replace(R.id.cardFragment, CardFragment.class, null)
//                .addToBackStack(null)
//                .commit();
    }
}