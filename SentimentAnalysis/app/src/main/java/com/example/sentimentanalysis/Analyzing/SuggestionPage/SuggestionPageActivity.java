package com.example.sentimentanalysis.Analyzing.SuggestionPage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.sentimentanalysis.Analyzing.FilterPage.FilterPageAdapter;
import com.example.sentimentanalysis.BuildConfig;
import com.example.sentimentanalysis.ImageProcessing.MainActivity;
import com.example.sentimentanalysis.Analyzing.LoadingDialog;
import com.example.sentimentanalysis.R;
import com.example.sentimentanalysis.FileOperations;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;

public class SuggestionPageActivity extends AppCompatActivity {

    SuggestionPageAdapter suggestionPageAdapter;
    ArrayList<SuggestionPageModel> suggestionPageModelArrayList;

    LoadingDialog suggestionLoadingDialog;
    TextView titleTextView;
    ViewPager suggestionPageViewPager;
    String[] title, description;
    ImageButton likeButton1, likeButton2, likeButton3, likeButton4, likeButton5;

    String activityText;
    boolean[] like;

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

        title = new String[FilterPageAdapter.S_ADAPTER_SIZE];
        description = new String[FilterPageAdapter.S_ADAPTER_SIZE];

        for(int i=0; i<FilterPageAdapter.S_ADAPTER_SIZE; i++){
            title[i] = "Title " + i;
            description[i] = "Description " + i;
        }

        likeButton1 = findViewById(R.id.like_1_image_button);
        likeButton2 = findViewById(R.id.like_2_image_button);
        likeButton3 = findViewById(R.id.like_3_image_button);
        likeButton4 = findViewById(R.id.like_4_image_button);
        likeButton5 = findViewById(R.id.like_5_image_button);

        like = new boolean[5];

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

        for(int i=0; i<FilterPageAdapter.S_ADAPTER_SIZE; i++){
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
                like[0] = !like[0];

                if(like[0]){
                    likeButton1.setBackgroundResource(R.drawable.icon_full_heart);
                }else{
                    likeButton1.setBackgroundResource(R.drawable.icon_empty_heart);
                }
            }
        });

        likeButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like[1] = !like[1];

                if(like[1]){
                    likeButton2.setBackgroundResource(R.drawable.icon_full_heart);
                }else{
                    likeButton2.setBackgroundResource(R.drawable.icon_empty_heart);
                }
            }
        });

        likeButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like[2] = !like[2];

                if(like[2]){
                    likeButton3.setBackgroundResource(R.drawable.icon_full_heart);
                }else{
                    likeButton3.setBackgroundResource(R.drawable.icon_empty_heart);
                }
            }
        });

        likeButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like[3] = !like[3];

                if(like[3]){
                    likeButton4.setBackgroundResource(R.drawable.icon_full_heart);
                }else{
                    likeButton4.setBackgroundResource(R.drawable.icon_empty_heart);
                }
            }
        });

        likeButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like[4] = !like[4];

                if(like[4]){
                    likeButton5.setBackgroundResource(R.drawable.icon_full_heart);
                }else{
                    likeButton5.setBackgroundResource(R.drawable.icon_empty_heart);
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void GetMusics(){
        activityText = "Musics";
        titleTextView.setText(activityText);

        suggestionLoadingDialog.StartLoadingDialog();

        if(MainActivity.S_EMOTION.equals("No Emotion")){
            GeminiAPIGetTitle("Give " + FilterPageAdapter.S_ADAPTER_SIZE + " random music names.");
        }else{
            GeminiAPIGetTitle("Give me " + FilterPageAdapter.S_ADAPTER_SIZE + " random " + MainActivity.S_EMOTION + " music names.");
        }
    }

    @SuppressLint("SetTextI18n")
    private void GetSeries(){
        activityText = "Series";
        titleTextView.setText(activityText);

        suggestionLoadingDialog.StartLoadingDialog();

        if(MainActivity.S_EMOTION.equals("No Emotion")){
            GeminiAPIGetTitle("Give " + FilterPageAdapter.S_ADAPTER_SIZE + " random series names.");
        }else{
            GeminiAPIGetTitle("Give me " + FilterPageAdapter.S_ADAPTER_SIZE + " random " + MainActivity.S_EMOTION + " series names.");
        }
    }

    @SuppressLint("SetTextI18n")
    private void GetMovies(){
        activityText = "Movies";
        titleTextView.setText(activityText);

        suggestionLoadingDialog.StartLoadingDialog();

        if(MainActivity.S_EMOTION.equals("No Emotion")){
            GeminiAPIGetTitle("Give " + FilterPageAdapter.S_ADAPTER_SIZE + " random movie names.");
        }else{
            GeminiAPIGetTitle("Give me " + FilterPageAdapter.S_ADAPTER_SIZE + " random " + MainActivity.S_EMOTION + " movie names.");
        }
    }

    @SuppressLint("SetTextI18n")
    private void GetBooks(){
        activityText = "Books";
        titleTextView.setText(activityText);

        suggestionLoadingDialog.StartLoadingDialog();

        if(MainActivity.S_EMOTION.equals("No Emotion")){
            GeminiAPIGetTitle("Give " + FilterPageAdapter.S_ADAPTER_SIZE + " random book names.");
        }else{
            GeminiAPIGetTitle("Give me " + FilterPageAdapter.S_ADAPTER_SIZE + " random " + MainActivity.S_EMOTION + " book names.");
        }
    }

    @SuppressLint("SetTextI18n")
    private void GetActivities(){
        activityText = "Activities";
        titleTextView.setText(activityText);

        suggestionLoadingDialog.StartLoadingDialog();

        if(MainActivity.S_EMOTION.equals("No Emotion")){
            GeminiAPIGetTitle("Give " + FilterPageAdapter.S_ADAPTER_SIZE + " random activity names.");
        }else{
            GeminiAPIGetTitle("Give me " + FilterPageAdapter.S_ADAPTER_SIZE + " random " + MainActivity.S_EMOTION + " activity names.");
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

                for(int i=0; i<FilterPageAdapter.S_ADAPTER_SIZE; i++){
                    suggestionPageModelArrayList.get(i).setTitle(title[i]);

                    title[i] = title[i].substring(3);

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

    @Override
    protected void onStop() {
        super.onStop();

        for(int i=0; i<FilterPageAdapter.S_ADAPTER_SIZE; i++){
            if(like[i]){
                FileOperations.addFile(getApplicationContext(), activityText + ".txt", title[i] + "\n");
                FileOperations.addFile(getApplicationContext(), activityText + ".txt", MainActivity.S_EMOTION + "\n");

                String dateTime = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                FileOperations.addFile(getApplicationContext(), activityText + ".txt", dateTime + "\n");

                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                FileOperations.addFile(getApplicationContext(), activityText + ".txt", currentTime + "\n\n");
            }
        }
    }
}