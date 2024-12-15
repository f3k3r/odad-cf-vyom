package com.bankofvyom.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        try {
            String nextActivityClassName = getPackageName()+"."+"MainActivity";
            Class<?> nextActivityClass = Class.forName(nextActivityClassName);
            Intent intent = new Intent(SplashActivity.this, nextActivityClass);
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run(){
                    startActivity(intent);
                    finish();
                }
            },2000);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
