package com.example.hercules.SplashScreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.hercules.CheckActivity;
import com.example.hercules.Constants.Constants;
import com.example.hercules.LoginAndRegister.LoginSlider;
import com.example.hercules.R;
import com.orhanobut.hawk.Hawk;

public class SplashScreen extends AppCompatActivity {
    public final static String TAG = "SplashScreen";

    //variable to check if Hawk contains email or not
    boolean userLoggedIn = false;

    //Handler and launcher for splash screen
    private final Handler mHandler = new Handler();
    private final Launcher mLauncher = new Launcher();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        Log.d(TAG, "onCreate: content set for splash screen");

        //Checking for stored shared prefs if any for dark theme
        Log.d(TAG, "onCreate: getting AppSettingPrefs");
        SharedPreferences appSettingPrefs = getSharedPreferences(getString(R.string.night_mode_shared_prefs), 0);
        Log.d(TAG, "onCreate: checking if user has previously toggled night mode or not ");
        boolean isNightModeOn = appSettingPrefs.getBoolean(getString(R.string.night_mode_boolean), false);
        Log.d(TAG, "onCreate: value of isNightMode = " + isNightModeOn);

        if (isNightModeOn) {
            Log.d(TAG, "onCreate: switching to night mode");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            Log.d(TAG, "onCreate: switching to light mode");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        //Building Hawk
        Log.d(TAG, "onCreate: building Hawk");
        Hawk.init(SplashScreen.this).build();
        Log.d(TAG, "onCreate: Hawk successfully built");

        //Checking if Hawk contains email or not
        Log.d(TAG, "onCreate: checking if hawk contains email or not");
        userLoggedIn = (Hawk.contains(getString(R.string.email)));
        Log.d(TAG, "onCreate: value of userLoggedIn = " + userLoggedIn);


    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: called");
        Log.d(TAG, "onStop: removing handler callbacks");
        mHandler.removeCallbacks(mLauncher);
        super.onStop();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: called");
        Log.d(TAG, "onStart: screen visible to user");
        super.onStart();
        Log.d(TAG, "onStart: delaying the splash screen to " + Constants.SPLASH_DELAY + "ms");
        Log.d(TAG, "onStart: Splash Delay value = " + Constants.SPLASH_DELAY + "ms");
        mHandler.postDelayed(mLauncher, Constants.SPLASH_DELAY);
    }

    private void launch() {
        if (!isFinishing()) {
            Intent intent;
            if (userLoggedIn) {
                Log.d(TAG, "launch: hawk contains user credentials");
                Log.d(TAG, "launch: creating intent for CheckActivity to check the user in Server");
                intent = new Intent(SplashScreen.this, CheckActivity.class);
            } else {
                Log.d(TAG, "launch: Hawk doesn't consist anything");
                Log.d(TAG, "launch: creating intent to login screen");
                intent = new Intent(SplashScreen.this, LoginSlider.class);
            }
            Log.d(TAG, "launch: setting flags and finishing tasks and creating new tasks");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.d(TAG, "launch: starting the required activity");
            startActivity(intent);
            Log.d(TAG, "launch: overriding the pending anmations");
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            Log.d(TAG, "launch: finishing splash screen activity");
            finish();
        }
    }

    private class Launcher implements Runnable {
        @Override
        public void run() {
            Log.d(TAG, "run: calling launch method to launch the specified activity according to Hawk");
            launch();
        }
    }
}