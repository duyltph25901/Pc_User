package com.example.pc_user.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.pc_user.MainActivity;
import com.example.pc_user.R;
import com.example.pc_user.functions.CustomTextColor;
import com.example.pc_user.functions.get_const.Const;
import com.example.pc_user.functions.notification_dialog.ShowDialog;
import com.example.pc_user.functions.validate.ValidateInput;
import com.example.pc_user.model.Cart;
import com.example.pc_user.model.InfoUser;
import com.example.pc_user.model.Order;
import com.example.pc_user.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {
    private Product mProduct;

    private TextView txtProductName, txtNewPrice, txtOldPrice, txtInfo;
    private ImageView imgProduct, imgBack, imgAddCart, imgSaleOff;
    private ConstraintLayout layoutOldPrice;
    private Button btnBuy;
    private CardView imgContainer;
    private LinearLayout layoutPrice, layoutBody;
    private ProgressDialog progressDialog;
    private EditText inputFullName, inputAddress, inputPhoneNumber;
    private Button btnCancel, btnHandelShopping;

    private String mUserName, mEmail;

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    private float v = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        _init();
        _showData();
        _setAnimation();
        _setTextColor();
        _setOnclick();
    }

    private void _init() {
        mProduct = (Product) getIntent().getSerializableExtra("KEY_NAME");
        txtProductName = findViewById(R.id.txtProductName);
        txtNewPrice = findViewById(R.id.txtNewPrice);
        txtOldPrice = findViewById(R.id.txtOldPrice);
        txtInfo = findViewById(R.id.txtInfo);
        imgProduct = findViewById(R.id.imgProduct);
        imgBack = findViewById(R.id.imgBack);
        imgAddCart = findViewById(R.id.imgAddCart);
        imgSaleOff = findViewById(R.id.imgSale);
        layoutOldPrice = findViewById(R.id.layoutOldPrice);
        btnBuy = findViewById(R.id.btnBuy);
        imgContainer = findViewById(R.id.containerImage);
        layoutPrice = findViewById(R.id.layoutPrice);
        layoutBody = findViewById(R.id.layoutBody);

        progressDialog = new ProgressDialog(this);
    }

    private void _showData() {
        Glide.with(DetailsActivity.this)
                .load(mProduct.getImage())
                .error(R.mipmap.ic_launcher_round)
                .into(imgProduct);
        txtNewPrice.setText(mProduct.getTax()+"");
        txtProductName.setText(mProduct.getName());
        txtInfo.setText(mProduct.getIntro());

        if (mProduct.getDiscount() > 0) {
            layoutOldPrice.setVisibility(View.VISIBLE);
            txtOldPrice.setText(mProduct.getPrice()+"");
            imgSaleOff.setVisibility(View.VISIBLE);
        } else {
            layoutOldPrice.setVisibility(View.GONE);
            imgSaleOff.setVisibility(View.GONE);
        }
    }

    private void _setOnclick() {
        imgBack.setOnClickListener(view -> _back());
        imgAddCart.setOnClickListener(view -> _addToCart());
        btnBuy.setOnClickListener(view -> _showDialogInfo());
    }

    private void _back(){
        finish();
    }

    private void _setTextColor() {
        CustomTextColor.customTextColor(txtProductName,
                ContextCompat.getColor(this, R.color.color_primary),
                ContextCompat.getColor(this, R.color.color_second));
    }

    private void _setAnimation() {
        // button back
        imgBack.setTranslationY(-500);
        imgBack.setAlpha(v);
        imgBack.animate().translationY(v).alpha(1).setDuration(500).setStartDelay(200).start();

        // img product
        imgContainer.setTranslationX(500);
        imgContainer.setAlpha(v);
        imgContainer.animate().translationX(v).alpha(1).setDuration(700).setStartDelay(300).start();

        // text product name
        txtProductName.setTranslationX(-500);
        txtProductName.setAlpha(v);
        txtProductName.animate().translationX(v).alpha(1).setDuration(700).setStartDelay(300).start();

        // layout price
        layoutPrice.setTranslationX(-500);
        layoutPrice.setAlpha(v);
        layoutPrice.animate().translationX(v).alpha(1).setDuration(700).setStartDelay(300).start();

        // layout body
        layoutBody.setTranslationY(500);
        layoutBody.setAlpha(v);
        layoutBody.animate().translationY(v).alpha(1).setDuration(1000).setStartDelay(300).start();

        // img sale
        imgSaleOff.setTranslationX(500);
        imgSaleOff.setAlpha(v);
        imgSaleOff.animate().translationX(v).alpha(1).setDuration(700).setStartDelay(500).start();

        btnBuy.setTranslationY(500);
        btnBuy.setAlpha(v);
        btnBuy.animate().translationY(v).alpha(1).setDuration(1000).setStartDelay(750).start();

        imgAddCart.setTranslationY(500);
        imgAddCart.setAlpha(v);
        imgAddCart.animate().translationY(v).alpha(1).setDuration(800).setStartDelay(500).start();
    }

    private void _addToCart() {
        if (!_isStocking()) {
            ShowDialog.handleShowDialog(this, Const.flagErrorDialog, "Sản phẩm này không còn đủ hàng!");
            return;
        }

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        progressDialog.cancel();

        for (Cart cart : MainActivity.carts) {
            if (cart.getIdProduct().matches(mProduct.getId())) {
                Cart cartTemp = cart;
                cartTemp.setId(cart.getId());
                cartTemp.setIdProduct(cart.getIdProduct());
                cartTemp.setPriceProduct(cart.getPriceProduct());
                cartTemp.setSumProduct(cart.getSumProduct() + 1);

                // handle add
                dbRef.child(Const.cartTableName).child(cart.getId()).setValue(cartTemp);
                dbRef.child(Const.cartTableName_BackEnd).child(cart.getId()).setValue(cartTemp);

                ShowDialog.handleShowDialog(this, Const.flagSuccessDialog, "Đã thêm sản phẩm vào giỏ hàng.");

                return;
            }
        }

        String id = _autoGenId();
        String idProduct = mProduct.getId();
        double priceProduct = mProduct.getTax();
        Cart cart = new Cart(id, idProduct, priceProduct, 1);

        // handle add
        dbRef.child(Const.cartTableName).child(id).setValue(cart);
        dbRef.child(Const.cartTableName_BackEnd).child(cart.getId()).setValue(cart);
        ShowDialog.handleShowDialog(this, Const.flagSuccessDialog, "Đã thêm sản phẩm vào giỏ hàng.");
    }

    private String _autoGenId() {
        return FirebaseDatabase.getInstance().getReference().push().getKey();
    }

    private void _showDialogInfo() {
        Dialog dialog = ShowDialog.getDialogShopping(this);
        _init(dialog);
        _setOnclick(dialog);
    }

    private void _init(Dialog dialog) {
        inputFullName = dialog.findViewById(R.id.inputFullName);
        inputAddress = dialog.findViewById(R.id.inputAddress);
        inputPhoneNumber = dialog.findViewById(R.id.inputPhoneNumber);
        btnCancel = dialog.findViewById(R.id.btnCancelShopping);
        btnHandelShopping = dialog.findViewById(R.id.btnShopping);

        if (!_isNotNullUserName()) {
            inputFullName.setVisibility(View.GONE);
        } else {
            inputFullName.setVisibility(View.VISIBLE);
        }
    }

    private boolean _isNotNullUserName() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return false;

        setUserName(user.getDisplayName());
        setEmail(user.getEmail());

        return user.getDisplayName() == null;
    }

    private void _setOnclick(Dialog dialog) {
        btnCancel.setOnClickListener(view -> dialog.cancel());
        btnHandelShopping.setOnClickListener(view -> {
            if (_isStocking()) {
                _handleShopping(dialog);
            } else {
                ShowDialog.handleShowDialog(this, Const.flagErrorDialog, "Sản phẩm không còn hàng!");
            }
        });
    }

    private void _handleShopping(Dialog dialog) {

        // Validate
        String fullName = inputFullName.getText().toString().trim();
        String phoneNumber = inputPhoneNumber.getText().toString().trim();
        String address = inputAddress.getText().toString().trim();

        fullName = (_isNotNullUserName()) ? fullName : mUserName;
        boolean isNull = ValidateInput.isNull(fullName, phoneNumber, address);
        boolean isNotPhoneNumber = ValidateInput.isPhoneNumber(phoneNumber);
        boolean isBreaking = ValidateInput.isBreakingShopping(this, isNull, isNotPhoneNumber);
        if (isBreaking) return;

        // handle add object cart to table cart backend
        String idCart = _addObjectCart();
        // handle add object order
        _handleAddObjectOrder(dialog, idCart);
    }

    private boolean _isStocking() {
        Product product = MainActivity.foundProductById(mProduct.getId());
        return product.getSum() > 0;
    }

    private String _addObjectCart() {
        String idCart = _autoGenId();
        String idProduct = mProduct.getId();
        double price = mProduct.getPrice();
        int sum = 1;

        Cart cart = new Cart(idCart, idProduct, price, sum);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child(Const.cartTableName_BackEnd).child(idCart).setValue(cart);
        Log.d("Add_Cart_To_Cart_Back_End", "Successful");

        return idCart;
    }

    private void _handleAddObjectOrder(Dialog dialog, String idCart) {
        String fullName = inputFullName.getText().toString().trim();
        String phoneNumber = inputPhoneNumber.getText().toString().trim();
        String address = inputAddress.getText().toString().trim();

        fullName = (_isNotNullUserName()) ? fullName : mUserName;

        String idOrder = _autoGenId();
        List<String> idCarts = new ArrayList<>();
        idCarts.add(idCart);
        double priceSum = mProduct.getPrice();
        InfoUser info = new InfoUser(mEmail, fullName, address, phoneNumber);
        String createdAt = _getCurrentDateTimeModern();
        boolean status = false;

        Order order = new Order(idOrder, idCarts, priceSum, info, createdAt, status);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child(Const.orderTableName).child(idOrder).setValue(order);

        // reduce product
        mProduct.setSum(mProduct.getSum() - 1);
        mProduct.setPurchases(mProduct.getPurchases() + 1);
        dbRef.child(Const.productTableName).child(mProduct.getId()).setValue(mProduct);

        dialog.cancel();
        ShowDialog.handleShowDialog(this, Const.flagSuccessDialog, "Đặt hàng thành công!!!");
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
}