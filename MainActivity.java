package com.example.liveforthefuture;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

class LanguageSelectionActivity extends AppCompatActivity {

    String[] languages = {"English", "Hindi", "Mandarin", "Cantonese"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout layout = new FrameLayout(this);
        setContentView(layout);

        showLanguageDialog();
    }

    private void showLanguageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Your Language");

        builder.setItems(languages, (dialog, which) -> {
            String selectedLanguage = languages[which];
            Toast.makeText(LanguageSelectionActivity.this, "Selected: " + selectedLanguage, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        builder.setCancelable(false);
        builder.show();
    }
}