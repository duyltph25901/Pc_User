package com.example.pc_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.pc_user.activities.HomeActivity;
import com.example.pc_user.activities.LoginContainerActivity;
import com.example.pc_user.activities.OnBoardingActivity;
import com.example.pc_user.functions.CustomTextColor;
import com.example.pc_user.functions.first_install_app.DataLocalManagement;
import com.example.pc_user.functions.get_const.Const;
import com.example.pc_user.model.Cart;
import com.example.pc_user.model.Order;
import com.example.pc_user.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView txtView;
    private LinearLayout layout;
    private LottieAnimationView lot;
    private Animation topAnim, bottomAnim;

    public static List<Product> products;
    public static List<Cart> carts;
    public static List<Cart> cartsBackend;
    public static List<Order> orders;

    public static void setProducts(List<Product> products) {
        MainActivity.products = products;
    }

    public static void setCarts(List<Cart> carts) {
        MainActivity.carts = carts;
    }

    public static void setOrders(List<Order> orders) {
        MainActivity.orders = orders;
    }

    public static void setCartsBackend(List<Cart> cartsBackend) {
        MainActivity.cartsBackend = cartsBackend;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _init();
        _setAnimation();
        _customUiText();
        _handleAction();
    }

    private void _init() {
        txtView = findViewById(R.id.txtShopName);
        layout = findViewById(R.id.layoutNameShop);
        lot = findViewById(R.id.lottieSplash);

        products = new ArrayList<>();
        orders = new ArrayList<>();
        carts = new ArrayList<>();
        cartsBackend = new ArrayList<>();

        _setDatabase();
    }

    private void _setDatabase() {
        setProducts(getProducts());
        setCarts(getCarts());
        setOrders(getOrders());
        setCartsBackend(getCartsBackend());
    }

    public static void reloadProducts() {
        setProducts(getProducts());
    }

    private void _customUiText() {
        CustomTextColor.customTextColor(txtView,
                ContextCompat.getColor(this, R.color.color_primary),
                ContextCompat.getColor(this, R.color.color_second_primary));
    }

    private void _setAnimation() {
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        lot.setAnimation(topAnim);
        layout.setAnimation(bottomAnim);
    }

    private void _handleAction() {
        if (!DataLocalManagement.getFirstInstall()) {
            DataLocalManagement.setFirstInstall(true);
            _navigationScreen(OnBoardingActivity.class);
        } else {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                _navigationScreen(LoginContainerActivity.class);
            } else {
                _navigationScreen(HomeActivity.class);
            }
        }
    }

    private void _navigationScreen(final Class className) {
        new Handler().postDelayed(() -> {
            startActivity(new Intent(this, className));
            finishAffinity();
        }, 5000);
    }

    public static List<Product> getProducts(String... input) {
        String inputElement = input.length == 0 ? "" : input[0];
        List<Product> products = new ArrayList<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child(Const.productTableName).orderByChild("id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                products.clear();
                if (inputElement.length() == 0 || inputElement.isEmpty() || input == null) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Product product = data.getValue(Product.class);
                        products.add(product);
                    }
                } else {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Product product = data.getValue(Product.class);
                        String productName = product.getName().toLowerCase().trim();
                        if (productName.contains(inputElement)) {
                            products.add(product);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error get products", error.getMessage());
            }
        });

        return products;
    }

    public static List<Cart> getCarts() {
        List<Cart> carts = new ArrayList<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child(Const.cartTableName).orderByChild("id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                carts.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Cart cart = data.getValue(Cart.class);
                    carts.add(cart);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error get carts", error.getMessage());
            }
        });

        return carts;
    }

    public static List<Cart> getCartsBackend() {
        List<Cart> carts = new ArrayList<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child(Const.cartTableName_BackEnd).orderByChild("id")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        carts.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Cart cart = data.getValue(Cart.class);
                            carts.add(cart);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Error get list carts backend", error.getMessage());
                    }
                });

        return carts;
    }

    public static Product foundProductById(String id) {
        for (Product product : products) {
            if (product.getId().matches(id)) {
                return product;
            }
        }

        return null;
    }

    public static List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child(Const.orderTableName).orderByChild("createdAt")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        orders.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Order order = data.getValue(Order.class);
                            orders.add(order);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Error get list order", error.getMessage());
                    }
                });

        return orders;
    }

    public static Cart foundCartById(String idCart) {
        Log.d("CheckCartList", (cartsBackend == null) + "");
        for(Cart cart : cartsBackend) {
            Log.d("CheckCartList", (cart.getId().matches(idCart))+"");
        }
        for (Cart cart : cartsBackend) {
            if (cart.getId().matches(idCart)) {
                Log.d("Check_cart_found_by_id", (cart.getId().matches(idCart))+"");
                return cart;
            }
        }

        return null;
    }

    public static Order foundOrderById(String id) {
        for (Order order : orders) {
            if (order.getId().matches(id)) return order;
        }

        return null;
    }
}