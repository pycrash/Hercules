package com.example.hercules.LoginAndRegister;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
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
import com.google.android.gms.tasks.OnSuccessListener;
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
    String companyName, email, phone, id, address, pincode, state, contactName, contactNumber, gstin;
    double discount;
    Handler handler;

    public final static String TAG = "LoginActivity";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        back = findViewById(R.id.back_login);
        back.setOnClickListener(view -> {
            Log.d(TAG, "onCreate: back pressed");
            onBackPressed();
            Log.d(TAG, "onCreate: removing handler callbacks");
            handler.removeCallbacksAndMessages(null);
            Log.d(TAG, "onCreate: finishing this activity");
            finish();
        });

        til_phone = findViewById(R.id.name_layout);
        til_password = findViewById(R.id.password_layout);
        root = findViewById(R.id.root_login);
        infoTextView = findViewById(R.id.info_login);
        mEmail = findViewById(R.id.email_address_login);
        passwordLayout = findViewById(R.id.password_linear_layout);
        mPassword = findViewById(R.id.password_login);
        btnContinue = findViewById(R.id.buttonContinue_login);
        passwordVisibility = findViewById(R.id.password_visibility);
        change_email = findViewById(R.id.change_email);


        //Making views invisible
        Log.d(TAG, "onCreate: making views invisible");
        passwordLayout.setVisibility(View.GONE);
        change_email.setVisibility(View.GONE);

        Log.d(TAG, "onCreate: checking internet connection");
        CheckInternetConnection.showNoInternetDialog(LoginActivity.this, handler);

        passwordVisibility.setOnClickListener(view -> {
            Log.d(TAG, "onCreate: password visibility toggler clicked");
            if (!passwordVisible) {
                Log.d(TAG, "onCreate: making password visible");
                passwordVisible = true;
                passwordVisibility.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_visibility_24), null, null, null);
                mPassword.setTransformationMethod(null);

            } else {
                Log.d(TAG, "onCreate: making password visibilty off");
                passwordVisible = false;
                passwordVisibility.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_visibility_off_24), null, null, null);
                mPassword.setTransformationMethod(new PasswordTransformationMethod());

            }
            mPassword.setSelection(mPassword.getText().length());
        });

        Log.d(TAG, "onCreate: adding text watcher to email edit text");
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
                    Log.d(TAG, "onTextChanged: email is correct");
                } else {
                    Log.d(TAG, "onTextChanged: email is not valid");
                    mEmail.getBackground().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(LoginActivity.this,
                            R.color.red), PorterDuff.Mode.SRC_ATOP));
                }
                if (til_phone.isErrorEnabled()) {
                    Log.d(TAG, "onTextChanged: error is enabled, disabling it");
                    til_phone.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isValidEmail(mEmail.getText().toString().toLowerCase().trim())) {
                    mEmail.getBackground().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(LoginActivity.this,
                            R.color.green), PorterDuff.Mode.SRC_ATOP));
                    Log.d(TAG, "onTextChanged: email is correct");
                }
            }
        });

        Log.d(TAG, "onCreate: adding text watcher to password edit text");
        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mPassword.getText().toString().length() > 3) {
                    mPassword.getBackground().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(LoginActivity.this,
                            R.color.green), PorterDuff.Mode.SRC_ATOP));
                    Log.d(TAG, "onTextChanged: password length greater than 3");
                } else {
                    Log.d(TAG, "onTextChanged: password length less than 3");
                    mPassword.getBackground().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(LoginActivity.this,
                            R.color.red), PorterDuff.Mode.SRC_ATOP));
                }
                if (til_password.isErrorEnabled()) {
                    til_password.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mPassword.getText().toString().length() > 4) {
                    mPassword.getBackground().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(LoginActivity.this,
                            R.color.green), PorterDuff.Mode.SRC_ATOP));
                    Log.d(TAG, "onTextChanged: password length greater than 3");
                }
            }
        });
        change_email.setOnClickListener(view -> {
            Log.d(TAG, "onCreate: change email button clicked");
            Log.d(TAG, "onCreate: building an alert dialog");
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle);
            builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss()).
                    setPositiveButton("Ok", (dialogInterface, i) -> {
                        SIGN_IN_STATUS = 0;
                        Log.d(TAG, "onCreate: making password views invisible");
                        passwordLayout.setVisibility(View.GONE);
                        change_email.setVisibility(View.GONE);
                        mEmail.setEnabled(true);
                        mEmail.setText("");
                        dialogInterface.dismiss();
                    }).setMessage("Are you sure, you want to change your email");

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
        btnContinue.setOnClickListener(view -> {
            Log.d(TAG, "onCreate: button continue is clicked");
            Log.d(TAG, "onCreate: checking internet connection");

            boolean isInternet = CheckInternetConnection.checkInternet(LoginActivity.this);
            if (!isInternet) {
                Log.d(TAG, "onCreate: no internet");
                Log.d(TAG, "onCreate: showing no internet toast");
                Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                btnContinue.setEnabled(true);
                return;
            }
            Log.d(TAG, "onCreate: disabling btnContinue and mEmail edit text ");
            btnContinue.setEnabled(false);
            mEmail.setEnabled(false);

            Log.d(TAG, "onCreate: checking which operation to perform");
            if (SIGN_IN_STATUS == 1) {
                Log.d(TAG, "onCreate: checking password");
                if (password.equals(mPassword.getText().toString().trim())) {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                    Log.d(TAG, "onCreate: building Hawk");
                    Hawk.init(LoginActivity.this).build();

                    Log.d(TAG, "onCreate: saving the  user credentials in local storage");
                    Hawk.put(getString(R.string.companyName), companyName);
                    Hawk.put(getString(R.string.email), email);
                    Hawk.put(getString(R.string.phone), phone);
                    Hawk.put(getString(R.string.id), id);
                    Hawk.put(getString(R.string.address), address);
                    Hawk.put(getString(R.string.pincode), pincode);
                    Hawk.put(getString(R.string.state), state);
                    Hawk.put(getString(R.string.contactName), contactName);
                    Hawk.put(getString(R.string.contactNumber), contactNumber);
                    Hawk.put(getString(R.string.gstin), gstin);
                    Hawk.put(getString(R.string.discount), discount);

                    Log.d(TAG, "onCreate: starting Home activity and finishing this activity");
                    startActivity(intent);
                    handler.removeCallbacksAndMessages(null);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    finish();


                } else {
                    Log.d(TAG, "onCreate: password is incorrect, setting error");
                    til_password.setError("Incorrect Password");
                    btnContinue.setEnabled(true);
                }

            } else {
                Log.d(TAG, "onCreate: checking whether email is correct or not");
                if (!isValidEmail(mEmail.getText().toString().trim().toLowerCase())) {
                    til_phone.setError("Invalid Email address");

                    Log.d(TAG, "onCreate: enabling edit text email and button");
                    btnContinue.setEnabled(true);
                    mEmail.setEnabled(true);
                    Log.d(TAG, "onCreate: returning");
                    return;
                }

                Log.d(TAG, "onCreate: disabling button");
                btnContinue.setEnabled(false);

                Log.d(TAG, "onCreate: showing progress dialog");
                ProgressDialog dialog = new ProgressDialog(LoginActivity.this, R.style.ProgressDialog);
                dialog.setMessage("Working on it !!");
                dialog.setCancelable(false);
                dialog.show();


                Log.d(TAG, "onCreate: checking whether user exists in Firestore or not");
                DocumentReference docIdRef = db.collection(getString(R.string.users)).document(mEmail.getText().toString().trim().toLowerCase());
                docIdRef.get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Log.d(TAG, "signInWIthCredential: Document snapshot exists");
                        Log.d(TAG, "onSuccess: getting user credentials");

                        companyName = documentSnapshot.getString(getString(R.string.companyName));
                        phone = documentSnapshot.getString(getString(R.string.phone));
                        email = mEmail.getText().toString().trim();
                        password = documentSnapshot.getString(getString(R.string.password));
                        id = documentSnapshot.getString(getString(R.string.id));
                        address = documentSnapshot.getString(getString(R.string.address));
                        pincode = documentSnapshot.getString(getString(R.string.pincode));
                        state = documentSnapshot.getString(getString(R.string.state));
                        contactName = documentSnapshot.getString(getString(R.string.contactName));
                        contactNumber = documentSnapshot.getString(getString(R.string.contactNumber));
                        gstin = documentSnapshot.getString(getString(R.string.gstin));
                        if (documentSnapshot.getDouble(getString(R.string.discount)) != null) {
                            discount = documentSnapshot.getDouble(getString(R.string.discount));
                        } else {
                            discount = 0;
                        }

                        Log.d(TAG, "onCreate: Company Name : " + companyName);
                        Log.d(TAG, "onCreate: ID : " + id);
                        Log.d(TAG, "onCreate: Phone:  " + phone);
                        Log.d(TAG, "onCreate: Email : " + email);
                        Log.d(TAG, "onCreate: Address : " + address);
                        Log.d(TAG, "onCreate: Pincode : " + pincode);
                        Log.d(TAG, "onCreate: State : " + state);
                        Log.d(TAG, "onCreate: Contact Name : " + contactName);
                        Log.d(TAG, "onCreate: Contact Number : " + contactNumber);
                        Log.d(TAG, "onCreate: GSTIN : " + gstin);
                        Log.d(TAG, "onSuccess: Discount : " + discount);


                        Log.d(TAG, "onSuccess: changing text of title");
                        infoTextView.setText(getString(R.string.enter_password));
                        change_email.setVisibility(View.VISIBLE);
                        SIGN_IN_STATUS = 1;
                        Log.d(TAG, "onSuccess: disabling email edittext");
                        mEmail.setEnabled(false);
                        Log.d(TAG, "onSuccess: making password layout visible");
                        passwordLayout.setVisibility(View.VISIBLE);
                    } else {
                        Log.d(TAG, "onSuccess: email is not registered, showing error msg");
                        til_phone.setError("Email address not registered");
                        Log.d(TAG, "onSuccess: enabling email edittext");
                        mEmail.setEnabled(true);
                        dialog.dismiss();
                    }
                }).addOnFailureListener(e -> {
                    Log.d(TAG, "onClick: failed to check User in Firestore with the following error " + e);
                    Toast.makeText(getApplicationContext(), String.valueOf(e), Toast.LENGTH_LONG).show();

                });
                Log.d(TAG, "onClick: enabling btnContinue again");
                btnContinue.setEnabled(true);
                Log.d(TAG, "onClick: dismissing the dialog");
                dialog.dismiss();

            }
        });


    }

    public static boolean isValidEmail(CharSequence target) {
        Log.d(TAG, "isValidEmail: checking whether email is valid or not");
        boolean isEmailCorrect = !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        Log.d(TAG, "isValidEmail: returning " + isEmailCorrect);
        return isEmailCorrect;
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: back button is pressed, finishing this activity");
        super.onBackPressed();
        finish();
        Log.d(TAG, "onBackPressed: removing callbacks");
        handler.removeCallbacksAndMessages(null);
    }
}
