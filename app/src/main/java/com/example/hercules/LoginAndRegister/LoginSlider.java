package com.example.hercules.LoginAndRegister;


import android.content.Intent;

import android.os.Bundle;

import com.example.hercules.Information.PrivacyPolicyActivity;
import com.example.hercules.Information.TermsAndConditionsActivity;
import com.example.hercules.utils.LoginSliderUtils.AutoScrollPagerAdapter;
import com.example.hercules.utils.LoginSliderUtils.AutoScrollViewPager;

import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hercules.R;

import com.google.android.material.tabs.TabLayout;

import com.google.firebase.firestore.FirebaseFirestore;
public class LoginSlider extends AppCompatActivity {
    private static final int AUTO_SCROLL_THRESHOLD_IN_MILLI = 4000;

    TextView terms;
    Button email_login;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_slider);

        terms = findViewById(R.id.terms);
        customTextView(terms);
        AutoScrollPagerAdapter autoScrollPagerAdapter =
                new AutoScrollPagerAdapter(getSupportFragmentManager());
        AutoScrollViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(autoScrollPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        // start auto scroll
        viewPager.startAutoScroll();
        // set auto scroll time in mili
        viewPager.setInterval(AUTO_SCROLL_THRESHOLD_IN_MILLI);
        // enable recycling using true
        viewPager.setCycle(true);

        email_login = findViewById(R.id.email_login);
        email_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginSlider.this, LoginActivity.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                          }
        });

}
    private void customTextView(TextView view) {
        SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                "By proceeding, you agree to our ");
        spanTxt.append("Term of services");
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
               startActivity(new Intent(getApplicationContext(), TermsAndConditionsActivity.class));
            }
        }, spanTxt.length() - "Term of services".length(), spanTxt.length(), 0);
        spanTxt.append(" and");
        spanTxt.append(" Privacy Policy");
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(getApplicationContext(), PrivacyPolicyActivity.class));
            }
        }, spanTxt.length() - " Privacy Policy".length(), spanTxt.length(), 0);
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }

}


