package com.example.sentimentanalysis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sentimentanalysis.ImageProcessing.HomePageActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    EditText eMailAddressEditText, passwordEditText;
    TextView eMailAddressInfoTextView, passwordInfoTextView;

    Button loginBtn, registerBtn, continueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        init();

        setButtonsListener();
    }

    private void init(){
        eMailAddressEditText = findViewById(R.id.e_mail_address_edit_text);
        eMailAddressInfoTextView = findViewById(R.id.e_mail_address_info_text_view);
        passwordEditText = findViewById(R.id.password_edit_text);
        passwordInfoTextView = findViewById(R.id.password_info_text_view);

        loginBtn = findViewById(R.id.login_btn);
        registerBtn = findViewById(R.id.register_btn);
        continueBtn = findViewById(R.id.continue_btn);
    }

    private void setButtonsListener(){
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                if(passwordEditText.getText().toString().isEmpty()){
                    passwordInfoTextView.setText("Empty!");
                }else{
                    passwordInfoTextView.setText("");
                }

                if(!eMailAddressEditText.getText().toString().isEmpty() && !passwordEditText.getText().toString().isEmpty()){
                    if(false){
                        Intent init = new Intent(MainActivity.this, HomePageActivity.class);
                        startActivity(init);
                    }else{
                        eMailAddressInfoTextView.setText("Wrong entry!");
                        passwordInfoTextView.setText("Wrong entry!");
                    }
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterPageActivity.class);
                startActivity(intent);
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
                startActivity(intent);
            }
        });
    }
}