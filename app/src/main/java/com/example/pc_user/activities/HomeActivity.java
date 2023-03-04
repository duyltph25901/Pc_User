package com.example.pc_user.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.pc_user.R;
import com.example.pc_user.adapter.vpg.HomeAdapter;
import com.example.pc_user.functions.transform_viewpager.ZoomOutPageTransformer;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 containerLayout;
    private HomeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        _init();
        _setAnimation();
    }

    private void _init() {
        tabLayout = findViewById(R.id.tabLayout);
        containerLayout = findViewById(R.id.layoutContainer);

        adapter = new HomeAdapter(HomeActivity.this);
        containerLayout.setAdapter(adapter);
        containerLayout.setPageTransformer(new ZoomOutPageTransformer());
        new TabLayoutMediator(tabLayout, containerLayout, (tab, position) -> {
            switch (position) {
                case 0: {
                    tab.setText("Trang chủ");
                    break;
                } case 1: {
                    tab.setText("Giỏ hàng");
                    break;
                } case 2: {
                    tab.setText("Đơn hàng");
                    break;
                } case 3: {
                    tab.setText("Tài khoản");
                    break;
                }
            }
        }).attach();
    }

    private void _setAnimation() {
        float v = 0;
        tabLayout.setTranslationY(-300);
        tabLayout.setAlpha(0);
        tabLayout.animate().translationY(v).alpha(1).setDuration(1000).setStartDelay(200).start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        _setAnimation();
    }
}