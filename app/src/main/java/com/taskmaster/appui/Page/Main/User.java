package com.taskmaster.appui.Page.Main;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.taskmaster.appui.FirebaseHandler.FirestoreHandler;
import com.taskmaster.appui.Services.GenericCallback;

public class User {

    FirebaseUser userAuth;
    DocumentSnapshot userDocumentSnapshot;

    public User (FirebaseUser userAuth) {
        this.userAuth = userAuth;
    }


    public FirebaseUser getUserAuth () {
        return userAuth;
    }

    public DocumentSnapshot getUserDocumentSnapshot () {
        return userDocumentSnapshot;
    }
}
