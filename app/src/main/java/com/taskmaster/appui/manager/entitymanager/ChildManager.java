package com.taskmaster.appui.manager.entitymanager;


import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.taskmaster.appui.entity.Child;
import com.taskmaster.appui.manager.firebasemanager.AuthManager;
import com.taskmaster.appui.manager.firebasemanager.FirestoreManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ChildManager {

    private ArrayList<Child> childList;

    private Context context;
    private FirebaseOptions options;
    private FirebaseApp tempFB;
    private FirebaseAuth tempAuth;
    private FirebaseFirestore tempFirestore;

    public ChildManager () {
        this.childList = new ArrayList<>();
    }

    public ChildManager (ArrayList<Child> childList) {
        this.childList = childList;
    }

    public ChildManager(Context context) {
        this.context = context;
    }

    public static Child parseChildData (Map<String, Object> childData) {
        Child c = new Child();
        c.setChildEmail((String) childData.get("Email"));
        c.setChildUsername((String) childData.get("Username"));
        c.setChildFirstname((String) childData.get("Firstname"));
        c.setChildLastname((String) childData.get("Lastname"));

        return c;
    }

    public static Map<String, Object> packChildData (Child child) {
        Map<String, Object> cd = new HashMap<>();
        cd.put("Email", child.getChildEmail());
        cd.put("Username", child.getChildUsername());
        cd.put("Firstname", child.getChildFirstname());
        cd.put("Lastname", child.getChildLastname());

        return cd;
    }

    /**
     * Pass a Map<String, Object> object to this method to create a child object
     * @param childData
     * @param childPassword
     */
    public void create (Map<String, Object> childData, String childPassword) {
        Child c = parseChildData(childData);
        c.setChildPassword(childPassword);
        initTempFirebaseConnection(context, c);
    }

    /**
     * Debug method used to create random child objects
     */
    public void create () {
        initTempFirebaseConnection(context, createTestChild());
    }

    private Child createTestChild() {
        Child c = new Child();
        Random r = new Random();
        c.setChildEmail("testchild" + r.nextInt(100) + "@email.com");
        c.setChildUsername("testchild");
        c.setChildFirstname("test");
        c.setChildLastname("child");
        c.setChildPassword("testchild");

        return c;
    }

    private void initTempFirebaseConnection (Context context, Child c) {
        options = new FirebaseOptions.Builder()
                .setApiKey("AIzaSyAkhsreVJJf0Hs_-wQ1SuAkXAp_J4tMihs")
                .setApplicationId("1:120375756304:android:fdb00f240e692a8f5b2c95")
                .setProjectId("taskmasterv3")
                //.setDatabaseUrl("https://taskmasterv3.firebaseio.com") // Apparently this is for realtime database only
                .build();
        tempFB = FirebaseApp.initializeApp(context, options, "Temp");
        tempAuth = FirebaseAuth.getInstance(tempFB);
        tempFirestore = FirebaseFirestore.getInstance(tempFB);

        createChildAuth(c);
    }

    private void createChildAuth (Child c) {
        tempAuth.createUserWithEmailAndPassword(c.getChildEmail(), c.getChildPassword()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                System.out.println("Successfully created child account");
                createChildData(c);
            } else {
                task.getException().printStackTrace();
                System.out.println("Failed to create child account");
            }
        });
    }

    private void createChildData (Child c) {
        FirebaseUser parent = AuthManager.getAuth().getCurrentUser();
        DocumentReference parentRef = FirestoreManager.getFirestore().collection("Users").document(parent.getUid());

        Map<String, Object> childData = packChildData(c);

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
