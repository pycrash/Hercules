package com.example.hercules.MyOrders;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hercules.Adapters.MyOrderProductsAdapter;
import com.example.hercules.Models.Common;
import com.example.hercules.Models.DataMessage;
import com.example.hercules.Models.MyResponse;
import com.example.hercules.Models.Requests;
import com.example.hercules.Models.Token;
import com.example.hercules.R;
import com.example.hercules.Remote.APIService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailedOrdersActivity extends AppCompatActivity {
    public TextView orderid, date,name, contact_name,mailing_name, phone, contact_phone, address,state,
            pincode,discount, amount, new_amount, gstin, email;
    public CardView done_order, cancel_order;
    RecyclerView recyclerView;
    TextView addNotes;
    TextView status;
    APIService apiService;
    public static final String TAG = "DetailedOrdersActivity";
    Requests pendingOrderModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_orders);

        ImageView back;
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        pendingOrderModel = (Requests) getIntent().getSerializableExtra("orderDetails");
        orderid = findViewById(R.id.new_order_id);
        date = findViewById(R.id.new_order_date);
        name = findViewById(R.id.new_order_name);
        contact_name = findViewById(R.id.new_order_contact_name);
        mailing_name = findViewById(R.id.new_order_mailing_name);
        phone = findViewById(R.id.new_order_number);
        contact_phone = findViewById(R.id.new_order_contact_number);
        address = findViewById(R.id.new_order_address);
        state = findViewById(R.id.new_order_state);
        pincode = findViewById(R.id.new_order_pincode);
        discount = findViewById(R.id.new_order_discount);
        amount = findViewById(R.id.new_order_price);
        new_amount = findViewById(R.id.new_order_new_price);
        status = findViewById(R.id.pending_order_status);
        gstin = findViewById(R.id.new_order_gstin);
        done_order = findViewById(R.id.pending_order_done);
        addNotes = findViewById(R.id.pending_order_add_notes);
        recyclerView = findViewById(R.id.new_order_items_recycler_view);
        email = findViewById(R.id.new_order_email);
        status = findViewById(R.id.pending_order_status);
        cancel_order = findViewById(R.id.cancel_order);


        orderid.setText(pendingOrderModel.getOrderID());
        date.setText(pendingOrderModel.getDate());
        name.setText(pendingOrderModel.getName());
        contact_name.setText(pendingOrderModel.getContactName());
        mailing_name.setText(pendingOrderModel.getMailingName());
        phone.setText(pendingOrderModel.getPhone());
        email.setText(pendingOrderModel.getEmail());
        contact_phone.setText(pendingOrderModel.getContactNumber());
        address.setText(pendingOrderModel.getAddress());
        state.setText(pendingOrderModel.getState());
        pincode.setText(pendingOrderModel.getPincode());
        discount.setText(pendingOrderModel.getDiscount());
        amount.setText(pendingOrderModel.getTotal());
        amount.setPaintFlags(amount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        new_amount.setText(pendingOrderModel.getNewTotal());
        gstin.setText(pendingOrderModel.getGstin());
        addNotes.setText(pendingOrderModel.getNotes());
        status.setText(pendingOrderModel.getStatus());
        addNotes.setText(pendingOrderModel.getNotes());

        if (pendingOrderModel.getStatus().equals("Completed")) {
            cancel_order.setVisibility(View.GONE);
            status.setBackgroundColor(ContextCompat.getColor(DetailedOrdersActivity.this, R.color.colorAccent));
        } else if (pendingOrderModel.getStatus().equals("Cancelled")) {
            cancel_order.setVisibility(View.GONE);
            status.setBackgroundColor(ContextCompat.getColor(DetailedOrdersActivity.this, R.color.red));
        } else {
            status.setBackgroundColor(ContextCompat.getColor(DetailedOrdersActivity.this, R.color.yellow));
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(DetailedOrdersActivity.this));
        MyOrderProductsAdapter cartAdapter = new MyOrderProductsAdapter(pendingOrderModel.getCart(), DetailedOrdersActivity.this);
        recyclerView.setAdapter(cartAdapter);
        done_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();

            }
        });
        cancel_order = findViewById(R.id.cancel_order);
        boolean isCancelled = pendingOrderModel.isCancelled();
        cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCancelled) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailedOrdersActivity.this, R.style.MyAlertDialogStyle);
                    builder.setCancelable(false);
                    builder.setMessage("You have already requested cancellation for this order. We will let you know once the order is cancelled.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Hawk.get("mailingName").toString().replaceAll(" ", "")).child("Orders")
                            .child(pendingOrderModel.getOrderID()).child("status");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String status = snapshot.getValue(String.class);
                            if (status.equals("Pending"))  {
                                FirebaseDatabase db = FirebaseDatabase.getInstance();
                                Map<String, Object> user = new HashMap<>();
                                user.put("cancelled", true);

                                DatabaseReference databaseReference = db.getReference("Requests").child("New Orders").child(orderid.getText().toString());
                                databaseReference.updateChildren(user);
                                databaseReference = db.getReference(mailing_name.getText().toString().trim().replaceAll(" ", "")).child("Orders").child(orderid.getText().toString());
                                databaseReference.updateChildren(user);

                                databaseReference = db.getReference(mailing_name.getText().toString().trim().replaceAll(" ", "")).child("New Orders").child(orderid.getText().toString());
                                databaseReference.updateChildren(user);
                                sendNotification();
                               Toast.makeText(DetailedOrdersActivity.this, "Requested for cancellation", Toast.LENGTH_SHORT).show();
                            } else if (status.equals("Approved")) {
                                FirebaseDatabase db = FirebaseDatabase.getInstance();
                                Map<String, Object> user = new HashMap<>();
                                user.put("cancelled", true);

                                DatabaseReference databaseReference = db.getReference("Requests").child("Pending Orders").child(orderid.getText().toString());
                                databaseReference.updateChildren(user);
                                databaseReference = db.getReference(mailing_name.getText().toString().trim().replaceAll(" ", "")).child("Orders").child(orderid.getText().toString());
                                databaseReference.updateChildren(user);

                                databaseReference = db.getReference(mailing_name.getText().toString().trim().replaceAll(" ", "")).child("Pending Orders").child(orderid.getText().toString());
                                databaseReference.updateChildren(user);
                                sendNotification();
                                Toast.makeText(DetailedOrdersActivity.this, "Requested for cancellation", Toast.LENGTH_SHORT).show();

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(DetailedOrdersActivity.this, R.style.MyAlertDialogStyle);
                                builder.setCancelable(false);
                                builder.setMessage("This order can't be cancelled as this order is already in transit");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
    private void sendNotification() {
        apiService = Common.getFCMService();
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
                    dataSend.put("title", "Cancel Order " + pendingOrderModel.getOrderID());
                    dataSend.put("message", pendingOrderModel.getName() + "has requested to cancel the order");
                    assert serverToken != null;
                    DataMessage content = new DataMessage(serverToken.getToken(), dataSend);
//                    Sender send = new Sender(serverToken.getToken(), dataSend);

                    apiService.sendNotification(content)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(@NonNull Call<MyResponse> call, @NonNull Response<MyResponse> response) {
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

}