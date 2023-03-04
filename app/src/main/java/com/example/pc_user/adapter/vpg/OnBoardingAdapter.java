package com.example.pc_user.adapter.vpg;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.pc_user.fragments.FirstOnBoardingFragment;
import com.example.pc_user.fragments.SecondOnBoardingFragment;
import com.example.pc_user.fragments.ThridOnBoardingFragment;

public class OnBoardingAdapter extends FragmentStateAdapter {
    public OnBoardingAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new FirstOnBoardingFragment();
            case 1: return new SecondOnBoardingFragment();
            case 2: return new ThridOnBoardingFragment();
            default: return new FirstOnBoardingFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
