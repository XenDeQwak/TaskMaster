package com.taskmaster.appui.Page;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.taskmaster.appui.R;

import java.util.HashMap;
import java.util.Map;

public class CodeVerification extends AppCompatActivity {

    AppCompatButton confirmButton;
    EditText codebox;
    EditText usernameBox;
    FirebaseFirestore db;
    FirebaseAuth auth;
    CollectionReference childRef;
    int id = 1;

    @SuppressLint("MissingInflatedId")
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
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // hooks
        confirmButton = findViewById(R.id.confirmButton);
        codebox = findViewById(R.id.editTextTextEmailAddress6);
        usernameBox = findViewById(R.id.userBox);

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
        String username = usernameBox.getText().toString().trim();

        if (code.isEmpty() || username.isEmpty()) {
            Toast.makeText(this, "Please enter the code and username", Toast.LENGTH_SHORT).show();
            return;
        }


        auth.createUserWithEmailAndPassword(username, code)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Map<String, Object> childData = new HashMap<>();
                        SharedPreferences.Editor editor = getSharedPreferences("UserPrefs", MODE_PRIVATE).edit();
                        editor.putString("code", code).apply();

                        childRef = db.collection("users");
                        childData.put("role", "child");
                        childData.put("username", username);
                        childData.put("childStr", 0);
                        childData.put("childInt", 0);
                        childData.put("questCount", 0);
                        childData.put("childAvatar", 0);
                        childData.put("floor", 1);
                        childData.put("parentCode", code);
                        childRef.document(auth.getCurrentUser().getUid()).set(childData);
                        startActivity(new Intent(CodeVerification.this, ChildLogin.class));

                        Toast.makeText(CodeVerification.this, "Login successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CodeVerification.this, "Login failed", Toast.LENGTH_SHORT).show();
                    }
        });
        db.collection("users").whereEqualTo("username", username).get()
                .addOnSuccessListener(userQuery -> {
                    if (!userQuery.isEmpty()) {
                        Toast.makeText(CodeVerification.this, "Username already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        db.collection("users").whereEqualTo("code", code).limit(1).get()
                                .addOnSuccessListener(querySnapshot -> {
                                    if (!querySnapshot.isEmpty()) {
                                    } else {
                                        Toast.makeText(CodeVerification.this, "Invalid Code", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(e -> Toast.makeText(CodeVerification.this, "Error verifying code: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(CodeVerification.this, "Error checking username: " + e.getMessage(), Toast.LENGTH_SHORT).show());
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