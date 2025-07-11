package com.taskmaster.appui.manager.viewmanager;

import android.app.Activity;
import android.util.Log;
import android.widget.Space;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.taskmaster.appui.data.AuthUserData;
import com.taskmaster.appui.entity.CurrentUser;
import com.taskmaster.appui.manager.firebasemanager.AuthManager;
import com.taskmaster.appui.manager.firebasemanager.FirestoreManager;
import com.taskmaster.appui.util.NavUtil;
import com.taskmaster.appui.view.login.Splash;

public class SignUpManager {

    private final Activity origin;
    private final String email, username, password, password2;

    public SignUpManager(Activity origin,
                         String email,
                         String username,
                         String password,
                         String password2
    ) {
        this.origin = origin;
        this.email = email;
        this.username = username;
        this.password = password;
        this.password2 = password2;
    }

    public void start() {
        AuthManager.createTemporaryUser(tempUser -> {
            checkEmail(tempUser);
        });
    }

    private void checkEmail (FirebaseUser tempUser) {
        FirestoreManager.checkIfEmailIsTaken(email, isEmailFree -> {
            if (isEmailFree) {
                checkUsername(tempUser);
            } else {
                tempUser.delete();
                Toast.makeText(origin, "Email is already taken", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void checkUsername (FirebaseUser tempUser) {
        FirestoreManager.checkIfUsernameIsTaken(username, isUsernameFree -> {
            if (isUsernameFree) {
                confirmPassword(tempUser);
            } else {
                tempUser.delete();
                Toast.makeText(origin, "Username is already taken", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmPassword (FirebaseUser tempUser) {
        if (password.equalsIgnoreCase(password2)) {
            createAccount(tempUser);
        } else {
            tempUser.delete();
            Toast.makeText(origin, "Passwords do not match", Toast.LENGTH_SHORT).show();
        }
    }

    private void createAccount(FirebaseUser tempUser) {

        CollectionReference Users = FirebaseFirestore.getInstance().collection("Users");

        CurrentUser user = CurrentUser.getInstance();
        user.setFirebaseUser(FirebaseAuth.getInstance().getCurrentUser());
        user.setUserData(new AuthUserData());
        user.getUserData().setEmail(email);
        user.getUserData().setUsername(username);
        user.getUserData().setRole("parent");
        //user.getUserData().setUserSnapshot(null);

        // Create user data in Firestore
        Task createUserData = Users.document(user.getFirebaseUser().getUid()).set(user.getUserData()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentReference dr = Users.document(user.getFirebaseUser().getUid());
                dr.update("id", dr.getId());
                user.getUserData().setId(dr.getId());
                Log.d("Debug", "Created User Document");
            } else {
                tempUser.delete();
                task.getException().printStackTrace();
            }
        });

        // Create User Auth
        AuthCredential credential = AuthManager.provideCredential(email, password);
        Task createUserAuth = tempUser.linkWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("Debug", "Created User Auth");
            } else {
                tempUser.delete();
                task.getException().printStackTrace();
            }
        });

        Tasks.whenAllComplete(createUserData, createUserAuth).addOnSuccessListener(task -> {
            Users.document(user.getUserData().getId()).get().addOnSuccessListener(ds -> {
                user.getUserData().setUserSnapshot(ds, e -> {
                    Log.d("Debug", "Successfully Created User");
                    Toast.makeText(origin, "Successfully created account!", Toast.LENGTH_SHORT).show();
                    NavUtil.instantNavigation(origin, Splash.class);
                });

            });
        });

    }
}
