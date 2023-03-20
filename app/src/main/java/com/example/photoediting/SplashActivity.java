package com.example.photoediting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(1000);
                    startActivity(new Intent(getApplicationContext(), select.class));
                    finish();

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                super.run();
            }
        };
        thread.start();

    }
}