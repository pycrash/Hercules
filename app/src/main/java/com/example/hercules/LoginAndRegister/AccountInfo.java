package com.example.hercules.LoginAndRegister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hercules.Home.HomeActivity;
import com.example.hercules.R;
import com.orhanobut.hawk.Hawk;

public class AccountInfo extends AppCompatActivity {

    public final static String TAG = "Account_Info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        TextView textView = findViewById(R.id.text_view);
        Button button = findViewById(R.id.buttonContinue_login);
        ImageView back;
        back = findViewById(R.id.back);
        back.setOnClickListener(view -> {
            Log.d(TAG, "onCreate: back image pressed, finishing the activity");
            onBackPressed();
            finish();
        });
        Log.d(TAG, "onCreate: Building Hawk");
        Hawk.init(getApplicationContext()).build();

        Log.d(TAG, "onCreate: Company Name : " + Hawk.get(getString(R.string.name)));
        Log.d(TAG, "onCreate: ID : " + Hawk.get(getString(R.string.mailingName)));
        Log.d(TAG, "onCreate: Phone:  " + Hawk.get(getString(R.string.phone)));
        Log.d(TAG, "onCreate: Email : " + Hawk.get(getString(R.string.email)));
        Log.d(TAG, "onCreate: Address : " + Hawk.get(getString(R.string.address)));
        Log.d(TAG, "onCreate: Pincode : " + Hawk.get(getString(R.string.pincode)));
        Log.d(TAG, "onCreate: State : " + Hawk.get(getString(R.string.state)));
        Log.d(TAG, "onCreate: Contact Name : " + Hawk.get(getString(R.string.contactName)));
        Log.d(TAG, "onCreate: Contact Number : " + Hawk.get(getString(R.string.contactNumber)));
        Log.d(TAG, "onCreate: GSTIN : " + Hawk.get(getString(R.string.gstin)));

        textView.setText(getString(R.string.ui_order_confirmation_info, Hawk.get(getString(R.string.name)),
                Hawk.get(getString(R.string.mailingName)), Hawk.get(getString(R.string.phone)),
                Hawk.get(getString(R.string.email)), Hawk.get(getString(R.string.address)),
                Hawk.get(getString(R.string.pincode)), Hawk.get(getString(R.string.state)),
                Hawk.get(getString(R.string.contactName)), Hawk.get(getString(R.string.contactNumber)),
                Hawk.get(getString(R.string.gstin))));

        button.setOnClickListener(view -> {
            Log.d(TAG, "onCreate: Done clicked, starting Home activity");
            startActivity(new Intent(AccountInfo.this, HomeActivity.class));
            finish();
        });

    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onCreate: back image pressed, finishing the activity");
        super.onBackPressed();
        finish();
    }
}