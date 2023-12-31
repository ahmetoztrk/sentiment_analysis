package com.example.duyguanalizi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class SentimentAnalysisPage extends AppCompatActivity {

    Button trueBtn, falseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentiment_analysis_page);

        Init();
        SetButtonsListener();
    }

    void Init() {
        trueBtn = findViewById(R.id.button8);
        falseBtn = findViewById(R.id.button9);
    }

    void SetButtonsListener(){
        trueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SentimentAnalysisPage.this, FilterPage.class);
                startActivity(intent);
            }
        });

        falseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SentimentAnalysisPage.this, HomePage.class);
                startActivity(intent);
            }
        });
    }
}