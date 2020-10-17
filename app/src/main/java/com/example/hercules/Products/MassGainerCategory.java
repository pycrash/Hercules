package com.example.hercules.Products;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hercules.Adapters.ProductAdapter;
import com.example.hercules.Interfaces.ItemClickListener;
import com.example.hercules.Models.ProductModel;
import com.example.hercules.R;

import java.util.ArrayList;
import java.util.List;


public class MassGainerCategory extends Fragment implements ItemClickListener {
    List<ProductModel> mList;
    public final String[] productName = {
            "Hercules Body Grow 1.0 Kg / 2.2 Lbs",
            "Hercules Body Grow 3.0 Kg / 6.6 Lbs",
            "Hercules Body Grow 5.0 Kg / 11.05 Lbs",
            "Hercules Rapid Mass 1.5 Kg / 3.3 Lbs",
            "Hercules Rapid Mass 3 Kg / 6.6 Lbs",
            "Hercules Rapid Mass 5 Kg / 11.05 Lbs",
    };
    public final double[] price = {1260, 3450, 5400, 2070, 3960,5790};
    public final int[] image = {R.drawable.gainer1,R.drawable.gainer2,R.drawable.gainer3, R.drawable.gainer4,R.drawable.gainer5,R.drawable.gainer6};
    public final double[] fat = {3, 3, 3,5, 5, 5};
    public final double[] carbo = {75, 75,75, 70, 70, 70 };
    public final double[] proteins = {20, 20,20, 22, 22, 22 };
    public final double[] calories = {403, 403,403, 403, 403, 403};
    public final double[] servings = {20, 60,100, 20, 60, 100};
    ProductAdapter adapter;
    public static MassGainerCategory newInstance(int position) {
        MassGainerCategory fragment = new MassGainerCategory();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);

        return fragment;
    }

    public MassGainerCategory() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            /* mPosition = getArguments().getInt("position");*/
        }

    }
    private List<ProductModel> prepareData(){

        List<ProductModel> list = new ArrayList<>();
        for(int i=0;i<productName.length;i++){
            ProductModel model = new ProductModel();
            model.setProductName(productName[i]);
            model.setPrice(price[i]);
            model.setImage(image[i]);
            model.setFat(fat[i]);
            model.setCarbo(carbo[i]);
            model.setProtein(proteins[i]);
            model.setCalories(calories[i]);
            model.setServings(servings[i]);
            list.add(model);
        }
        return list;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recycleViewWheyProtein);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        mList = prepareData();
        ProductAdapter adapter = new ProductAdapter(mList, view.getContext());
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view, int position) {
        ProductModel model = mList.get(position);
        Intent intent = new Intent(getContext(), ProductShowcase.class);
        intent.putExtra("position", position);
        intent.putExtra("category", getString(R.string.category_gainer));
        startActivity(intent);
    }
}

