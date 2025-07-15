package com.taskmaster.appui.manager.entitymanager;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AuthUserManager {

    private final CollectionReference Quests = FirebaseFirestore.getInstance().collection("Users");

    public static void initializeCurrentUser () {

    }

}
