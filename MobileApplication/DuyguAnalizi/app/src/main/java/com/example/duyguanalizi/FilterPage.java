package com.example.duyguanalizi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;
public class FilterPage extends AppCompatActivity {

    CheckBox seriesChkBox, aChkBox, filmChkBox, bookChkBox, activityChkBox;
    Button seeSuggestionBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_page);

        Init();
        SetButtonsListener();
    }

    void Init(){
        seriesChkBox = findViewById(R.id.check_music);
        aChkBox = findViewById(R.id.checkBox_series);
        filmChkBox = findViewById(R.id.checkBox_film);
        bookChkBox = findViewById(R.id.checkBox_book);
        activityChkBox = findViewById(R.id.checkBox_activity);

        seeSuggestionBtn = findViewById(R.id.button11);
    }

    void SetButtonsListener() {
        seeSuggestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FilterPage.this, SuggestionPage.class);
                startActivity(intent);
            }
        });
    }
}