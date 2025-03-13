package com.taskmaster.appui;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ChildLogin extends AppCompatActivity {

    AppCompatButton confirmButton;
    TextView signUpTextView;
    EditText usernameBox;
    FirebaseFirestore db;
    FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.child_login);

        // hide status bar and nav bar
        hideSystemBars();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        confirmButton = findViewById(R.id.button3);
        usernameBox = findViewById(R.id.userBox);
        signUpTextView = findViewById(R.id.textView4);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyUser();
            }
        });

        signUpTextView.setOnClickListener(v -> startActivity(new Intent(ChildLogin.this, CodeVerification.class)));

    }

    private void verifyUser() {
        String username = usernameBox.getText().toString().trim();

        if (username.isEmpty()) {
            Toast.makeText(this, "Please enter your username", Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String code = pref.getString("code", "");
        SharedPreferences.Editor editor = getSharedPreferences("UserPrefs", MODE_PRIVATE).edit();

        auth.signInWithEmailAndPassword(username, code)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        editor.putString("role", "child");
                        editor.apply();

                        Toast.makeText(ChildLogin.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ChildLogin.this, QuestManagement.class));

                    } else {
                        Toast.makeText(ChildLogin.this, "Login failed", Toast.LENGTH_SHORT).show();
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