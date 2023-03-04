package com.example.pc_user.fragments;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pc_user.MainActivity;
import com.example.pc_user.R;
import com.example.pc_user.adapter.rcv.CartAdapter;
import com.example.pc_user.functions.get_const.Const;
import com.example.pc_user.functions.notification_dialog.ShowDialog;
import com.example.pc_user.functions.validate.ValidateInput;
import com.example.pc_user.model.Cart;
import com.example.pc_user.model.InfoUser;
import com.example.pc_user.model.Order;
import com.example.pc_user.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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

    // fragment
    private View view;
    private RecyclerView rcv;
    private TextView txtSumPrice;
    private LinearLayout btnShopping;

    // dialog
    private EditText inputFullName;
    private EditText inputPhoneNumber;
    private EditText inputAddress;
    private Button btnCancel, btnHandelShopping;

    private List<Cart> carts;
    private CartAdapter adapter;
    private String mUserName, mEmail;
    private double mPrice;

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getEmail() {
        return mEmail;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double mPrice) {
        this.mPrice = mPrice;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cart, container, false);
        _init();
        _loadSumPrice();
        _getDatabase();
        _setOnclick();

        return view;
    }

    private void _init() {
        rcv = view.findViewById(R.id.rcvCart);
        txtSumPrice = view.findViewById(R.id.textSumPrice);
        btnShopping = view.findViewById(R.id.layoutShopping);

        carts = new ArrayList<>();
        adapter = new CartAdapter(getActivity(), carts, new CartAdapter.ClickItem() {
            @Override
            public void increase(Cart cart) {
                _inCrease(cart);
            }

            @Override
            public void reduce(Cart cart) {
                _reduce(cart);
            }
        });
        _loadSumPrice();
    }

    private void _getDatabase() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child(Const.cartTableName).orderByChild("id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                carts.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Cart cart = data.getValue(Cart.class);
                    assert cart != null;
                    Product product = MainActivity.foundProductById(cart.getIdProduct());
                    assert product != null;
                    if (product.getSum() <= 0) {
                        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                        dbRef.child(Const.cartTableName).child(cart.getId()).removeValue();
                    } else {
                        carts.add(cart);
                    }
                }

                _loadSumPrice();
                adapter.setCarts(carts);
                rcv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void _inCrease(final Cart cart) {
        int sumProduct = cart.getSumProduct() + 1;
        Product product = MainActivity.foundProductById(cart.getIdProduct());
        if (product == null) return;
        if (sumProduct >= product.getSum()) {
            ShowDialog.handleShowDialog(getActivity(), Const.flagErrorDialog, "Số lượng tồn kho không đủ hàng!!!");
            return;
        }

        cart.setSumProduct(sumProduct);
        _updateCart(cart);
        _loadSumPrice();
    }

    private void _reduce(final Cart cart) {
        int sumProduct = cart.getSumProduct() - 1;
        if (sumProduct == 0) {
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
            dbRef.child(Const.cartTableName).child(cart.getId()).removeValue();
            _loadSumPrice();
        } else {
            cart.setSumProduct(sumProduct);
            _updateCart(cart);
        }

        _loadSumPrice();
    }

    private void _updateCart(final Cart cart) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child(Const.cartTableName).child(cart.getId()).setValue(cart);

        _loadSumPrice();
    }

    private void _loadSumPrice() {
        double price = 0;
        for (Cart cart : carts) {
            price += cart.getPriceProduct() * cart.getSumProduct();
        }

        setPrice(price);
        txtSumPrice.setText("Tổng tiền:    " + price + " $");
    }

    private void _setOnclick() {
        btnShopping.setOnClickListener(view -> {
            if (_isStocking()) {
                _showDialogOrder();
            } else {
                ShowDialog.handleShowDialog(getActivity(), Const.flagErrorDialog, "Một vài sản phẩm đẫ hết hàng, vui lòng kiểm tra lại!!");
                return;
            }
        });
    }

    private boolean _isStocking() {
        for (Cart cart : carts) {
            Product product = MainActivity.foundProductById(cart.getIdProduct());
            assert product != null;
            if (product.getSum() <= 0) return false;
        }

        return true;
    }

    private void _showDialogOrder() {
        if (carts.isEmpty()) {
            ShowDialog.handleShowDialog(getActivity(), Const.flagErrorDialog, "Hiện tại không có đơn hàng nào!!!");
            return;
        }

        Dialog dialogOrder = ShowDialog.getDialogShopping(getActivity());
        _init(dialogOrder);
        _setOnclick(dialogOrder);
    }

    private void _init(Dialog dialog) {
        inputAddress = dialog.findViewById(R.id.inputAddress);
        inputFullName = dialog.findViewById(R.id.inputFullName);
        inputPhoneNumber = dialog.findViewById(R.id.inputPhoneNumber);

        btnCancel = dialog.findViewById(R.id.btnCancelShopping);
        btnHandelShopping = dialog.findViewById(R.id.btnShopping);

        if (!_isNotNullUserName()) {
            inputFullName.setVisibility(View.GONE);
        } else {
            inputFullName.setVisibility(View.VISIBLE);
        }
    }

    private void _setOnclick(Dialog dialog) {
        btnCancel.setOnClickListener(view -> dialog.cancel());
        btnHandelShopping.setOnClickListener(view -> _handleShopping(dialog));
    }

    private void _handleShopping(Dialog dialog) {
        String fullName = inputFullName.getText().toString().trim();
        String phoneNumber = inputPhoneNumber.getText().toString().trim();
        String address = inputAddress.getText().toString().trim();

        fullName = (_isNotNullUserName()) ? fullName : getUserName();
        boolean isNull = ValidateInput.isNull(fullName, phoneNumber, address);
        boolean isPhoneNumber = ValidateInput.isPhoneNumber(phoneNumber);
        boolean isBreaking = ValidateInput.isBreakingShopping(getActivity(), isNull, isPhoneNumber);

        if (isBreaking) return;

        String id = _autoGenId();
        InfoUser info = new InfoUser(getEmail(), fullName, address, phoneNumber);
        List<String> idCarts = _getAllIdCarts();
        double sumPrice = getPrice();
        String createdAt = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                ? _getCurrentDateTimeModern() : _getCurrentDateTimeBasic();
        boolean status = false;

        Order order = new Order(id, idCarts, sumPrice, info, createdAt, status);
        _handleAddOrder(order, dialog);
    }

    private void _handleAddOrder(final Order order, Dialog dialog) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child(Const.orderTableName).child(order.getId()).setValue(order);

        _inCreaseProduct(order);
        dialog.cancel();
        ShowDialog.handleShowDialog(getActivity(), Const.flagSuccessDialog, "Đặt hàng thành công!");
        _removeAllCarts();
        _loadSumPrice();
    }

    private void _inCreaseProduct(final Order order) {
        List<String> idCarts = order.getIdCart();

        for (String idCart : idCarts) {
            Cart cart = MainActivity.foundCartById(idCart);
            String idProduct = cart.getIdProduct();
            int productSum = cart.getSumProduct();
            Product product = MainActivity.foundProductById(idProduct);
            if (product == null) return;
            product.setSum(product.getSum() - productSum);
            product.setPurchases(product.getPurchases() + 1);

            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
            dbRef.child(Const.productTableName).child(product.getId()).setValue(product);
        }

        MainActivity.reloadProducts();
    }

    private void _removeAllCarts() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child(Const.cartTableName).removeValue();
    }

    private boolean _isNotNullUserName() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return false;

        setUserName(user.getDisplayName());
        setEmail(user.getEmail());

        return user.getDisplayName() == null;
    }

    private String _autoGenId() {
        return FirebaseDatabase.getInstance().getReference().push().getKey();
    }

    private List<String> _getAllIdCarts() {
        List<String> idCarts = new ArrayList<>();

        for (Cart cart : carts) {
            idCarts.add(cart.getId());
        }

        return idCarts;
    }

    private String _getCurrentDateTimeModern() {
        DateTimeFormatter dtf = null;
        LocalDateTime now = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            now = LocalDateTime.now();
        }

        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                ? dtf.format(now) + ""
                : _getCurrentDateTimeBasic();
    }

    public String _getCurrentDateTimeBasic() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss", Locale.getDefault());

        return sdf.format(new Date());
    }

    @Override
    public void onResume() {
        super.onResume();

        _getDatabase();
        _loadSumPrice();
    }
}