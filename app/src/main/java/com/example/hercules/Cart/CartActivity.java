package com.example.hercules.Cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hercules.Adapters.CartAdapter;
import com.example.hercules.Home.HomeActivity;
import com.example.hercules.OrderActivities.ConfirmationActivity;
import com.example.hercules.Database.Database;
import com.example.hercules.Database.Order;
import com.example.hercules.Interfaces.ItemClickListener;
import com.example.hercules.R;
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
    int amount;
    int d;
    int new_amount;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        total = findViewById(R.id.total);
        newtotal = findViewById(R.id.newTotal);


        place = findViewById(R.id.place_order);
        placeOrder = findViewById(R.id.ashu);
        animation = findViewById(R.id.animation_cart);
        recyclerView = findViewById(R.id.cart_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        loadCart();
        amount = cartAdapter.grandTotal();
        newtotal.setText(getString(R.string.price, String.format("%.2f", (double) amount * (double) ((double) (100 - (double) d) / 100))));

        Hawk.init(getApplicationContext()).build();
        d =  Hawk.get("discount", 0);
        total.setText(getString(R.string.price, String.valueOf(amount)));
        newtotal.setText(getString(R.string.price, String.format("%.2f", (double) amount * (double) ((double) (100 - (double) d) / 100))));
        total.setPaintFlags(total.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this, ConfirmationActivity.class);
                intent.putExtra("total", Integer.parseInt(total.getText().toString().replaceAll("[^\\d]", " ").trim()));
                startActivity(intent);

            }
        });
        handler= new Handler();
        int delay = 10;

        handler.postDelayed(new Runnable(){
            public void run(){

                total.setText(getString(R.string.price, String.valueOf(cartAdapter.totalChange())));
                newtotal.setText(getString(R.string.price, String.format("%.2f", (double) (cartAdapter.totalChange()) * (double) ((double) (100 - (double) d) / 100))));
                cart = new Database(CartActivity.this).getCarts();
                if(cart.size() == 0) {
                    animation.setVisibility(View.VISIBLE);
                    placeOrder.setVisibility(View.GONE);
                }
                handler.postDelayed(this, delay);
            }
        }, delay);
        back = findViewById(R.id.back_login);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
                handler.removeCallbacksAndMessages(null);

            }
        });
    }

    private void loadCart() {
        cart = new Database(this).getCarts();
        if(cart.size() == 0) {
            animation.setVisibility(View.VISIBLE);
            placeOrder.setVisibility(View.GONE);
        }
        cartAdapter  = new CartAdapter(cart, CartActivity.this);
        recyclerView.setAdapter(cartAdapter);
        cartAdapter.setClickListener(this);
    }

    @Override
    public void onClick(View view, int position) {
        total.setText(getString(R.string.price, String.valueOf(cartAdapter.totalChange())));
        newtotal.setText(getString(R.string.price, String.format("%.2f", (double) (cartAdapter.totalChange()) * (double) ((double) (100 - (double) d) / 100))));
    }
}