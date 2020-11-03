package com.example.hercules.Information;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hercules.Home.HomeActivity;
import com.example.hercules.R;
import com.orhanobut.hawk.Hawk;

public class PrivacyPolicyActivity extends AppCompatActivity {

    TextView privacy_policy;
    CardView done;

    public static final String TAG = "Privacy_Policy_Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_privacy_policy);

        privacy_policy = findViewById(R.id.privacy_policy);
        privacy_policy.setText(R.string.privacy_policy);
        Hawk.init(getApplicationContext()).build();

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
            startActivity(new Intent(PrivacyPolicyActivity.this, HomeActivity.class));
            finish();
        });
        if (!Hawk.contains("email")) {
            done.setVisibility(View.GONE);
        } else {
            done.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: back Button pressed");
        super.onBackPressed();
        finish();
    }
}