package com.example.pc_user.adapter.rcv;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pc_user.MainActivity;
import com.example.pc_user.R;
import com.example.pc_user.model.Cart;
import com.example.pc_user.model.Product;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{
    private Context context;
    private List<Cart> carts;
    private ClickItem clickItem;

    public CartAdapter(Context context, List<Cart> carts, ClickItem clickItem) {
        this.context = context;
        this.carts = carts;
        this.clickItem = clickItem;
    }

    public void setCarts(List<Cart> carts) {
        this.carts = carts;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cart = carts.get(position);
        if (cart == null) return;

        holder.onBindViewHolder(cart);
        holder.txtIncrease.setOnClickListener(view -> clickItem.increase(cart));
        holder.txtReduce.setOnClickListener(view -> clickItem.reduce(cart));
    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView imgProduct;
        private TextView txtProductName;
        private TextView txtProductPrice;
        private TextView txtReduce, txtSum, txtIncrease;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            _init(itemView);
        }

        private void _init(View view) {
            imgProduct = view.findViewById(R.id.imgProduct);
            txtProductName = view.findViewById(R.id.textProductName);
            txtProductPrice = view.findViewById(R.id.textPrice);
            txtReduce = view.findViewById(R.id.textReduce);
            txtIncrease = view.findViewById(R.id.textIncrease);
            txtSum = view.findViewById(R.id.textSum);
        }

        public void onBindViewHolder(final Cart cart) {
            Product product = MainActivity.foundProductById(cart.getIdProduct());
            Log.d("Check user found", (product == null) + "");
            if (product == null) return;
            String productName = product.getName();
            productName = (productName.length() >= 30)
                    ? productName.substring(0, 30).toUpperCase() + "..."
                    : productName.toUpperCase();

            Glide.with(context)
                    .load(product.getImage())
                    .error(R.mipmap.ic_launcher)
                    .into(imgProduct);
            txtProductName.setText(productName);
            txtProductPrice.setText(product.getTax()+"$");
            txtSum.setText(String.valueOf(cart.getSumProduct()));
        }
    }

    public interface ClickItem {
        void increase(Cart cart);
        void reduce(Cart cart);
    }
}
