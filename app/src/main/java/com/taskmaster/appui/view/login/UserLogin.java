package com.taskmaster.appui.view.login;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.taskmaster.appui.manager.viewmanager.LogInManager;
import com.taskmaster.appui.view.child.ChildPageQuestBoard;
import com.taskmaster.appui.view.parent.ParentPageQuestBoard;
import com.taskmaster.appui.util.NavUtil;
import com.taskmaster.appui.R;

public class UserLogin extends AppCompatActivity {

    public static UserLogin userLogin;
    Animation pop_out_Anim, fade_in_Anim;
    TextView forgotPasswordTextView, signUpTextView;
    AppCompatButton confirmButton;
    EditText emailbox, passwordbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        userLogin = this;

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.parent_login);
        NavUtil.hideSystemBars(this);

        pop_out_Anim = AnimationUtils.loadAnimation(this, R.anim.pop_out_animation);
        fade_in_Anim = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation);

        emailbox = findViewById(R.id.usernameLogInBox);
        passwordbox = findViewById(R.id.passwordLogInBox);
        forgotPasswordTextView = findViewById(R.id.textView3);
        confirmButton = findViewById(R.id.confirmButton);
        signUpTextView = findViewById(R.id.signupTextView);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class<?>[] destinations = {ParentPageQuestBoard.class, ChildPageQuestBoard.class};
                LogInManager logInManager = new LogInManager();
                logInManager.attemptUserLogin(emailbox.getText().toString(), passwordbox.getText().toString(), UserLogin.this, destinations);
            }
        });

        NavUtil.setNavigation(this, signUpTextView, SignUp.class);


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
                            Toast.makeText(UserLogin.this, "Login successful", Toast.LENGTH_SHORT).show();
                            startActivity(QuestManagement);
                        } else {

                        }
                    }
                });

    }
    */
    }
}
