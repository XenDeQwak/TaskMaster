package com.taskmaster.appui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextWatcher;
import android.text.Editable;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class CodeVerification extends AppCompatActivity {

    Button confirmButton;
    EditText codebox;
    FirebaseFirestore taskmasterDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.code_verification);

        // hide status bar and nav bar
        hideSystemBars();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // initialize Firestore
        taskmasterDatabase = FirebaseFirestore.getInstance();

        // hooks
        confirmButton = findViewById(R.id.button3);
        codebox = findViewById(R.id.editTextTextEmailAddress6);

        // replace init text
        setupEditText(codebox, "Code");

        // test for button
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCode();
            }
        });
    }

    private void verifyCode() {
        String code = codebox.getText().toString().trim();

        if (code.isEmpty()) {
            Toast.makeText(this, "Please enter the code", Toast.LENGTH_SHORT).show();
            return;
        }

        taskmasterDatabase.collection("users")
                .whereEqualTo("code", code)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.getString("code").equals(code)) {
                                Toast.makeText(CodeVerification.this, "Code is valid!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(CodeVerification.this, QuestManagement.class));
                            }
                        }
                    }
                    Toast.makeText(CodeVerification.this, "Invalid code", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(CodeVerification.this, "Error verifying code: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void setupEditText(EditText editText, final String initialText) {
        editText.setText(initialText);

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && editText.getText().toString().equals(initialText)) {
                    editText.setText("");
                }
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed for this implementation
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed for this implementation
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editText.getText().toString().equals(initialText)) {
                    editText.setText("");
                }
            }
        });
    }

    private void hideSystemBars() {
        // hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Hide the nav bar
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}