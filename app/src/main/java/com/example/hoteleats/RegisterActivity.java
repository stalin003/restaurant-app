package com.example.hoteleats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    EditText username, password, confirmPassword;
    Button registerBtn, loginBtn;
    RadioGroup radioGroup;
    RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.registerUsername);
        password = findViewById(R.id.registerPassword);
        confirmPassword = findViewById(R.id.registerConfirmPassword);
        radioGroup = findViewById(R.id.radioGroup);

        registerBtn = findViewById(R.id.register_btn);
        loginBtn = findViewById(R.id.loginback);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                radioButton = findViewById(radioGroup.getCheckedRadioButtonId());

                String usernameField = username.getText().toString().trim();
                String passwordField = password.getText().toString().trim();
                String confirmPassField = confirmPassword.getText().toString().trim();
                String accessField = radioButton.getText().toString().trim();

                if (usernameField.isEmpty() || passwordField.isEmpty() || confirmPassField.isEmpty()) {

                    Toast.makeText(view.getContext(), "plz fill the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (passwordField.matches(confirmPassField)) {
                    MyDatabaseHelper myDb = new MyDatabaseHelper(RegisterActivity.this);
                    myDb.register(usernameField, passwordField, accessField);

                    Log.d(TAG, "onClick: username: " + usernameField + ", password: " + passwordField + ", access: " + accessField);

                    username.getText().clear();
                    password.getText().clear();
                    confirmPassword.getText().clear();

                    Toast.makeText(view.getContext(), "registered successfully", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(view.getContext(), "password not matches", Toast.LENGTH_SHORT).show();

                }



            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                view.getContext().startActivity(intent);
            }
        });

    }
}