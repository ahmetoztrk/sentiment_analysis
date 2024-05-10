package com.example.sentimentanalysis.Analyzing.FilterPage;

import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_page);

        init();

        loadCards();
    }

    private void init(){
        filterPageViewPager = findViewById(R.id.filter_page_vp);
        filterPageCurrentMoodTextView = findViewById(R.id.current_mood_tv);

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
}