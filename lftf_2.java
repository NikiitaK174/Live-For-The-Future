package com.example.liveforthefuture;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class lftf_2 extends AppCompatActivity {

    EditText editTextDOB, editTextDueDate; // Date of Birth and Due Date fields
    ImageButton btnEmergency; // Emergency button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lftf2); // Your layout file

        // Handle screen insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize EditTexts
        editTextDOB = findViewById(R.id.editTextDOB);
        editTextDueDate = findViewById(R.id.editTextDueDate);

        // Show calendar when clicked
        editTextDOB.setOnClickListener(v -> showDatePickerDialog(editTextDOB));
        editTextDueDate.setOnClickListener(v -> showDatePickerDialog(editTextDueDate));

        // Emergency button
        btnEmergency = findViewById(R.id.btnEmergency);
        btnEmergency.setOnClickListener(v -> makeEmergencyDial());
    }

    // Shared DatePicker method
    private void showDatePickerDialog(EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    editText.setText(selectedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    // Emergency dial method (no permission required)
    private void makeEmergencyDial() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:911"));
        startActivity(intent);
    }
}