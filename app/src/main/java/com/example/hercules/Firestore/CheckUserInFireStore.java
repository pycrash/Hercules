package com.example.hercules.Firestore;

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
import com.example.hercules.Home.HomeActivity;
import com.example.hercules.LoginAndRegister.LoginSlider;
import com.example.hercules.R;
import com.example.hercules.utils.InternetUtils.CheckInternetConnection;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
        Log.d(TAG, "onCreate: checking ");
        CheckInternetConnection.showNoInternetDialog( CheckUserInFireStore.this, handler);
        checkUser();

    }
    public void checkUser() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Hawk.init(CheckUserInFireStore.this).build();
        DocumentReference doc = db.collection("Users").document(Hawk.get("email"));
        doc.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Intent intent = new Intent(CheckUserInFireStore.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                Hawk.init(CheckUserInFireStore.this).build();
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
                finish();
            } else {
                Intent intent = new Intent(CheckUserInFireStore.this, LoginSlider.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Hawk.deleteAll();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();
            }
        }).addOnFailureListener(e -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(CheckUserInFireStore.this, R.style.MyAlertDialogStyle);
            builder.setMessage("Error connecting to server").setPositiveButton("Retry", (dialogInterface, i) -> checkUser());
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }
}