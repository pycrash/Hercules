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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hercules.Adapters.CartAdapter;
import com.example.hercules.CheckActivity;
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
import com.google.firebase.firestore.FirestoreRegistrar;
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
    int new_amount;
    Handler handler;
    ProgressDialog progressDialog;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
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



        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this, ConfirmationActivity.class);
                intent.putExtra("total", total.getText().toString());
                intent.putExtra("newTotal", newtotal.getText().toString().trim());
                intent.putExtra("discount", d);
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

    @Override
    protected void onStart() {
        super.onStart();
         progressDialog = new ProgressDialog(CartActivity.this, R.style.ProgressDialog);
         progressDialog.setMessage("Getting your realtime discount");
         progressDialog.setCancelable(false);
         progressDialog.show();

        Hawk.init(CartActivity.this).build();
        DocumentReference doc = db.collection("Users").document(Hawk.get("email"));
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    Hawk.put("discount", documentSnapshot.get("discount"));
                    d = documentSnapshot.getDouble("discount");
                    total.setText(getString(R.string.price, String.valueOf(amount)));
                    newtotal.setText(getString(R.string.price, String.format("%.2f", (double) amount * (double) ((double) (100 - (double) d) / 100))));
                    total.setPaintFlags(total.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                    progressDialog.dismiss();

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this, R.style.MyAlertDialogStyle);
                    builder.setMessage("Session timed out").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(CartActivity.this, LoginSlider.class));
                            Hawk.deleteAll();
                            overridePendingTransition(R.anim.fadeout, R.anim.fadein);
                            finishAffinity();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this, R.style.MyAlertDialogStyle);
                builder.setMessage("Error connecting to server, try again after some time").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onBackPressed();

                    }

                });
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}