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

public class ReturnPolicyActivity extends AppCompatActivity {
    TextView returnPolicy;
    CardView done;

    public static final String TAG = "Return_Policy_Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_return_policy);
        returnPolicy = findViewById(R.id.privacy_policy);
        returnPolicy.setText(R.string.returns_policy);


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
            startActivity(new Intent(ReturnPolicyActivity.this, HomeActivity.class));
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