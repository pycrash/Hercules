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
        public TextView mProductName    ;
        public TextView mPrice;
        public ImageView mImg ;
        public TextView fat, carbo, protein, calories, servings;
        TextView textView_fat, textView_carbo, textView_proteins, textView_calories;

        public ExampleViewHolder(View itemView) {
            super(itemView);

            mProductName = (TextView) itemView.findViewById(R.id.product_name);
            fat = (TextView) itemView.findViewById(R.id.fat);
            carbo = (TextView) itemView.findViewById(R.id.carbo);
            protein = (TextView) itemView.findViewById(R.id.proteins);
            calories = (TextView) itemView.findViewById(R.id.calories);
            servings = (TextView) itemView.findViewById(R.id.servings);
            textView_fat = (TextView) itemView.findViewById(R.id.textview_fat);
            textView_carbo = (TextView) itemView.findViewById(R.id.textview_carbo);
            textView_proteins = (TextView) itemView.findViewById(R.id.textview_proteins);
            textView_calories = (TextView) itemView.findViewById(R.id.textview_calories);

            mImg = (ImageView) itemView.findViewById(R.id.product_image);
            mPrice = (TextView) itemView.findViewById(R.id.product_price);
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
        viewHolder.mPrice.setText(mContext.getString(R.string.price, String.valueOf((int)mList.get(position).getPrice())));
        viewHolder.fat.setText(mContext.getString(R.string.gram, String.valueOf(mList.get(position).getFat())));
        viewHolder.carbo.setText(mContext.getString(R.string.gram, String.valueOf(mList.get(position).getCarbo())));
        viewHolder.protein.setText(mContext.getString(R.string.gram, String.valueOf(mList.get(position).getProtein())));
        viewHolder.calories.setText(mContext.getString(R.string.kcal, String.valueOf(mList.get(position).getCalories())));
        viewHolder.servings.setText(String.valueOf((int)mList.get(position).getServings()));

        if (mList.get(position).getProductName().equals("HERCULES Rapid BCAA  350 g / 0.9 Lbs Fruit Punch") ||
                mList.get(position).getProductName().equals("HERCULES Rapid BCAA  350 g / 0.9 Lbs Watermelon") ) {
            viewHolder.textView_fat.setText("Leucine");
            viewHolder.textView_carbo.setText("Glutamine");
            viewHolder.textView_proteins.setText("Isoleucine");
            viewHolder.textView_calories.setText("Valine");
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

