package com.example.pc_user.adapter.vpg;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.pc_user.fragments.AccountFragment;
import com.example.pc_user.fragments.CartFragment;
import com.example.pc_user.fragments.HomeFragment;
import com.example.pc_user.fragments.OrderFragment;

public class HomeAdapter extends FragmentStateAdapter {
    public HomeAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: {
                return new HomeFragment();
            } case 1: {
                return new CartFragment();
            } case 2: {
                return new OrderFragment();
            } case 3: {
                return new AccountFragment();
            } default: {
                return new HomeFragment();
            }
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
