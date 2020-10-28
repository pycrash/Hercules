package com.example.hercules.SplashScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hercules.CheckActivity;
import com.example.hercules.Home.HomeActivity;
import com.example.hercules.LoginAndRegister.LoginSlider;
import com.example.hercules.R;
import com.example.hercules.utils.CheckInternetConnection;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.hawk.Hawk;

public class SplashScreen extends AppCompatActivity {
    String TAG = "SplashScreen";
    private static final int SPLASH_DELAY = 900;
    boolean userLoggedIn = false;
    private final Handler mHandler = new Handler();
    private final Launcher mLauncher = new Launcher();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Hawk.init(SplashScreen.this).build();
        userLoggedIn = (Hawk.contains("email"));
        SharedPreferences appSettingPrefs = getSharedPreferences("AppSettingPrefs", 0);
        boolean isNightModeOn = appSettingPrefs.getBoolean("NightMode", false);

        if (isNightModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }
    @Override
    protected void onStop() {
        mHandler.removeCallbacks(mLauncher);
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mHandler.postDelayed(mLauncher, SPLASH_DELAY);
    }
    private void launch() {
        if (!isFinishing()) {
            if (userLoggedIn) {
                Intent intent = new Intent(SplashScreen.this, CheckActivity.class);
                Log.d(TAG, "onCreate: SplashScreen to login activity intent started");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();


            } else {

                Intent intent = new Intent(SplashScreen.this, LoginSlider.class);
                Log.d(TAG, "onCreate: SplashScreen to login activity intent started");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();

            }
            finish();
        }
    }
    private class Launcher implements Runnable {
        @Override
        public void run() {
            launch();
        }
    }
}