package com.example.sentimentanalysis;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sentimentanalysis.ImageProcessing.HomePageActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterPageActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    HashMap<String, Object> data;

    ImageView profileImageView;
    EditText usernameEditText, eMailAddressEditText, passwordEditText, rePasswordEditText;
    TextView ageTextView, usernameInfoTextView, eMailAddressInfoTextView, ageInfoTextView, passwordInfoTextView, rePasswordInfoTextView, genderInfoTextView;
    RadioButton maleRadioBtn, femaleRadioBtn;
    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

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
        maleRadioBtn = findViewById(R.id.male_radio_btn);
        femaleRadioBtn = findViewById(R.id.female_radio_btn);
        genderInfoTextView  = findViewById(R.id.gender_info_text_view);

        registerBtn = findViewById(R.id.register_btn);
    }

    private void initFirebase(){
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
    }

    private void setButtonsListener(){
        ageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(RegisterPageActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        maleRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(femaleRadioBtn.isChecked()){
                    femaleRadioBtn.setChecked(false);
                }
            }
        });

        femaleRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(maleRadioBtn.isChecked()){
                    maleRadioBtn.setChecked(false);
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                            passwordInfoTextView.setText("At least 7!");
                            rePasswordInfoTextView.setText("");
                        }else {
                            rePasswordInfoTextView.setText("");
                        }
                    }
                }

                if(!maleRadioBtn.isChecked() && !femaleRadioBtn.isChecked()){
                    genderInfoTextView.setText("Empty!");
                }else{
                    genderInfoTextView.setText("");
                }

                if(usernameInfoTextView.getText().toString().isEmpty() && eMailAddressInfoTextView.getText().toString().isEmpty() && ageInfoTextView.getText().toString().isEmpty() && passwordInfoTextView.getText().toString().isEmpty() && rePasswordInfoTextView.getText().toString().isEmpty() && genderInfoTextView.getText().toString().isEmpty()){
                    auth.createUserWithEmailAndPassword(eMailAddressEditText.getText().toString(), passwordEditText.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(RegisterPageActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();

                            user = auth.getCurrentUser();

                            data = new HashMap<>();
                            data.put("username", usernameEditText.getText().toString());
                            data.put("eMailAddress", eMailAddressEditText.getText().toString());
                            data.put("age", ageTextView.getText().toString());
                            data.put("password", passwordEditText.getText().toString());

                            if(maleRadioBtn.isChecked()){
                                data.put("gender", "male");
                            }else if(femaleRadioBtn.isChecked()){
                                data.put("gender", "female");
                            }else{
                                data.put("gender", "other");
                            }

                            data.put("uid", user.getUid());

                            reference.child("users").child(user.getUid()).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterPageActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                            Intent intent = new Intent(RegisterPageActivity.this, HomePageActivity.class);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterPageActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
