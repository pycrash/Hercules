package com.example.hercules.Trading;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hercules.Adapters.TradingAdapter;
import com.example.hercules.Models.UploadPDF;
import com.example.hercules.R;
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
 * Use the {@link SOLFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SOLFragment extends Fragment {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    LinearLayout no_trading;
    //list to store uploads data
    List<UploadPDF> uploadList;
    TradingAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "SOL_FRAGMENT";

    public SOLFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SOLFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SOLFragment newInstance(String param1, String param2) {
        SOLFragment fragment = new SOLFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: inflating sol fragment");
        View view = inflater.inflate(R.layout.fragment_s_o_l, container, false);


        no_trading = view.findViewById(R.id.no_trading);
        Log.d(TAG, "onCreateView: making progress bar visible");
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);


        Log.d(TAG, "onCreateView: setting up recycler view");
        uploadList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_sol);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        uploadList = new ArrayList<>();

        Log.d(TAG, "onCreateView: going to loadSOL method");
        loadSOL();
        return view;

    }

    private void loadSOL() {
        Log.d(TAG, "loadSOL: here");
        Log.d(TAG, "loadSOL: building Hawk");
        Hawk.init(Objects.requireNonNull(getContext())).build();

        Log.d(TAG, "loadSOL: referencing child SOL for " + Hawk.get(getString(R.string.id)));
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Hawk.get(getString(R.string.id)).toString().replaceAll(" ", "")).child(getString(R.string.SOL));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "onDataChange: clearing the array list - uploadList");
                uploadList.clear();
                Log.d(TAG, "onDataChange: getting the children of SOL");
                for (DataSnapshot ds : snapshot.getChildren()) {
                    UploadPDF newOrderModel = ds.getValue(UploadPDF.class);
                    uploadList.add(newOrderModel);
                    adapter = new TradingAdapter(uploadList, getContext());
                    recyclerView.setAdapter(adapter);

                }
                Log.d(TAG, "onDataChange: retrieve successful, making progress bar Invisible");
                progressBar.setVisibility(View.GONE);

                Log.d(TAG, "onDataChange: checking if the uploadList is empty or not");
                if (uploadList.size() == 0) {
                    Log.d(TAG, "onDataChange: uploadList is empty");
                    Log.d(TAG, "onDataChange: making lottie animation visible");
                    no_trading.setVisibility(View.VISIBLE);
                } else {
                    Log.d(TAG, "onDataChange: uploadList is not empty");
                    Log.d(TAG, "onDataChange: making lottie animation invisible");
                    no_trading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: error encountered while retrieving childs");
                Log.d(TAG, "onCancelled: error : " + error);
                Toast.makeText(getContext(), "Can't connect to server, try again after some time", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onCancelled: going back to Home activity");
                getActivity().onBackPressed();
                Log.d(TAG, "onCancelled: finishing the activity");
                getActivity().finish();
            }
        });
    }
}