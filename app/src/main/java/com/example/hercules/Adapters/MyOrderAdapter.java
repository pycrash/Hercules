package com.example.hercules.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hercules.Database.Order;
import com.example.hercules.Models.Requests;
import com.example.hercules.MyOrders.DetailedOrdersActivity;
import com.example.hercules.R;

import java.io.Serializable;
import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyOrderHolder>  {

    List<Requests> listData;
    Context context;
    String TAG = "My Orders";

    public MyOrderAdapter(Context context, List<Requests> listData) {
        this.listData = listData;
        this.context = context;
    }

    class MyOrderHolder extends RecyclerView.ViewHolder implements Serializable {
        public TextView status, id, date,amount, new_amount;
        public CardView view_details;
        RecyclerView recyclerView;


        public MyOrderHolder(@NonNull View itemView) {
            super(itemView);

            status = itemView.findViewById(R.id.my_order_status);
            id = itemView.findViewById(R.id.my_order_id);
            date = itemView.findViewById(R.id.my_order_date);
            amount = itemView.findViewById(R.id.my_order_price);
            view_details = itemView.findViewById(R.id.my_order_details);
            recyclerView = itemView.findViewById(R.id.my_order_recycler_view);
            new_amount = itemView.findViewById(R.id.my_order_new_price);
        }

    }



    @NonNull
    @Override
    public MyOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.individual_my_order_item, parent, false);
        return new MyOrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderHolder holder, int position) {
        //getting data
        String orderid = listData.get(position).getOrderID();
        String date = listData.get(position).getDate();
        String amount = listData.get(position).getTotal();
        String status = listData.get(position).getStatus();
        List<Order> orders = listData.get(position).getCart();
        holder.id.setText(orderid);
        holder.status.setText(status);
        holder.date.setText(date);
        holder.amount.setText(amount);
        holder.amount.setPaintFlags(holder.amount.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        holder.new_amount.setText(listData.get(position).getNewTotal());
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        MyOrderProductsAdapter cartAdapter  = new MyOrderProductsAdapter(listData.get(position).getCart(), context);
        holder.recyclerView.setAdapter(cartAdapter);

        holder.view_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailedOrdersActivity.class);
                intent.putExtra("orderDetails",  listData.get(position));
                context.startActivity(intent);
            }
        });

        if (listData.get(position).getStatus().equals("Completed")) {
            holder.status.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
        } else if (listData.get(position).getStatus().equals("Cancelled")) {
            holder.status.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
        } else {
            holder.status.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow));
        }


    }


    @Override
    public int getItemCount() {
        return listData.size();
    }

}
