package com.example.hercules.OrderActivities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hercules.Cart.CartActivity;
import com.example.hercules.Database.Database;
import com.example.hercules.Database.Order;
import com.example.hercules.Models.Requests;
import com.example.hercules.R;
import com.example.hercules.utils.CheckInternetConnection;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.hawk.Hawk;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ConfirmationActivity extends AppCompatActivity {

    TextView address, total;
    Button btnContinue;
    ImageView back;
    List<Order> order = new ArrayList<>();
    int totalAmount;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference request ;
    TextView discount, newTotal;
    AlertDialog dialog;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        discount = findViewById(R.id.discount);
        newTotal = findViewById(R.id.new_total);

        int d =  Hawk.get("discount", 0);
        Hawk.init(getApplicationContext()).build();
        address = findViewById(R.id.address_text_view);
        total = findViewById(R.id.total);
        btnContinue = findViewById(R.id.buttonContinue_login);
        back = findViewById(R.id.back_login);
        totalAmount = getIntent().getIntExtra("total", 0);
        checkInternet();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
                handler.removeCallbacksAndMessages(null);
            }
        });

        Hawk.init(getApplicationContext()).build();
        address.setText("Name: " + Hawk.get("name") + "\n\n" +
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

        total.setText(getString(R.string.price, String.valueOf(getIntent().getIntExtra("total", 0))));
        discount.setText(d + " %");
        newTotal.setText(getString(R.string.price, String.format("%.2f", (double) totalAmount * (double) ((double) (100 - (double) d) / 100))));

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog = new ProgressDialog(ConfirmationActivity.this, R.style.ProgressDialog);
                progressDialog.setMessage("Placing your order");
                progressDialog.setCancelable(false);
                progressDialog.show();
                order = new Database(ConfirmationActivity.this).getCarts();

                Calendar calendar = Calendar.getInstance();
                String date = new SimpleDateFormat("ddMMyyyyHHMMss").format(new Date());


                String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                String orderID = "OD"+date+Hawk.get("name").toString().trim();

                Requests requests = new Requests(
                        orderID,
                        currentDate,
                        Hawk.get("name"),
                        Hawk.get("phone"),
                        Hawk.get("email"),
                        Hawk.get("contactName"),
                        Hawk.get("mailingName"),
                        Hawk.get("contactNumber"),
                        Hawk.get("gstin"),
                        discount.getText().toString(),
                        Hawk.get("address"),
                        Hawk.get("pincode"),
                        Hawk.get("state"),
                        "Pending",
                        newTotal.getText().toString(),
                        total.getText().toString(),
                        order,
                        "");
                request = database.getReference("Requests").child("New Orders");
                request.child(orderID).setValue(requests);

                request = database.getReference(Hawk.get("name")).child("Orders");
                request.child(orderID).setValue(requests);

                request = database.getReference(Hawk.get("name")).child("New Orders");
                request.child(orderID).setValue(requests);

                Toast.makeText(getApplicationContext(), "Order Placed", Toast.LENGTH_SHORT).show();
                new Database(getBaseContext()).cleanCart();
                progressDialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), OrderDoneActivity.class);
                intent.putExtra("orderid", orderID);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.fadeout);
                finish();
            }
        });

    }
    void checkInternet() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ConfirmationActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View mView = inflater.inflate(R.layout.dialog_no_internet, null);
        mBuilder.setView(mView);
        mBuilder.setCancelable(false);
        dialog = mBuilder.create();
         handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 10);
                boolean isInternet = CheckInternetConnection.checkInternet(ConfirmationActivity.this);
                if (!isInternet) {
                    dialog.show();
                } else {
                    dialog.hide();
                }
            }
        }, 20);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        handler.removeCallbacksAndMessages(null);
    }
}