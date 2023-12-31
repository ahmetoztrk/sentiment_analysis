package com.example.duyguanalizi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterPage extends AppCompatActivity {

    //Cinsiyette eklensin
    Button backBtn, continueBtn;
    EditText nameEdtTxt, surnameEdtTxt, password1EdtTxt, password2EdtTxt, mailEdtTxt;
    SeekBar ageSeekB;
    TextView ageTxt, nameEmpTxt, surnameEmpTxt, password1EmpTxt, password2EmpTxt, mailEmpTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        Init();
        SetButtonsListener();
        SetSeekBarsListener();
    }

    void Init() {
        backBtn = findViewById(R.id.button7);
        continueBtn = findViewById(R.id.button5);

        nameEdtTxt = findViewById(R.id.editTextText3);
        surnameEdtTxt = findViewById(R.id.editTextText4);
        password1EdtTxt = findViewById(R.id.editTextTextPassword);
        password2EdtTxt = findViewById(R.id.editTextTextPassword2);
        mailEdtTxt = findViewById(R.id.editTextText5);

        ageSeekB = findViewById(R.id.seekBar3);

        ageTxt = findViewById(R.id.textView2);
        nameEmpTxt = findViewById(R.id.textView3);
        surnameEmpTxt = findViewById(R.id.textView4);
        password1EmpTxt = findViewById(R.id.textView5);
        password2EmpTxt = findViewById(R.id.textView6);
        mailEmpTxt = findViewById(R.id.textView7);
    }

    void SetButtonsListener() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent init = new Intent(RegisterPage.this, LoginPage.class);
                startActivity(init);
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!nameEdtTxt.getText().toString().equals("") && !surnameEdtTxt.getText().toString().equals("") &&
                        !password1EdtTxt.getText().toString().equals("") && !mailEdtTxt.getText().toString().equals("")) {
                    Intent init = new Intent(RegisterPage.this, HomePage.class);
                    startActivity(init);
                } else {
                    if (nameEdtTxt.getText().toString().equals("")) {
                        nameEmpTxt.setText("İsminizi girmek zorundasiniz!");
                    } else {
                        nameEmpTxt.setText("");
                    }

                    if (surnameEdtTxt.getText().toString().equals("")) {
                        surnameEmpTxt.setText("Soyadınızı girmek zorundasiniz!");
                    } else {
                        surnameEmpTxt.setText("");
                    }

                    if (password1EdtTxt.getText().toString().equals("")) {
                        password1EmpTxt.setText("Şifrenizi girmek zorundasiniz!");
                    } else {
                        password1EmpTxt.setText("");
                    }

                    if (password2EdtTxt.getText().toString().equals("")) {
                        password2EmpTxt.setText("Şifrenizi tekrar girmek zorundasiniz!");
                    } else {
                        password2EmpTxt.setText("");
                    }

                    if (mailEdtTxt.getText().toString().equals("")) {
                        mailEmpTxt.setText("Mail adresinizi girmek zorundasiniz!");
                    } else {
                        mailEmpTxt.setText("");
                    }

                    if(!password1EdtTxt.getText().toString().equals(password2EdtTxt.getText().toString())){
                        password1EmpTxt.setText("Şifreler ayni degil");
                    }
                }
            }
        });
    }

    void SetSeekBarsListener() {
        ageSeekB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                ageSeekB.setProgress(progress);
                ageTxt.setText("Yas: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
}
