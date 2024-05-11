package com.example.sentimentanalysis;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterPageActivity extends AppCompatActivity {

    ImageView profileImageView;
    EditText usernameEditText, eMailAddressEditText, passwordEditText, rePasswordEditText;
    TextView ageTextView, usernameInfoTextView, eMailAddressInfoTextView, ageInfoTextView, passwordInfoTextView, rePasswordInfoTextView;
    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        init();

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

        registerBtn = findViewById(R.id.register_btn);
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
                        rePasswordInfoTextView.setText("");
                    }
                }
            }
        });
    }
}
