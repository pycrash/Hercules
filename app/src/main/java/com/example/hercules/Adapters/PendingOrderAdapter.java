package com.example.hercules.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hercules.Models.Requests;
import com.example.hercules.MyOrders.MyOrders;
import com.example.hercules.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PendingOrderAdapter extends RecyclerView.Adapter<PendingOrderAdapter.MyOrderHolder> {

    List<Requests> listData;
    Context context;
    String TAG = "My Orders";

    public PendingOrderAdapter(Context context, List<Requests> listData) {
        this.listData = listData;
        this.context = context;
    }

    class MyOrderHolder extends RecyclerView.ViewHolder {
        public TextView orderid, date,name, contact_name,mailing_name, phone, contact_phone, address,state,
                pincode,discount, amount, new_amount, gstin, email;
        public CardView done_order;
        RecyclerView recyclerView;
        TextView addNotes;
        TextView status;


        public MyOrderHolder(@NonNull View itemView) {
            super(itemView);
            orderid = itemView.findViewById(R.id.new_order_id);
            date = itemView.findViewById(R.id.new_order_date);
            name = itemView.findViewById(R.id.new_order_name);
            contact_name = itemView.findViewById(R.id.new_order_contact_name);
            mailing_name = itemView.findViewById(R.id.new_order_mailing_name);
            phone = itemView.findViewById(R.id.new_order_number);
            contact_phone = itemView.findViewById(R.id.new_order_contact_number);
            address = itemView.findViewById(R.id.new_order_address);
            state = itemView.findViewById(R.id.new_order_state);
            pincode = itemView.findViewById(R.id.new_order_pincode);
            discount = itemView.findViewById(R.id.new_order_discount);
            amount = itemView.findViewById(R.id.new_order_price);
            new_amount = itemView.findViewById(R.id.new_order_new_price);
            status = itemView.findViewById(R.id.pending_order_status);
            gstin = itemView.findViewById(R.id.new_order_gstin);
            done_order = itemView.findViewById(R.id.pending_order_done);
            addNotes = itemView.findViewById(R.id.pending_order_add_notes);
            recyclerView = itemView.findViewById(R.id.new_order_items_recycler_view);
            email =  itemView.findViewById(R.id.new_order_email);
            status = itemView.findViewById(R.id.pending_order_status);


        }

    }



    @NonNull
    @Override
    public MyOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.individual_pending_orders, parent, false);
        return new MyOrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyOrderHolder holder, final int position) {
        //getting data


        holder.orderid.setText(listData.get(position).getOrderID());
        holder.date.setText(listData.get(position).getDate());
        holder.name.setText(listData.get(position).getCompanyName());
        holder.contact_name.setText(listData.get(position).getContactName());
        holder.mailing_name.setText(listData.get(position).getID());
        holder.phone.setText(listData.get(position).getPhone());
        holder.email.setText(listData.get(position).getEmail());
        holder.contact_phone.setText(listData.get(position).getContactNumber());
        holder.address.setText(listData.get(position).getAddress());
        holder.state.setText(listData.get(position).getState());
        holder.pincode.setText(listData.get(position).getPincode());
        holder.discount.setText(listData.get(position).getDiscount());
        holder.amount.setText(listData.get(position).getTotal());
        holder.amount.setPaintFlags(holder.amount.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        holder.new_amount.setText(listData.get(position).getNewTotal());
        holder.gstin.setText(listData.get(position).getGstin());
        holder.addNotes.setText(listData.get(position).getNotes());
        holder.status.setText(listData.get(position).getStatus());
        holder.addNotes.setText(listData.get(position).getNotes());
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        MyOrderProductsAdapter cartAdapter  = new MyOrderProductsAdapter(listData.get(position).getCart(), context);
        holder.recyclerView.setAdapter(cartAdapter);
        holder.done_order.setOnClickListener(view -> {
            Intent intent = new Intent(context, MyOrders.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

}
