package com.example.sentimentanalysis;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfilePageActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    HashMap<String, Object> data;

    ImageView profileImageView;
    EditText usernameEditText, eMailAddressEditText, passwordEditText, rePasswordEditText;
    TextView ageTextView, usernameInfoTextView, eMailAddressInfoTextView, ageInfoTextView, passwordInfoTextView, rePasswordInfoTextView;
    Button deleteBtn, updateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        init();

        initFirebase();

        setButtonsListener();
    }

    private void init(){
        profileImageView = findViewById(R.id.profile_image_view);

        usernameEditText = findViewById(R.id.username_edit_text);
        usernameInfoTextView = findViewById(R.id.username_info_text_view);
        eMailAddressEditText = findViewById(R.id.e_mail_address_edit_text);
        eMailAddressInfoTextView = findViewById(R.id.e_mail_address_info_text_view);
        ageTextView = findViewById(R.id.age_text_view);
        ageInfoTextView = findViewById(R.id.age_info_text_view);
        passwordEditText = findViewById(R.id.password_edit_text);
        passwordInfoTextView = findViewById(R.id.password_info_text_view);
        rePasswordEditText = findViewById(R.id.re_password_edit_text);
        rePasswordInfoTextView = findViewById(R.id.re_password_info_text_view);

        deleteBtn = findViewById(R.id.delete_btn);
        updateBtn = findViewById(R.id.update_btn);
    }

    private void initFirebase(){
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();

        FirebaseAPIGetData(user.getUid());
    }

    private void setButtonsListener(){
        ageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(ProfilePageActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        ageTextView.setText(date);
                    }
                }, year, month, day);

                dialog.show();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ProfilePageActivity.this);
                alert.setTitle("Delete Account")
                        .setMessage("Do you want to delete your account?")
                        .setIcon(R.drawable.icon_books)
                        .setCancelable(false)
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();

                                DeleteData(user.getUid());

                                Intent intent = new Intent(ProfilePageActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ProfilePageActivity.this);
                alert.setTitle("Update Account")
                        .setMessage("Do you want to update your account?")
                        .setIcon(R.drawable.icon_movies)
                        .setCancelable(false)
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();

                                if(usernameEditText.getText().toString().isEmpty()){
                                    usernameInfoTextView.setText("Empty!");
                                }else{
                                    usernameInfoTextView.setText("");
                                }

                                if(eMailAddressEditText.getText().toString().isEmpty()){
                                    eMailAddressInfoTextView.setText("Empty!");
                                }else{
                                    Pattern pattern;
                                    Matcher matcher;
                                    String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                                    pattern = Pattern.compile(EMAIL_PATTERN);
                                    CharSequence cs = eMailAddressEditText.getText().toString();
                                    matcher = pattern.matcher(cs);

                                    if(!matcher.matches()){
                                        eMailAddressInfoTextView.setText("Invalid email");
                                    }else{
                                        eMailAddressInfoTextView.setText("");
                                    }
                                }

                                if(ageTextView.getText().toString().isEmpty()){
                                    ageInfoTextView.setText("Empty!");
                                }else{
                                    ageInfoTextView.setText("");
                                }

                                if(passwordEditText.getText().toString().isEmpty()){
                                    passwordInfoTextView.setText("Empty!");
                                }else{
                                    passwordInfoTextView.setText("");
                                }

                                if(rePasswordEditText.getText().toString().isEmpty()){
                                    rePasswordInfoTextView.setText("Empty!");
                                }else{
                                    if(!passwordEditText.getText().toString().equals(rePasswordEditText.getText().toString())){
                                        rePasswordInfoTextView.setText("Not the same!");
                                    }else{
                                        if(passwordEditText.getText().toString().length() < 7){
                                            passwordInfoTextView.setText("At least 7 characters!");
                                            rePasswordInfoTextView.setText("");
                                        }else {
                                            rePasswordInfoTextView.setText("");
                                        }
                                    }
                                }

                                if(usernameInfoTextView.getText().toString().isEmpty() && eMailAddressInfoTextView.getText().toString().isEmpty() && ageInfoTextView.getText().toString().isEmpty() && passwordInfoTextView.getText().toString().isEmpty() && rePasswordInfoTextView.getText().toString().isEmpty()) {
                                    data = new HashMap<>();
                                    data.put("username", usernameEditText.getText().toString());
                                    data.put("eMailAddress", eMailAddressEditText.getText().toString());
                                    data.put("age", ageTextView.getText().toString());
                                    data.put("password", passwordEditText.getText().toString());

                                    FirebaseAPIUpdateData(user.getUid(), data);
                                }
                            }
                        })
                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();

                                FirebaseAPIGetData(user.getUid());
                            }
                        }).show();
            }
        });
    }

    private void FirebaseAPIGetData(String uid){
        reference = FirebaseDatabase.getInstance().getReference("users").child(uid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snp : snapshot.getChildren()){
                    if(Objects.equals(snp.getKey(), "username")){
                        usernameEditText.setText(snp.getValue().toString());
                    }else if(Objects.equals(snp.getKey(), "eMailAddress")){
                        eMailAddressEditText.setText(snp.getValue().toString());
                    }else if(Objects.equals(snp.getKey(), "age")){
                        ageTextView.setText(snp.getValue().toString());
                    }else if(Objects.equals(snp.getKey(), "password")){
                        passwordEditText.setText(snp.getValue().toString());
                        rePasswordEditText.setText(snp.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfilePageActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void FirebaseAPIUpdateData(final String uid, HashMap<String, Object> hashMap){
        reference = FirebaseDatabase.getInstance().getReference("users").child(uid);

        reference.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                FirebaseAPIGetData(uid);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void DeleteData(String uid){
        reference = FirebaseDatabase.getInstance().getReference("users").child(uid);

        reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        user.delete();
        auth.getCurrentUser().delete();
    }
}
