package com.example.hercules.utils.Notifications;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.hercules.Models.Common;
import com.example.hercules.Models.DataMessage;
import com.example.hercules.Models.MyResponse;
import com.example.hercules.Models.Token;
import com.example.hercules.R;
import com.example.hercules.Remote.APIService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationUtil {
    public static void sendNotification(String TAG, String title, String message, Context context) {

        Log.d(TAG, "sendNotification: building APIService");
        APIService apiService;
        apiService = Common.getFCMService();

        Log.d(TAG, "sendNotification: creating a database reference");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tokens");
        Query data = databaseReference.orderByChild(context.getString(R.string.serverToken)).equalTo(true);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Token serverToken = postSnapshot.getValue(Token.class);

//                 com.example.hercules.Models.Notification notification = new com.example.hercules.Models.Notification("Hercules", "You have new order : "+ orderID);
//                 Sender content = new Sender(serverToken.getToken(), notification);

                    Map<String, String> dataSend = new HashMap<>();
                    dataSend.put(context.getString(R.string.notif_title), title);
                    dataSend.put(context.getString(R.string.notif_message), message);
                    assert serverToken != null;
                    DataMessage content = new DataMessage(serverToken.getToken(), dataSend);
//                  Sender send = new Sender(serverToken.getToken(), dataSend);

                    Log.d(TAG, "onDataChange: sending the notification");
                    apiService.sendNotification(content)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(@NonNull Call<MyResponse> call, @NonNull Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        assert response.body() != null;
                                        if (response.body().success == 1) {
//                                       Toast.makeText(getApplicationContext(), "Notification sent", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "onResponse: Notification sent");
                                        } else {
//                                        Toast.makeText(getApplicationContext(), "Failed !!", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "onResponse: Failed to send notification");
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<MyResponse> call, @NonNull Throwable t) {
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

