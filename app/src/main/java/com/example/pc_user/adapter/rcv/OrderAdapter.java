package com.example.pc_user.adapter.rcv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pc_user.R;
import com.example.pc_user.model.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>{
    private Context context;
    private List<Order> orders;
    private ClickItem clickItem;

    public OrderAdapter(Context context, List<Order> orders, ClickItem clickItem) {
        this.context = context;
        this.orders = orders;
        this.clickItem = clickItem;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        if (order == null) return;

        holder.onBindViewHolder(order);
        holder.txtShowDetails.setOnClickListener(view -> clickItem.showDetails(order));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView txtId, txtCreatedAt, txtStatus;
        private TextView txtShowDetails;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            _init(itemView);
        }

        private void _init(View view) {
            txtId = view.findViewById(R.id.txtIdOrder);
            txtCreatedAt = view.findViewById(R.id.txtCreatedAt);
            txtStatus = view.findViewById(R.id.txtStatusOrder);
            txtShowDetails = view.findViewById(R.id.txtShowDetail);
        }

        public void onBindViewHolder(final Order order) {
            String id = order.getId();
            id = (id.length() >= 11) ? id.substring(0, 11) + "..." : id;
            String status = (order.isStatus()) ? "Đã chấp nhận" : "Đang chờ";

            txtId.setText(id);
            txtCreatedAt.setText(order.getCreatedAd());
            txtStatus.setText(status);
        }
    }

    public interface ClickItem {
        void showDetails(Order order);
    }
}

