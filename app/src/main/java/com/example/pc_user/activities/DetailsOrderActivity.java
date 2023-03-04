package com.example.pc_user.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.pc_user.MainActivity;
import com.example.pc_user.R;
import com.example.pc_user.adapter.vpg.DetailsOrderAdapter;
import com.example.pc_user.functions.transform_viewpager.ZoomOutPageTransformer;
import com.example.pc_user.model.Cart;
import com.example.pc_user.model.Order;
import com.example.pc_user.model.Product;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class DetailsOrderActivity extends AppCompatActivity {
    private Order mOrder;
    private List<Product> products;

    private TextView txtIdOrder;
    private TabLayout tabLayout;
    private ViewPager2 layoutContainer;

    private DetailsOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_order);

        _init();
        _getKey();
        _getProducts();

        _setData();
    }

    private void _init() {
        products = new ArrayList<>();
        adapter =  new DetailsOrderAdapter(DetailsOrderActivity.this);

        txtIdOrder = findViewById(R.id.textIdOrder);
        tabLayout = findViewById(R.id.tabBill);
        layoutContainer = findViewById(R.id.layoutContainerBill);

        layoutContainer.setAdapter(adapter);
        layoutContainer.setPageTransformer(new ZoomOutPageTransformer());
        new TabLayoutMediator(tabLayout, layoutContainer, (tab, position) -> {
            switch (position) {
                case 0: {
                    tab.setText("Thông tin khách hàng");
                    break;
                } case 1: {
                    tab.setText("Thông tin đơn hàng");
                    break;
                } case 2: {
                    tab.setText("Danh sách sản phẩm");
                    break;
                }
            }
        }).attach();
    }

    private void _getKey() {
        mOrder = (Order) getIntent().getSerializableExtra("KEY_ORDER_ITEM");
    }

    private void _getProducts() {
        for (String idCart : mOrder.getIdCart()) {
            Cart cart = MainActivity.foundCartById(idCart);
            Log.d("CheckCart", (cart == null) + "");
            if (cart == null) return;
            Product product = MainActivity.foundProductById(cart.getIdProduct());
            products.add(product);
        }
    }

    private void _setData() {
        txtIdOrder.setText(mOrder.getId());
    }
}