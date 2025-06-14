package com.taskmaster.appui.manager.firebasemanager;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.taskmaster.appui.util.GenericCallback;

public class AuthManager {

    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private static FirebaseAuth auth = FirebaseAuth.getInstance();

    // AuthCredential can take many forms. This will allow us to easily
    // expand our sign-in methods in the future if we want to.

    public static Task<AuthResult> signInUser (AuthCredential credential, GenericCallback<Boolean> callback) {
        Log.d("Debug","Sign-in method: " + credential.getSignInMethod());
        Task signInAttempTask = null;
        try {
            signInAttempTask = FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("Debug", "Successful Sign In");
                            } else {
                                Log.d("Debug", "Unsuccessful Sign In");
                                Log.d("Error", task.getException().toString());
                            }
                            callback.onCallback(task.isSuccessful());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return signInAttempTask;
    }


    public static void createTemporaryUser (GenericCallback<FirebaseUser> callback) {
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("Debug", "Successful Create Temp User");
                    callback.onCallback(task.getResult().getUser());
                } else {
                    Log.d("Debug", "Unsuccessful Create Temp User");
                    Log.d("Error", task.getException().toString());
                }
            }
        });
    }

    public static AuthCredential provideCredential (String email, String password) {
        return EmailAuthProvider.getCredential(email, password);
    }

    public static FirebaseAuth getAuth() {
        return auth;
    }
}
