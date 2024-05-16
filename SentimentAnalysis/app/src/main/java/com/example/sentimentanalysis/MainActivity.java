package com.example.sentimentanalysis;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;

    EditText eMailAddressEditText, passwordEditText;
    TextView eMailAddressInfoTextView, passwordInfoTextView;
    Button loginBtn, registerBtn;
    ImageButton passwordOpenCloseBtn;
    boolean passwordOpenClose;
    int passwordEditTextInputType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        initFirebase();

        setButtonsListener();
    }

    private void init(){
        eMailAddressEditText = findViewById(R.id.e_mail_address_edit_text);
        eMailAddressInfoTextView = findViewById(R.id.e_mail_address_info_text_view);
        passwordEditText = findViewById(R.id.password_edit_text);
        passwordInfoTextView = findViewById(R.id.password_info_text_view);

        loginBtn = findViewById(R.id.login_btn);
        registerBtn = findViewById(R.id.register_btn);
        passwordOpenCloseBtn = findViewById(R.id.password_open_close_btn);

        passwordOpenClose = false;
        passwordEditTextInputType = passwordEditText.getInputType();
    }

    private void initFirebase(){
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if(user != null){
            eMailAddressEditText.setText(user.getEmail());
        }
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
                    auth.signInWithEmailAndPassword(eMailAddressEditText.getText().toString(), passwordEditText.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                            Intent init = new Intent(MainActivity.this, HomePageActivity.class);
                            startActivity(init);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                            eMailAddressInfoTextView.setText("Wrong entry!");
                            passwordInfoTextView.setText("Wrong entry!");
                        }
                    });
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

        passwordOpenCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(passwordOpenClose){
                    passwordOpenCloseBtn.setBackgroundResource(R.drawable.icon_password_close);
                    passwordEditText.setInputType(passwordEditTextInputType);
                    passwordOpenClose = false;
                }else{
                    passwordOpenCloseBtn.setBackgroundResource(R.drawable.icon_password_open);
                    passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordOpenClose = true;
                }
            }
        });
    }
}