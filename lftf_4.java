package com.example.liveforthefuture;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lftf4);  // Set the layout for the main screen

        // Initialize buttons
        Button waterLogButton = findViewById(R.id.waterLogButton);
        Button foodLogButton = findViewById(R.id.foodLogButton);
        Button fitnessButton = findViewById(R.id.fitnessButton);
        Button feelingsTrackerButton = findViewById(R.id.feelingsTrackerButton);

        // Set click listeners for each button
        waterLogButton.setOnClickListener(v -> {
            // Navigate to the Water page activity
            Intent intent = new Intent(MainActivity.this, WaterPageActivity.class);
            startActivity(intent);
        });

        foodLogButton.setOnClickListener(v -> {
            // Navigate to the Food page activity
            Intent intent = new Intent(MainActivity.this, FoodPageActivity.class);
            startActivity(intent);
        });

        fitnessButton.setOnClickListener(v -> {
            // Navigate to the Fitness page activity
            Intent intent = new Intent(MainActivity.this, FitnessPageActivity.class);
            startActivity(intent);
        });

        feelingsTrackerButton.setOnClickListener(v -> {
            // Navigate to the Feelings Tracker page activity
            Intent intent = new Intent(MainActivity.this, FeelingsTrackerPageActivity.class);
            startActivity(intent);
        });
    }
}


