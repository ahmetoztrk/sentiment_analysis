package com.example.sentimentanalysis;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class StatisticsPageActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;

    TextView musicsBtn, seriesBtn, moviesBtn, booksBtn, activitiesBtn, select, previouslyLikedTextView;
    Button statisticDeleteBtn;
    StringBuilder musicsPreviouslyLikedText, seriesPreviouslyLikedText, moviesPreviouslyLikedText, booksPreviouslyLikedText, activitiesPreviouslyLikedText;

    String page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_page);

        init();

        initFirebase();

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

        statisticDeleteBtn = findViewById(R.id.statistic_delete_btn);

        musicsPreviouslyLikedText = new StringBuilder();
        seriesPreviouslyLikedText = new StringBuilder();
        moviesPreviouslyLikedText = new StringBuilder();
        booksPreviouslyLikedText = new StringBuilder();
        activitiesPreviouslyLikedText = new StringBuilder();
    }

    private void initFirebase(){
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();

        FirebaseAPIGetData(user.getUid());
    }

    private void setButtonsListener(){
        musicsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = "Musics";

                select.animate().x(0).setDuration(100);

                previouslyLikedTextView.setText(musicsPreviouslyLikedText.toString());
            }
        });

        seriesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = "Series";

                int size = seriesBtn.getWidth();
                select.animate().x(size).setDuration(100);

                previouslyLikedTextView.setText(seriesPreviouslyLikedText.toString());
            }
        });

        moviesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = "Movies";

                int size = seriesBtn.getWidth() * 2;
                select.animate().x(size).setDuration(100);

                previouslyLikedTextView.setText(moviesPreviouslyLikedText.toString());
            }
        });

        booksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = "Books";

                int size = seriesBtn.getWidth() * 3;
                select.animate().x(size).setDuration(100);

                previouslyLikedTextView.setText(booksPreviouslyLikedText.toString());
            }
        });

        activitiesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = "Activities";

                int size = seriesBtn.getWidth() * 4;
                select.animate().x(size).setDuration(100);

                previouslyLikedTextView.setText(activitiesPreviouslyLikedText.toString());
            }
        });

        statisticDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("suggestions").child(page);

                reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        previouslyLikedTextView.setText("");

                        if(page.equals("Musics")){
                            musicsPreviouslyLikedText.delete(0,musicsPreviouslyLikedText.length());
                        }else if(page.equals("Series")){
                            seriesPreviouslyLikedText.delete(0,seriesPreviouslyLikedText.length());
                        }else if(page.equals("Movies")){
                            moviesPreviouslyLikedText.delete(0,moviesPreviouslyLikedText.length());
                        }else if(page.equals("Books")){
                            booksPreviouslyLikedText.delete(0,booksPreviouslyLikedText.length());
                        }else if(page.equals("Activities")){
                            activitiesPreviouslyLikedText.delete(0,activitiesPreviouslyLikedText.length());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });
    }

    private void FirebaseAPIGetData(String uid){
        reference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("suggestions").child("Musics");
        GetPreviouslyLiked("Musics", musicsPreviouslyLikedText);

        reference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("suggestions").child("Series");
        GetPreviouslyLiked("Series", seriesPreviouslyLikedText);

        reference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("suggestions").child("Movies");
        GetPreviouslyLiked("Movies", moviesPreviouslyLikedText);

        reference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("suggestions").child("Books");
        GetPreviouslyLiked("Books", booksPreviouslyLikedText);

        reference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("suggestions").child("Activities");
        GetPreviouslyLiked("Activities", activitiesPreviouslyLikedText);
    }

    private void GetPreviouslyLiked(String activity, StringBuilder previouslyLikedText){
        Query query = reference.orderByChild("category").equalTo(activity);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                    createSuggestionsText(previouslyLikedText,
                            suggestionSnapshot.child("suggestion").getValue(String.class),
                            suggestionSnapshot.child("category").getValue(String.class),
                            suggestionSnapshot.child("emotion").getValue(String.class),
                            suggestionSnapshot.child("date").getValue(String.class),
                            suggestionSnapshot.child("time").getValue(String.class)
                    );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StatisticsPageActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createSuggestionsText(StringBuilder previouslyLikedText, String suggestion, String category, String emotion, String date, String time){
        previouslyLikedText
                .append(getString(R.string.suggestion_x)).append(suggestion).append("\n")
                .append(getString(R.string.category_x)).append(category).append("\n")
                .append(getString(R.string.emotion_x)).append(emotion).append("\n")
                .append(getString(R.string.date_x)).append(date).append("\n")
                .append(getString(R.string.time_x)).append(time).append("\n")
                .append("\n\n");
    }
}
