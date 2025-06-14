package com.taskmaster.appui.Page.Login;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.taskmaster.appui.manager.firebasemanager.AuthManager;
import com.taskmaster.appui.Page.Main.User;
import com.taskmaster.appui.util.NavUtil;
import com.taskmaster.appui.util.SignUpFlowService;

public class LogInManager {

    AuthCredential credential;

    public void attemptUserLogin (String email, String password, Activity origin, final Class<?> destination) {


        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(origin, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        credential = AuthManager.provideCredential(email, password);

        try {

            AuthManager.signInUser(credential, IsSignInSuccess -> {
                if (IsSignInSuccess) {
                    Toast.makeText(origin, "Login successful", Toast.LENGTH_SHORT).show();
                    User newUser = User.getInstance();
                    newUser.setUser(FirebaseAuth.getInstance().getCurrentUser());
                    newUser.loadDocumentSnapshot(documentSnapshot -> {
                        NavUtil.instantNavigation(origin, destination);
                    });
                } else {
                    Toast.makeText(origin, "Login failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void attemptUserSignUp (View[] signUpInformation, Activity origin) {

        EditText emailbox, usernamebox, passwordbox, firstnamebox, lastnamebox;
        emailbox = (EditText) signUpInformation[0];
        usernamebox = (EditText) signUpInformation[1];
        passwordbox = (EditText) signUpInformation[2];
        firstnamebox = (EditText) signUpInformation[3];
        lastnamebox = (EditText) signUpInformation[4];

        SignUpFlowService signUpFlow = new SignUpFlowService(
                origin,
                emailbox.getText().toString(),
                usernamebox.getText().toString(),
                passwordbox.getText().toString(),
                firstnamebox.getText().toString(),
                lastnamebox.getText().toString()
        );
        signUpFlow.start();

    }

}
