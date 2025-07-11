package com.taskmaster.appui.manager.viewmanager;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.taskmaster.appui.manager.firebasemanager.AuthManager;
import com.taskmaster.appui.entity.User;
import com.taskmaster.appui.util.NavUtil;
import com.taskmaster.appui.view.child.ChildPageQuestBoard;
import com.taskmaster.appui.view.parent.ParentPageQuestBoard;

import java.util.Objects;

public class LogInManager {

    AuthCredential credential;

    public void attemptUserLogin (String email, String password, Activity origin, final Class<?>[] destination) {

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(origin, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        credential = AuthManager.provideCredential(email, password);

        try {

            AuthManager.signInUser(credential, signInResult -> {

                Boolean signInSuccessful = (Boolean)signInResult[0];
                String signInRole = (String)signInResult[1];

                if (signInSuccessful) {
                    Toast.makeText(origin, "Login successful", Toast.LENGTH_SHORT).show();
                    User newUser = User.getInstance();
                    newUser.setUser(FirebaseAuth.getInstance().getCurrentUser());
                    Class<?> dest = (Objects.equals(signInRole, "parent"))? ParentPageQuestBoard.class : ChildPageQuestBoard.class;
//                    System.out.println(signInRole);
//                    System.out.println((Objects.equals(signInRole, "parent")));
//                    System.out.println(dest);
                    newUser.loadDocumentSnapshot(documentSnapshot -> NavUtil.instantNavigation(origin, dest));
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

        SignUpManager signUpManager = new SignUpManager(
                origin,
                emailbox.getText().toString(),
                usernamebox.getText().toString(),
                passwordbox.getText().toString(),
                firstnamebox.getText().toString(),
                lastnamebox.getText().toString()
        );
        signUpManager.start();

    }

}
