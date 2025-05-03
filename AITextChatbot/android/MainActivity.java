package com.example.aichatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private Button startChatButton;
    private TextView appTitle;
    private TextView appDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        startChatButton = findViewById(R.id.startChatButton);
        appTitle = findViewById(R.id.appTitle);
        appDescription = findViewById(R.id.appDescription);

        // Generate a unique session ID for this chat instance
        final String sessionId = UUID.randomUUID().toString();

        // Set up click listener for the start chat button
        startChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to launch the ChatActivity
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                // Pass the session ID to the chat activity
                intent.putExtra("SESSION_ID", sessionId);
                startActivity(intent);
            }
        });
    }
}
