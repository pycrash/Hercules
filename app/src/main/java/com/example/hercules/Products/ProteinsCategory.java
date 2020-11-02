package com.example.hercules.Products;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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


public class ProteinsCategory extends Fragment implements ItemClickListener {
    List<ProductModel> mList;
    public String[] productName;
    public String[] price;
    public int[] image;
    public String[] fat;
    public String[] carbo;
    public String[] proteins;
    public String[] calories;
    public String[] servings;

    public static final String TAG = "Proteins_Category";

    public ProteinsCategory() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private List<ProductModel> prepareData() {
        Log.d(TAG, "prepareData: setting up data for Pre Workout Category");
        productName = new String[]{getString(R.string.protein1_name), getString(R.string.protein2_name)};
        price = new String[]{getString(R.string.protein1_price), getString(R.string.protein2_price)};
        image = new int[]{R.drawable.protein1, R.drawable.protein2};
        fat = new String[]{getString(R.string.protein1_fat), getString(R.string.protein2_fat)};
        carbo = new String[]{getString(R.string.protein1_carbo), getString(R.string.protein2_carbo)};
        proteins = new String[]{getString(R.string.protein1_proteins), getString(R.string.protein2_proteins)};
        calories = new String[]{getString(R.string.protein1_calories), getString(R.string.protein2_calories)};
        servings = new String[]{getString(R.string.protein1_servings), getString(R.string.protein2_servings)};

        Log.d(TAG, "prepareData: setting up data on recycler view");
        List<ProductModel> list = new ArrayList<>();
        for (int i = 0; i < productName.length; i++) {
            ProductModel model = new ProductModel();
            model.setProductName(productName[i]);
            model.setPrice(Double.parseDouble(price[i]));
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

        Log.d(TAG, "onCreateView: setting up recycler view");
        RecyclerView recyclerView = view.findViewById(R.id.recycleViewWheyProtein);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        mList = prepareData();
        ProductAdapter adapter = new ProductAdapter(mList, view.getContext());
        Log.d(TAG, "onCreateView: setting up adapter on recycler view");
        recyclerView.setAdapter(adapter);
        Log.d(TAG, "onCreateView: setting up click listener on recycler view");
        adapter.setClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view, int position) {
        Log.d(TAG, "onClick: going to Product Showcase intent");
        Intent intent = new Intent(getContext(), ProductShowcase.class);
        Log.d(TAG, "onClick: putting recycler view position and ctageory in intent");
        Log.d(TAG, "onClick: position : " + position);
        Log.d(TAG, "onClick: category : " + getString(R.string.category_protein));
        intent.putExtra(getString(R.string.position), position);
        intent.putExtra(getString(R.string.category), getString(R.string.category_protein));
        startActivity(intent);

    }


}
