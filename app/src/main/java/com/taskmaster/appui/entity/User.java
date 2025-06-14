package com.taskmaster.appui.entity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.taskmaster.appui.manager.firebasemanager.FirestoreManager;
import com.taskmaster.appui.util.GenericCallback;

public class User {

    FirebaseUser userAuth;
    DocumentSnapshot userDocumentSnapshot;
    private static User instance;

    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }

    private User(){} //Singleton

    public void setUser(FirebaseUser userAuth){
        this.userAuth = userAuth;
    }

    public FirebaseUser getUserAuth () {
        return userAuth;
    }
    public void setUserAuth(FirebaseUser userAuth){
        this.userAuth = userAuth;
    }
    public DocumentSnapshot getDocumentSnapshot () {
        return userDocumentSnapshot;
    }

    public void setDocumentSnapshot(DocumentSnapshot userDocumentSnapshot) {
        this.userDocumentSnapshot = userDocumentSnapshot;
    }

    public void loadDocumentSnapshot(GenericCallback<Void> callback){
        FirestoreManager.getUserInformation(userAuth.getUid(), documentSnapshot -> {
            setDocumentSnapshot(documentSnapshot);
            callback.onCallback(null);
        });
    }
}
