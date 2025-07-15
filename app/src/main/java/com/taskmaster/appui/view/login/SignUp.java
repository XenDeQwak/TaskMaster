package com.taskmaster.appui.view.login;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    EditText emailbox, passwordbox, usernamebox, cpasswordbox;
    View[] passwordStrength;
    Boolean isPasswordSecure = false;

    @SuppressLint("NewApi")
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
        cpasswordbox = findViewById(R.id.passwordSignUpBox2);
        confirmButton = findViewById(R.id.confirmButton);
        TextView returnText = findViewById(R.id.loginTextView);
        NavUtil.setNavigation(this, returnText, UserLogin.class);

        passwordStrength = new View[] {
                findViewById(R.id.passwordStrengthBarWeakFrame),
                findViewById(R.id.passwordStrengthBarAverageFrame),
                findViewById(R.id.passwordStrengthBarStrongFrame)
        };

        View[] signUpInformation = {emailbox, usernamebox, passwordbox, cpasswordbox};


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
                int checkBoxes = 0;
                if(hasUppercase){checkBoxes++;}
                if(hasNumber){checkBoxes++;}
                if(hasSpecial){checkBoxes++;}

                for (int i = 0; i < passwordStrength.length; i++) {
                    if (checkBoxes-1 == i || (0 == i && 0 == checkBoxes && !passwordbox.getText().toString().isEmpty()))
                        passwordStrength[i].setVisibility(VISIBLE);
                    else
                        passwordStrength[i].setVisibility(GONE);
                }

                if (checkBoxes < 3) {
                    isPasswordSecure = true;
                }
            }
        });

        confirmButton.setOnClickListener(v -> {

            if (emailbox.getText().toString().isEmpty()
                    || usernamebox.getText().toString().isEmpty()
                    || passwordbox.getText().toString().isEmpty()
                    || cpasswordbox.getText().toString().isEmpty()
            ) {
                Toast.makeText(this, "Please fill up all fields", Toast.LENGTH_LONG).show();
                return;
            }

            if (!isPasswordSecure) {
                Toast.makeText(this, "Password is not strong enough", Toast.LENGTH_LONG).show();
                return;
            }

            Toast.makeText(this, "Creating account...", Toast.LENGTH_LONG).show();
            LogInManager logInManager = new LogInManager();
            logInManager.attemptUserSignUp(signUpInformation, SignUp.this);

        });
    }
}