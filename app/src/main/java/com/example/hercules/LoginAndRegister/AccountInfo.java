package com.example.hercules.LoginAndRegister;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hercules.R;
import com.orhanobut.hawk.Hawk;

public class AccountInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        TextView textView = findViewById(R.id.text_view);
        Button button = findViewById(R.id.buttonContinue_login);
        ImageView back;
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        Hawk.init(getApplicationContext()).build();
        textView.setText("Name: " + Hawk.get("name") + "\n\n" +
                "Phone: "+Hawk.get("phone") + "\n\n" +
                "Email: "+Hawk.get("email") + "\n\n" +
                "Mailing Name: " + Hawk.get("mailingName") +"\n\n" +
                "Address: " + Hawk.get("address") +"\n\n" +
                "Pincode: " + Hawk.get("pincode") +"\n\n" +
                "State: " + Hawk.get("state") +"\n\n" +
                "Contact Name: " + Hawk.get("contactName") +"\n\n" +
                "Contact Number: " + Hawk.get("contactNumber") +"\n\n" +
                "Gstin: " + Hawk.get("gstin") +"\n\n"
        );

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}