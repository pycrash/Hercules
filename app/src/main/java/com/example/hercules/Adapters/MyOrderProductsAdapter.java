package com.example.hercules.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hercules.Database.Order;
import com.example.hercules.R;

import java.util.List;

public class MyOrderProductsAdapter extends RecyclerView.Adapter<MyOrderProductsAdapter.CartViewHolder> {

    private final List<Order> listData;
    private final Context context;

    static class CartViewHolder extends RecyclerView.ViewHolder {
        public TextView price, name, id, quantity;
        public ImageView img;


        public void setName(TextView name) {
            this.name = name;
        }

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.my_order_product_name);
            price = itemView.findViewById(R.id.my_order_product_price);
            id = itemView.findViewById(R.id.my_order_product_id);
            img = itemView.findViewById(R.id.my_order_product_image);
            quantity = itemView.findViewById(R.id.my_order_quantity);


        }


    }

    public MyOrderProductsAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.indiviual_item_my_order, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        int multiplier = listData.get(position).getMultiplier();
        int[] ij = {(listData.get(position).getQuantity()) / multiplier};
        holder.name.setText(listData.get(position).getProductName());
        holder.id.setText(context.getString(R.string.product_id, listData.get(position).getProductID()));
        holder.img.setImageResource(listData.get(position).getImageResource());
        holder.price.setText(context.getString(R.string.price, String.valueOf(listData.get(position).getPrice() * listData.get(position).getQuantity())));
        holder.quantity.setText("Quantity : " + listData.get(position).getQuantity());

    }


    @Override
    public int getItemCount() {
        return listData.size();
    }


}
