    package com.example.hercules;

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
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hercules.MyOrders.MyOrders;
import com.example.hercules.utils.CheckInternetConnection;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trading extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    CardView done, request;
    ArrayList<Integer> itemsSelected;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    androidx.appcompat.app.AlertDialog dialog;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trading);

        checkInternet();
        ImageView back;
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
                handler.removeCallbacksAndMessages(null);
            }
        });
        Hawk.init(Trading.this).build();
        Dialog dialog;
        final String[] items = {"Ledger", "SOL"};


        itemsSelected = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);

        builder.setTitle("Select the ones you want to request : ");

        builder.setMultiChoiceItems(items, null,
                new DialogInterface.OnMultiChoiceClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int selectedItemId,
                                        boolean isSelected) {
                        if (isSelected) {

                            itemsSelected.add(selectedItemId);
                        } else if (itemsSelected.contains(selectedItemId)) {

                            itemsSelected.remove(Integer.valueOf(selectedItemId));
                        }
                    }
                })
                .setPositiveButton("Done!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (itemsSelected.size() == 0) {
                            Toast.makeText(getApplicationContext(), "No Items Selected", Toast.LENGTH_LONG).show();

                        } else {
                            requestDocs();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {


                    }
                });

        dialog = builder.create();




        viewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        done = findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        request = findViewById(R.id.request);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
    }

    private void requestDocs() {

        if (itemsSelected.contains(0)) {
            checkLeisure();
        }
        if (itemsSelected.contains(1)) {
            checkSOL();
        }

    }

    private void checkLeisure() {
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(Trading.this, R.style.ProgressDialog);
        progressDialog.setMessage("Requesting Ledger .....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        DocumentReference phoneCheck = db.collection("Leisure").document(Hawk.get("email"));
        phoneCheck.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                   Toast.makeText(getApplicationContext(), "You have already requested Ledger", Toast.LENGTH_SHORT).show();
                    progressDialog.hide();
                } else {
                    final Map<String, Object> User = new HashMap<>();
                    User.put("name", Hawk.get("name"));
                    User.put("email", Hawk.get("email"));
                    User.put("phone", Hawk.get("phone"));
                    User.put("mailingName",  Hawk.get("mailingName"));
                    User.put("address",  Hawk.get("address"));
                    User.put("pincode",  Hawk.get("pincode"));
                    User.put("state",  Hawk.get("state"));
                    User.put("contactName",  Hawk.get("contactName"));
                    User.put("contactNumber", Hawk.get("contactNumber"));
                    User.put("gstin",  Hawk.get("gstin"));
                    User.put("discount",Hawk.get("discount"));

                    DocumentReference documentReference = db.collection("Leisure").document(Hawk.get("email"));
                    documentReference.set(User).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Requested Leisure", Toast.LENGTH_SHORT).show();
                            progressDialog.hide();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.hide();
                            Toast.makeText(getApplicationContext(), String.valueOf(e), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void checkSOL() {
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(Trading.this, R.style.ProgressDialog);
        progressDialog.setMessage("Requesting SOL .....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        DocumentReference phoneCheck = db.collection("SOL").document(Hawk.get("email"));
        phoneCheck.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Toast.makeText(getApplicationContext(), "You have already requested SOL", Toast.LENGTH_SHORT).show();
                    progressDialog.hide();
                } else {
                    final Map<String, Object> User = new HashMap<>();
                    User.put("name", Hawk.get("name"));
                    User.put("email", Hawk.get("email"));
                    User.put("phone", Hawk.get("phone"));
                    User.put("mailingName",  Hawk.get("mailingName"));
                    User.put("address",  Hawk.get("address"));
                    User.put("pincode",  Hawk.get("pincode"));
                    User.put("state",  Hawk.get("state"));
                    User.put("contactName",  Hawk.get("contactName"));
                    User.put("contactNumber", Hawk.get("contactNumber"));
                    User.put("gstin",  Hawk.get("gstin"));
                    User.put("discount",Hawk.get("discount"));

                    DocumentReference documentReference = db.collection("SOL").document(Hawk.get("email"));
                    documentReference.set(User).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Requested SOL", Toast.LENGTH_SHORT).show();
                            progressDialog.hide();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.hide();
                            Toast.makeText(getApplicationContext(), String.valueOf(e), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new LeisureFragment(), "Ledger");
        adapter.addFragment(new SOLFragment(), "SOL");

        viewPager.setAdapter(adapter);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);

        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
           }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    void checkInternet() {
        androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(Trading.this);
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
                boolean isInternet = CheckInternetConnection.checkInternet(Trading.this);
                if (!isInternet) {
                    dialog.show();
                } else {
                    dialog.hide();
                }
            }
        }, 20);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        handler.removeCallbacksAndMessages(null);
    }
}