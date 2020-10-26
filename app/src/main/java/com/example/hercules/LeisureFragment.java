package com.example.hercules;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hercules.Adapters.TradingAdapter;
import com.example.hercules.Models.UploadPDF;
import com.example.hercules.Products.ProductShowcase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LeisureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeisureFragment extends Fragment {
    RecyclerView recyclerView;
    ProgressBar progressBar;
    LinearLayout no_trading;

    //list to store uploads data
    List<UploadPDF> uploadList;
    //these are the views
    TradingAdapter adapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LeisureFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LeisureFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LeisureFragment newInstance(String param1, String param2) {
        LeisureFragment fragment = new LeisureFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_s_o_l, container, false);
        no_trading = view.findViewById(R.id.no_trading);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        uploadList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_sol);
        //adding a clicklistener on listview
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        uploadList = new ArrayList<>();

        loadOrders();

        return view;
    }
    private void loadOrders() {
        Hawk.init(Objects.requireNonNull(getContext())).build();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Hawk.get("mailingName").toString().replaceAll(" ", "")).child("Leisure");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uploadList.clear();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    UploadPDF newOrderModel = ds.getValue(UploadPDF.class);
                    uploadList.add(newOrderModel);
                    adapter = new TradingAdapter(uploadList, getContext());
                    recyclerView.setAdapter(adapter);
                }
                progressBar.setVisibility(View.GONE);
                if (uploadList.size() == 0) {
                    no_trading.setVisibility(View.VISIBLE);
                } else {
                    no_trading.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                getActivity().onBackPressed();
                getActivity().finish();
            }
        });
    }
}