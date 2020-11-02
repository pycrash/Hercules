package com.example.hercules.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;

import com.andremion.counterfab.CounterFab;
import com.example.hercules.Cart.CartActivity;
import com.example.hercules.Database.Database;
import com.example.hercules.Information.AboutUsActivity;
import com.example.hercules.Information.ReturnPolicyActivity;
import com.example.hercules.Information.ShippingAndDeliveryPolicyActivity;
import com.example.hercules.Information.TermsAndConditionsActivity;
import com.example.hercules.LoginAndRegister.AccountInfo;
import com.example.hercules.Models.Token;
import com.example.hercules.MyOrders.MyOrders;
import com.example.hercules.Information.PrivacyPolicyActivity;
import com.example.hercules.Products.MassGainerCategory;
import com.example.hercules.Products.MuscleBuilderCategory;
import com.example.hercules.Products.PreWorkoutCategory;
import com.example.hercules.LoginAndRegister.LoginSlider;
import com.example.hercules.Products.ProteinsCategory;
import com.example.hercules.Trading.Trading;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.orhanobut.hawk.Hawk;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.hercules.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private TabLayout tabLayout;
    ImageView menu, call;
    DrawerLayout drawer;
    NavigationView navigationView;
    private SwitchCompat switcher;
    CounterFab cart;
    int cartCount;

    public final static String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.d(TAG, "onCreate: here");
        drawer = findViewById(R.id.drawer_layout);
        cart = findViewById(R.id.fab_cart);
        menu = findViewById(R.id.experiment);
        tabLayout = findViewById(R.id.tabs);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        Log.d(TAG, "onCreate: setting up viewpager");
        ViewPager viewPager = findViewById(R.id.container);
        setupViewPager(viewPager);

        Log.d(TAG, "onCreate: setting up tabs with viewpager");
        tabLayout.setupWithViewPager(viewPager);

        Log.d(TAG, "onCreate: setting up custom tab icons");
        setupTabIcons();

        Log.d(TAG, "onCreate: setting up navigation view toggle");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                HomeActivity.this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Log.d(TAG, "onCreate: setting on Click listener on navigation view");
        navigationView.setNavigationItemSelectedListener(HomeActivity.this);
        menu.setOnClickListener(v -> drawer.openDrawer(GravityCompat.START));

        call = findViewById(R.id.call);
        call.setOnClickListener(view -> {
            Log.d(TAG, "onCreate: call button clicked");
            Log.d(TAG, "onCreate: going to phone intent");
            Log.d(TAG, "onCreate: phone number : " + getString(R.string.business_phone));
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getString(R.string.business_phone)));
            startActivity(intent);
        });

        Log.d(TAG, "onCreate: handling custom dark mode switch");
        Menu menu1 = navigationView.getMenu();
        MenuItem menuItem = menu1.findItem(R.id.dark_mode_switch);
        View actionView = MenuItemCompat.getActionView(menuItem);

        //Checking for stored shared prefs if any for dark theme
        Log.d(TAG, "onCreate: getting AppSettingPrefs");
        SharedPreferences appSettingPrefs = getSharedPreferences(getString(R.string.night_mode_shared_prefs), 0);
        Log.d(TAG, "onCreate: opening shared prefs editor");
        final SharedPreferences.Editor sharedPrefsEdit = appSettingPrefs.edit();
        Log.d(TAG, "onCreate: checking if user has previously toggled night mode or not ");
        boolean isNightModeOn = appSettingPrefs.getBoolean(getString(R.string.night_mode_boolean), false);
        Log.d(TAG, "onCreate: value of isNightMode = " + isNightModeOn);

        Log.d(TAG, "onCreate: toggling dark mode");
        switcher = actionView.findViewById(R.id.switcher);
        switcher.setChecked(isNightModeOn);
        switcher.setOnClickListener(v -> {
            Log.d(TAG, "onCreate: switch is clicked");
            if (switcher.isChecked()) {
                Log.d(TAG, "onCreate: switch is checked");
                Log.d(TAG, "onCreate: enabling dark theme");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                overridePendingTransition(R.anim.fadeout, R.anim.fadein);
                sharedPrefsEdit.putBoolean(getString(R.string.night_mode_boolean), true);
            } else {
                Log.d(TAG, "onCreate: switch is unchecked");
                Log.d(TAG, "onCreate: disabling dark theme");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                sharedPrefsEdit.putBoolean(getString(R.string.night_mode_boolean), false);
            }
            sharedPrefsEdit.apply();
        });

        Log.d(TAG, "onCreate: setting the count of items on cart flaoting action button");
        Log.d(TAG, "onCreate: ");

        cartCount = (int) new Database(HomeActivity.this).count();
        // Set the count value to show on badge
        cart.setCount(cartCount);
        Log.d(TAG, "onCreate: setting the count value : " + cartCount + " on FAB cart");

        cart.setOnClickListener(view -> {
            Log.d(TAG, "onCreate: cart Fab clicked");
            Log.d(TAG, "onCreate: going to Cart Activity");
            startActivity(new Intent(HomeActivity.this, CartActivity.class));
            Log.d(TAG, "onCreate: overriding the pending transitions");
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        });

        Log.d(TAG, "onCreate: building Hawk");
        Hawk.init(getApplicationContext()).build();

        Log.d(TAG, "onCreate: calling up setupHeaderView method");
        setupHeaderView();

        Log.d(TAG, "onCreate: calling updateToken method");
        updateToken(FirebaseInstanceId.getInstance().getToken());
    }

    private void updateToken(String token) {
        Log.d(TAG, "updateToken: here");
        Log.d(TAG, "updateToken: getting the reference of Tokens");
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = db.getReference(getString(R.string.token));
        Token data = new Token(token, false);
        databaseReference.child(Hawk.get(getString(R.string.phone))).setValue(data);
    }


    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: user has pressed back button");
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            Log.d(TAG, "onBackPressed: drawer is open, so closing drawer");
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Log.d(TAG, "onBackPressed: drawer is closed");
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        Log.d(TAG, "onCreateOptionsMenu: Inflating the menu");
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.d(TAG, "onNavigationItemSelected: Handling navigation view item clicks here");
        // Handle navigation view item clicks here.
        Log.d(TAG, "onNavigationItemSelected: getting the id of the item that is clicked");
        int id = item.getItemId();

        Log.d(TAG, "onNavigationItemSelected: id selected is " + id);
        if (id == R.id.log_out) {
            Log.d(TAG, "onNavigationItemSelected: user has selected logout button");
            Log.d(TAG, "onNavigationItemSelected: building logout ALert Dialog");
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.MyAlertDialogStyle);
            builder.setMessage("Are you sure you want to log out?");
            builder.setPositiveButton("OK", (dialogInterface, i) -> {
                Log.d(TAG, "onNavigationItemSelected: user has selected OK, so logging the user out");

                Log.d(TAG, "onNavigationItemSelected: cleaning the cart");
                new Database(getBaseContext()).cleanCart();

                Log.d(TAG, "onNavigationItemSelected: removing " + Hawk.get(getString(R.string.phone)) + " from Tokens");
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = db.getReference(getString(R.string.token));
                Hawk.init(getApplicationContext()).build();
                databaseReference.child(Hawk.get(getString(R.string.phone))).removeValue();

                Log.d(TAG, "onNavigationItemSelected: Deleting all values from Hawk");
                Hawk.deleteAll();

                Log.d(TAG, "onNavigationItemSelected: starting LoginSlider Activity");
                startActivity(new Intent(HomeActivity.this, LoginSlider.class));
                finish();
            }).setNegativeButton("No", (dialogInterface, i) -> {
                Log.d(TAG, "onNavigationItemSelected: user has selected No for log out, so dismissing the logout button");
                dialogInterface.dismiss();
            }).setCancelable(false);

            AlertDialog dialog = builder.create();
            dialog.show();

        }
        if (id == R.id.my_orders) {
            Log.d(TAG, "onNavigationItemSelected: User has selected My Orders");
            startActivity(new Intent(HomeActivity.this, MyOrders.class));
        }
        if (id == R.id.change_account_info) {
            Log.d(TAG, "onNavigationItemSelected: User has selected Account Info");
            startActivity(new Intent(HomeActivity.this, AccountInfo.class));
        }
        if (id == R.id.privacy_policy) {
            Log.d(TAG, "onNavigationItemSelected: User has selected Privacy Policy");
            startActivity(new Intent(HomeActivity.this, PrivacyPolicyActivity.class));
        }
        if (id == R.id.return_policy) {
            Log.d(TAG, "onNavigationItemSelected: User has selected Return Policy");
            startActivity(new Intent(HomeActivity.this, ReturnPolicyActivity.class));
        }
        if (id == R.id.shipping_and_delivery_policy) {
            Log.d(TAG, "onNavigationItemSelected: User has selected Shipping and Delivery Policy");
            startActivity(new Intent(HomeActivity.this, ShippingAndDeliveryPolicyActivity.class));
        }
        if (id == R.id.about_us) {
            Log.d(TAG, "onNavigationItemSelected: User has selected About US");
            startActivity(new Intent(HomeActivity.this, AboutUsActivity.class));
        }
        if (id == R.id.terms_and_conditions) {
            Log.d(TAG, "onNavigationItemSelected: User has selected Terms and Conditions");
            startActivity(new Intent(HomeActivity.this, TermsAndConditionsActivity.class));
        }
        if (id == R.id.trading) {
            Log.d(TAG, "onNavigationItemSelected: User has selected Trading");
            startActivity(new Intent(HomeActivity.this, Trading.class));
        }

        Log.d(TAG, "onNavigationItemSelected: close the drawer once an item is clicked");
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void setupTabIcons() {
        Log.d(TAG, "setupTabIcons: setting the tab layout");

        final ViewGroup nullParent = null;
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, nullParent);
        tabOne.setText(getString(R.string.ui_category_mass_gainer));
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_baseline_keyboard_24, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, nullParent);
        tabTwo.setText(getString(R.string.ui_category_muscle_builder));
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.preworkout, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, nullParent);
        tabThree.setText(getString(R.string.ui_protein));
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.protein, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, nullParent);
        tabFour.setText(getString(R.string.ui_category_pre_workout));
        tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.workoutessentials, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabFour);
    }

    private void setupViewPager(ViewPager viewPager) {
        Log.d(TAG, "setupViewPager: setting up view pager");
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new MassGainerCategory(), getString(R.string.ui_category_mass_gainer));
        adapter.addFrag(new MuscleBuilderCategory(), getString(R.string.ui_category_muscle_builder));
        adapter.addFrag(new ProteinsCategory(), getString(R.string.ui_protein));
        adapter.addFrag(new PreWorkoutCategory(), getString(R.string.ui_category_pre_workout));
        Log.d(TAG, "setupViewPager: setting the adapter to viewpager");
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
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    void setupHeaderView() {
        Log.d(TAG, "setupHeaderView: setting the header view in navigation view");
        Hawk.init(HomeActivity.this).build();
        View hView = navigationView.getHeaderView(0);

        Log.d(TAG, "setupHeaderView: Header View credentials");
        Log.d(TAG, "setupHeaderView: name: " + Hawk.get(getString(R.string.name)));
        Log.d(TAG, "setupHeaderView: email : " + Hawk.get(getString(R.string.email)));
        Log.d(TAG, "setupHeaderView: phone : " + Hawk.get(getString(R.string.phone)));

        TextView header_name = hView.findViewById(R.id.textview_header_name);
        String name = Hawk.get(getString(R.string.name));
        header_name.setText(name);

        TextView header_email = hView.findViewById(R.id.textView_header_email_address);
        String email = Hawk.get(getString(R.string.email));
        header_email.setText(email);

        TextView header_phone = hView.findViewById(R.id.textView_header_phone_no);
        String phone = Hawk.get(getString(R.string.phone));
        header_phone.setText(phone);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cartCount = (int) new Database(HomeActivity.this).count();
        cart.setCount(cartCount); // Set the count value to show on badge
        Log.d(TAG, "onCreate: setting the count value : " + cartCount + " on FAB cart");

    }

    @Override
    protected void onStop() {
        super.onStop();
        cartCount = (int) new Database(HomeActivity.this).count();
        cart.setCount(cartCount); // Set the count value to show on badge
        Log.d(TAG, "onCreate: setting the count value : " + cartCount + " on FAB cart");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        cartCount = (int) new Database(HomeActivity.this).count();
        cart.setCount(cartCount); // Set the count value to show on badge
        Log.d(TAG, "onCreate: setting the count value : " + cartCount + " on FAB cart");

    }
}
