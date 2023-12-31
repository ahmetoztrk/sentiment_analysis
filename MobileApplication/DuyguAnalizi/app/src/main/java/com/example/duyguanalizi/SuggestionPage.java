package com.example.duyguanalizi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SuggestionPage extends AppCompatActivity {

    Button mainMenuBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion_page);

        Init();
        SetButtonsListener();
    }

    void Init(){
        mainMenuBtn = findViewById(R.id.button10);
    }

    void SetButtonsListener() {
        mainMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent init = new Intent(SuggestionPage.this, HomePage.class);
                startActivity(init);
            }
        });
    }
}
