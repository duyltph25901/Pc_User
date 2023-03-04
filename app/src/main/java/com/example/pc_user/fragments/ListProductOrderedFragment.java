package com.example.pc_user.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pc_user.MainActivity;
import com.example.pc_user.R;
import com.example.pc_user.activities.DetailsActivity;
import com.example.pc_user.adapter.rcv.ProductBoughtAdapter;
import com.example.pc_user.db_local.bill.BillDatabase;
import com.example.pc_user.model.Cart;
import com.example.pc_user.model.Order;
import com.example.pc_user.model.Product;
import com.example.pc_user.model.view_model.Bill;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListProductOrderedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListProductOrderedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListProductOrderedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListProductOrderedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListProductOrderedFragment newInstance(String param1, String param2) {
        ListProductOrderedFragment fragment = new ListProductOrderedFragment();
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
    private RecyclerView rcv;
    private ProgressDialog progressDialog;

    private Bill bill;
    private Order order;
    private List<Product> products;
    private ProductBoughtAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list_product_ordered, container, false);
        _init();
        _getData();

        return view;
    }

    private void _init() {
        rcv = view.findViewById(R.id.rcvProductBought);
        products = new ArrayList<>();

        adapter = new ProductBoughtAdapter(getActivity(), products, product -> {
            Intent intent = new Intent(getActivity(), DetailsActivity.class);
            intent.putExtra("KEY_NAME", product);
            startActivity(intent);
        });
    }

    private void _getData() {
        bill = BillDatabase.getInstance(getActivity()).BillDao().getBill();
        order = MainActivity.foundOrderById(bill.getIdOrder());

        if (order == null) return;
        for (String idCart : order.getIdCart()) {
            Cart cart = MainActivity.foundCartById(idCart);
            Log.d("CHECK_CART_FOUND", (cart == null) + "");
            if (cart == null) return;
            Product product = MainActivity.foundProductById(cart.getIdProduct());
            Log.d("CHECK_PRODUCT_FOUND", (product == null) + "");

            products.add(product);
        }

        // dang co van de o day
        for (Product product : products) {
            System.out.println(product.getName());
        }

        adapter.setProducts(products);
        rcv.setAdapter(adapter);
    }
}