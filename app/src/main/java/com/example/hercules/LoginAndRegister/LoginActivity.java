package com.example.hercules.LoginAndRegister;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hercules.Home.HomeActivity;
import com.example.hercules.R;
import com.example.hercules.utils.InternetUtils.CheckInternetConnection;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.hawk.Hawk;

public class LoginActivity extends AppCompatActivity {
    RelativeLayout root;
    ImageView back;
    TextView infoTextView;
    EditText mEmail;
    LinearLayout passwordLayout;
    EditText mPassword;
    Button btnContinue;
    TextView passwordVisibility;
    boolean passwordVisible = false;
    //1 for checking OTP
    int SIGN_IN_STATUS = 0;
    TextView change_email;
    TextInputLayout til_phone, til_password;
    String password;
    String name, email, phone, mailingName, address, pincode, state, contactName, contactNumber,gstin;
    int discount;

    Handler handler;

    public final static String TAG = "LoginActivity";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        //Making views invisible
        back = findViewById(R.id.back_login);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                handler.removeCallbacksAndMessages(null);
                finish();
            }
        });
        til_phone = findViewById(R.id.name_layout);
        til_password = findViewById(R.id.password_layout);

        root = findViewById(R.id.root_login);
        back = findViewById(R.id.back_login);
        infoTextView = findViewById(R.id.info_login);
        mEmail = findViewById(R.id.email_address_login);
        passwordLayout = findViewById(R.id.password_linear_layout);
        mPassword = findViewById(R.id.password_login);
        btnContinue = findViewById(R.id.buttonContinue_login);
        passwordVisibility = findViewById(R.id.password_visibility);
        change_email = findViewById(R.id.change_email);

        //Making views invisible
        passwordLayout.setVisibility(View.GONE);
        change_email.setVisibility(View.GONE);

        checkInternet();
            passwordVisibility.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!passwordVisible) {
                        passwordVisible = true;
                        passwordVisibility.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_visibility_24), null, null, null);
                        mPassword.setTransformationMethod(null);
                        mPassword.setSelection(mPassword.getText().length());

                    } else {
                        passwordVisible = false;
                        passwordVisibility.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_visibility_off_24), null, null, null);
                        mPassword.setTransformationMethod(new PasswordTransformationMethod());
                        mPassword.setSelection(mPassword.getText().length());

                    }
                }
            });

        mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged:started");
                if (isValidEmail(mEmail.getText().toString().toLowerCase().trim())) {
                    mEmail.getBackground().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(LoginActivity.this,
                            R.color.green), PorterDuff.Mode.SRC_ATOP));
                    Log.d(TAG, "onTextChanged:Phone length greater than 9, enabling btnContinue");
                } else {
                    Log.d(TAG, "onTextChanged: btnContinue disabled as Phone length not greater than 9");
                    Log.d(TAG, "onTextChanged: changing the background tint to red");
                    mEmail.getBackground().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(LoginActivity.this,
                            R.color.red), PorterDuff.Mode.SRC_ATOP));
                }
                if (til_phone.isErrorEnabled()) {
                    til_phone.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "afterTextChanged: checking for phone length ");
                if (isValidEmail(mEmail.getText().toString().toLowerCase().trim())) {
                    mEmail.getBackground().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(LoginActivity.this,
                            R.color.green), PorterDuff.Mode.SRC_ATOP));
                    Log.d(TAG, "onTextChanged:Phone length greater than 9, enabling btnContinue");
                }
            }
        });

        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged:started");
                if (mPassword.getText().toString().length() > 4) {
                    mPassword.getBackground().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(LoginActivity.this,
                            R.color.green), PorterDuff.Mode.SRC_ATOP));
                    Log.d(TAG, "onTextChanged:Phone length greater than 9, enabling btnContinue");
                } else {
                    Log.d(TAG, "onTextChanged: btnContinue disabled as Phone length not greater than 9");
                    Log.d(TAG, "onTextChanged: changing the background tint to red");
                    mPassword.getBackground().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(LoginActivity.this,
                            R.color.red), PorterDuff.Mode.SRC_ATOP));
                }
                if (til_password.isErrorEnabled()) {
                    til_password.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "afterTextChanged: checking for phone length ");
                if (mPassword.getText().toString().length() > 4) {
                    mPassword.getBackground().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(LoginActivity.this,
                            R.color.green), PorterDuff.Mode.SRC_ATOP));
                    Log.d(TAG, "onTextChanged:Phone length greater than 9, enabling btnContinue");
                }
            }
        });
        change_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SIGN_IN_STATUS = 0;
                        //Making views invisible
                        passwordLayout.setVisibility(View.GONE);
                        change_email.setVisibility(View.GONE);
                        mEmail.setEnabled(true);
                        mEmail.setText("");
                        dialogInterface.dismiss();
                    }
                }).setMessage("Are you sure, you want to change your email");

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isInternet = CheckInternetConnection.checkInternet(LoginActivity.this);
                if (!isInternet) {
                    Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    btnContinue.setEnabled(true);
                    return;
                }
                btnContinue.setEnabled(false);
                mEmail.setEnabled(false);
                if (SIGN_IN_STATUS == 1) {
                    if (password.equals(mPassword.getText().toString().trim())) {
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                        Hawk.init(LoginActivity.this).build();
                        Hawk.put("name", name);
                        Hawk.put("email", email);
                        Hawk.put("phone", phone);
                        Hawk.put("mailingName", mailingName);
                        Hawk.put("address", address);
                        Hawk.put("pincode", pincode);
                        Hawk.put("state", state);
                        Hawk.put("contactName", contactName);
                        Hawk.put("contactNumber", contactNumber);
                        Hawk.put("gstin", gstin);
                        Hawk.put("discount", discount);

                        startActivity(intent);
                        handler.removeCallbacksAndMessages(null);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        finish();


                    } else {
                        til_password.setError("Incorrect Password");
                        btnContinue.setEnabled(true);
                        return;
                    }

                } else {
                    if (!isValidEmail(mEmail.getText().toString().trim().toLowerCase())) {
                        til_phone.setError("Invalid Email address");
                        btnContinue.setEnabled(true);
                        mEmail.setEnabled(true);
                        return;
                    }
                    btnContinue.setEnabled(false);
                    ProgressDialog dialog = new ProgressDialog(LoginActivity.this, R.style.ProgressDialog);
                    dialog.setMessage("Working on it !!");
                    dialog.setCancelable(false);
                    dialog.show();



                    DocumentReference docIdRef = db.collection("Users").document(mEmail.getText().toString().trim().toLowerCase());
                    docIdRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                Log.d(TAG, "signInWIthCredential: Document snapshot exists");


                                 name = documentSnapshot.getString("name");
                                 phone = documentSnapshot.getString("phone");
                                 email = mEmail.getText().toString().trim();
                                 password = documentSnapshot.getString("password");
                                mailingName = documentSnapshot.getString("mailingName");
                                 address = documentSnapshot.getString("address");
                                pincode = documentSnapshot.getString("pincode");
                                state = documentSnapshot.getString("state");
                                contactName = documentSnapshot.getString("contactName");
                                contactNumber = documentSnapshot.getString("contactNumber");
                                gstin = documentSnapshot.getString("gstin");
                                discount = Math.toIntExact(documentSnapshot.getLong("discount"));

                                dialog.dismiss();
                                infoTextView.setText("Enter your password");
                                change_email.setVisibility(View.VISIBLE);
                                SIGN_IN_STATUS = 1;
                                mEmail.setEnabled(false);
                                btnContinue.setEnabled(true);
                                passwordLayout.setVisibility(View.VISIBLE);
                            } else {
                                til_phone.setError("Email address not registered");
                                mEmail.setEnabled(true);
                                btnContinue.setEnabled(true);
                                dialog.dismiss();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), String.valueOf(e), Toast.LENGTH_LONG).show();
                            btnContinue.setEnabled(true);
                            dialog.dismiss();
                        }
                    });

                }
            }});


    }
    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    void checkInternet() {
        final Snackbar snackbar = Snackbar.make(root, "No Internet", 10000);
        snackbar.setBackgroundTint(ContextCompat.getColor(getApplicationContext(), R.color.cardviewColor));
        handler  = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 10);
                boolean isInternet = CheckInternetConnection.checkInternet(LoginActivity.this);
                if (!isInternet) {
                    snackbar.show();
                } else {
                    snackbar.dismiss();
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
