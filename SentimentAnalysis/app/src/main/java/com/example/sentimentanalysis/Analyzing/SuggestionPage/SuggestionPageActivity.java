package com.example.sentimentanalysis.Analyzing.SuggestionPage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.sentimentanalysis.R;

import java.util.ArrayList;

public class SuggestionPageActivity extends AppCompatActivity {

    SuggestionPageAdapter suggestionPageAdapter;
    ArrayList<SuggestionPageModel> suggestionPageModelArrayList;

    ViewPager suggestionPageViewPager;
    TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion_page);

        init();

        loadCards();
    }

    private void init(){
        suggestionPageViewPager = findViewById(R.id.suggestion_page_vp);
        titleTextView = findViewById(R.id.suggestion_page_title_tv);

        Intent intent = getIntent();
        int position = intent.getIntExtra("position",0);

        if(position == 0){
            GetMusics();
        }else if(position == 1){
            GetSeries();
        }else if(position == 2){
            GetMovies();
        }else if(position == 3){
            GetBooks();
        }else if(position == 4){
            GetActivities();
        }
    }

    private void loadCards(){
        suggestionPageModelArrayList = new ArrayList<>();

        suggestionPageModelArrayList.add(new SuggestionPageModel(
                "Title 01",
                "Description 01",
                R.drawable.ic_launcher_background));

        suggestionPageModelArrayList.add(new SuggestionPageModel(
                "Title 02",
                "Description 02",
                R.drawable.ic_launcher_background));

        suggestionPageModelArrayList.add(new SuggestionPageModel(
                "Title 03",
                "Description 03",
                R.drawable.ic_launcher_background));

        suggestionPageModelArrayList.add(new SuggestionPageModel(
                "Title 4",
                "Description 04",
                R.drawable.ic_launcher_background));

        suggestionPageModelArrayList.add(new SuggestionPageModel(
                "Title 5",
                "Description 05",
                R.drawable.ic_launcher_background));

        suggestionPageAdapter = new SuggestionPageAdapter(this, suggestionPageModelArrayList);

        suggestionPageViewPager.setAdapter(suggestionPageAdapter);

        suggestionPageViewPager.setPadding(100,0,100,0);
    }

    @SuppressLint("SetTextI18n")
    private void GetMusics(){
        titleTextView.setText("Musics");
    }

    @SuppressLint("SetTextI18n")
    private void GetSeries(){
        titleTextView.setText("Series");
    }

    @SuppressLint("SetTextI18n")
    private void GetMovies(){
        titleTextView.setText("Movies");
    }

    @SuppressLint("SetTextI18n")
    private void GetBooks(){
        titleTextView.setText("Books");
    }

    @SuppressLint("SetTextI18n")
    private void GetActivities(){
        titleTextView.setText("Activities");
    }
}