package com.example.hoteleats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    EditText username, password;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button login = findViewById(R.id.login_btn);

        username = findViewById(R.id.registerUsername);
        password = findViewById(R.id.registerConfirmPassword);

        MyDatabaseHelper myDB = new MyDatabaseHelper(MainActivity.this);

        preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        editor = preferences.edit();

        if(preferences.contains("name") && preferences.contains("access")) {
            if (preferences.getString("access", "default").matches("admin")) {

                Intent intent = new Intent(this, AdminActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            } else if (preferences.getString("access", "default").matches("user")) {

                Intent intent = new Intent(this, UserActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }


        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (username.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty()) {
                    Toast.makeText(view.getContext(), "plz fill the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                Cursor cursor = myDB.getUserData(username.getText().toString().trim(),
                        password.getText().toString().trim());

                Log.d(TAG, "onClick: username: " + username.getText().toString() + ", password: " + password.getText().toString() + ", count: " + cursor.getCount());

                if(cursor.getCount() == 1) {
                    Log.d(TAG, "onClick: columnCount: " + cursor.getColumnCount());
                    Log.d(TAG, "onClick: columnName: " + cursor.getColumnName(0));
                    cursor.moveToNext();
                    Log.d(TAG, "onClick: id: " + cursor.getInt(0));
                    Log.d(TAG, "onClick: name: " + cursor.getString(1));
                    Log.d(TAG, "onClick: password: " + cursor.getString(2));
                    Log.d(TAG, "onClick: access: " + cursor.getString(3));

                    String access = cursor.getString(3);

                    Toast.makeText(view.getContext(), "login successful", Toast.LENGTH_SHORT).show();

                    editor.putString("name", username.getText().toString().trim());
                    editor.putString("access", access);
                    editor.commit();

                    if (access.matches("admin")) {
                        Intent intent = new Intent(view.getContext(), AdminActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        view.getContext().startActivity(intent);
                    } else {
                        Intent intent = new Intent(view.getContext(), UserActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        view.getContext().startActivity(intent);
                    }

                } else {
                    Toast.makeText(view.getContext(), "invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        };

        login.setOnClickListener(onClickListener);

        Button register = findViewById(R.id.register_btn);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                view.getContext().startActivity(intent);
            }
        });

    }
}