package com.taskmaster.appui.util;


import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.taskmaster.appui.manager.firebasemanager.AuthManager;
import com.taskmaster.appui.manager.firebasemanager.FirestoreManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ChildCreator {

    Context context;
    String childEmail;
    String childPassword;
    String childUsername;
    String childFirstname;
    String childLastname;

    FirebaseOptions options;
    FirebaseApp tempFB;
    FirebaseAuth tempAuth;
    FirebaseFirestore tempFirestore;

    public ChildCreator (Context context) {
        this.context = context;
    }

    public void create (Map<String, Object> childData, String childPassword) {
        childEmail = (String) childData.get("Email");
        childUsername = (String) childData.get("Username");
        childFirstname = (String) childData.get("Firstname");
        childLastname = (String) childData.get("Lastname");
        this.childPassword = childPassword;
        initTempFirebaseConnection(context);
    }

    public void create () {
        createTestChildForDebug();
        initTempFirebaseConnection(context);
    }

    private void createTestChildForDebug () {
        Random r = new Random();
        childEmail = "testchild" + r.nextInt(100) + "@email.com";
        childUsername = "testchild";
        childFirstname = "test";
        childLastname = "child";
        this.childPassword = "testchild";
    }

    private void initTempFirebaseConnection (Context context) {
        options = new FirebaseOptions.Builder()
                .setApiKey("AIzaSyAkhsreVJJf0Hs_-wQ1SuAkXAp_J4tMihs")
                .setApplicationId("1:120375756304:android:fdb00f240e692a8f5b2c95")
                .setProjectId("taskmasterv3")
                //.setDatabaseUrl("https://taskmasterv3.firebaseio.com") // Apparently this is for realtime database only
                .build();
        tempFB = FirebaseApp.initializeApp(context, options, "Temp");
        tempAuth = FirebaseAuth.getInstance(tempFB);
        tempFirestore = FirebaseFirestore.getInstance(tempFB);

        createChildAuth();
    }

    private void createChildAuth () {
        tempAuth.createUserWithEmailAndPassword(childEmail, childPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                System.out.println("Successfully created child account");
                createChildData();
            } else {
                task.getException().printStackTrace();
                System.out.println("Failed to create child account");
            }
        });
    }

    private void createChildData () {
        FirebaseUser parent = AuthManager.getAuth().getCurrentUser();
        DocumentReference parentRef = FirestoreManager.getFirestore().collection("Users").document(parent.getUid());

        Map<String, Object> childData = new HashMap<>();
        childData.put("Email", childEmail);
        childData.put("Username", childUsername);
        childData.put("Firstname", childFirstname);
        childData.put("Lastname", childLastname);
        childData.put("ParentRef", parentRef);
        childData.put("Role", "child");

        tempAuth.addAuthStateListener(auth -> {
            FirebaseUser child = auth.getCurrentUser();
            tempFirestore.collection("Users").document(parent.getUid()).collection("adventurers").document(child.getUid()).set(childData)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            System.out.println("Successfully created child data document");
                        } else {
                            System.out.println("Failed to create child data document");
                        }
                        tempFB.delete();
                    });

            Map<String, Object> childParentReference  = new HashMap<>();
            childParentReference.put("Parent", parent.getUid());
            tempFirestore.collection("ChildParentLookUp").document(child.getUid()).set(childParentReference)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            System.out.println("Successfully created child-parent reference document");
                        } else {
                            System.out.println("Failed to create child-parent reference document");
                        }
                    });
        });




    }

}
