package com.example.duyguanalizi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomePage extends AppCompatActivity {

    Button backBtn, camBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Init();
        SetButtonsListener();
    }

    void Init(){
        backBtn = findViewById(R.id.button6);
        camBtn = findViewById(R.id.button3);
    }

    void SetButtonsListener(){
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent init = new Intent(HomePage.this, LoginPage.class);
                startActivity(init);
            }
        });

        camBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent init = new Intent(HomePage.this, SentimentAnalysisPage.class);
                startActivity(init);
            }
        });
    }
}
