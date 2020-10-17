package com.example.hercules.Products;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hercules.Adapters.ProductAdapter;
import com.example.hercules.Models.ProductModel;
import com.example.hercules.Interfaces.ItemClickListener;
import com.example.hercules.R;

import java.util.ArrayList;
import java.util.List;


public class ProteinsCategory extends Fragment  implements ItemClickListener {
    List<ProductModel> mList;
    public String[] productName = {"Hercules Rapid Whey 1 Kg / 2.2 Lbs", "Hercules Rapid Whey 2 Kg / 4.4 Lbs"};
    public final double[] price = {2999, 5650};
    public final int[] image = {R.drawable.protein1, R.drawable.protein2};
    public final double[] fat = {0.51, 0.51};
    public final double[] carbo = {3, 3};
    public final double[] proteins = {26.18, 26.18};
    public final double[] calories = {135, 135};
    public final double[] servings = {29, 59};

    public static ProteinsCategory newInstance(int position) {
        ProteinsCategory fragment = new ProteinsCategory();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);

        return fragment;
    }

    public ProteinsCategory() {

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
        intent.putExtra("category", getString(R.string.category_protein));
        startActivity(intent);

    }



}
