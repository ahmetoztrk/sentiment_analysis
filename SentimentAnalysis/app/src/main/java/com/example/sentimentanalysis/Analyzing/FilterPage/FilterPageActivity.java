package com.example.sentimentanalysis.Analyzing.FilterPage;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import com.example.sentimentanalysis.Constants;
import com.example.sentimentanalysis.R;

public class FilterPageActivity extends AppCompatActivity {

    FilterPageAdapter filterPageAdapter;
    ArrayList<FilterPageModel> filterPageModelArrayList;

    ViewPager filterPageViewPager;
    TextView filterPageCurrentMoodTextView;
    CheckBox suggestionAgeCheckBox, suggestionGenderCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_page);

        init();

        loadCards();

        setButtonsListener();
    }

    private void init(){
        filterPageViewPager = findViewById(R.id.filter_page_vp);
        filterPageCurrentMoodTextView = findViewById(R.id.current_mood_tv);
        suggestionAgeCheckBox = findViewById(R.id.suggestion_age_check_box);
        suggestionGenderCheckBox = findViewById(R.id.suggestion_gender_check_box);

        filterPageCurrentMoodTextView.setText(String.format("'%s'", Constants.S_EMOTION));
    }

    private void loadCards(){
        filterPageModelArrayList = new ArrayList<>();

        filterPageModelArrayList.add(new FilterPageModel(
                "Musics",
                "5 different musics were selected according to your feelings.",
                R.drawable.icon_musics));

        filterPageModelArrayList.add(new FilterPageModel(
                "Series",
                "5 different series were selected according to your feelings.",
                R.drawable.icon_series));

        filterPageModelArrayList.add(new FilterPageModel(
                "Movies",
                "5 different movies were selected according to your feelings.",
                R.drawable.icon_movies));

        filterPageModelArrayList.add(new FilterPageModel(
                "Books",
                "5 different books were selected according to your feelings.",
                R.drawable.icon_books));

        filterPageModelArrayList.add(new FilterPageModel(
                "Activities",
                "5 different activities were selected according to your feelings.",
                R.drawable.icon_activities));

        filterPageAdapter = new FilterPageAdapter(this, filterPageModelArrayList);

        filterPageViewPager.setAdapter(filterPageAdapter);

        filterPageViewPager.setPadding(100,0,100,0);
    }

    private void setButtonsListener(){
        suggestionAgeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.S_AGE_OPTION = suggestionAgeCheckBox.isChecked();
            }
        });

        suggestionGenderCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.S_GENDER_OPTION = suggestionGenderCheckBox.isChecked();
            }
        });
    }
}