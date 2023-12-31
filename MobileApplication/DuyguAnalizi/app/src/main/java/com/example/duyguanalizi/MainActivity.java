package com.example.duyguanalizi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Init();
    }

    void Init(){
        Intent intent = new Intent(MainActivity.this, LoginPage.class);
        startActivity(intent);
    }
}