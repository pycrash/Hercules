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
import com.example.hercules.Interfaces.ItemClickListener;
import com.example.hercules.Models.ProductModel;
import com.example.hercules.R;

import java.util.ArrayList;
import java.util.List;


public class PreWorkoutCategory extends Fragment implements ItemClickListener {
    List<ProductModel> mList;
    public final String[] productName = {
            "Hercules Blast 450 g / 0.9 Lbs Green Apple Flavour",
            "Hercules Blast 450 g / 0.9 Lbs Watermelon Flavour",
            "Hercules Volts Isotonic Drink 1.0 kg / 2.2 Lbs Orange Flavour",
            "Hercules Volts Isotonic Drink 1.0 kg / 2.2 Lbs Pineapple Flavour",
            "HERCULES Rapid BCAA  350 g / 0.9 Lbs Fruit Punch",
            "HERCULES Rapid BCAA  350 g / 0.9 Lbs Watermelon"
    };
    public final double[] price = {2610, 2610,610, 610, 1980, 1980};
    public final int[] image = {R.drawable.pre1,R.drawable.pre2,R.drawable.pre3,R.drawable.pre4,R.drawable.pre5,R.drawable.pre6};
    public final double[] fat = {0, 0,0,0,2500,2500};
    public final double[] carbo = {4, 4,28.5,28.5,1000,1000};
    public final double[] proteins = {0,0,0,0,1250,1250};
    public final double[] calories = {60.1, 60.1, 117, 117,1250, 1250};
    public final double[] servings = {30, 30,33,33,30,30};

    public static PreWorkoutCategory newInstance(int position) {
        PreWorkoutCategory fragment = new PreWorkoutCategory();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);


        return fragment;
    }

    public PreWorkoutCategory() {

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
            intent.putExtra("category", getString(R.string.category_pre));
            startActivity(intent);
        }
}



