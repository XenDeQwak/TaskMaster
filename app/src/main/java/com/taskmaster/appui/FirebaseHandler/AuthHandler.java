package com.taskmaster.appui.FirebaseHandler;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthHandler {


    // AuthCredential can take many forms. This will allow us to easily
    // expand our sign-in methods in the future if we want to.
    AuthCredential credential;


    public static Task<AuthResult> signInUser (AuthCredential credential) {
        Log.d("Debug","Sign-in method: " + credential.getSignInMethod());
        Task signInAttempt = null;
        try {
            signInAttempt = FirebaseAuth.getInstance().signInWithCredential(credential)
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return signInAttempt;
    }


}
