package com.taskmaster.appui.FirebaseHandler;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthHandler {

    FirebaseAuth auth;

    public AuthHandler () {
        auth = FirebaseAuth.getInstance();
    }

    public Task signInUser (String username, String password) {
        Task signInAttempt = auth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Debug", "Successful: " + task.toString());
                        } else {
                            Log.d("Debug", "Unsuccessful: " + task.toString());
                            Log.d("Error", task.getException().toString());
                        }
                    }
                });
        return signInAttempt;
    }

}
