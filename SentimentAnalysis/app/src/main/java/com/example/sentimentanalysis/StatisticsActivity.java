package com.example.sentimentanalysis;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StatisticsActivity extends AppCompatActivity {

    TextView musicsBtn, seriesBtn, moviesBtn, booksBtn, activitiesBtn, select, previouslyLikedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        init();

        setButtonsListener();
    }

    private void init(){
        musicsBtn = findViewById(R.id.musics_btn);
        seriesBtn = findViewById(R.id.series_btn);
        moviesBtn = findViewById(R.id.movies_btn);
        booksBtn = findViewById(R.id.books_btn);
        activitiesBtn = findViewById(R.id.activities_btn);

        select = findViewById(R.id.select);

        previouslyLikedTextView = findViewById(R.id.previously_liked_text_view);

        GetPreviouslyLiked("Musics");
    }

    private void setButtonsListener(){
        musicsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select.animate().x(0).setDuration(100);

                GetPreviouslyLiked("Musics");
            }
        });

        seriesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = seriesBtn.getWidth();
                select.animate().x(size).setDuration(100);

                GetPreviouslyLiked("Series");
            }
        });

        moviesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = seriesBtn.getWidth() * 2;
                select.animate().x(size).setDuration(100);

                GetPreviouslyLiked("Movies");
            }
        });

        booksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = seriesBtn.getWidth() * 3;
                select.animate().x(size).setDuration(100);

                GetPreviouslyLiked("Books");
            }
        });

        activitiesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = seriesBtn.getWidth() * 4;
                select.animate().x(size).setDuration(100);

                GetPreviouslyLiked("Activities");
            }
        });
    }

    private void GetPreviouslyLiked(String fileName){
        previouslyLikedTextView.setText(FileOperations.readFile(getApplicationContext(), fileName + ".txt"));
    }
}
