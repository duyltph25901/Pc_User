package com.example.pc_user.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.pc_user.MainActivity;
import com.example.pc_user.R;
import com.example.pc_user.db_local.bill.BillDatabase;
import com.example.pc_user.model.Order;
import com.example.pc_user.model.view_model.Bill;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderInfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderInfoFragment newInstance(String param1, String param2) {
        OrderInfoFragment fragment = new OrderInfoFragment();
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

    private TextView textCreatedAt, textPriceSum, textStatus;
    private View view;

    private Bill bill;
    private Order order;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order_info, container, false);

        _init();
        _getData();
        _showDataView();

        return view;
    }

    private void _init() {
        textCreatedAt = view.findViewById(R.id.textCreatedAt);
        textPriceSum = view.findViewById(R.id.textPriceSum);
        textStatus = view.findViewById(R.id.textOrderStatus);
    }

    private void _getData() {
        bill = BillDatabase.getInstance(getActivity()).BillDao().getBill();
        order = MainActivity.foundOrderById(bill.getIdOrder());
    }

    private void _showDataView() {
        textCreatedAt.setText("Đặt hàng lúc: " + order.getCreatedAd());
        textPriceSum.setText("Tổng tiền: " + order.getSumPrice() + "$");
        textStatus.setText("Trạng thái: " +
                        (order.isStatus() ? "Giao hàng thành công" : "Đang chờ")
                );
    }
}