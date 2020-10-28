package com.example.hercules.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.hercules.Database.Database;
import com.example.hercules.Database.Order;
import com.example.hercules.Interfaces.ItemClickListener;
import com.example.hercules.R;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Order> listData = new ArrayList<>();
    private Context context;
    double totalPrice = 0;
    private ItemClickListener clickListener;
    boolean removeAnything = false;
    int p;
    String TAG = "Cart";
    class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView price, name, id;
        public ImageView img;
        ElegantNumberButton quantity;


        public void setName(TextView name) {
            this.name = name;
        }
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cart_product_name);
            price = itemView.findViewById(R.id.cart_product_price);
            id = itemView.findViewById(R.id.card_product_id);
            img = itemView.findViewById(R.id.cart_product_image);
            quantity = itemView.findViewById(R.id.quantity);
            itemView.setTag(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onClick(view, getAdapterPosition());
        };

        }
    }

    public CartAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.indiviual_item_ccart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        int multiplier = listData.get(position).getMultiplier();
        int[] ij = {(listData.get(position).getQuantity()) / multiplier};
        holder.name.setText(listData.get(position).getProductName());
        holder.id.setText(context.getString(R.string.product_id, listData.get(position).getProductID()));
        holder.img.setImageResource(listData.get(position).getImageResource());
        holder.quantity.setNumber(String.valueOf(listData.get(position).getQuantity()));
        holder.price.setText(context.getString(R.string.price, String.valueOf(listData.get(position).getPrice() * listData.get(position).getQuantity())));


            holder.quantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                @Override
                public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                    if (newValue > oldValue) {

                        ij[0] = ij[0] + 1;
                        Log.d(TAG, "onValueChange: "+ij[0]);
                        Log.d(TAG, "onValueChange: "+ij[0]*multiplier);
                        holder.quantity.setNumber(String.valueOf(ij[0] * multiplier));
                        new Database(context).updateQuantity(listData.get(position).getProductID(), ij[0] * multiplier);
                        totalPrice = totalPrice + listData.get(position).getPrice() * multiplier;
                        String price = context.getString(R.string.price, String.valueOf(listData.get(position).getPrice() * ij[0] * multiplier));
                        holder.price.setText(price);
                    } else {

                        ij[0] = ij[0] - 1;
                        Log.d(TAG, "onValueChange: ");
                        Log.d(TAG, "onValueChange: "+ij[0]);
                        Log.d(TAG, "onValueChange: "+ij[0]*multiplier);
                        Log.d(TAG, "onValueChange:string "+( ij[0] * multiplier));
                        holder.quantity.setNumber(String.valueOf(ij[0] * multiplier));
                        totalPrice = totalPrice - listData.get(position).getPrice() * multiplier;
                        String price = context.getString(R.string.price, String.valueOf(listData.get(position).getPrice() * ij[0] * multiplier));
                        holder.price.setText(price);
                        if (ij[0] == 0) {
                            holder.quantity.setNumber("0");
                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                            builder.setMessage("Are you sure you want to remove this product ?");
                            builder.setCancelable(false);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    new Database(context).deleteFromDatabase(listData.get(position).getProductID());
                                    String price = context.getString(R.string.price, String.valueOf(listData.get(position).getPrice() * multiplier));
                                    holder.price.setText(price);
                                    removeAnything = true;
                                    p = position;
                                    removeAnything();
                                    dialogInterface.dismiss();

                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    holder.quantity.setNumber(String.valueOf(multiplier));
                                    dialogInterface.cancel();
                                    totalPrice = totalPrice + listData.get(position).getPrice() * multiplier;
                                    String price = context.getString(R.string.price, String.valueOf(listData.get(position).getPrice() * multiplier));
                                    holder.price.setText(price);
                                    ij[0] = 1;
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();

                        }
                    }
                }
            });

        }


    @Override
    public int getItemCount() {
        return listData.size();
    }

    public double grandTotal() {
        for(int i=0; i < listData.size(); i++) {
            totalPrice += listData.get(i).getPrice() * listData.get(i).getQuantity();
        }
        return totalPrice;
    }
    public double totalChange() {
        return totalPrice;
    }
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;

    }
    public void removeAnything() {
        if (removeAnything) {
            listData.remove(p);
        }
        notifyItemRemoved(p);
        notifyDataSetChanged();
        removeAnything = false;

    }
}
