package com.example.hercules.OrderActivities;

import androidx.annotation.NonNull;
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

import com.example.hercules.Database.Database;
import com.example.hercules.Database.Order;
import com.example.hercules.Models.Common;
import com.example.hercules.Models.DataMessage;
import com.example.hercules.Models.MyResponse;
import com.example.hercules.Models.Requests;
import com.example.hercules.Models.Sender;
import com.example.hercules.Models.Token;
import com.example.hercules.R;
import com.example.hercules.Remote.APIService;
import com.example.hercules.utils.CheckInternetConnection;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.hawk.Hawk;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmationActivity extends AppCompatActivity {

    TextView address, total;
    Button btnContinue;
    ImageView back;
    List<Order> order = new ArrayList<>();
    String old_total, new_Total;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference request;
    TextView discount, newTotal;
    AlertDialog dialog;
    Handler handler;
    APIService apiService;
    private static final String TAG = "ConfirmationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        discount = findViewById(R.id.discount);
        newTotal = findViewById(R.id.new_total);

        apiService = Common.getFCMService();

        double d = getIntent().getDoubleExtra("discount", 0);
        Hawk.init(getApplicationContext()).build();
        address = findViewById(R.id.address_text_view);
        total = findViewById(R.id.total);
        btnContinue = findViewById(R.id.buttonContinue_login);
        back = findViewById(R.id.back_login);
        old_total = getIntent().getStringExtra("total");
        new_Total = getIntent().getStringExtra("newTotal");
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
        address.setText("Company Name: " + Hawk.get("name") + "\n\n" +
                "Mailing Name: " + Hawk.get("mailingName") + "\n\n" +
                "Phone: " + Hawk.get("phone") + "\n\n" +
                "Email: " + Hawk.get("email") + "\n\n" +
                "Address: " + Hawk.get("address") + "\n\n" +
                "Pincode: " + Hawk.get("pincode") + "\n\n" +
                "State: " + Hawk.get("state") + "\n\n" +
                "Contact Name: " + Hawk.get("contactName") + "\n\n" +
                "Contact Number: " + Hawk.get("contactNumber") + "\n\n" +
                "Gstin: " + Hawk.get("gstin") + "\n\n"
        );

        total.setText(old_total);
        discount.setText(d + " %");
        newTotal.setText(new_Total);

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
                String orderID = "OD" + date + Hawk.get("mailingName").toString().trim().replaceAll(" ", "");

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
                        "",
                        false);
                request = database.getReference("Requests").child("New Orders");
                request.child(orderID).setValue(requests);

                request = database.getReference(Hawk.get("mailingName").toString().replaceAll(" ", "")).child("Orders");
                request.child(orderID).setValue(requests);

                request = database.getReference(Hawk.get("mailingName").toString().replaceAll(" ", "")).child("New Orders");
                request.child(orderID).setValue(requests);

                new Database(getBaseContext()).cleanCart();
                sendNotification(orderID);
                progressDialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), OrderDoneActivity.class);
                intent.putExtra("orderid", orderID);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();
            }
        });

    }

    private void sendNotification(String orderID) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tokens");
        Query data = databaseReference.orderByChild("serverToken").equalTo(true);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Token serverToken = postSnapshot.getValue(Token.class);

//                 com.example.hercules.Models.Notification notification = new com.example.hercules.Models.Notification("Hercules", "You have new order : "+ orderID);
//                 Sender content = new Sender(serverToken.getToken(), notification);


                    Map<String, String> dataSend = new HashMap<>();
                    dataSend.put("title", "New Order");
                    dataSend.put("message", "You have got a new order : " + orderID);
                    assert serverToken != null;
                    DataMessage content = new DataMessage(serverToken.getToken(), dataSend);
//                    Sender send = new Sender(serverToken.getToken(), dataSend);

                    apiService.sendNotification(content)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(@NonNull Call<MyResponse> call,@NonNull Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        assert response.body() != null;
                                        if (response.body().success == 1) {
//                                            Toast.makeText(getApplicationContext(), "Notification sent", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "onResponse: Notification sent");
                                        } else {
//                                        Toast.makeText(getApplicationContext(), "Failed !!", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "onResponse: Failed to send notification");
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<MyResponse> call,@NonNull Throwable t) {
                                    Log.e(TAG, "onFailure: API service failed with the following throwable " + t);

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: sending notification failed " + error);
            }
        });
    }

    void checkInternet() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ConfirmationActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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