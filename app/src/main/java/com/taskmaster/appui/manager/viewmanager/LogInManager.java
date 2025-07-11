package com.taskmaster.appui.manager.viewmanager;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.taskmaster.appui.data.AuthUserData;
import com.taskmaster.appui.entity.CurrentUser;
import com.taskmaster.appui.manager.firebasemanager.AuthManager;
import com.taskmaster.appui.manager.firebasemanager.FirestoreManager;
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
                    CurrentUser newCurrentUser = CurrentUser.getInstance();
                    newCurrentUser.setFirebaseUser(FirebaseAuth.getInstance().getCurrentUser());
                    newCurrentUser.setUserData(new AuthUserData());
                    Class<?> dest = (Objects.equals(signInRole, "parent"))? ParentPageQuestBoard.class : ChildPageQuestBoard.class;
                    FirestoreManager.getUserInformation(FirebaseAuth.getInstance().getCurrentUser().getUid(), ds -> {
                        newCurrentUser.getUserData().setUserSnapshot(ds, e -> {
                            NavUtil.instantNavigation(origin, dest);
                        });

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

        EditText emailbox, usernamebox, passwordbox, cpasswordbox;
        emailbox = (EditText) signUpInformation[0];
        usernamebox = (EditText) signUpInformation[1];
        passwordbox = (EditText) signUpInformation[2];
        cpasswordbox = (EditText) signUpInformation[3];

        SignUpManager signUpManager = new SignUpManager(
                origin,
                emailbox.getText().toString(),
                usernamebox.getText().toString(),
                passwordbox.getText().toString(),
                cpasswordbox.getText().toString()
        );
        signUpManager.start();

    }

}
