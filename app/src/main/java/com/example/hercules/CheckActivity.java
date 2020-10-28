package com.example.hercules;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.hercules.Home.HomeActivity;
import com.example.hercules.LoginAndRegister.LoginSlider;
import com.example.hercules.OrderActivities.ConfirmationActivity;
import com.example.hercules.SplashScreen.SplashScreen;
import com.example.hercules.utils.CheckInternetConnection;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.hawk.Hawk;

public class CheckActivity extends AppCompatActivity {
    AlertDialog dialog;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        checkInternet();
        checkUser();

    }
    public void checkUser() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Hawk.init(CheckActivity.this).build();
        DocumentReference doc = db.collection("Users").document(Hawk.get("email"));
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Intent intent = new Intent(CheckActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    Hawk.init(CheckActivity.this).build();
                    Hawk.put("name", documentSnapshot.get("name"));
                    Hawk.put("email", documentSnapshot.get("email"));
                    Hawk.put("phone", documentSnapshot.get("phone"));
                    Hawk.put("mailingName", documentSnapshot.get("mailingName"));
                    Hawk.put("address", documentSnapshot.get("address"));
                    Hawk.put("pincode", documentSnapshot.get("pincode"));
                    Hawk.put("state", documentSnapshot.get("state"));
                    Hawk.put("contactName", documentSnapshot.get("contactName"));
                    Hawk.put("contactNumber", documentSnapshot.get("contactNumber"));
                    Hawk.put("gstin", documentSnapshot.get("gstin"));
                    Hawk.put("discount", documentSnapshot.get("discount"));

                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    handler.removeCallbacksAndMessages(null);
                    finish();
                } else {
                    Intent intent = new Intent(CheckActivity.this, LoginSlider.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Hawk.deleteAll();
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    handler.removeCallbacksAndMessages(null);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CheckActivity.this, R.style.MyAlertDialogStyle);
                builder.setMessage("Error connecting to server").setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        checkUser();
                    }
                });
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    void checkInternet() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(CheckActivity.this);
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
                boolean isInternet = CheckInternetConnection.checkInternet(CheckActivity.this);
                if (!isInternet) {
                    dialog.show();
                } else {
                    dialog.hide();
                }
            }
        }, 20);
    }
}