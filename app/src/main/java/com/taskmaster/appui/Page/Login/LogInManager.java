package com.taskmaster.appui.Page.Login;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QuerySnapshot;
import com.taskmaster.appui.FirebaseHandler.AuthHandler;
import com.taskmaster.appui.FirebaseHandler.FirestoreHandler;
import com.taskmaster.appui.Page.Main.QuestManagement;
import com.taskmaster.appui.Page.Main.User;
import com.taskmaster.appui.Services.GenericCallback;
import com.taskmaster.appui.Services.NavUtil;
import com.taskmaster.appui.Services.SignUpFlowService;

public class LogInManager {

    AuthCredential credential;

    public void attemptUserLogin (String email, String password, Activity origin, final Class<?> destination) {


        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(origin, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        credential = AuthHandler.provideCredential(email, password);

        try {

            AuthHandler.signInUser(credential, IsSignInSuccess -> {
                if (IsSignInSuccess) {
                    Toast.makeText(origin, "Login successful", Toast.LENGTH_SHORT).show();
                    NavUtil.instantNavigation(origin, destination);
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
