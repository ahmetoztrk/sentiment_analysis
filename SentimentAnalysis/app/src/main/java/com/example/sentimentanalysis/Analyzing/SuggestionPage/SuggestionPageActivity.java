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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
    String categoryName;
    String categoryText;
    boolean[] like;
    int iconID;

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

        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);

        OnDataFetchedCallback callback = new OnDataFetchedCallback() {
            @Override
            public void onDataFetched(String previouslyLikedSuggestions) {
                GetSuggestion(previouslyLikedSuggestions);
            }

            @Override
            public void onDataNotAvailable() {
                GetSuggestion("");
            }
        };

        if (position == 0) {
            FirebaseAPIGetData(user.getUid(), "Musics", callback);
        } else if (position == 1) {
            FirebaseAPIGetData(user.getUid(), "Series", callback);
        } else if (position == 2) {
            FirebaseAPIGetData(user.getUid(), "Movies", callback);
        } else if (position == 3) {
            FirebaseAPIGetData(user.getUid(), "Books", callback);
        } else if (position == 4) {
            FirebaseAPIGetData(user.getUid(), "Activities", callback);
        }
    }

    private void loadCards(){
        suggestionPageModelArrayList = new ArrayList<>();

        for(int i=0; i<Constants.S_ADAPTER_SIZE; i++){
            suggestionPageModelArrayList.add(new SuggestionPageModel(
                    title[i],
                    description[i],
                    iconID));
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
        activityText = getString(R.string.Musics);
        categoryText = "Musics";
        categoryName = getString(R.string.music);
        titleTextView.setText(activityText);
        iconID = R.drawable.icon_musics;

        suggestionLoadingDialog.StartLoadingDialog();
    }

    @SuppressLint("SetTextI18n")
    private void GetSeries(){
        activityText = getString(R.string.Series);
        categoryText = "Series";
        categoryName = getString(R.string.series);
        titleTextView.setText(activityText);
        iconID = R.drawable.icon_series;

        suggestionLoadingDialog.StartLoadingDialog();
    }

    @SuppressLint("SetTextI18n")
    private void GetMovies(){
        activityText = getString(R.string.Movies);
        categoryText = "Movies";
        categoryName = getString(R.string.movie);
        titleTextView.setText(activityText);
        iconID = R.drawable.icon_movies;

        suggestionLoadingDialog.StartLoadingDialog();
    }

    @SuppressLint("SetTextI18n")
    private void GetBooks(){
        activityText = getString(R.string.Books);
        categoryText = "Books";
        categoryName = getString(R.string.book);
        iconID = R.drawable.icon_books;

        titleTextView.setText(activityText);

        suggestionLoadingDialog.StartLoadingDialog();
    }

    @SuppressLint("SetTextI18n")
    private void GetActivities(){
        activityText = getString(R.string.Activities);
        categoryText = "Activities";
        categoryName = getString(R.string.activity);
        iconID = R.drawable.icon_activities;

        titleTextView.setText(activityText);

        suggestionLoadingDialog.StartLoadingDialog();
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
                String dateTime = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

                user = auth.getCurrentUser();

                data = new HashMap<>();
                data.put("suggestion", title[i]);
                data.put("category", categoryText);
                data.put("emotion", Constants.S_EMOTION);
                data.put("date", dateTime);
                data.put("time", currentTime);

                reference = FirebaseDatabase.getInstance().getReference();
                reference.child("users").child(user.getUid()).child("suggestions").child(categoryText).child(title[i]).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    private void GetSuggestion(String previouslyLikedSuggestions){
        if(Constants.S_AGE_OPTION && Constants.S_GENDER_OPTION && Constants.S_LIKE_OLD_NICE_SUGGESTIONS_OPTION){
            if(dataExists){
                GeminiAPIGetTitle(getString(R.string.query_age_and_gender_and_liked,
                        Constants.S_AGE,
                        Constants.S_GENDER,
                        Constants.S_ADAPTER_SIZE,
                        Constants.S_EMOTION,
                        categoryName,
                        previouslyLikedSuggestions));

                if(!Constants.S_DEBUG_MODE) {
                    Toast.makeText(SuggestionPageActivity.this,
                            getString(R.string.query_age_and_gender_and_liked,
                                    Constants.S_AGE,
                                    Constants.S_GENDER,
                                    Constants.S_ADAPTER_SIZE,
                                    Constants.S_EMOTION,
                                    categoryName,
                                    previouslyLikedSuggestions), Toast.LENGTH_LONG).show();
                }
            }else{
                GeminiAPIGetTitle(getString(R.string.query_age_and_gender,
                        Constants.S_AGE,
                        Constants.S_GENDER,
                        Constants.S_ADAPTER_SIZE,
                        Constants.S_EMOTION,
                        categoryName));

                if(!Constants.S_DEBUG_MODE) {
                    Toast.makeText(SuggestionPageActivity.this,
                            getString(R.string.query_age_and_gender,
                                    Constants.S_AGE,
                                    Constants.S_GENDER,
                                    Constants.S_ADAPTER_SIZE,
                                    Constants.S_EMOTION,
                                    categoryName), Toast.LENGTH_LONG).show();
                }
            }
        }else if(Constants.S_AGE_OPTION && Constants.S_GENDER_OPTION){
            GeminiAPIGetTitle(getString(R.string.query_age_and_gender,
                    Constants.S_AGE,
                    Constants.S_GENDER,
                    Constants.S_ADAPTER_SIZE,
                    Constants.S_EMOTION,
                    categoryName));

            if(!Constants.S_DEBUG_MODE) {
                Toast.makeText(SuggestionPageActivity.this,
                        getString(R.string.query_age_and_gender,
                                Constants.S_AGE,
                                Constants.S_GENDER,
                                Constants.S_ADAPTER_SIZE,
                                Constants.S_EMOTION,
                                categoryName), Toast.LENGTH_LONG).show();
            }
        }else if(Constants.S_AGE_OPTION && Constants.S_LIKE_OLD_NICE_SUGGESTIONS_OPTION){
            if(dataExists){
                GeminiAPIGetTitle(getString(R.string.query_age_and_liked,
                        Constants.S_AGE,
                        Constants.S_ADAPTER_SIZE,
                        Constants.S_EMOTION,
                        categoryName,
                        previouslyLikedSuggestions));

                if(!Constants.S_DEBUG_MODE) {
                    Toast.makeText(SuggestionPageActivity.this,
                            getString(R.string.query_age_and_liked,
                                    Constants.S_AGE,
                                    Constants.S_ADAPTER_SIZE,
                                    Constants.S_EMOTION,
                                    categoryName,
                                    previouslyLikedSuggestions), Toast.LENGTH_LONG).show();
                }
            }else{
                GeminiAPIGetTitle(getString(R.string.query_age,
                        Constants.S_AGE,
                        Constants.S_ADAPTER_SIZE,
                        Constants.S_EMOTION,
                        categoryName));

                if(!Constants.S_DEBUG_MODE) {
                    Toast.makeText(SuggestionPageActivity.this,
                            getString(R.string.query_age,
                                    Constants.S_AGE,
                                    Constants.S_ADAPTER_SIZE,
                                    Constants.S_EMOTION,
                                    categoryName), Toast.LENGTH_LONG).show();
                }
            }
        }else if(Constants.S_AGE_OPTION){
            GeminiAPIGetTitle(getString(R.string.query_age,
                    Constants.S_AGE,
                    Constants.S_ADAPTER_SIZE,
                    Constants.S_EMOTION,
                    categoryName));

            if(!Constants.S_DEBUG_MODE) {
                Toast.makeText(SuggestionPageActivity.this,
                        getString(R.string.query_age,
                                Constants.S_AGE,
                                Constants.S_ADAPTER_SIZE,
                                Constants.S_EMOTION,
                                categoryName), Toast.LENGTH_LONG).show();
            }
        }else if(Constants.S_GENDER_OPTION && Constants.S_LIKE_OLD_NICE_SUGGESTIONS_OPTION){
            if(dataExists) {
                GeminiAPIGetTitle(getString(R.string.query_gender_and_liked,
                        Constants.S_GENDER,
                        Constants.S_ADAPTER_SIZE,
                        Constants.S_EMOTION,
                        categoryName,
                        previouslyLikedSuggestions));

                if (!Constants.S_DEBUG_MODE) {
                    Toast.makeText(SuggestionPageActivity.this,
                            getString(R.string.query_gender_and_liked,
                                    Constants.S_GENDER,
                                    Constants.S_ADAPTER_SIZE,
                                    Constants.S_EMOTION,
                                    categoryName,
                                    previouslyLikedSuggestions), Toast.LENGTH_LONG).show();
                }
            }else{
                GeminiAPIGetTitle(getString(R.string.query_gender,
                        Constants.S_GENDER,
                        Constants.S_ADAPTER_SIZE,
                        Constants.S_EMOTION,
                        categoryName));

                if(!Constants.S_DEBUG_MODE) {
                    Toast.makeText(SuggestionPageActivity.this,
                            getString(R.string.query_gender,
                                    Constants.S_GENDER,
                                    Constants.S_ADAPTER_SIZE,
                                    Constants.S_EMOTION,
                                    categoryName), Toast.LENGTH_LONG).show();
                }
            }
        }else if(Constants.S_GENDER_OPTION){
            GeminiAPIGetTitle(getString(R.string.query_gender,
                    Constants.S_GENDER,
                    Constants.S_ADAPTER_SIZE,
                    Constants.S_EMOTION,
                    categoryName));

            if(!Constants.S_DEBUG_MODE) {
                Toast.makeText(SuggestionPageActivity.this,
                        getString(R.string.query_gender,
                                Constants.S_GENDER,
                                Constants.S_ADAPTER_SIZE,
                                Constants.S_EMOTION,
                                categoryName), Toast.LENGTH_LONG).show();
            }
        }else if(Constants.S_LIKE_OLD_NICE_SUGGESTIONS_OPTION){
            if(dataExists) {
                GeminiAPIGetTitle(getString(R.string.query_liked,
                        Constants.S_ADAPTER_SIZE,
                        Constants.S_EMOTION,
                        categoryName,
                        previouslyLikedSuggestions));

                if (!Constants.S_DEBUG_MODE) {
                    Toast.makeText(SuggestionPageActivity.this,
                            getString(R.string.query_liked,
                                    Constants.S_ADAPTER_SIZE,
                                    Constants.S_EMOTION,
                                    categoryName,
                                    previouslyLikedSuggestions), Toast.LENGTH_LONG).show();
                }
            }else{
                GeminiAPIGetTitle(getString(R.string.query,
                        Constants.S_ADAPTER_SIZE,
                        Constants.S_EMOTION,
                        categoryName));

                if(!Constants.S_DEBUG_MODE) {
                    Toast.makeText(SuggestionPageActivity.this,
                            getString(R.string.query,
                                    Constants.S_ADAPTER_SIZE,
                                    Constants.S_EMOTION,
                                    categoryName), Toast.LENGTH_LONG).show();
                }
            }
        }else {
            GeminiAPIGetTitle(getString(R.string.query,
                    Constants.S_ADAPTER_SIZE,
                    Constants.S_EMOTION,
                    categoryName));

            if(!Constants.S_DEBUG_MODE) {
                Toast.makeText(SuggestionPageActivity.this,
                        getString(R.string.query,
                                Constants.S_ADAPTER_SIZE,
                                Constants.S_EMOTION,
                                categoryName), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void FirebaseAPIGetData(String uid, String category, OnDataFetchedCallback callback) {
        reference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("suggestions").child(categoryText);
        GetPreviouslyLiked(category, new StringBuilder(), callback);
    }
    boolean dataExists = false;

    private void GetPreviouslyLiked(String activity, StringBuilder previouslyLikedText, OnDataFetchedCallback callback) {
        Query query = reference.orderByChild("category").equalTo(activity);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    dataExists = true;

                    for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                        createSuggestionsText(previouslyLikedText, suggestionSnapshot.child("suggestion").getValue(String.class));
                    }

                    if (previouslyLikedText.length() > 2) {
                        previouslyLikedText.delete(previouslyLikedText.length() - 2, previouslyLikedText.length());
                    }

                    callback.onDataFetched(previouslyLikedText.toString());
                } else {
                    dataExists = false;

                    callback.onDataFetched("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SuggestionPageActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                callback.onDataFetched(""); // Error occurred, send empty string
            }
        });
    }

    private void createSuggestionsText(StringBuilder previouslyLikedText, String suggestion) {
        if (suggestion != null && !suggestion.isEmpty()) {
            previouslyLikedText.append(suggestion).append(", ");
        }
    }

    interface OnDataFetchedCallback {
        void onDataFetched(String previouslyLikedSuggestions);
        void onDataNotAvailable();
    }
}