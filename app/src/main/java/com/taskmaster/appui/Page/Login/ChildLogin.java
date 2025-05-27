package com.taskmaster.appui.Page.Login;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.taskmaster.appui.FirebaseHandler.AuthHandler;
import com.taskmaster.appui.FirebaseHandler.FirestoreHandler;
import com.taskmaster.appui.Page.Main.QuestManagement;
import com.taskmaster.appui.Page.NavUtil;
import com.taskmaster.appui.R;

public class ChildLogin extends AppCompatActivity {

    AppCompatButton confirmButton;
    TextView signUpTextView;
    EditText emailBox, passwordBox;


    FirestoreHandler firestoreHandler;
    AuthHandler authHandler;
    Intent QuestManagementIntent, CodeVerificationIntent;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.child_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Init Database Handlers
        firestoreHandler = new FirestoreHandler();
        authHandler = new AuthHandler();

        // Init Intents
        QuestManagementIntent = new Intent(ChildLogin.this, com.taskmaster.appui.Page.Main.QuestManagement.class);
        CodeVerificationIntent = new Intent(ChildLogin.this, CodeVerification.class);

        // Init components
        emailBox = findViewById(R.id.emailBox);
        passwordBox = findViewById(R.id.passwordBox);
        confirmButton = findViewById(R.id.confirmButton);
        signUpTextView = findViewById(R.id.signupTextView);

        // Hide status bar and nav bar
        NavUtil.hideSystemBars(this);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log-in Logic
                LogInManager logInManager = new LogInManager();
                logInManager.attemptUserLogin(emailBox.getText().toString(), passwordBox.getText().toString(), ChildLogin.this, QuestManagement.class);
            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CodeVerificationIntent);
            }
        });

    }



}