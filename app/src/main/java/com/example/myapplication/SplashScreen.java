package com.example.myapplication;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.TextureView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity {

    public static int SPLASH_TIME = 3500;

    //variables
    Animation topAnim;
    TextView appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

    //    requestAllPermission();
        //Animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);

        //Hooks
        appName = findViewById(R.id.textView);
        appName.setAnimation(topAnim);

        //Redirect to Login Screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                Boolean check = sharedPreferences.getBoolean("flag", false);
                Intent intent;
                if (check){
                    intent = new Intent(SplashScreen.this, HomeScreen.class);
                }else {
                    intent = new Intent(SplashScreen.this, LoginScreen.class);
                }

                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME);
    }
}