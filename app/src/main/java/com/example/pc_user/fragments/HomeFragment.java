package com.example.pc_user.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.pc_user.MainActivity;
import com.example.pc_user.R;
import com.example.pc_user.activities.DetailsActivity;
import com.example.pc_user.adapter.rcv.ProductAdapter;
import com.example.pc_user.functions.get_const.Const;
import com.example.pc_user.functions.notification_dialog.ShowDialog;
import com.example.pc_user.model.Cart;
import com.example.pc_user.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private View view;
    private EditText inputSearch;
    private ImageView imgSearch;
    private RecyclerView rcv;
    private LinearLayout layoutSearch;
    private LottieAnimationView notFound;
    private ProgressDialog progressDialog;

    private static List<Product> products;
    private List<Cart> carts;
    private ProductAdapter adapter;

    private DatabaseReference dbRef;

    private static final String tableProduct = Const.productTableName;
    private static final String tableCart = Const.cartTableName;

    private float v = 0;

    public static void setProducts(List<Product> products) {
        HomeFragment.products = products;
    }

    public static List<Product> getProducts() {
        return products;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        _init();
        _setAnimation();
        _getDatabase(inputSearch.getText().toString().trim());
        _setOnclick();

        return view;
    }

    private void _init() {
        inputSearch = view.findViewById(R.id.inputSearch);
        imgSearch = view.findViewById(R.id.imgSearch);
        rcv = view.findViewById(R.id.rcvProduct);
        layoutSearch = view.findViewById(R.id.layoutSearch);
        notFound = view.findViewById(R.id.notFound);
        progressDialog = new ProgressDialog(getActivity());

        products = new ArrayList<>();
        carts = new ArrayList<>();

        dbRef = FirebaseDatabase.getInstance().getReference();

        adapter = new ProductAdapter(getActivity(), products, new ProductAdapter.ClickItem() {
            @Override
            public void addCart(Product product) {
                if (!_isStocking(product)) {
                    ShowDialog.handleShowDialog(getActivity(), Const.flagErrorDialog, "Số lượng hàng tồn kho không đủ!");
                    return;
                }

                progressDialog.show();
                _addProductToCart(product);
            }

            @Override
            public void showDetail(Product product) {
                _handlerShowDetail(product);
            }
        });
    }

    private boolean _isStocking(final Product product) {
        return product.getSum() > 0;
    }

    private void _getDatabase(String input) {
        _getDatabaseProduct(input);
        new Handler()
                .postDelayed(() -> {
                    _showData();
                    _getDatabaseCart();
                }, 5000);
    }

    private String _getInputKey() {
        String input = inputSearch.getText().toString().trim();
        input = (input.length() == 0 || input.isEmpty() || input == null)
                ? "" : input;

        return input;
    }

    private void _getDatabaseProduct(String input) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child(Const.productTableName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                products.clear();
                if (input.isEmpty()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Product product = data.getValue(Product.class);
                        products.add(product);
                    }
                } else {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Product product = data.getValue(Product.class);
                        if (product.getName().toLowerCase().contains(input.toLowerCase())) {
                            products.add(product);
                        }
                    }
                }

                adapter.setProducts(products);
                rcv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error get product list", error.getMessage());
            }
        });
    }

    private void _showData() {
        progressDialog.cancel();
        // getDatabase product
        if (products.size() != 0) {
            // not found => animation reverse
            notFound.animate().translationYBy(-2000f).setDuration(1300);
            new Handler()
                    .postDelayed(() -> {
                        progressDialog.cancel();
                        adapter.setProducts(getProducts());

                        notFound.setVisibility(View.GONE);
                        rcv.setVisibility(View.VISIBLE);
                        rcv.setAdapter(adapter);
                    }, 1300);
        } else {
            // set animation
            notFound.setTranslationY(-700);
            notFound.setAlpha(v);
            notFound.animate().translationY(v).alpha(1).setDuration(1300).setStartDelay(200).start();

            rcv.setVisibility(View.GONE);
            notFound.setVisibility(View.VISIBLE);
        }
    }

    private void _getDatabaseCart() {
        carts = MainActivity.carts;
    }

    private void _setOnclick() {
        imgSearch.setOnClickListener(view -> _search());
    }

    private void _search() {
        progressDialog.show();
        String input = _getInputKey();
        _getDatabase(input);
        _displayKeyboard();
    }

    private void _handlerShowDetail(final Product product) {
        Intent intent = new Intent(getActivity(), DetailsActivity.class);
        intent.putExtra("KEY_NAME", product);
        startActivity(intent);
    }

    private void _setAnimation() {
        // layout search
        layoutSearch.setTranslationX(-700);
        layoutSearch.setAlpha(v);
        layoutSearch.animate().translationX(v).alpha(1).setDuration(1300).setStartDelay(200).start();
    }

    private void _displayKeyboard() {
        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(requireActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void _addProductToCart(final Product product) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        progressDialog.cancel();

        for (Cart cart : carts) {
            if (cart.getIdProduct().matches(product.getId())) {
                Cart cartTemp = cart;
                cartTemp.setId(cart.getId());
                cartTemp.setIdProduct(cart.getIdProduct());
                cartTemp.setPriceProduct(cart.getPriceProduct());

                int sum = cart.getSumProduct() + 1;
                cartTemp.setSumProduct(sum);

                // handle add
                dbRef.child(Const.cartTableName).child(cart.getId()).setValue(cartTemp);
                dbRef.child(Const.cartTableName_BackEnd).child(cart.getId()).setValue(cartTemp);

                ShowDialog.handleShowDialog(getActivity(), Const.flagSuccessDialog, "Đã thêm sản phẩm vào giỏ hàng.");
                return;
            }
        }

        String id = _autoGenId();
        String idProduct = product.getId();
        double priceProduct = product.getTax();
        Cart cart = new Cart(id, idProduct, priceProduct, 1);

        // handle add
        dbRef.child(Const.cartTableName).child(id).setValue(cart);
        dbRef.child(Const.cartTableName_BackEnd).child(cart.getId()).setValue(cart);
        ShowDialog.handleShowDialog(getActivity(), Const.flagSuccessDialog, "Đã thêm sản phẩm vào giỏ hàng.");
    }

    private String _autoGenId() {
        return FirebaseDatabase.getInstance().getReference().push().getKey();
    }

    @Override
    public void onResume() {
        super.onResume();

        MainActivity.reloadProducts();
    }
}