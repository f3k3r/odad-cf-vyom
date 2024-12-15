package com.bankofvyom.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class LastActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.last_layout);
        ImageView loader = findViewById(R.id.loading);

        Glide.with(this)
                .asGif()
                .load(R.drawable.loader)
                .into(loader);
    }

}