package com.taskmaster.appui.Page;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.taskmaster.appui.FirebaseHandler.AuthHandler;
import com.taskmaster.appui.FirebaseHandler.FirestoreHandler;
import com.taskmaster.appui.R;

public class LogIn extends AppCompatActivity {

    Animation pop_out_Anim, fade_in_Anim;
    ImageView container1, container2, container3, bg, logo, logo_shadow, line;
    TextView forgotPasswordTextView, signUpTextView, logInTextView;
    AppCompatButton confirmButton;
    EditText emailbox, passwordbox;

    FirestoreHandler firestoreHandler;
    AuthHandler authHandler;

    Intent SignUp, QuestManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SignUp = new Intent(LogIn.this, SignUp.class);
        QuestManagement = new Intent(LogIn.this, QuestManagement.class);

        firestoreHandler = new FirestoreHandler();
        authHandler = new AuthHandler();

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.log_in);

        hideSystemBars();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pop_out_Anim = AnimationUtils.loadAnimation(this, R.anim.pop_out_animation);
        fade_in_Anim = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation);

        emailbox = findViewById(R.id.editTextTextEmailAddress);
        passwordbox = findViewById(R.id.editTextTextEmailAddress2);
        forgotPasswordTextView = findViewById(R.id.textView3);
        confirmButton = findViewById(R.id.button);
        signUpTextView = findViewById(R.id.signupTextView);
        container1 = findViewById(R.id.imageView7);
        container2 = findViewById(R.id.imageView9);
        container3 = findViewById(R.id.imageView10);
        logo = findViewById(R.id.imageView13);
        logo_shadow = findViewById(R.id.imageView14);
        bg = findViewById(R.id.imageView6);
        line = findViewById(R.id.imageView15);
        logInTextView = findViewById(R.id.textView);

        bg.setAnimation(fade_in_Anim);
        logo.setAnimation(pop_out_Anim);
        logo_shadow.setAnimation(pop_out_Anim);
        emailbox.setAnimation(pop_out_Anim);
        passwordbox.setAnimation(pop_out_Anim);
        forgotPasswordTextView.setAnimation(pop_out_Anim);
        confirmButton.setAnimation(pop_out_Anim);
        signUpTextView.setAnimation(pop_out_Anim);
        container1.setAnimation(pop_out_Anim);
        container2.setAnimation(pop_out_Anim);
        container3.setAnimation(pop_out_Anim);
        line.setAnimation(pop_out_Anim);
        logInTextView.setAnimation(pop_out_Anim);

        confirmButton.setOnClickListener(v -> authenticateUser());
        signUpTextView.setOnClickListener(v -> {
                    startActivity(SignUp);
                });
    }

    private void authenticateUser() {
        String username = emailbox.getText().toString().trim();
        String password = passwordbox.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        authHandler.signInUser(username, password)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LogIn.this, "Login successful", Toast.LENGTH_SHORT).show();
                            startActivity(QuestManagement);
                        } else {

                        }
                    }
                });

    }

    private void hideSystemBars() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
