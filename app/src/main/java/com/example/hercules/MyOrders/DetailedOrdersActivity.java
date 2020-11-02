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
import com.example.hercules.utils.InternetUtils.CheckInternetConnection;
import com.example.hercules.utils.Notifications.NotificationUtil;
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
    public TextView orderid, date, name, contact_name, mailing_name, phone, contact_phone, address, state,
            pincode, discount, amount, new_amount, gstin, email, textCancel;
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

        Log.d(TAG, "onCreate: ");
        ImageView back;
        back = findViewById(R.id.back);
        back.setOnClickListener(view -> {
            Log.d(TAG, "onCreate: back image pressed");
            onBackPressed();
            Log.d(TAG, "onCreate: finishing this activity");
            finish();
        });

        Log.d(TAG, "onCreate: ");
        pendingOrderModel = (Requests) getIntent().getSerializableExtra(getString(R.string.orderDetails));
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
        textCancel = findViewById(R.id.text_cancellation);


        Log.d(TAG, "onCreate: got the following info from previous activity");
        Log.d(TAG, "onCreate: Order ID : " + pendingOrderModel.getOrderID());
        Log.d(TAG, "onCreate: Order Date : " + pendingOrderModel.getDate());
        Log.d(TAG, "onCreate: ID : " + pendingOrderModel.getName());
        Log.d(TAG, "onCreate: Contact Name : " + pendingOrderModel.getContactName());
        Log.d(TAG, "onCreate: Company Name : " + pendingOrderModel.getMailingName());
        Log.d(TAG, "onCreate: Phone : " + pendingOrderModel.getPhone());
        Log.d(TAG, "onCreate: Email : " + pendingOrderModel.getEmail());
        Log.d(TAG, "onCreate: Contact Number : " + pendingOrderModel.getContactNumber());
        Log.d(TAG, "onCreate: Address : " + pendingOrderModel.getAddress());
        Log.d(TAG, "onCreate: State : " + pendingOrderModel.getState());
        Log.d(TAG, "onCreate: Pincode : " + pendingOrderModel.getPincode());
        Log.d(TAG, "onCreate: Discount : " + pendingOrderModel.getDiscount());
        Log.d(TAG, "onCreate: Total : " + pendingOrderModel.getTotal());
        Log.d(TAG, "onCreate: New Total : " + pendingOrderModel.getNewTotal());
        Log.d(TAG, "onCreate: Notes : " + pendingOrderModel.getNotes());
        Log.d(TAG, "onCreate: Status : " + pendingOrderModel.getStatus());

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

        if (pendingOrderModel.getStatus().equals(getString(R.string.completed))) {
            Log.d(TAG, "onCreate: order is completed");
            Log.d(TAG, "onCreate: making cancel order visibility invisible");
            cancel_order.setVisibility(View.GONE);
            Log.d(TAG, "onCreate: making status textview color green");
            status.setBackgroundColor(ContextCompat.getColor(DetailedOrdersActivity.this, R.color.colorAccent));
        } else if (pendingOrderModel.getStatus().equals(getString(R.string.cancelled))) {
            Log.d(TAG, "onCreate: order is cancelled");
            Log.d(TAG, "onCreate: making cancel order visibility invisible");
            cancel_order.setVisibility(View.GONE);
            Log.d(TAG, "onCreate: making status textview color red");
            status.setBackgroundColor(ContextCompat.getColor(DetailedOrdersActivity.this, R.color.red));
        } else {
            Log.d(TAG, "onCreate: order is in transit");
            Log.d(TAG, "onCreate: making status textview color yellow");
            status.setBackgroundColor(ContextCompat.getColor(DetailedOrdersActivity.this, R.color.yellow));
        }

        Log.d(TAG, "onCreate: setting up recycler view");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(DetailedOrdersActivity.this));
        MyOrderProductsAdapter cartAdapter = new MyOrderProductsAdapter(pendingOrderModel.getCart(), DetailedOrdersActivity.this);
        recyclerView.setAdapter(cartAdapter);
        done_order.setOnClickListener(view -> {
            Log.d(TAG, "onCreate: user has clicked done, going to MyOrders activity");
            onBackPressed();
            Log.d(TAG, "onCreate: finishing this activity");
            finish();

        });

        cancel_order = findViewById(R.id.cancel_order);
        boolean isCancelled = pendingOrderModel.isCancelled();
        if (isCancelled) {
            Log.d(TAG, "onCreate: user has already requested to cancel this order");
            textCancel.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, "onCreate: user has not requested to cancel this order");
            textCancel.setVisibility(View.GONE);
        }

        cancel_order.setOnClickListener(view -> {
            Log.d(TAG, "onCreate: user has requested to cancel the order");
            if (!CheckInternetConnection.check(DetailedOrdersActivity.this)) {
                Toast.makeText(DetailedOrdersActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
            } else {
                if (isCancelled) {
                    Log.d(TAG, "onCreate: user has previously requeted this service");
                    Log.d(TAG, "onCreate: notifying the user that he has already requested cancellation for this order");
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailedOrdersActivity.this, R.style.MyAlertDialogStyle);
                    builder.setCancelable(false);
                    builder.setMessage("You have already requested cancellation for this order. We will let you know once the order is cancelled.");
                    builder.setPositiveButton("OK", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        Log.d(TAG, "onClick: dismissing the dialog");
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    Log.d(TAG, "onCreate: sending this order for cancellation");
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Hawk.get(getString(R.string.mailingName))
                            .toString().replaceAll(" ", "")).child(getString(R.string.firebase_orders))
                            .child(pendingOrderModel.getOrderID()).child(getString(R.string.status));
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String status = snapshot.getValue(String.class);
                            if (status.equals(getString(R.string.pending))) {
                                FirebaseDatabase db = FirebaseDatabase.getInstance();
                                Map<String, Object> user = new HashMap<>();
                                user.put("cancelled", true);

                                DatabaseReference databaseReference = db.getReference(getString(R.string.firebase_request))
                                        .child(getString(R.string.firebase_new_orders)).child(orderid.getText().toString());
                                databaseReference.updateChildren(user);
                                databaseReference = db.getReference(mailing_name.getText().toString().trim().replaceAll(" ", ""))
                                        .child(getString(R.string.firebase_orders)).child(orderid.getText().toString());
                                databaseReference.updateChildren(user);

                                databaseReference = db.getReference(mailing_name.getText().toString().trim().replaceAll(" ", ""))
                                        .child(getString(R.string.firebase_new_orders)).child(orderid.getText().toString());
                                databaseReference.updateChildren(user);

                                Log.d(TAG, "onDataChange: sending notification to admin");
                                String title = "Cancel Order " + pendingOrderModel.getOrderID();
                                String message = pendingOrderModel.getName() + "has requested to cancel the order";
                                NotificationUtil.sendNotification(TAG, title, message, DetailedOrdersActivity.this);

                                Log.d(TAG, "onDataChange: showing success toast to user");
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

                                Log.d(TAG, "onDataChange: sending notification to admin");
                                String title = "Cancel Order " + pendingOrderModel.getOrderID();
                                String message = pendingOrderModel.getName() + "has requested to cancel the order";
                                NotificationUtil.sendNotification(TAG, title, message, DetailedOrdersActivity.this);

                                Log.d(TAG, "onDataChange: showing success toast to user");
                                Toast.makeText(DetailedOrdersActivity.this, "Requested for cancellation", Toast.LENGTH_SHORT).show();

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(DetailedOrdersActivity.this, R.style.MyAlertDialogStyle);
                                builder.setCancelable(false);
                                builder.setMessage("This order can't be cancelled as this order is already in transit");
                                builder.setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss());
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d(TAG, "onCancelled: error encountered while retrieving childs");
                            Log.d(TAG, "onCancelled: error : " + error);
                            Toast.makeText(DetailedOrdersActivity.this, "Can't connect to server, try again after some time", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}