package com.taskmaster.appui.entity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.taskmaster.appui.data.AuthUserData;
import com.taskmaster.appui.manager.firebasemanager.FirestoreManager;
import com.taskmaster.appui.util.GenericCallback;

public class CurrentUser extends AuthUser {
    private static CurrentUser instance;
    public static CurrentUser getInstance() {
        if (instance == null) {
            instance = new CurrentUser();
        }
        return instance;
    }
    private CurrentUser(){super();} //Singleton
}
