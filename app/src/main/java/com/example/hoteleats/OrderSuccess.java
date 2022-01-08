package com.example.hoteleats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class OrderSuccess extends AppCompatActivity {

    TextView items, price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        Intent intent = getIntent();
        String itemList = intent.getExtras().getString("itemList");
        String totalPrice = intent.getExtras().getString("totalPrice");

        items = findViewById(R.id.textView17);
        price = findViewById(R.id.textView20);

        items.setMovementMethod(new ScrollingMovementMethod());
        items.setText(itemList);
        price.setText(totalPrice);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, UserActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
}