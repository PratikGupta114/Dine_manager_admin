package com.example.dine_manager_admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    private TextView    dineManagerText, footerText, adminText;
    private ImageView   restoGLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#2C0003"));

        setupUI();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }, 3000);

        Animation fadeAnimation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.fade_animation);
        dineManagerText.startAnimation(fadeAnimation);
        footerText.startAnimation(fadeAnimation);
        restoGLogo.startAnimation(fadeAnimation);
        adminText.startAnimation(fadeAnimation);
    }

    private void setupUI() {
        dineManagerText = findViewById(R.id.tv1_dineManagerText);
        footerText = findViewById(R.id.tv1_footerText);
        restoGLogo = findViewById(R.id.imv1_restog_logo_footer);
        adminText = findViewById(R.id.tv1_adminText);
    }
}
