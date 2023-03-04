package com.example.pc_user.adapter.vpg;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.pc_user.fragments.CustomerInfoFragment;
import com.example.pc_user.fragments.ListProductOrderedFragment;
import com.example.pc_user.fragments.OrderInfoFragment;

public class DetailsOrderAdapter extends FragmentStateAdapter {
    public DetailsOrderAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: {
                return new CustomerInfoFragment();
            } case 1: {
                return new OrderInfoFragment();
            } case 2: {
                return new ListProductOrderedFragment();
            } default: {
                return null;
            }
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
