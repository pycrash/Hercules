package com.example.hercules.Cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hercules.Adapters.CartAdapter;
import com.example.hercules.Home.HomeActivity;
import com.example.hercules.LoginAndRegister.LoginSlider;
import com.example.hercules.OrderActivities.ConfirmationActivity;
import com.example.hercules.Database.Database;
import com.example.hercules.Database.Order;
import com.example.hercules.Interfaces.ItemClickListener;
import com.example.hercules.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements ItemClickListener {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TextView total, newtotal;
    ImageView back;
    LinearLayout animation, placeOrder;
    CardView place;
    CartAdapter cartAdapter;
    List<Order> cart = new ArrayList<>();
    double amount;
    double d;
    Handler handler;
    ProgressDialog progressDialog;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static final String TAG = "CartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Log.d(TAG, "onCreate: started");

        total = findViewById(R.id.total);
        newtotal = findViewById(R.id.newTotal);
        place = findViewById(R.id.place_order);
        placeOrder = findViewById(R.id.ashu);
        animation = findViewById(R.id.animation_cart);

        Log.d(TAG, "onCreate: setting up recyler view");
        recyclerView = findViewById(R.id.cart_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Log.d(TAG, "onCreate: calling loadCart method");
        loadCart();

        amount = cartAdapter.grandTotal();
        Log.d(TAG, "onCreate: loading grand total, grand total :  " + amount);

        Log.d(TAG, "onCreate: setting up new total");
        newtotal.setText(getString(R.string.price, String.format("%.2f", amount * ((100 - d) / 100))));


        placeOrder.setOnClickListener(view -> {
            Log.d(TAG, "onClick: place order clicked");
            Intent intent = new Intent(CartActivity.this, ConfirmationActivity.class);
            intent.putExtra(getString(R.string.total), total.getText().toString());
            intent.putExtra(getString(R.string.new_total), newtotal.getText().toString().trim());
            intent.putExtra(getString(R.string.discount), d);

            Log.d(TAG, "onClick: starting confirmation activity");
            startActivity(intent);
        });
        handler = new Handler();
        int delay = 10;

        handler.postDelayed(new Runnable() {
            public void run() {
                total.setText(getString(R.string.price, String.valueOf(cartAdapter.totalChange())));
                newtotal.setText(getString(R.string.price, String.format("%.2f", (cartAdapter.totalChange()) * ((100 - d) / 100))));
                cart = new Database(CartActivity.this).getCarts();
                if (cart.size() == 0) {
                    animation.setVisibility(View.VISIBLE);
                    placeOrder.setVisibility(View.GONE);
                }
                handler.postDelayed(this, delay);
            }
        }, delay);
        back = findViewById(R.id.back_login);
        back.setOnClickListener(view -> {
            Log.d(TAG, "onClick: back pressed");
            onBackPressed();
            finish();
            handler.removeCallbacksAndMessages(null);

        });
    }

    private void loadCart() {
        Log.d(TAG, "loadCart: getting cart");
        cart = new Database(this).getCarts();
        if (cart.size() == 0) {
            animation.setVisibility(View.VISIBLE);
            placeOrder.setVisibility(View.GONE);
        }
        cartAdapter = new CartAdapter(cart, CartActivity.this);
        recyclerView.setAdapter(cartAdapter);
        cartAdapter.setClickListener(this);
    }

    @Override
    public void onClick(View view, int position) {
        total.setText(getString(R.string.price, String.valueOf(cartAdapter.totalChange())));
        newtotal.setText(getString(R.string.price, String.format("%.2f", (cartAdapter.totalChange()) * ((100 - d) / 100))));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: user can see activity");
        Log.d(TAG, "onStart: showing progress dialog");
        progressDialog = new ProgressDialog(CartActivity.this, R.style.ProgressDialog);
        progressDialog.setMessage("Getting your realtime discount");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Log.d(TAG, "onStart: building Hawk");
        Hawk.init(CartActivity.this).build();

        Log.d(TAG, "onStart: connecting to Firestore");
        DocumentReference doc = db.collection(getString(R.string.users)).document(Hawk.get(getString(R.string.email)));
        doc.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Log.d(TAG, "onStart: document snapshot exists");
                Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                Hawk.put(getString(R.string.discount), documentSnapshot.get(getString(R.string.discount)));
                if (documentSnapshot.getDouble(getString(R.string.discount)) != null) {
                    d = documentSnapshot.getDouble(getString(R.string.discount));
                } else {
                    d = 0;
                }
                total.setText(getString(R.string.price, String.valueOf(amount)));
                newtotal.setText(getString(R.string.price, String.format("%.2f", amount * ((100 - d) / 100))));
                total.setPaintFlags(total.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                progressDialog.dismiss();

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this, R.style.MyAlertDialogStyle);
                builder.setMessage("Session timed out").setPositiveButton("Ok", (dialogInterface, i) -> {
                    Log.d(TAG, "onClick: going to LoginSlider activity");
                    startActivity(new Intent(CartActivity.this, LoginSlider.class));
                    Hawk.deleteAll();
                    overridePendingTransition(R.anim.fadeout, R.anim.fadein);
                    finish();
                });
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "onStart: could not connect to server");
            Log.e(TAG, "onStart: got the following error  " + e);
            AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this, R.style.MyAlertDialogStyle);
            builder.setMessage("Error connecting to server, try again after some time").setPositiveButton("Ok", (dialogInterface, i) -> {
                onBackPressed();
                finish();

            });
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }
}