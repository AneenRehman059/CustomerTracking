package com.zasa.superduper.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import com.zasa.superduper.CustomToastError;
import com.zasa.superduper.Home.HomeFragment;
import com.zasa.superduper.Profile.ProfileActivity;
import com.zasa.superduper.R;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    FloatingActionButton floatingActionButton;
    ArrayList<Fragment> fragments = new ArrayList<>();
    NavigationView navigationView;
    TextView headerUsername, abUserName;
    DrawerLayout drawer;
    View header;
    View view;
    Context context;
    CircleImageView userHeaderImage;
    int value;
    Handler handler = new Handler();
    ProgressBar progressBar;
    TextView text_id;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupDrawer();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        header = navigationView.getHeaderView(0);
        bottomNavigationView.setBackground(null);
        headerUsername = header.findViewById(R.id.tv_haader_name);
        progressBar = findViewById(R.id.progressbarId);
        text_id = findViewById(R.id.textid);
        view = findViewById(android.R.id.content);
        abUserName = findViewById(R.id.ab_username);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                startProgress();
            }
        });
        thread.start()

        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        fragments.add(new HomeFragment());

//        fragments.add(new MoreFragment());
        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, fragments.get(0)).commit();
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragments.get(0)).commit();
            } else if (item.getItemId() == R.id.operation) {

                if(isTimeAutomatic(HomeActivity.this)) {
                    startActivity(new Intent(HomeActivity.this, RoutesActivity.class));
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    finish();
                }
                else{
                    new CustomToastError().Show_Toast(HomeActivity.this, view, getString(R.string.error_msg));
//                    Toast.makeText(HomeActivity.this, "Please set auto time in datetime setting", Toast.LENGTH_SHORT).show();
                }
            } else if (item.getItemId() == R.id.setting) {
                startActivity(new Intent(HomeActivity.this, SettingActivity.class));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
            } else if (item.getItemId() == R.id.more) {
                startActivity(new Intent(HomeActivity.this, MoreActivity.class));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragments.get(1)).commit();
            }
            return true;
        });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, new HomeFragment());
        transaction.commit();

//        context = HomeActivity.this;

    }
    public static boolean isTimeAutomatic(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
        } else {
            return android.provider.Settings.System.getInt(c.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) == 1;
        }
    }
    public void setupDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {

            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(intent);
        } else if (id == R.id.sync_down) {
//            Intent intent = new Intent(HomeActivity.this, FAQsActivity.class);
//            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//            startActivity(intent);

        } else if (id == R.id.sync_up) {

//            Intent intent1 = new Intent(context, WebViewActivity.class);
//            intent1.putExtra("URL", Privacy_Policy_Url);
//            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//            startActivity(intent1);
//            finish();
        } else if (id == R.id.privacy_policy) {

//            Intent intent1 = new Intent(context, WebViewActivity.class);
//            intent1.putExtra("URL", Terms_Conditions_Url);
//            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//            startActivity(intent1);
//            finish();
        } else if (id == R.id.setting) {
//            Intent intent = new Intent(HomeActivity.this, ContactUsActivity.class);
//            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//            startActivity(intent);
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count ==0){
            finish();
            super.onBackPressed();
        }
        else
        {
            getSupportFragmentManager().popBackStack();
        }
    }

    public void startProgress() {
        for (value = 0; value < 100; value = value + 1) {
            try {
                Thread.sleep(50);
                progressBar.setProgress(value);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    text_id.setText(String.valueOf(value));
                }
            }, 5000);
        }
    }

}