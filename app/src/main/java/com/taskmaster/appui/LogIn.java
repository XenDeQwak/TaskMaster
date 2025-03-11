package com.taskmaster.appui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class LogIn extends AppCompatActivity {

    Animation pop_out_Anim, fade_in_Anim;
    ImageView container1, container2, container3, bg, logo, logo_shadow, line;
    TextView forgotPasswordTextView, signUpTextView, logInTextView;
    AppCompatButton confirmButton;
    EditText emailbox, passwordbox;
    FirebaseFirestore db;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        signUpTextView = findViewById(R.id.textView4);
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

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        confirmButton.setOnClickListener(v -> authenticateUser());
        forgotPasswordTextView.setOnClickListener(v -> Toast.makeText(LogIn.this, "Forgot Password? clicked", Toast.LENGTH_SHORT).show());
        signUpTextView.setOnClickListener(v -> startActivity(new Intent(LogIn.this, SignUp.class)));
    }

    private void authenticateUser() {
        String username = emailbox.getText().toString().trim();
        String password = passwordbox.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.getString("password").equals(password)) {
                                SharedPreferences.Editor editor = getSharedPreferences("UserPrefs", MODE_PRIVATE).edit();
                                editor.putString("username", username);
                                editor.putString("role", "parent");
                                editor.apply();

                                FirebaseUser user = auth.getCurrentUser();
                                if (user != null) {
                                    String uid = user.getUid();
                                    editor.putString("uid", uid);
                                    editor.apply();
                                }
                                Toast.makeText(LogIn.this, "Login successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LogIn.this, QuestManagement.class));
                                return;
                            }
                        }
                    }
                    checkUsername(username, password);
                });
    }

    private void checkUsername(String username, String password) {
        db.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.getString("password").equals(password)) {
                                Toast.makeText(LogIn.this, "Login successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LogIn.this, QuestManagement.class));
                                return;
                            }
                        }
                    }
                    Toast.makeText(LogIn.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
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
