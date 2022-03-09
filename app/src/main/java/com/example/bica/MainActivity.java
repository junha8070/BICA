package com.example.bica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 mViewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;
    int[] arr = new int[]{R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment AddCard = new AddCardFragment().newInstance();
        Fragment Card = new CardFragment().newInstance();
        Fragment Favorite = new FavoriteFragment().newInstance();
        Fragment MyCard = new MyCardFragment().newInstance();
        Fragment Setting = new SettingFragment().newInstance();
        mViewPager = findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(3);
        tabLayout = findViewById(R.id.tab_layout);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerAdapter.addFrag(Card);
        viewPagerAdapter.addFrag(Favorite);
        viewPagerAdapter.addFrag(AddCard);
        viewPagerAdapter.addFrag(MyCard);
        viewPagerAdapter.addFrag(Setting);
        mViewPager.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(tabLayout, mViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setIcon(arr[position]);
            }
        }).attach();

    }
}