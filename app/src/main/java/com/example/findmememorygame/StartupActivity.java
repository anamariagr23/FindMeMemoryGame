package com.example.findmememorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import android.os.Bundle;

public class StartupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        Button demoButton = findViewById(R.id.demo_button);
        demoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start MainActivity
                Intent intent = new Intent(StartupActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Optional: Finish the StartupActivity to prevent going back to it
            }

        });

        Button learnButton = findViewById(R.id.learn_button);
        learnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartupActivity.this, LearnActivity.class);
                startActivity(intent);
                finish();
            }

        });

    }
}
