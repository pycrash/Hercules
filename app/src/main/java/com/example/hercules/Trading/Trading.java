package com.example.hercules.Trading;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hercules.R;
import com.example.hercules.utils.InternetUtils.CheckInternetConnection;
import com.example.hercules.utils.Notifications.NotificationUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trading extends AppCompatActivity {
    CardView done, request;
    ArrayList<Integer> itemsSelected;
    ImageView back;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    androidx.appcompat.app.AlertDialog dialog;
    Handler handler;
    public final static String TAG = "Trading";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trading);

        Log.d(TAG, "onCreate: calling no internet method");
        CheckInternetConnection.showNoInternetDialog( Trading.this, handler);

        back = findViewById(R.id.back);
        back.setOnClickListener(view -> {
            Log.d(TAG, "onClick: back pressed");
            onBackPressed();
            Log.d(TAG, "onClick: finishing the activity");
            finish();
            Log.d(TAG, "onClick: removing callbacks from handler");
            handler.removeCallbacksAndMessages(null);
        });

        Log.d(TAG, "onCreate: building Hawk");
        Hawk.init(Trading.this).build();

        Log.d(TAG, "onCreate: building request documents dialog");
        Dialog dialog;
        final String[] items = {getString(R.string.ledger), getString(R.string.SOL)};
        itemsSelected = new ArrayList<>();


        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        builder.setTitle("Select the ones you want to request : ");
        builder.setMultiChoiceItems(items, null,
                (dialog1, selectedItemId, isSelected) -> {
                    if (isSelected) {
                        Log.d(TAG, "onClick: adding " + selectedItemId + " item to ArrayList");
                        itemsSelected.add(selectedItemId);
                    } else if (itemsSelected.contains(selectedItemId)) {
                        Log.d(TAG, "onClick: removing " + selectedItemId + " item from ArrayList");
                        itemsSelected.remove(Integer.valueOf(selectedItemId));
                    }
                })
                .setPositiveButton("Done", (dialog12, id) -> {
                    if (itemsSelected.size() == 0) {
                        Log.d(TAG, "onClick: nothing selected");
                        Toast.makeText(getApplicationContext(), "No Items Selected", Toast.LENGTH_LONG).show();
                    } else {
                        Log.d(TAG, "onClick: ledger or SOL selected, going to requestDocs method");
                        requestDocs();
                    }
                })
                .setNegativeButton("Cancel", (dialog13, id) -> {
                    Log.d(TAG, "onClick: Cancel clicked, dismissing dialog");
                    dialog13.dismiss();
                });

        dialog = builder.create();

        ViewPager viewPager = findViewById(R.id.container);
        Log.d(TAG, "onCreate: setting up ViewPager");
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        done = findViewById(R.id.done);
        done.setOnClickListener(view -> {
            Log.d(TAG, "onClick: done clicked, going to home");
            onBackPressed();
            Log.d(TAG, "onClick: finishing the activity");
            handler.removeCallbacksAndMessages(null);
            finish();
        });

        request = findViewById(R.id.request);
        request.setOnClickListener(view -> {
            Log.d(TAG, "onClick: request clicked");
            Log.d(TAG, "onClick: showing the custom dialog");
            dialog.show();
        });
    }

    private void requestDocs() {

        if (itemsSelected.contains(0)) {
            Log.d(TAG, "requestDocs: ledger is selected");
            Log.d(TAG, "requestDocs: going to checkLedger method");
            checkLedger();
        }
        if (itemsSelected.contains(1)) {
            Log.d(TAG, "requestDocs: SOL is selected");
            Log.d(TAG, "requestDocs: going to checkSOL method");
            checkSOL();
        }

    }

    private void checkLedger() {

        Log.d(TAG, "checkLedger: building progress dialog");
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(Trading.this, R.style.ProgressDialog);
        progressDialog.setMessage("Requesting Ledger .....");
        progressDialog.setCancelable(false);
        Log.d(TAG, "checkLedger: showing progress dialog");
        progressDialog.show();

        Log.d(TAG, "checkLedger: checking if the request already exists in FireStore or not !!");
        DocumentReference phoneCheck = db.collection(getString(R.string.ledger)).document(Hawk.get(getString(R.string.email)));
        phoneCheck.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Log.d(TAG, "onSuccess: documentSnapshot exists, displaying Toast");
                Toast.makeText(getApplicationContext(), "You have already requested Ledger", Toast.LENGTH_SHORT).show();

            } else {

                Log.d(TAG, "onSuccess: documentSnapshot doesn't exists");
                Log.d(TAG, "onSuccess: starting user request to upload in Firestore");

                final Map<String, Object> User = new HashMap<>();
                User.put(getString(R.string.name), Hawk.get(getString(R.string.name)));
                User.put(getString(R.string.email), Hawk.get(getString(R.string.email)));
                User.put(getString(R.string.phone), Hawk.get(getString(R.string.phone)));
                User.put(getString(R.string.mailingName), Hawk.get(getString(R.string.mailingName)));
                User.put(getString(R.string.address), Hawk.get(getString(R.string.address)));
                User.put(getString(R.string.pincode), Hawk.get(getString(R.string.pincode)));
                User.put(getString(R.string.state), Hawk.get(getString(R.string.state)));
                User.put(getString(R.string.contactName), Hawk.get(getString(R.string.contactName)));
                User.put(getString(R.string.contactNumber), Hawk.get(getString(R.string.contactNumber)));
                User.put(getString(R.string.gstin), Hawk.get(getString(R.string.gstin)));
                User.put(getString(R.string.discount), Hawk.get(getString(R.string.discount)));

                Log.d(TAG, "onSuccess: uploading the following documents in Firestore in collection Ledger");
                Log.d(TAG, "UserCredentials: " + getString(R.string.name) + " : " + Hawk.get(getString(R.string.name)));
                Log.d(TAG, "UserCredentials: " + getString(R.string.email) + " : " + Hawk.get(getString(R.string.email)));
                Log.d(TAG, "UserCredentials: " + getString(R.string.phone) + " : " + Hawk.get(getString(R.string.phone)));
                Log.d(TAG, "UserCredentials: " + getString(R.string.mailingName) + " : " + Hawk.get(getString(R.string.mailingName)));
                Log.d(TAG, "UserCredentials: " + getString(R.string.address) + " : " + Hawk.get(getString(R.string.address)));
                Log.d(TAG, "UserCredentials: " + getString(R.string.pincode) + " : " + Hawk.get(getString(R.string.pincode)));
                Log.d(TAG, "UserCredentials: " + getString(R.string.state) + " : " + Hawk.get(getString(R.string.state)));
                Log.d(TAG, "UserCredentials: " + getString(R.string.contactName) + " : " + Hawk.get(getString(R.string.contactName)));
                Log.d(TAG, "UserCredentials: " + getString(R.string.contactNumber) + " : " + Hawk.get(getString(R.string.contactNumber)));
                Log.d(TAG, "UserCredentials: " + getString(R.string.gstin) + " : " + Hawk.get(getString(R.string.gstin)));
                Log.d(TAG, "UserCredentials: " + getString(R.string.discount) + " : " + Hawk.get(getString(R.string.discount)));


                DocumentReference documentReference = db.collection(getString(R.string.ledger)).document(Hawk.get(getString(R.string.email)));
                documentReference.set(User).addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "onSuccess: Successfully added the request to Collection Ledger");
                    Log.d(TAG, "onSuccess: showing success Toast message");
                    Toast.makeText(getApplicationContext(), "Requested Ledger", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onSuccess: sending the notification");
                    String title = getString(R.string.ledger) + " Requests";
                    String message = Hawk.get(getString(R.string.name)) + " has requested for " + getString(R.string.ledger);
                    Log.d(TAG, "onSuccess: sending the following notification credentials");
                    Log.d(TAG, "onSuccess: title: " + title);
                    Log.d(TAG, "onSuccess: message: " + message);
                    NotificationUtil.sendNotification(TAG, title, message, Trading.this);
                }).addOnFailureListener(e -> {
                    Log.d(TAG, "onFailure: Can't request Ledger document");
                    Log.d(TAG, "onFailure: got the following exception while requesting Ledger " + e);
                    Toast.makeText(getApplicationContext(), "Can't connect to server, Retry again", Toast.LENGTH_SHORT).show();
                });
            }
            Log.d(TAG, "onSuccess: hiding the progress bar");
            progressDialog.hide();
        }).addOnFailureListener(e -> {
            Log.d(TAG, "onFailure: couldn't check whether Ledger request for " + Hawk.get(getString(R.string.name)) + "exists or not");
            Log.d(TAG, "onFailure: got the following exception " + e);
            Toast.makeText(getApplicationContext(), "Can't connect to server, Retry again", Toast.LENGTH_SHORT).show();
        });
        Log.d(TAG, "onSuccess: hiding the progress bar");
        progressDialog.hide();
    }

    private void checkSOL() {

        Log.d(TAG, "checkSOL: building progress dialog");
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(Trading.this, R.style.ProgressDialog);
        progressDialog.setMessage("Requesting SOL .....");
        progressDialog.setCancelable(false);
        Log.d(TAG, "checkSOL: showing progress dialog");
        progressDialog.show();

        Log.d(TAG, "checkSOL: checking if the request already exists in FireStore or not !!");
        DocumentReference phoneCheck = db.collection("SOL").document(Hawk.get("email"));
        phoneCheck.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {

                Log.d(TAG, "onSuccess: documentSnapshot exists, displaying Toast");
                Toast.makeText(getApplicationContext(), "You have already requested SOL", Toast.LENGTH_SHORT).show();

            } else {

                Log.d(TAG, "onSuccess: documentSnapshot doesn't exists");
                Log.d(TAG, "onSuccess: starting user request to upload in Firestore");

                final Map<String, Object> User = new HashMap<>();
                User.put(getString(R.string.name), Hawk.get(getString(R.string.name)));
                User.put(getString(R.string.email), Hawk.get(getString(R.string.email)));
                User.put(getString(R.string.phone), Hawk.get(getString(R.string.phone)));
                User.put(getString(R.string.mailingName), Hawk.get(getString(R.string.mailingName)));
                User.put(getString(R.string.address), Hawk.get(getString(R.string.address)));
                User.put(getString(R.string.pincode), Hawk.get(getString(R.string.pincode)));
                User.put(getString(R.string.state), Hawk.get(getString(R.string.state)));
                User.put(getString(R.string.contactName), Hawk.get(getString(R.string.contactName)));
                User.put(getString(R.string.contactNumber), Hawk.get(getString(R.string.contactNumber)));
                User.put(getString(R.string.gstin), Hawk.get(getString(R.string.gstin)));
                User.put(getString(R.string.discount), Hawk.get(getString(R.string.discount)));

                Log.d(TAG, "onSuccess: uploading the following documents in Firestore in collection Ledger");
                Log.d(TAG, "UserCredentials: " + getString(R.string.name) + " : " + Hawk.get(getString(R.string.name)));
                Log.d(TAG, "UserCredentials: " + getString(R.string.email) + " : " + Hawk.get(getString(R.string.email)));
                Log.d(TAG, "UserCredentials: " + getString(R.string.phone) + " : " + Hawk.get(getString(R.string.phone)));
                Log.d(TAG, "UserCredentials: " + getString(R.string.mailingName) + " : " + Hawk.get(getString(R.string.mailingName)));
                Log.d(TAG, "UserCredentials: " + getString(R.string.address) + " : " + Hawk.get(getString(R.string.address)));
                Log.d(TAG, "UserCredentials: " + getString(R.string.pincode) + " : " + Hawk.get(getString(R.string.pincode)));
                Log.d(TAG, "UserCredentials: " + getString(R.string.state) + " : " + Hawk.get(getString(R.string.state)));
                Log.d(TAG, "UserCredentials: " + getString(R.string.contactName) + " : " + Hawk.get(getString(R.string.contactName)));
                Log.d(TAG, "UserCredentials: " + getString(R.string.contactNumber) + " : " + Hawk.get(getString(R.string.contactNumber)));
                Log.d(TAG, "UserCredentials: " + getString(R.string.gstin) + " : " + Hawk.get(getString(R.string.gstin)));
                Log.d(TAG, "UserCredentials: " + getString(R.string.discount) + " : " + Hawk.get(getString(R.string.discount)));

                DocumentReference documentReference = db.collection(getString(R.string.SOL)).document(Hawk.get(getString(R.string.email)));
                documentReference.set(User).addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "onSuccess: Successfully added the request to Collection SOL");
                    Log.d(TAG, "onSuccess: showing success Toast message");
                    Toast.makeText(getApplicationContext(), "Requested SOL", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onSuccess: sending the notification");
                    String title = getString(R.string.SOL) + " Requests";
                    String message = Hawk.get(getString(R.string.name)) + " has requested for " + getString(R.string.SOL);
                    Log.d(TAG, "onSuccess: sending the following notification credentials");
                    Log.d(TAG, "onSuccess: title: " + title);
                    Log.d(TAG, "onSuccess: message: " + message);
                    NotificationUtil.sendNotification(TAG, title, message, Trading.this);

                }).addOnFailureListener(e -> {
                    Log.d(TAG, "onFailure: Can't request SOL document");
                    Log.d(TAG, "onFailure: got the following exception while requesting SOL " + e);
                    Toast.makeText(getApplicationContext(), "Can't connect to server, Retry again", Toast.LENGTH_SHORT).show();
                });
            }
        }).addOnFailureListener(e -> {
            Log.d(TAG, "onFailure: couldn't check whether SOL request for " + Hawk.get(getString(R.string.name)) + "exists or not");
            Log.d(TAG, "onFailure: got the following exception " + e);
            Toast.makeText(getApplicationContext(), "Can't connect to server, Retry again", Toast.LENGTH_SHORT).show();
        });
        Log.d(TAG, "onSuccess: hiding the progress bar");
        progressDialog.hide();
    }

    private void setupViewPager(ViewPager viewPager) {
        Log.d(TAG, "setupViewPager: setting up viewpager");
        Log.d(TAG, "setupViewPager: setting the adapter for viewpager");
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Log.d(TAG, "setupViewPager: adding Ledger and SOL fragments");
        adapter.addFragment(new LedgerFragment(), getString(R.string.ledger));
        adapter.addFragment(new SOLFragment(), getString(R.string.SOL));
        Log.d(TAG, "setupViewPager: setting the adapter on viewpager");
        viewPager.setAdapter(adapter);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);

        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "getItem: called");
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            Log.d(TAG, "getCount: returning " + mFragmentList.size());
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            Log.d(TAG, "addFragment: adding fragment and title");
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Log.d(TAG, "getPageTitle: returning the title of Fragment");
            Log.d(TAG, "getPageTitle: returning " + mFragmentTitleList.get(position));
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacksAndMessages(null);
    }
}