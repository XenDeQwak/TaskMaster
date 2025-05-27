package com.taskmaster.appui.Page.Login;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.taskmaster.appui.FirebaseHandler.AuthHandler;
import com.taskmaster.appui.FirebaseHandler.FirestoreHandler;
import com.taskmaster.appui.Page.NavUtil;

public class LogInManager {

    AuthCredential credential;

    public void attemptUserLogin(String email, String password, Activity origin, final Class<?> destination) {

        credential = EmailAuthProvider.getCredential(email, password);

        try {

            Task<AuthResult> signInAttempt = AuthHandler.signInUser(credential);
            signInAttempt.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(origin, "Login successful", Toast.LENGTH_SHORT).show();
                        NavUtil.instantNavigation(origin, destination);
                    } else {
                        Toast.makeText(origin, "Login failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
