package com.taskmaster.appui.view.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.taskmaster.appui.R;
import com.taskmaster.appui.manager.viewmanager.LogInManager;
import com.taskmaster.appui.util.NavUtil;

public class SignUp extends AppCompatActivity {

    Animation pop_out_Anim, fade_in_Anim;
    boolean hasSpecial,hasUppercase,hasNumber;
    AppCompatButton confirmButton;
    EditText emailbox, passwordbox, usernamebox, firstnamebox, lastnamebox;
    FrameLayout[] passwordStrength = new FrameLayout[3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.sign_up);
        NavUtil.hideSystemBars(this);

        pop_out_Anim = AnimationUtils.loadAnimation(this, R.anim.pop_out_animation);
        fade_in_Anim = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation);

        emailbox = findViewById(R.id.emailSignUpBox);
        usernamebox = findViewById(R.id.usernameSignUpBox);
        passwordbox = findViewById(R.id.passwordSignUpBox);
        firstnamebox = findViewById(R.id.firstnameSignUpBox);
        lastnamebox = findViewById(R.id.lastnameSignUpBox);
        confirmButton = findViewById(R.id.confirmButton);
        TextView returnText = findViewById(R.id.loginTextView);
        NavUtil.setNavigation(this, returnText, UserLogin.class);

        passwordStrength = new FrameLayout[] {
                findViewById(R.id.passwordStrengthBarWeakFrame),
                findViewById(R.id.passwordStrengthBarAverageFrame),
                findViewById(R.id.passwordStrengthBarStrongFrame)
        };

        View[] signUpInformation = {emailbox, usernamebox, passwordbox, firstnamebox, lastnamebox};

        passwordbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hasUppercase = s.toString().matches(".*[A-Z].*");
                hasNumber = s.toString().matches(".*[0-9].*");
                hasSpecial = s.toString().matches(".*[!@#$%^&+=].*");
            }

            @Override
            public void afterTextChanged(Editable s) {
                int checkBoxes=0;
                if(hasUppercase){checkBoxes++;}
                if(hasNumber){checkBoxes++;}
                if(hasSpecial){checkBoxes++;}
                confirmButton.setEnabled(checkBoxes != 0);

                if(s.length()<8) {
                    passwordStrength[0].setVisibility(View.VISIBLE);
                }else {
                    for (FrameLayout f : passwordStrength) f.setVisibility(View.GONE);
                    if(checkBoxes==0) {passwordStrength[checkBoxes].setVisibility(View.VISIBLE);
                    }else{passwordStrength[checkBoxes-1].setVisibility(View.VISIBLE);}
                }
            }
        });

        confirmButton.setOnClickListener(v -> {
            // Sign-up logic
            // Yes Im also using LogInManager for SignUp because why not
            LogInManager logInManager = new LogInManager();
            logInManager.attemptUserSignUp(signUpInformation, SignUp.this);
        });
    }
}