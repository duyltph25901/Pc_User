package com.example.pc_user.adapter.rcv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pc_user.R;
import com.example.pc_user.model.Product;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class ProductBoughtAdapter extends RecyclerView.Adapter<ProductBoughtAdapter.ProductBoughtViewHolder>{
    private Context context;
    private List<Product> products;
    private ClickItem clickItem;

    public ProductBoughtAdapter(Context context, List<Product> products, ClickItem clickItem) {
        this.context = context;
        this.products = products;
        this.clickItem = clickItem;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductBoughtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductBoughtViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_product_bought, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ProductBoughtViewHolder holder, int position) {
        Product product = products.get(position);
        if (product == null) return;

        holder.onBindViewHolder(product);
        holder.textShowDetails.setOnClickListener(view -> {
            clickItem.showDetailsProduct(product);
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductBoughtViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView imageProduct;
        private TextView textProductName, textPrice;
        private TextView textShowDetails;

        public ProductBoughtViewHolder(@NonNull View itemView) {
            super(itemView);

            _init(itemView);
        }

        private void _init(View view) {
            imageProduct = view.findViewById(R.id.imgProduct);
            textProductName = view.findViewById(R.id.txtProductName);
            textPrice = view.findViewById(R.id.txtPriceProduct);
            textShowDetails = view.findViewById(R.id.textViewShowDetails);
        }

        public void onBindViewHolder(final Product product) {
            String name = product.getName();
            name = (name.length() >= 11)
                    ? name.substring(0, 11).toUpperCase() + "..."
                    : name.toUpperCase();
            textProductName.setText(name);
            textPrice.setText(product.getPrice() + "");
            Glide.with(context)
                    .load(product.getImage())
                    .error(R.mipmap.ic_launcher)
                    .into(imageProduct);
        }
    }

    public interface ClickItem {
        void showDetailsProduct(final Product product);
    }
}
