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

import com.example.hercules.Database.Database;
import com.example.hercules.Database.Order;
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
    private static final String TAG = "ConfirmationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        Log.d(TAG, "onCreate: here");
        Log.d(TAG, "onCreate: calling no Internet util");
        CheckInternetConnection.showNoInternetDialog(ConfirmationActivity.this, handler);

        discount = findViewById(R.id.discount);
        newTotal = findViewById(R.id.new_total);
        address = findViewById(R.id.address_text_view);
        total = findViewById(R.id.total);
        btnContinue = findViewById(R.id.buttonContinue_login);
        back = findViewById(R.id.back_login);

        Log.d(TAG, "onCreate: getting extra intent from Cart Activity");
        old_total = getIntent().getStringExtra(getString(R.string.total));
        new_Total = getIntent().getStringExtra(getString(R.string.new_total));
        double d = getIntent().getDoubleExtra(getString(R.string.discount), 0);

        Log.d(TAG, "onCreate: got the following info from previous activity");
        Log.d(TAG, "onCreate: old total : " + old_total);
        Log.d(TAG, "onCreate: new total : " + newTotal);
        Log.d(TAG, "onCreate: discount : " + discount);

        Log.d(TAG, "onCreate: building Hawk");
        Hawk.init(getApplicationContext()).build();

        back.setOnClickListener(view -> {
            Log.d(TAG, "onClick: Back Button Pressed, going to cart activity");
            onBackPressed();
            Log.d(TAG, "onClick: finishing this activity");
            finish();
            handler.removeCallbacksAndMessages(null);
        });

        Log.d(TAG, "onCreate: setting the user credentials");
        Log.d(TAG, "onCreate: Company Name : " + Hawk.get(getString(R.string.name)));

        address.setText(getString(R.string.ui_order_confirmation_info, Hawk.get(getString(R.string.name)),
                Hawk.get(getString(R.string.mailingName)), Hawk.get(getString(R.string.phone)),
                Hawk.get(getString(R.string.email)), Hawk.get(getString(R.string.address)),
                Hawk.get(getString(R.string.pincode)), Hawk.get(getString(R.string.state)),
                Hawk.get(getString(R.string.contactName)), Hawk.get(getString(R.string.contactNumber)),
                Hawk.get(getString(R.string.gstin))));

        total.setText(old_total);
        discount.setText(getString(R.string.discount_display, String.valueOf(d)));
        newTotal.setText(new_Total);

        btnContinue.setOnClickListener(view -> {
            ProgressDialog progressDialog = new ProgressDialog(ConfirmationActivity.this, R.style.ProgressDialog);
            progressDialog.setMessage("Placing your order");
            progressDialog.setCancelable(false);
            progressDialog.show();
            order = new Database(ConfirmationActivity.this).getCarts();

            Calendar calendar = Calendar.getInstance();
            String date = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());


            String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
            String orderID = "OD" + date + Hawk.get(getString(R.string.mailingName)).toString().trim().replaceAll(" ", "");

            Log.d(TAG, "onCreate: uploading the customer request to Firebase");
            Requests requests = new Requests(
                    orderID,
                    currentDate,
                    Hawk.get(getString(R.string.name)),
                    Hawk.get(getString(R.string.phone)),
                    Hawk.get(getString(R.string.email)),
                    Hawk.get(getString(R.string.contactName)),
                    Hawk.get(getString(R.string.mailingName)),
                    Hawk.get(getString(R.string.contactNumber)),
                    Hawk.get(getString(R.string.gstin)),
                    discount.getText().toString(),
                    Hawk.get(getString(R.string.address)),
                    Hawk.get(getString(R.string.pincode)),
                    Hawk.get(getString(R.string.state)),
                    getString(R.string.pending),
                    newTotal.getText().toString(),
                    total.getText().toString(),
                    order,
                    "",
                    false);

            request = database.getReference(getString(R.string.firebase_request)).child(getString(R.string.firebase_new_orders));
            request.child(orderID).setValue(requests);

            request = database.getReference(Hawk.get(getString(R.string.mailingName)).toString().replaceAll(" ", "")).child("Orders");
            request.child(orderID).setValue(requests);

            request = database.getReference(Hawk.get(getString(R.string.mailingName)).toString().replaceAll(" ", ""))
                    .child(getString(R.string.firebase_new_orders));
            request.child(orderID).setValue(requests);

            Log.d(TAG, "onCreate: cleaning the cart");
            new Database(getBaseContext()).cleanCart();

            Log.d(TAG, "onCreate: preparing the notification for sending");
            String title = "New Order";
            String message = "You have got a new order - " + orderID;
            NotificationUtil.sendNotification(TAG, title, message, ConfirmationActivity.this);
            Log.d(TAG, "onCreate: notification sent");
            Log.d(TAG, "onCreate: dismissing the progress dialog");
            progressDialog.dismiss();

            Log.d(TAG, "onCreate: starting the order done activity");
            Intent intent = new Intent(getApplicationContext(), OrderDoneActivity.class);
            intent.putExtra(getString(R.string.orderid), orderID);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            finish();
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed: back button is pressed");
        Log.d(TAG, "onBackPressed: going to cart activity");
        Log.d(TAG, "onBackPressed: finishing this activity");
        finish();
        handler.removeCallbacksAndMessages(null);
    }

}