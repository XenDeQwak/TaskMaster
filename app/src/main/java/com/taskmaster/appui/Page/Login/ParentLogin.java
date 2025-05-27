package com.taskmaster.appui.Page.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

public class ParentLogin extends AppCompatActivity {

    Animation pop_out_Anim, fade_in_Anim;
    //ImageView container1, container2, container3, bg, logo, logo_shadow, line;
    TextView forgotPasswordTextView, signUpTextView, logInTextView;
    AppCompatButton confirmButton;
    EditText emailbox, passwordbox;

    FirestoreHandler firestoreHandler;
    AuthHandler authHandler;

    Intent SignUpIntent, QuestManagementIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SignUpIntent = new Intent(ParentLogin.this, com.taskmaster.appui.Page.SignUp.class);
        QuestManagementIntent = new Intent(ParentLogin.this, com.taskmaster.appui.Page.Main.QuestManagement.class);

        firestoreHandler = new FirestoreHandler();
        authHandler = new AuthHandler();

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.parent_login);

        NavUtil.hideSystemBars(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pop_out_Anim = AnimationUtils.loadAnimation(this, R.anim.pop_out_animation);
        fade_in_Anim = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation);

        emailbox = findViewById(R.id.emailBox2);
        passwordbox = findViewById(R.id.passwordBox2);
        forgotPasswordTextView = findViewById(R.id.textView3);
        confirmButton = findViewById(R.id.confirmButton2);
        signUpTextView = findViewById(R.id.signupTextView);

        // Lets be real now we dont need this
        /*
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
        */

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log-in Logic
                LogInManager logInManager = new LogInManager();
                logInManager.attemptUserLogin(emailbox.getText().toString(), passwordbox.getText().toString(), ParentLogin.this, QuestManagement.class);
            }
        });


        signUpTextView.setOnClickListener(v -> {
                    startActivity(SignUpIntent);
                });


    }



    /*
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
                            Toast.makeText(ParentLogin.this, "Login successful", Toast.LENGTH_SHORT).show();
                            startActivity(QuestManagement);
                        } else {

                        }
                    }
                });

    }
    */

}
