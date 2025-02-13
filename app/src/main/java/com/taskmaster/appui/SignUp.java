package com.taskmaster.appui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.UUID;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    Animation pop_out_Anim, fade_in_Anim;
    ImageView container1, container2, container3, bg, logo, logo_shadow;
    TextView logInTextView;
    Button confirmButton;
    EditText emailbox, passwordbox, usernamebox, firstnamebox, lastnamebox;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.sign_up);

        hideSystemBars();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pop_out_Anim = AnimationUtils.loadAnimation(this, R.anim.pop_out_animation);
        fade_in_Anim = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation);

        emailbox = findViewById(R.id.editTextTextEmailAddress2);
        passwordbox = findViewById(R.id.editTextTextEmailAddress3);
        usernamebox = findViewById(R.id.editTextTextEmailAddress);
        firstnamebox = findViewById(R.id.editTextTextEmailAddress4);
        lastnamebox = findViewById(R.id.editTextTextEmailAddress5);
        confirmButton = findViewById(R.id.button);
        container1 = findViewById(R.id.imageView7);
        container2 = findViewById(R.id.imageView9);
        container3 = findViewById(R.id.imageView10);
        logo = findViewById(R.id.imageView13);
        logo_shadow = findViewById(R.id.imageView14);
        bg = findViewById(R.id.imageView6);
        logInTextView = findViewById(R.id.textView);

        bg.setAnimation(fade_in_Anim);
        logo.setAnimation(pop_out_Anim);
        logo_shadow.setAnimation(pop_out_Anim);
        emailbox.setAnimation(pop_out_Anim);
        passwordbox.setAnimation(pop_out_Anim);
        usernamebox.setAnimation(pop_out_Anim);
        firstnamebox.setAnimation(pop_out_Anim);
        lastnamebox.setAnimation(pop_out_Anim);
        confirmButton.setAnimation(pop_out_Anim);
        container1.setAnimation(pop_out_Anim);
        container2.setAnimation(pop_out_Anim);
        container3.setAnimation(pop_out_Anim);
        logInTextView.setAnimation(pop_out_Anim);

        db = FirebaseFirestore.getInstance();

        confirmButton.setOnClickListener(v -> checkIfUserExists());
    }

    private void checkIfUserExists() {
        String email = emailbox.getText().toString().trim();
        String username = usernamebox.getText().toString().trim();

        if (email.isEmpty() || username.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("users").whereEqualTo("email", email).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        Toast.makeText(SignUp.this, "Email already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        checkUsernameExists(username);
                    }
                });
    }

    private void checkUsernameExists(String username) {
        db.collection("users").whereEqualTo("username", username).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        Toast.makeText(SignUp.this, "Username already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        registerUser();
                    }
                });
    }

    private void registerUser() {
        String email = emailbox.getText().toString().trim();
        String password = passwordbox.getText().toString().trim();
        String username = usernamebox.getText().toString().trim();
        String firstname = firstnamebox.getText().toString().trim();
        String lastname = lastnamebox.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || username.isEmpty() || firstname.isEmpty() || lastname.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // generates room code
        String generatedCode = UUID.randomUUID().toString().substring(0, 7);

        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("password", password);
        user.put("username", username);
        user.put("firstname", firstname);
        user.put("lastname", lastname);
        user.put("code", generatedCode);

        db.collection("users")
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(SignUp.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUp.this, LogIn.class));
                })
                .addOnFailureListener(e -> Toast.makeText(SignUp.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
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
