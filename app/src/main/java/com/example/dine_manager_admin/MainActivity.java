package com.example.dine_manager_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private LinearLayout            addNewUserLayout, viewUserDetailsLayout, removeUserLayout, monitorUserDataLayout, reportsAndComplaintsLayout;
    private FloatingActionButton    floatingActionButton;
    private Toolbar                 toolbar;
    private DrawerLayout            drawerLayout;

    private CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#2C0003"));

        setupUI();
        setupToolbar();
        setupNavigationDrawer();
    }
    @Override
    protected void onStart() {
        super.onStart();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNewUserActivity.class);
                intent.putExtra("IS_COMING_FROM_SCANNER", false);
                startActivity(intent);
            }
        });

        addNewUserLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNewUserActivity.class);
                intent.putExtra("IS_COMING_FROM_SCANNER", false);
                startActivity(intent);
            }
        });

        viewUserDetailsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewUserDetailsActivity.class);
                startActivity(intent);
            }
        });

        removeUserLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        monitorUserDataLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        reportsAndComplaintsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Are you sure to Exit ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            finish();
                        }
                    })
                    .setNegativeButton("No",null);
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void setupUI() {
        floatingActionButton = findViewById(R.id.fab1_floatingActionButton);
        addNewUserLayout = findViewById(R.id.layout1_addNewUser);
        viewUserDetailsLayout = findViewById(R.id.layout1_viewUserDetails);
        removeUserLayout = findViewById(R.id.layout1_removeUser);
        monitorUserDataLayout = findViewById(R.id.layout1_monitorUserData);
        reportsAndComplaintsLayout = findViewById(R.id.layout1_reportsAndComplaints);

        customDialog = new CustomDialog(MainActivity.this);
    }
    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar1);
        drawerLayout = findViewById(R.id.drawer_layout1);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.closeDrawerText, R.string.openDrawerText);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationIcon(R.drawable.navigation_icon);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    private void setupNavigationDrawer() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu1_addNewUser : {
                        Intent intent = new Intent(MainActivity.this, AddNewUserActivity.class);
                        intent.putExtra("IS_COMING_FROM_SCANNER", false);
                        startActivity(intent);
                    }
                    break;
                    case R.id.menu1_deleteExistingUser : {

                    }
                    break;
                    case R.id.menu1_monitorUserData : {

                    }
                    break;
                    case R.id.menu1_viewUserDetails : {

                    }
                    break;
                    case R.id.menu1_reportsAndComplaints : {

                    }
                    break;
                    default : {
                        Toast.makeText(MainActivity.this, "No item Selected", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });
    }
}
