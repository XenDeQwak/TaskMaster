package com.taskmaster.appui.entity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.taskmaster.appui.data.AuthUserData;
import com.taskmaster.appui.manager.firebasemanager.FirestoreManager;
import com.taskmaster.appui.util.GenericCallback;

public class AuthUser {

    private AuthUserData userData;
    private FirebaseUser firebaseUser;

    public AuthUserData getUserData() {
        return userData;
    }

    public void setUserData(AuthUserData userData) {
        this.userData = userData;
    }

    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public void setFirebaseUser(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
    }
}
