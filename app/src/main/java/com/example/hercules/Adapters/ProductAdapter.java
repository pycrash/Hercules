package com.example.hercules.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hercules.Interfaces.ItemClickListener;
import com.example.hercules.Models.ProductModel;
import com.example.hercules.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ExampleViewHolder> {
    private List<ProductModel> mList;
    private Context mContext;
    private ItemClickListener clickListener;


    public class ExampleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mProductName;
        public TextView mPrice;
        public ImageView mImg;
        public TextView fat, carbo, protein, calories, servings;
        TextView textView_fat, textView_carbo, textView_proteins, textView_calories;

        public ExampleViewHolder(View itemView) {
            super(itemView);

            mProductName = itemView.findViewById(R.id.product_name);
            fat = itemView.findViewById(R.id.fat);
            carbo = itemView.findViewById(R.id.carbo);
            protein = itemView.findViewById(R.id.proteins);
            calories = itemView.findViewById(R.id.calories);
            servings = itemView.findViewById(R.id.servings);
            textView_fat = itemView.findViewById(R.id.textview_fat);
            textView_carbo = itemView.findViewById(R.id.textview_carbo);
            textView_proteins = itemView.findViewById(R.id.textview_proteins);
            textView_calories = itemView.findViewById(R.id.textview_calories);

            mImg = itemView.findViewById(R.id.product_image);
            mPrice = itemView.findViewById(R.id.product_price);
            itemView.setTag(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }

    public ProductAdapter(List<ProductModel> list, Context context) {
        mList = list;
        mContext = context;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.indiviual_item, parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder viewHolder, int position) {

        viewHolder.mProductName.setText(mList.get(position).getProductName());
        viewHolder.mImg.setImageResource(mList.get(position).getImage());
        viewHolder.mPrice.setText(mContext.getString(R.string.price, String.valueOf((int) mList.get(position).getPrice())));
        viewHolder.fat.setText(mContext.getString(R.string.gram, mList.get(position).getFat()));
        viewHolder.carbo.setText(mContext.getString(R.string.gram, mList.get(position).getCarbo()));
        viewHolder.protein.setText(mContext.getString(R.string.gram, mList.get(position).getProtein()));
        viewHolder.calories.setText(mContext.getString(R.string.kcal, mList.get(position).getCalories()));
        viewHolder.servings.setText(mList.get(position).getServings());

        if (mList.get(position).getProductName().equals(mContext.getString(R.string.pre5_name)) ||
                mList.get(position).getProductName().equals(mContext.getString(R.string.pre6_name))) {

            viewHolder.textView_fat.setText(mContext.getString(R.string.ui_leucine));
            viewHolder.textView_carbo.setText(mContext.getString(R.string.ui_glutamine));
            viewHolder.textView_proteins.setText(mContext.getString(R.string.ui_isoleucine));
            viewHolder.textView_calories.setText(mContext.getString(R.string.ui_valine));
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;

    }

}

