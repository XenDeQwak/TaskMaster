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

    public static void signInUser (AuthCredential credential, GenericCallback<Object[]> callback) {
        Log.d("Debug","Sign-in method: " + credential.getSignInMethod());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Debug", "Successful Sign In");
                        firestore
                                .collection("Users")
                                .document(task.getResult().getUser().getUid())
                                .get()
                                .addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful()) {
                                        Log.d("Debug", "Successful retrieved currentUser role");
                                        String role = (String) task2.getResult().get("Role");
                                        callback.onCallback(new Object[]{
                                                task.isSuccessful(),
                                                role
                                        });
                                    } else {
                                        Log.d("Debug", "Unsuccessful Sign In");
                                        Log.d("Error", task2.getException().toString());
                                    }
                                });
                    } else {
                        Log.d("Debug", "Unsuccessful Sign In");
                        Log.d("Error", task.getException().toString());
                    }
                });
    }


    public static void createTemporaryUser (GenericCallback<FirebaseUser> callback) {
        auth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("Debug", "Successful Create Temp CurrentUser");
                    callback.onCallback(task.getResult().getUser());
                } else {
                    Log.d("Debug", "Unsuccessful Create Temp CurrentUser");
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
