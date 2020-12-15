package com.example.hercules.utils.InternetUtils;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.hercules.R;

public class MyReciever extends BroadcastReceiver {
    ExampleBottomSheetDialog bottomSheet;
    @Override
    public void onReceive(final Context context, final Intent intent) {
        bottomSheet = new ExampleBottomSheetDialog();

        String status = NetworkUtil.getConnectivityStatusString(context);
        if(status.isEmpty()||status.equals("No internet is available")||status.equals("No Internet Connection")) {
            status="No Internet Connection";
        }

    }
}
