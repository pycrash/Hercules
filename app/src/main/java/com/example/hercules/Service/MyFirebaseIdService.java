package com.example.hercules.Service;

import com.example.hercules.Models.Token;
import com.example.hercules.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.orhanobut.hawk.Hawk;

public class MyFirebaseIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String tokenRefreshed = FirebaseInstanceId.getInstance().getId();
        updateTokenToFirebase(tokenRefreshed);
    }

    private void updateTokenToFirebase(String tokenRefreshed) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = db.getReference(getString(R.string.token));
        Token token = new Token(tokenRefreshed, false);
        Hawk.init(getApplicationContext()).build();
        databaseReference.child(Hawk.get(getString(R.string.phone))).setValue(token);
    }
}
