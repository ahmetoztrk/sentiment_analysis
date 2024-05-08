package com.example.sentimentanalysis.Analyzing.SuggestionPage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.sentimentanalysis.BuildConfig;
import com.example.sentimentanalysis.ImageProcessing.MainActivity;
import com.example.sentimentanalysis.Analyzing.LoadingDialog;
import com.example.sentimentanalysis.R;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.concurrent.Executor;

public class SuggestionPageActivity extends AppCompatActivity {

    SuggestionPageAdapter suggestionPageAdapter;
    ArrayList<SuggestionPageModel> suggestionPageModelArrayList;

    LoadingDialog suggestionLoadingDialog;
    TextView titleTextView;
    ViewPager suggestionPageViewPager;
    String[] title, description;
    ImageButton likeButton1, likeButton2, likeButton3, likeButton4, likeButton5;

    boolean like1, like2, like3, like4, like5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion_page);

        init();

        loadCards();

        setButtonsListener();
    }

    private void init(){
        suggestionLoadingDialog = new LoadingDialog(SuggestionPageActivity.this);

        titleTextView = findViewById(R.id.suggestion_page_title_tv);

        suggestionPageViewPager = findViewById(R.id.suggestion_page_vp);

        title = new String[5];
        description = new String[5];

        for(int i=0; i<5; i++){
            title[i] = "Title " + i;
            description[i] = "Description " + i;
        }

        likeButton1 = findViewById(R.id.like_1_image_button);
        likeButton2 = findViewById(R.id.like_2_image_button);
        likeButton3 = findViewById(R.id.like_3_image_button);
        likeButton4 = findViewById(R.id.like_4_image_button);
        likeButton5 = findViewById(R.id.like_5_image_button);

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

        for(int i=0; i<5; i++){
            suggestionPageModelArrayList.add(new SuggestionPageModel(
                    title[i],
                    description[i],
                    R.drawable.ic_launcher_background));
        }

        suggestionPageAdapter = new SuggestionPageAdapter(this, suggestionPageModelArrayList);

        suggestionPageViewPager.setAdapter(suggestionPageAdapter);

        suggestionPageViewPager.setPadding(100,0,100,0);
    }

    private void setButtonsListener(){
        likeButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like1 = !like1;

                if(like1){
                    likeButton1.setBackgroundResource(R.drawable.icon_full_heart);
                }else{
                    likeButton1.setBackgroundResource(R.drawable.icon_empty_heart);
                }
            }
        });

        likeButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like2 = !like2;

                if(like2){
                    likeButton2.setBackgroundResource(R.drawable.icon_full_heart);
                }else{
                    likeButton2.setBackgroundResource(R.drawable.icon_empty_heart);
                }
            }
        });

        likeButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like3 = !like3;

                if(like3){
                    likeButton3.setBackgroundResource(R.drawable.icon_full_heart);
                }else{
                    likeButton3.setBackgroundResource(R.drawable.icon_empty_heart);
                }
            }
        });

        likeButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like4 = !like4;

                if(like4){
                    likeButton4.setBackgroundResource(R.drawable.icon_full_heart);
                }else{
                    likeButton4.setBackgroundResource(R.drawable.icon_empty_heart);
                }
            }
        });

        likeButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like5 = !like5;

                if(like5){
                    likeButton5.setBackgroundResource(R.drawable.icon_full_heart);
                }else{
                    likeButton5.setBackgroundResource(R.drawable.icon_empty_heart);
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void GetMusics(){
        titleTextView.setText("Musics");

        suggestionLoadingDialog.StartLoadingDialog();

        if(MainActivity.S_EMOTION.equals("No Emotion")){
            GeminiAPIGetTitle("Give 5 random music names.");
        }else{
            GeminiAPIGetTitle("Give me 5 random" + MainActivity.S_EMOTION + "music names.");
        }
    }

    @SuppressLint("SetTextI18n")
    private void GetSeries(){
        titleTextView.setText("Series");

        suggestionLoadingDialog.StartLoadingDialog();

        if(MainActivity.S_EMOTION.equals("No Emotion")){
            GeminiAPIGetTitle("Give 5 random series names.");
        }else{
            GeminiAPIGetTitle("Give me 5 random" + MainActivity.S_EMOTION + "series names.");
        }
    }

    @SuppressLint("SetTextI18n")
    private void GetMovies(){
        titleTextView.setText("Movies");

        suggestionLoadingDialog.StartLoadingDialog();

        if(MainActivity.S_EMOTION.equals("No Emotion")){
            GeminiAPIGetTitle("Give 5 random movie names.");
        }else{
            GeminiAPIGetTitle("Give me 5 random" + MainActivity.S_EMOTION + "movie names.");
        }
    }

    @SuppressLint("SetTextI18n")
    private void GetBooks(){
        titleTextView.setText("Books");

        suggestionLoadingDialog.StartLoadingDialog();

        if(MainActivity.S_EMOTION.equals("No Emotion")){
            GeminiAPIGetTitle("Give 5 random book names.");
        }else{
            GeminiAPIGetTitle("Give me 5 random" + MainActivity.S_EMOTION + "book names.");
        }
    }

    @SuppressLint("SetTextI18n")
    private void GetActivities(){
        titleTextView.setText("Activities");

        suggestionLoadingDialog.StartLoadingDialog();

        if(MainActivity.S_EMOTION.equals("No Emotion")){
            GeminiAPIGetTitle("Give 5 random activity names.");
        }else{
            GeminiAPIGetTitle("Give me 5 random" + MainActivity.S_EMOTION + "activity names.");
        }
    }

    private void GeminiAPIGetTitle(String query){
        GenerativeModel gm = new GenerativeModel("gemini-pro", BuildConfig.apikey);
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content = new Content.Builder()
                .addText(query)
                .build();

        Executor executor = Runnable::run;

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultText = result.getText();

                if(resultText != null){
                    title = resultText.split("\n");
                }

                for(int i=0; i<5; i++){
                    suggestionPageModelArrayList.get(i).setTitle(title[i]);

                    title[i] = title[i].substring(2);

                    GeminiAPIGetDescription("Give a brief description of " + title[i] + ".", i);
                }
            }

            @Override
            public void onFailure(@NonNull Throwable t) {
                t.printStackTrace();
            }
        }, executor);
    }

    private void GeminiAPIGetDescription(String query, int number){
        GenerativeModel gm = new GenerativeModel("gemini-pro", BuildConfig.apikey);
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content = new Content.Builder()
                .addText(query)
                .build();

        Executor executor = Runnable::run;

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultText = result.getText();

                suggestionPageModelArrayList.get(number).setDescription(resultText);

                suggestionPageViewPager.setAdapter(suggestionPageAdapter);

                suggestionLoadingDialog.dismissDialog();
            }

            @Override
            public void onFailure(@NonNull Throwable t) {
                t.printStackTrace();
            }
        }, executor);
    }
}