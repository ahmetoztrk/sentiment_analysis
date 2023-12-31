package com.example.duyguanalizi;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LoginPage extends AppCompatActivity {
    EditText nameEdtTxt, passwordEdtTxt;
    Button loginBtn, registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        Init();
        SetButtonsListener();
    }

    void Init() {
        nameEdtTxt = findViewById(R.id.editTextText);
        passwordEdtTxt = findViewById(R.id.editTextText2);

        loginBtn = findViewById(R.id.button);
        registerBtn = findViewById(R.id.button2);
    }

    void SetButtonsListener() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameEdtTxt.getText().toString().equals("asd") && passwordEdtTxt.getText().toString().equals("123")) {
                    Intent init = new Intent(LoginPage.this, HomePage.class);
                    startActivity(init);
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent init = new Intent(LoginPage.this, RegisterPage.class);
                startActivity(init);
            }
        });
    }
}
