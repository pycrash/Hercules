package com.example.hercules.MyOrders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hercules.Adapters.MyOrderAdapter;
import com.example.hercules.Models.Requests;
import com.example.hercules.R;
import com.example.hercules.utils.InternetUtils.CheckInternetConnection;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

public class MyOrders extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Requests> list;
    MyOrderAdapter adapter;
    ProgressBar progressBar;
    LinearLayout noOrders;
    Handler handler;

    public static final String TAG = "MyOrdersActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        Log.d(TAG, "onCreate: Here");
        noOrders = findViewById(R.id.new_orders_no_orders);

        Log.d(TAG, "onCreate: calling check Internet method");
        CheckInternetConnection.showNoInternetDialog(MyOrders.this, handler);
        ImageView back;
        back = findViewById(R.id.back_login);
        back.setOnClickListener(view -> {
            Log.d(TAG, "onClick: back button is pressed");
            onBackPressed();
            Log.d(TAG, "onClick: finishing this activity");
            finish();
            Log.d(TAG, "onClick: removing handler callbacks and messages");
            handler.removeCallbacksAndMessages(null);
        });
        Log.d(TAG, "onCreate: making progress bar visible");
        progressBar = findViewById(R.id.my_orders_progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        Log.d(TAG, "onCreate: setting up recycler view");
        recyclerView = findViewById(R.id.my_orders_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();

        Log.d(TAG, "onCreate: calling loadOrders method");
        loadOrders();


    }

    private void loadOrders() {
        Hawk.init(MyOrders.this).build();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Hawk.get(getString(R.string.id)).toString().replaceAll(" ", "")).child(getString(R.string.firebase_orders));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                Log.d(TAG, "onDataChange: getting child from Orders");
                for (DataSnapshot ds : snapshot.getChildren()) {

                    Requests requests = ds.getValue(Requests.class);
                    list.add(requests);
                    adapter = new MyOrderAdapter(MyOrders.this, list);
                    recyclerView.setAdapter(adapter);

                }
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onDataChange: " + list.size());
                if (list.size() == 0) {
                    Log.d(TAG, "onDataChange: no orders for this user, making animation visible");
                    noOrders.setVisibility(View.VISIBLE);
                } else {
                    Log.d(TAG, "onDataChange: making animation invisible");
                    noOrders.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: error encountered while retrieving childs");
                Log.d(TAG, "onCancelled: error : " + error);
                Toast.makeText(MyOrders.this, "Can't connect to server, try again after some time", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onCancelled: going back to Home activity");
                onBackPressed();
                Log.d(TAG, "onCancelled: finishing the activity");
                finish();
                progressBar.setVisibility(View.GONE);

            }

        });
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: back is pressed");
        super.onBackPressed();
        Log.d(TAG, "onBackPressed: finishing the activity");
        finish();
        Log.d(TAG, "onBackPressed: removing callbacks and messages from handler");
        handler.removeCallbacksAndMessages(null);
    }

}