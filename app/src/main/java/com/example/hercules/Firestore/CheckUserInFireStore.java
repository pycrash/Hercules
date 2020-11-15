package com.example.hercules.Firestore;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.hercules.Home.HomeActivity;
import com.example.hercules.LoginAndRegister.LoginSlider;
import com.example.hercules.R;
import com.example.hercules.utils.InternetUtils.CheckInternetConnection;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.hawk.Hawk;

public class CheckUserInFireStore extends AppCompatActivity {

    public static final String TAG = "CheckUserInFireStore";
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        Log.d(TAG, "onCreate: started");

        Log.d(TAG, "onCreate: calling different methods");
        Log.d(TAG, "onCreate: checking internet connection");
        handler = new Handler();
        CheckInternetConnection.showNoInternetDialog(CheckUserInFireStore.this, handler);

        Log.d(TAG, "onCreate: calling checkUser method");
        checkUser();

    }

    public void checkUser() {
        Log.d(TAG, "checkUser: started");

        Log.d(TAG, "checkUser: connecting to FireStore5");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Hawk.init(CheckUserInFireStore.this).build();
        DocumentReference doc = db.collection(getString(R.string.users)).document(Hawk.get(getString(R.string.email)));
        doc.get().addOnSuccessListener(documentSnapshot -> {
            Intent intent;
            if (documentSnapshot.exists()) {
                Log.d(TAG, "checkUser: document snapshot exists");

                intent = new Intent(CheckUserInFireStore.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                Log.d(TAG, "checkUser: building Hawk");
                Hawk.init(CheckUserInFireStore.this).build();

                Log.d(TAG, "checkUser: saving user credentials");
                Hawk.put(getString(R.string.companyName), documentSnapshot.get(getString(R.string.companyName)));
                Hawk.put(getString(R.string.email), documentSnapshot.get(getString(R.string.email)));
                Hawk.put(getString(R.string.phone), documentSnapshot.get(getString(R.string.phone)));
                Hawk.put(getString(R.string.id), documentSnapshot.get(getString(R.string.id)));
                Hawk.put(getString(R.string.address), documentSnapshot.get(getString(R.string.address)));
                Hawk.put(getString(R.string.pincode), documentSnapshot.get(getString(R.string.pincode)));
                Hawk.put(getString(R.string.state), documentSnapshot.get(getString(R.string.state)));
                Hawk.put(getString(R.string.contactName), documentSnapshot.get(getString(R.string.contactName)));
                Hawk.put(getString(R.string.contactNumber), documentSnapshot.get(getString(R.string.contactNumber)));
                Hawk.put(getString(R.string.gstin), documentSnapshot.get(getString(R.string.gstin)));
                Hawk.put(getString(R.string.discount), documentSnapshot.get(getString(R.string.discount)));

                Log.d(TAG, "checkUser: saved the following info");
                Log.d(TAG, "onCreate: Company Name : " + Hawk.get(getString(R.string.companyName)));
                Log.d(TAG, "onCreate: ID : " + Hawk.get(getString(R.string.id)));
                Log.d(TAG, "onCreate: Phone:  " + Hawk.get(getString(R.string.phone)));
                Log.d(TAG, "onCreate: Email : " + Hawk.get(getString(R.string.email)));
                Log.d(TAG, "onCreate: Address : " + Hawk.get(getString(R.string.address)));
                Log.d(TAG, "onCreate: Pincode : " + Hawk.get(getString(R.string.pincode)));
                Log.d(TAG, "onCreate: State : " + Hawk.get(getString(R.string.state)));
                Log.d(TAG, "onCreate: Contact Name : " + Hawk.get(getString(R.string.contactName)));
                Log.d(TAG, "onCreate: Contact Number : " + Hawk.get(getString(R.string.contactNumber)));
                Log.d(TAG, "onCreate: GSTIN : " + Hawk.get(getString(R.string.gstin)));

            } else {
                Log.d(TAG, "checkUser: user doesn't exists in Firestore");
                Log.d(TAG, "checkUser: going to loginslider activity");
                intent = new Intent(CheckUserInFireStore.this, LoginSlider.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                Hawk.deleteAll();
            }
            Log.d(TAG, "checkUser: starting activity");
            startActivity(intent);
            Log.d(TAG, "checkUser: overriding the pending transitions and finishing");
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            finish();
        }).addOnFailureListener(e -> {
            Log.d(TAG, "checkUser: could not connect to server");
            Log.d(TAG, "checkUser: got the following error " + e);
            Log.d(TAG, "checkUser: showing the alert dialog to user");
            AlertDialog.Builder builder = new AlertDialog.Builder(CheckUserInFireStore.this, R.style.MyAlertDialogStyle);
            builder.setMessage("Error connecting to server").setPositiveButton("Retry", (dialogInterface, i) -> checkUser());
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }
}