package com.example.pc_user.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.pc_user.R;
import com.example.pc_user.adapter.vpg.OnBoardingAdapter;
import com.example.pc_user.functions.transform_viewpager.DepthPageTransformer;

import me.relex.circleindicator.CircleIndicator3;

public class OnBoardingActivity extends AppCompatActivity {
    private TextView textSkip;
    private Button buttonGetStarted;
    private ImageView imageNext;
    private ViewPager2 layoutContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        _init();
        _setOnclick();
    }

    private void _init() {
        textSkip = findViewById(R.id.textSkip);
        CircleIndicator3 circleIndicator = findViewById(R.id.circleIndicator3);
        buttonGetStarted = findViewById(R.id.buttonGetStarted);
        imageNext = findViewById(R.id.imageNext);
        layoutContainer = findViewById(R.id.layoutContainer);

        OnBoardingAdapter adapter = new OnBoardingAdapter(OnBoardingActivity.this);
        layoutContainer.setAdapter(adapter);
        circleIndicator.setViewPager(layoutContainer);
        layoutContainer.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });
        layoutContainer.setPageTransformer(new DepthPageTransformer());
    }

    private void _setOnclick() {
        buttonGetStarted.setOnClickListener(view -> _handleGetStarted());
        imageNext.setOnClickListener(v -> _nextViewPager());
        textSkip.setOnClickListener(v -> _skipViewPager());
    }

    private void _handleGetStarted() {
        startActivity(new Intent(OnBoardingActivity.this, LoginContainerActivity.class));
    }

    private void _nextViewPager() {
        int currentIndex = layoutContainer.getCurrentItem();

        if (currentIndex < 2) {
            layoutContainer.setCurrentItem(currentIndex + 1);
        } else {
            currentIndex = 0;
            layoutContainer.setCurrentItem(currentIndex);
        }
    }

    private void _skipViewPager() {
        layoutContainer.setCurrentItem(2);
    }
}