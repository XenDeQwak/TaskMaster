package com.taskmaster.appui.view.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

import com.taskmaster.appui.R;
import com.taskmaster.appui.manager.viewmanager.LogInManager;
import com.taskmaster.appui.util.NavUtil;

public class SignUp extends AppCompatActivity {

    Animation pop_out_Anim, fade_in_Anim;
    ImageView container1, container2, container3, bg, logo, logo_shadow;
    TextView logInTextView;
    boolean hasSpecial,hasUppercase,hasNumber;
    AppCompatButton confirmButton;
    EditText emailbox, passwordbox, usernamebox, firstnamebox, lastnamebox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.sign_up);
        NavUtil.hideSystemBars(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pop_out_Anim = AnimationUtils.loadAnimation(this, R.anim.pop_out_animation);
        fade_in_Anim = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation);

        emailbox = findViewById(R.id.emailSignUpBox);
        usernamebox = findViewById(R.id.usernameSignUpBox);
        passwordbox = findViewById(R.id.passwordSignUpBox);
        firstnamebox = findViewById(R.id.firstnameSignUpBox);
        lastnamebox = findViewById(R.id.lastnameSignUpBox);
        confirmButton = findViewById(R.id.confirmButton2);

        View[] signUpInformation = {emailbox, usernamebox, passwordbox, firstnamebox, lastnamebox};

        passwordbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 8) {confirmButton.setEnabled(true);}
                if (s.toString().matches(".*[A-Z].*")) {hasUppercase=true;}
                else{hasUppercase=false;}
                if (s.toString().matches(".*[0-9].*")) {hasNumber=true;}
                else{hasNumber=false;}
                if (s.toString().matches(".*[@#$%^&+=].*")) {hasSpecial=true;}
                else{hasSpecial=false;}
            }
            @Override
            public void afterTextChanged(Editable s) {
                int checkBoxes=0;
                if(hasUppercase){checkBoxes++;}
                if(hasNumber){checkBoxes++;}
                if(hasSpecial){checkBoxes++;}
                confirmButton.setEnabled(checkBoxes != 0);

                if(checkBoxes==1){Toast.makeText(SignUp.this,"weak",Toast.LENGTH_SHORT).show();}
                if(checkBoxes==2){Toast.makeText(SignUp.this,"med",Toast.LENGTH_SHORT).show();}
                if(checkBoxes==3){Toast.makeText(SignUp.this,"str",Toast.LENGTH_SHORT).show();}
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign-up logic
                // Yes Im also using LogInManager for SignUp because why not
                LogInManager logInManager = new LogInManager();
                logInManager.attemptUserSignUp(signUpInformation, SignUp.this);
            }
        });

        // Lets be real now we dont need this
        /*
        pop_out_Anim = AnimationUtils.loadAnimation(this, R.anim.pop_out_animation);
        fade_in_Anim = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation);
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
        */

    }
}