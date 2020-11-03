package com.example.hercules.Information;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hercules.Home.HomeActivity;
import com.example.hercules.R;

public class ShippingAndDeliveryPolicyActivity extends AppCompatActivity {
    TextView shippingPolicy;
    CardView done;

    public static final String TAG = "Shipping_Policy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_shipping);
        shippingPolicy = findViewById(R.id.privacy_policy);
        shippingPolicy.setText(R.string.shipping_policy);


        ImageView back;
        back = findViewById(R.id.back);
        back.setOnClickListener(view -> {
            Log.d(TAG, "onClick: back image pressed");
            onBackPressed();
            finish();
        });
        done = findViewById(R.id.done);
        done.setOnClickListener(view -> {
            Log.d(TAG, "onClick: done clicked, going to Home activity");
            startActivity(new Intent(ShippingAndDeliveryPolicyActivity.this, HomeActivity.class));
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: back Button pressed");
        super.onBackPressed();
        finish();
    }
}