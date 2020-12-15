package com.example.hercules.MyOrders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hercules.Adapters.MyOrderAdapter;
import com.example.hercules.Models.Requests;
import com.example.hercules.R;
import com.example.hercules.utils.InternetUtils.MyReciever;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.hawk.Hawk;

import org.imaginativeworld.oopsnointernet.ConnectionCallback;
import org.imaginativeworld.oopsnointernet.NoInternetDialog;
import org.imaginativeworld.oopsnointernet.NoInternetSnackbar;

import java.util.ArrayList;
import java.util.List;


public class MyOrders extends AppCompatActivity{
    RecyclerView recyclerView;
    List<Requests> list;
    MyOrderAdapter adapter;
    ProgressBar progressBar;
    LinearLayout noOrders;
    // No Internet Dialog
    NoInternetDialog noInternetDialog;

    // No Internet Snackbar
    NoInternetSnackbar noInternetSnackbar;
    public static final String TAG = "MyOrdersActivity";
    private BroadcastReceiver MyReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        Log.d(TAG, "onCreate: Here");
        noOrders = findViewById(R.id.new_orders_no_orders);
        noInternetDialog = new NoInternetDialog.Builder(MyOrders.this).build();

        Log.d(TAG, "onCreate: calling check Internet method");
        MyReceiver = new MyReciever();

        ImageView back;
        back = findViewById(R.id.back_login);
        back.setOnClickListener(view -> {
            Log.d(TAG, "onClick: back button is pressed");
            onBackPressed();
            Log.d(TAG, "onClick: finishing this activity");
            finish();
            Log.d(TAG, "onClick: removing handler callbacks and messages");
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
    }

    @Override
    protected void onResume() {
        super.onResume();

        // No Internet Dialog
        NoInternetDialog.Builder builder1 = new NoInternetDialog.Builder(this);

        builder1.setConnectionCallback(new ConnectionCallback() { // Optional
            @Override
            public void hasActiveConnection(boolean hasActiveConnection) {
                // ...
            }
        });
        builder1.setCancelable(false); // Optional
        builder1.setNoInternetConnectionTitle("No Internet"); // Optional
        builder1.setNoInternetConnectionMessage("Check your Internet connection and try again"); // Optional
        builder1.setShowInternetOnButtons(true); // Optional
        builder1.setPleaseTurnOnText("Please turn on"); // Optional
        builder1.setWifiOnButtonText("Wifi"); // Optional
        builder1.setMobileDataOnButtonText("Mobile data"); // Optional

        builder1.setOnAirplaneModeTitle("No Internet"); // Optional
        builder1.setOnAirplaneModeMessage("You have turned on the airplane mode."); // Optional
        builder1.setPleaseTurnOffText("Please turn off"); // Optional
        builder1.setAirplaneModeOffButtonText("Airplane mode"); // Optional
        builder1.setShowAirplaneModeOffButtons(true); // Optional

        noInternetDialog = builder1.build();


        // No Internet Snackbar
        NoInternetSnackbar.Builder builder2 = new NoInternetSnackbar.Builder(this, (ViewGroup) findViewById(android.R.id.content));

        builder2.setConnectionCallback(new ConnectionCallback() { // Optional
            @Override
            public void hasActiveConnection(boolean hasActiveConnection) {
                // ...
            }
        });
        builder2.setIndefinite(true); // Optional
        builder2.setNoInternetConnectionMessage("No active Internet connection!"); // Optional
        builder2.setOnAirplaneModeMessage("You have turned on the airplane mode!"); // Optional
        builder2.setSnackbarActionText("Settings");
        builder2.setShowActionToDismiss(false);
        builder2.setSnackbarDismissActionText("OK");

        noInternetSnackbar = builder2.build();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // No Internet Dialog
        if (noInternetDialog != null) {
            noInternetDialog.destroy();
        }

        // No Internet Snackbar
        if (noInternetSnackbar != null) {
            noInternetSnackbar.destroy();
        }
    }
}