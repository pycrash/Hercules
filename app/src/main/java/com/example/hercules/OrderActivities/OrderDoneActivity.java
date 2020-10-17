package com.example.hercules.OrderActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hercules.Home.HomeActivity;
import com.example.hercules.R;

public class OrderDoneActivity extends AppCompatActivity {

    Button btnContinue;
    TextView orderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_done);

        orderId = findViewById(R.id.order_id);
        orderId.setText(getIntent().getStringExtra("orderid"));
        btnContinue = findViewById(R.id.buttonContinue_login);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderDoneActivity.this, HomeActivity.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();
                finishAffinity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(OrderDoneActivity.this, HomeActivity.class));
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        finishAffinity();

    }
}