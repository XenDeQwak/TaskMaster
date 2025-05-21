package com.taskmaster.appui.FirebaseHandler;

import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreHandler {

    FirebaseFirestore db;

    public FirestoreHandler () {
        db = FirebaseFirestore.getInstance();
    }

}
