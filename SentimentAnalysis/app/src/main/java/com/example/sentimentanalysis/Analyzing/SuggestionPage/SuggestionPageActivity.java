package com.example.sentimentanalysis.Analyzing.SuggestionPage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.sentimentanalysis.BuildConfig;
import com.example.sentimentanalysis.Constants;
import com.example.sentimentanalysis.Analyzing.LoadingDialog;
import com.example.sentimentanalysis.R;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.Executor;

public class SuggestionPageActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    HashMap<String, Object> data;

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

        initFirebase();

        loadCards();

        setButtonsListener();
    }

    private void init(){
        suggestionLoadingDialog = new LoadingDialog(SuggestionPageActivity.this);

        titleTextView = findViewById(R.id.suggestion_page_title_tv);

        suggestionPageViewPager = findViewById(R.id.suggestion_page_vp);

        title = new String[Constants.S_ADAPTER_SIZE];
        description = new String[Constants.S_ADAPTER_SIZE];

        for(int i=0; i<Constants.S_ADAPTER_SIZE; i++){
            title[i] = getString(R.string.Title) + i;
            description[i] = getString(R.string.Description) + i;
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

    private void initFirebase(){
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
    }

    private void loadCards(){
        suggestionPageModelArrayList = new ArrayList<>();

        for(int i=0; i<Constants.S_ADAPTER_SIZE; i++){
            suggestionPageModelArrayList.add(new SuggestionPageModel(
                    title[i],
                    description[i],
                    getIcon()));
        }

        suggestionPageAdapter = new SuggestionPageAdapter(this, suggestionPageModelArrayList);

        suggestionPageViewPager.setAdapter(suggestionPageAdapter);

        suggestionPageViewPager.setPadding(100,0,100,0);
    }

    private int getIcon(){
        Intent intent = getIntent();
        int position = intent.getIntExtra("position",0);

        if(position == 0){
            return R.drawable.icon_musics;
        }else if(position == 1){
            return R.drawable.icon_series;
        }else if(position == 2){
            return R.drawable.icon_movies;
        }else if(position == 3){
            return R.drawable.icon_books;
        }else if(position == 4){
            return R.drawable.icon_activities;
        }else{
            return R.drawable.icon_profile_1;
        }
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
        activityText = getString(R.string.Musics);
        titleTextView.setText(activityText);

        suggestionLoadingDialog.StartLoadingDialog();

        GetSuggestion(getString(R.string.music));
    }

    @SuppressLint("SetTextI18n")
    private void GetSeries(){
        activityText = getString(R.string.Series);
        titleTextView.setText(activityText);

        suggestionLoadingDialog.StartLoadingDialog();

        GetSuggestion(getString(R.string.series));
    }

    @SuppressLint("SetTextI18n")
    private void GetMovies(){
        activityText = getString(R.string.Movies);
        titleTextView.setText(activityText);

        suggestionLoadingDialog.StartLoadingDialog();

        GetSuggestion(getString(R.string.movie));
    }

    @SuppressLint("SetTextI18n")
    private void GetBooks(){
        activityText = getString(R.string.Books);
        titleTextView.setText(activityText);

        suggestionLoadingDialog.StartLoadingDialog();

        GetSuggestion(getString(R.string.book));
    }

    @SuppressLint("SetTextI18n")
    private void GetActivities(){
        activityText = getString(R.string.Activities);
        titleTextView.setText(activityText);

        suggestionLoadingDialog.StartLoadingDialog();

        GetSuggestion(getString(R.string.activity));
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

                for(int i=0; i<Constants.S_ADAPTER_SIZE; i++){
                    suggestionPageModelArrayList.get(i).setTitle(title[i]);

                    title[i] = title[i].substring(3);

                    GeminiAPIGetDescription(getString(R.string.query_description, title[i]), i);

                    if(Constants.S_DEBUG_MODE){
                        Toast.makeText(SuggestionPageActivity.this, getString(R.string.query_description, title[i]), Toast.LENGTH_LONG).show();
                    }
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

        for(int i=0; i<Constants.S_ADAPTER_SIZE; i++) {
            if (like[i]) {
                //FileOperations.addFile(getApplicationContext(), activityText + ".txt", title[i] + "\n");
                //FileOperations.addFile(getApplicationContext(), activityText + ".txt", Constants.S_EMOTION + "\n");

                String dateTime = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                //FileOperations.addFile(getApplicationContext(), activityText + ".txt", dateTime + "\n");

                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                //FileOperations.addFile(getApplicationContext(), activityText + ".txt", currentTime + "\n\n");

                
                user = auth.getCurrentUser();

                data = new HashMap<>();
                data.put("suggestion", title[i]);
                data.put("category", activityText);
                data.put("emotion", Constants.S_EMOTION);
                data.put("date", dateTime);
                data.put("time", currentTime);

                reference.child("users").child(user.getUid()).child("suggestions").child(title[i]).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        }
    }

    private void GetSuggestion(String categoryName){
        if(Constants.S_AGE_OPTION && Constants.S_GENDER_OPTION){
            GeminiAPIGetTitle(getString(R.string.query_age_and_gender,
                    Constants.S_AGE,
                    Constants.S_GENDER,
                    Constants.S_ADAPTER_SIZE,
                    Constants.S_EMOTION,
                    categoryName));

            if(Constants.S_DEBUG_MODE) {
                Toast.makeText(SuggestionPageActivity.this, getString(R.string.query_age_and_gender,
                        Constants.S_AGE,
                        Constants.S_GENDER,
                        Constants.S_ADAPTER_SIZE,
                        Constants.S_EMOTION,
                        categoryName), Toast.LENGTH_LONG).show();
            }
        }else if(Constants.S_AGE_OPTION){
            GeminiAPIGetTitle(getString(R.string.query_age,
                    Constants.S_AGE,
                    Constants.S_ADAPTER_SIZE,
                    Constants.S_EMOTION,
                    categoryName));

            if(Constants.S_DEBUG_MODE) {
                Toast.makeText(SuggestionPageActivity.this, getString(R.string.query_age,
                        Constants.S_AGE,
                        Constants.S_ADAPTER_SIZE,
                        Constants.S_EMOTION,
                        categoryName), Toast.LENGTH_LONG).show();
            }
        }else if(Constants.S_GENDER_OPTION){
            GeminiAPIGetTitle(getString(R.string.query_gender,
                    Constants.S_GENDER,
                    Constants.S_ADAPTER_SIZE,
                    Constants.S_EMOTION,
                    categoryName));

            if(Constants.S_DEBUG_MODE) {
                Toast.makeText(SuggestionPageActivity.this, getString(R.string.query_gender,
                        Constants.S_GENDER,
                        Constants.S_ADAPTER_SIZE,
                        Constants.S_EMOTION,
                        categoryName), Toast.LENGTH_LONG).show();
            }
        }else{
            GeminiAPIGetTitle(getString(R.string.query,
                    Constants.S_ADAPTER_SIZE,
                    Constants.S_EMOTION,
                    categoryName));

            if(Constants.S_DEBUG_MODE) {
                Toast.makeText(SuggestionPageActivity.this, getString(R.string.query,
                        Constants.S_ADAPTER_SIZE,
                        Constants.S_EMOTION,
                        categoryName), Toast.LENGTH_LONG).show();
            }
        }
    }
}