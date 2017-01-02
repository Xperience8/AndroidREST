package com.example.juras.damitest.login;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.juras.damitest.R;

/**
 * Shows company logo as theme and immediately launch login activity
 * */
public class SplashActivity extends AppCompatActivity
{

    /** Immediately launch login activity*/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }
}
