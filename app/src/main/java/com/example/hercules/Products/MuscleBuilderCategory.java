package com.example.hercules.Products;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hercules.Adapters.ProductAdapter;
import com.example.hercules.Interfaces.ItemClickListener;
import com.example.hercules.Models.ProductModel;
import com.example.hercules.R;

import java.util.ArrayList;
import java.util.List;


public class MuscleBuilderCategory extends Fragment implements ItemClickListener {
    List<ProductModel> mList;
    public final String[] productName = {
            "Hercules Rapid ISOTECH 1.0 Kg / 2.2 Lbs",
            "Hercules Rapid ISOTECH 3.0 Kg / 6.6 Lbs",
            "Hercules Rapid Lean 1.5 Kg / 3,3 Lbs",
            "Hercules Rapid Lean 3.0 Kg / 6.6 Lbs",
    };
    public final double[] price = {2340, 6199, 2700, 4990};
    public final int[] image = {R.drawable.builder1,R.drawable.builder2,R.drawable.builder3,R.drawable.builder4};
    public final double[] fat = {3, 3, 1.5, 1.5};
    public final double[] carbo = {90, 90, 60, 60};
    public final double[] proteins = {44, 44, 32, 32};
    public final double[] calories = {398, 398, 377, 377};
    public final double[] servings = {30, 60, 30, 60 };

    public MuscleBuilderCategory() {
        // Required empty public constructor
    }

    public static MuscleBuilderCategory newInstance(int position) {
        MuscleBuilderCategory fragment = new MuscleBuilderCategory();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);

        return fragment;
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
        intent.putExtra("category", getString(R.string.category_builder));
        startActivity(intent);
    }
}