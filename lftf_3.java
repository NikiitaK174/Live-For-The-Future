package com.example.liveforthefuture;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lftf2); // Make sure your XML file is named activity_login.xml

        editTextEmail = findViewById(R.id.editTextEmail);
        findViewById(R.id.editTextPassword);
        Button buttonSignUp = findViewById(R.id.buttonSignUp);
        ImageButton btnEmergency = findViewById(R.id.btnEmergency);

        // Emergency Button Click -> Dial 911
        btnEmergency.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:911"));
            startActivity(callIntent);
        });

        buttonSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, lftf_3.class);
            startActivity(intent);
        });
    }

    public EditText getEditTextEmail() {
        return editTextEmail;
    }

    public void setEditTextEmail(EditText editTextEmail) {
        this.editTextEmail = editTextEmail;
    }
}