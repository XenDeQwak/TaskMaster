package com.taskmaster.appui.Services;

import android.app.Activity;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;
import com.taskmaster.appui.FirebaseHandler.AuthHandler;
import com.taskmaster.appui.FirebaseHandler.FirestoreHandler;
import com.taskmaster.appui.Page.Login.UserLogin;

import java.util.HashMap;
import java.util.Map;

public class SignUpFlowService {

    private final Activity origin;
    private final String email, username, password, firstname, lastname;

    public SignUpFlowService(Activity origin,
                             String email,
                             String username,
                             String password,
                             String firstname,
                             String lastname
    ) {
        this.origin = origin;
        this.email = email;
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public void start() {
        AuthHandler.createTemporaryUser(tempUser -> {
            checkEmail(tempUser);
        });
    }

    private void checkEmail (FirebaseUser tempUser) {
        FirestoreHandler.checkIfEmailIsTaken(email, isEmailFree -> {
            if (isEmailFree) {
                checkUsername(tempUser);
            } else {
                Toast.makeText(origin, "Email is already taken", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUsername (FirebaseUser tempUser) {
        FirestoreHandler.checkIfUsernameIsTaken(username, isUsernameFree -> {
            if (isUsernameFree) {
                createAccount(tempUser);
            } else {
                Toast.makeText(origin, "Username is already taken", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createAccount(FirebaseUser tempUser) {

        Map<String, Object> userData = new HashMap<>();
        userData.put("Email", email);
        userData.put("Username", username);
        userData.put("Firstname", firstname);
        userData.put("Lastname", lastname);
        userData.put("Role", "parent");

        AuthCredential credential = AuthHandler.provideCredential(email, password);
        FirestoreHandler.createUser(tempUser, credential, userData, e -> {
            Toast.makeText(origin, "User registered successfully", Toast.LENGTH_SHORT).show();
            NavUtil.instantNavigation(origin, UserLogin.class);
        });
    }
}
