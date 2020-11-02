package com.example.hercules.Products;

import android.content.ClipData;
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
import com.example.hercules.Interfaces.ItemClickListener;
import com.example.hercules.Models.ProductModel;
import com.example.hercules.R;

import java.util.ArrayList;
import java.util.List;


public class MuscleBuilderCategory extends Fragment implements ItemClickListener {
    List<ProductModel> mList;
    public String[] productName;
    public String[] price;
    public int[] image;
    public String[] fat;
    public String[] carbo;
    public String[] proteins;
    public String[] calories;
    public String[] servings;

    public static final String TAG = "Muscle_builder_category";

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

    }

    private List<ProductModel> prepareData() {
        Log.d(TAG, "prepareData: setting up data for Muscle Builder Category");

        productName = new String[]{getString(R.string.builder1_name), getString(R.string.builder2_name),
                getString(R.string.builder3_name), getString(R.string.builder4_name)};

        image = new int[]{R.drawable.builder1, R.drawable.builder2, R.drawable.builder3, R.drawable.builder4};

        price = new String[]{getString(R.string.builder1_price), getString(R.string.builder2_price),
                getString(R.string.builder3_price), getString(R.string.builder4_price)};

        fat = new String[]{getString(R.string.builder1_fat), getString(R.string.builder2_fat),
                getString(R.string.builder3_fat), getString(R.string.builder4_fat)};

        carbo = new String[]{getString(R.string.builder1_carbo), getString(R.string.builder2_carbo),
                getString(R.string.builder3_carbo), getString(R.string.builder4_carbo)};

        proteins = new String[]{getString(R.string.builder1_proteins), getString(R.string.builder2_proteins),
                getString(R.string.builder3_proteins), getString(R.string.builder4_proteins)};

        calories = new String[]{getString(R.string.builder1_calories), getString(R.string.builder2_calories),
                getString(R.string.builder3_calories), getString(R.string.builder4_calories)};

        servings = new String[]{getString(R.string.builder1_servings), getString(R.string.builder2_servings),
                getString(R.string.builder3_servings), getString(R.string.builder4_servings)};


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
        Log.d(TAG, "onClick: category : " + getString(R.string.category_builder));
        intent.putExtra(getString(R.string.position), position);
        intent.putExtra(getString(R.string.category), getString(R.string.category_builder));
        startActivity(intent);
    }
}