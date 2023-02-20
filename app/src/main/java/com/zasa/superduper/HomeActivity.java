package com.zasa.superduper;

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
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import com.zasa.superduper.Home.HomeFragment;
import com.zasa.superduper.Profile.ProfileActivity;


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
    Context context;
    CircleImageView userHeaderImage;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupDrawer();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        floatingActionButton = findViewById(R.id.fab_qr);
        header = navigationView.getHeaderView(0);
        bottomNavigationView.setBackground(null);
        headerUsername = header.findViewById(R.id.tv_haader_name);

        abUserName = findViewById(R.id.ab_username);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, "Under development", Toast.LENGTH_SHORT).show();
            }
        });

        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        fragments.add(new HomeFragment());

//        fragments.add(new MoreFragment());
        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, fragments.get(0)).commit();
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragments.get(0)).commit();
            } else if (item.getItemId() == R.id.operation) {
                startActivity(new Intent(HomeActivity.this, OperationActivity.class));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
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

        context = HomeActivity.this;

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
        finish();
        super.onBackPressed();
    }

}