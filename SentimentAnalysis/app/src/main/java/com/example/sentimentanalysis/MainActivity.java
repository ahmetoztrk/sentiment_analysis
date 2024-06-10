package com.example.sentimentanalysis;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import com.example.sentimentanalysis.ImageProcessing.HomePageActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;

    Spinner languageSpinner;
    EditText eMailAddressEditText, passwordEditText;
    TextView eMailAddressInfoTextView, passwordInfoTextView;
    Switch rememberMeSwitchCompat, debugModeSwitchCompat;
    Button loginBtn, registerBtn;
    ImageButton passwordOpenCloseBtn;
    boolean passwordOpenClose;
    int passwordEditTextInputType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPermission();

        initConfigs();

        setContentView(R.layout.activity_main);

        init();

        initFirebase();

        setButtonsListener();
    }

    private void initConfigs(){
        if(FileOperations.getOptionValue(getApplicationContext(),"config.txt","language").equals("")){
            FileOperations.addFile(getApplicationContext(),"config.txt","language=en");

            setLanguage("en");
        }else{
            if(FileOperations.getOptionValue(getApplicationContext(),"config.txt","language").equals("en")){
                setLanguage("en");
            }else{
                setLanguage("tr");
            }
        }

        if(FileOperations.getOptionValue(getApplicationContext(),"config.txt","remember_me_option").equals("")){
            FileOperations.addFile(getApplicationContext(),"config.txt","remember_me_option=false");
        }

        if(FileOperations.getOptionValue(getApplicationContext(),"config.txt","password").equals("")){
            FileOperations.addFile(getApplicationContext(),"config.txt","password=");
        }
    }

    private void setLanguage(String langCode){
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        FileOperations.setOptionValue(getApplicationContext(),"config.txt","language", langCode);
    }

    private void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                startActivityForResult(intent, Constants.S_PERMISSION_REQUEST_CODE_MANAGE_EXTERNAL_STORAGE);
            }
        } else {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.S_PERMISSION_REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
            }

            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.S_PERMISSION_REQUEST_CODE_READ_EXTERNAL_STORAGE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constants.S_PERMISSION_REQUEST_CODE_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, R.string.Write_External_Storage_permission_granted, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.Write_External_Storage_permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void init(){
        languageSpinner = findViewById(R.id.language_spinner);
        eMailAddressEditText = findViewById(R.id.e_mail_address_edit_text);
        eMailAddressInfoTextView = findViewById(R.id.e_mail_address_info_text_view);
        passwordEditText = findViewById(R.id.password_edit_text);
        passwordInfoTextView = findViewById(R.id.password_info_text_view);

        rememberMeSwitchCompat = findViewById(R.id.remember_me_switch_compat);
        debugModeSwitchCompat = findViewById(R.id.debug_mode_switch_compat);
        loginBtn = findViewById(R.id.login_btn);
        registerBtn = findViewById(R.id.register_btn);
        passwordOpenCloseBtn = findViewById(R.id.password_open_close_btn);

        passwordOpenClose = false;
        passwordEditTextInputType = passwordEditText.getInputType();

        String[] languages = {getString(R.string.Choose_app_language), "En", "Tr"};
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, languages);

        languageSpinner.setAdapter(adapter);
    }

    private void initFirebase(){
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if(FileOperations.getOptionValue(getApplicationContext(),"config.txt","remember_me_option").equals("true")){
            rememberMeSwitchCompat.setChecked(true);

            Constants.S_REMEMBER_ME_OPTION = true;

            if(user != null){
                eMailAddressEditText.setText(user.getEmail());
                passwordEditText.setText(FileOperations.getOptionValue(getApplicationContext(),"config.txt","password"));
            }
        }else{
            rememberMeSwitchCompat.setChecked(false);

            Constants.S_REMEMBER_ME_OPTION = false;
        }
    }

    private void setButtonsListener(){
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLang = parent.getItemAtPosition(position).toString();

                if(selectedLang.equals("En")){
                    setLanguage("en");

                    finish();
                    Intent init = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(init);
                }else if (selectedLang.equals("Tr")) {
                    setLanguage("tr");

                    finish();
                    Intent init = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(init);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        rememberMeSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Constants.S_REMEMBER_ME_OPTION = isChecked;
            }
        });
        debugModeSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Constants.S_DEBUG_MODE = isChecked;
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eMailAddressEditText.getText().toString().isEmpty()){
                    eMailAddressInfoTextView.setText(R.string.Empty);
                }else{
                    Pattern pattern;
                    Matcher matcher;
                    String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                    pattern = Pattern.compile(EMAIL_PATTERN);
                    CharSequence cs = eMailAddressEditText.getText().toString();
                    matcher = pattern.matcher(cs);

                    if(!matcher.matches()){
                        eMailAddressInfoTextView.setText(R.string.Invalid_email);
                    }else{
                        eMailAddressInfoTextView.setText("");
                    }
                }

                if(passwordEditText.getText().toString().isEmpty()){
                    passwordInfoTextView.setText(R.string.Empty);
                }else{
                    passwordInfoTextView.setText("");
                }

                if(!eMailAddressEditText.getText().toString().isEmpty() && !passwordEditText.getText().toString().isEmpty()){
                    auth.signInWithEmailAndPassword(eMailAddressEditText.getText().toString(), passwordEditText.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(MainActivity.this, R.string.Login_Successful, Toast.LENGTH_SHORT).show();

                            if(Constants.S_REMEMBER_ME_OPTION){
                                FileOperations.setOptionValue(getApplicationContext(),"config.txt", "remember_me_option","true");
                                FileOperations.setOptionValue(getApplicationContext(),"config.txt","password",passwordEditText.getText().toString());
                            }else{
                                FileOperations.setOptionValue(getApplicationContext(),"config.txt","remember_me_option","false");
                                FileOperations.setOptionValue(getApplicationContext(),"config.txt","password","");
                            }

                            Intent init = new Intent(MainActivity.this, HomePageActivity.class);
                            startActivity(init);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                            eMailAddressInfoTextView.setText(R.string.Wrong_entry);
                            passwordInfoTextView.setText(R.string.Wrong_entry);
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