package com.example.hercules.MyOrders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

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
    AlertDialog dialog;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        noOrders = findViewById(R.id.new_orders_no_orders);
        noOrders.setVisibility(View.GONE);

        checkInternet();
        ImageView back;
        back = findViewById(R.id.back_login);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
                handler.removeCallbacksAndMessages(null);
            }
        });
        progressBar = findViewById(R.id.my_orders_progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) findViewById(R.id.my_orders_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        loadOrders();


    }

    private void loadOrders() {
        Hawk.init(MyOrders.this).build();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Hawk.get("mailingName").toString().replaceAll(" ", "")).child("Orders");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot ds : snapshot.getChildren()) {

                    Requests requests = ds.getValue(Requests.class);
                    list.add(requests);
                    adapter = new MyOrderAdapter(MyOrders.this, list);
                    recyclerView.setAdapter(adapter);

                }
                progressBar.setVisibility(View.GONE);
                Log.d("TAG", "onDataChange: "+list.size());
                if (list.size() == 0) {
                    noOrders.setVisibility(View.VISIBLE);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);

            }

        });
    }
    void checkInternet() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MyOrders.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View mView = inflater.inflate(R.layout.dialog_no_internet, null);
        mBuilder.setView(mView);
        mBuilder.setCancelable(false);
        dialog = mBuilder.create();
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 10);
                boolean isInternet = CheckInternetConnection.checkInternet(MyOrders.this);
                if (!isInternet) {
                    dialog.show();
                } else {
                    dialog.hide();
                }
            }
        }, 20);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        handler.removeCallbacksAndMessages(null);
    }

}