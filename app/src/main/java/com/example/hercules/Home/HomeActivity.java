package com.example.hercules.Home;

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
import com.example.hercules.MyOrders.MyOrders;
import com.example.hercules.Information.PrivacyPolicyActivity;
import com.example.hercules.Products.MassGainerCategory;
import com.example.hercules.Products.MuscleBuilderCategory;
import com.example.hercules.Products.PreWorkoutCategory;
import com.example.hercules.LoginAndRegister.LoginSlider;
import com.example.hercules.Products.ProteinsCategory;
import com.example.hercules.Trading;
import com.orhanobut.hawk.Hawk;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    private ViewPager viewPager;
    DrawerLayout drawer;
    NavigationView navigationView;
    private SwitchCompat switcher;
    CounterFab cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawer = findViewById(R.id.drawer_layout);

        cart = findViewById(R.id.fab_cart);
        viewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(viewPager);
        menu=findViewById(R.id.experiment);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
         navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                HomeActivity.this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(HomeActivity.this);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        call = findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "9971800270"));
                startActivity(intent);
            }
        });
        Menu menu1 = navigationView.getMenu();
        MenuItem menuItem = menu1.findItem(R.id.dark_mode_switch);
        View actionView = MenuItemCompat.getActionView(menuItem);
        SharedPreferences appSettingPrefs = getSharedPreferences("AppSettingPrefs", 0);
        final SharedPreferences.Editor sharedPrefsEdit = appSettingPrefs.edit();
        boolean isNightModeOn = appSettingPrefs.getBoolean("NightMode", false);


        switcher = (SwitchCompat) actionView.findViewById(R.id.switcher);

        if (isNightModeOn) {
            switcher.setChecked(true);
        } else {
            switcher.setChecked(false);
        }
        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switcher.isChecked()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    overridePendingTransition(R.anim.fadeout, R.anim.fadein);
                    sharedPrefsEdit.putBoolean("NightMode", true);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    sharedPrefsEdit.putBoolean("NightMode", false);
                }
                sharedPrefsEdit.apply();
            }
        });
        cart.setCount((int)new Database(getApplicationContext()).count()); // Set the count value to show on badge
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

        setupHeaderView();
    }



    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.log_out) {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.MyAlertDialogStyle);
            builder.setMessage("Are you sure you want to log out?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    new Database(getBaseContext()).cleanCart();

                    Hawk.deleteAll();
                    startActivity(new Intent(HomeActivity.this, LoginSlider.class));
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }
        if (id == R.id.my_orders) {
            startActivity(new Intent(HomeActivity.this, MyOrders.class));
        }
        if (id == R.id.change_account_info) {
            startActivity(new Intent(HomeActivity.this, AccountInfo.class));
        }
        if (id == R.id.privacy_policy) {
            startActivity(new Intent(HomeActivity.this, PrivacyPolicyActivity.class));
        }
        if (id == R.id.return_policy) {
            startActivity(new Intent(HomeActivity.this, ReturnPolicyActivity.class));
        }
        if (id == R.id.shipping_and_delivery_policy) {
            startActivity(new Intent(HomeActivity.this, ShippingAndDeliveryPolicyActivity.class));
        }
        if (id == R.id.about_us) {
            startActivity(new Intent(HomeActivity.this, AboutUsActivity.class));
        }
        if (id == R.id.terms_and_conditions) {
            startActivity(new Intent(HomeActivity.this, TermsAndConditionsActivity.class));
        }
        if (id == R.id.trading) {
            startActivity(new Intent(HomeActivity.this, Trading.class));
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Mass Gainer");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_baseline_keyboard_24, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Muscle Builder");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.preworkout, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("Protein");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.protein, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFour.setText("Pre-Workout / Essentials");
        tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.workoutessentials, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabFour);
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new MassGainerCategory(), "Mass Gainer");
        adapter.addFrag(new MuscleBuilderCategory(), "Muscle Builder");
        adapter.addFrag(new ProteinsCategory(), "Protein");
        adapter.addFrag(new PreWorkoutCategory(), "Pre-Workout / Essentials");
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
        Hawk.init(HomeActivity.this).build();
        View hView = navigationView.getHeaderView(0);
        TextView header_name = (TextView) hView.findViewById(R.id.textview_header_name);
        String name = Hawk.get("name");
        header_name.setText(name);

        TextView header_email = (TextView) hView.findViewById(R.id.textView_header_email_address);
        String email = Hawk.get("email");
        header_email.setText(email);

        TextView header_phone = (TextView) hView.findViewById(R.id.textView_header_phone_no);
        String phone = Hawk.get("phone");
        header_phone.setText(phone);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        cart.setCount((int) new Database(getApplicationContext()).count()); // Set the count value to show on badge

    }

    @Override
    protected void onResume() {
        super.onResume();
        cart.setCount((int) new Database(getApplicationContext()).count()); // Set the count value to show on badge

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
