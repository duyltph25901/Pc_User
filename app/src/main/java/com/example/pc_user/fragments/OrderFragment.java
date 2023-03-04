package com.example.pc_user.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pc_user.R;
import com.example.pc_user.activities.DetailsOrderActivity;
import com.example.pc_user.adapter.rcv.OrderAdapter;
import com.example.pc_user.functions.get_const.Const;
import com.example.pc_user.functions.interact_db_local.DBLocal;
import com.example.pc_user.model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
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

    private List<Order> orders;
    private OrderAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order, container, false);
        _init();
        _getDatabase();

        return view;
    }

    private void _init() {
        rcv = view.findViewById(R.id.rcvOrder);
        orders = new ArrayList<>();
        adapter = new OrderAdapter(getActivity(), orders, this::_showDetailOrder);

        progressDialog = new ProgressDialog(getActivity());
    }

    private void _getDatabase() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child(Const.orderTableName)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        orders.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Order order = data.getValue(Order.class);
                            orders.add(order);
                        }

                        Collections.reverse(orders);
                        adapter.setOrders(orders);
                        rcv.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void _showDetailOrder(final Order order) {
        // add data to local database
        DBLocal.saveBillCurrent(getActivity(), order.getId());
        progressDialog.show();

        new Handler()
                .postDelayed(() -> {
                    progressDialog.cancel();
                    Intent intent = new Intent(getActivity(), DetailsOrderActivity.class);
                    intent.putExtra("KEY_ORDER_ITEM", order);
                    startActivity(intent);
                }, 3000);
    }
}