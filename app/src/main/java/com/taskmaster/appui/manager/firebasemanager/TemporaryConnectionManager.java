package com.taskmaster.appui.manager.firebasemanager;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.taskmaster.appui.entity.Child;
import com.taskmaster.appui.manager.entitymanager.ChildManager;

import java.util.HashMap;
import java.util.Map;

public class TemporaryConnectionManager {

    private static FirebaseOptions options = new FirebaseOptions.Builder()
            .setApiKey("AIzaSyAkhsreVJJf0Hs_-wQ1SuAkXAp_J4tMihs")
                .setApplicationId("1:120375756304:android:fdb00f240e692a8f5b2c95")
                .setProjectId("taskmasterv3")
                .build();;
    private static FirebaseApp tempFB;
    private static FirebaseAuth tempAuth;
    private static FirebaseFirestore tempFirestore;
    private static Context context;

    public static void startTempConnection (Context context) {
        TemporaryConnectionManager.context = context;
    }

    public static void uploadChild (Child c) {
        Log.d("Debug", "Starting Temporary Connection");
        tempFB = FirebaseApp.initializeApp(context, options, c.hashCode()+"");
        tempAuth = FirebaseAuth.getInstance(tempFB);
        tempFirestore = FirebaseFirestore.getInstance(tempFB);

        tempAuth.signOut();

        createChildAuth(c);
    }

    private static void createChildAuth (Child c) {

        tempAuth.addAuthStateListener(auth -> {
            if (auth.getCurrentUser() == null || auth.getCurrentUser().isAnonymous()) return;
            FirebaseUser u = auth.getCurrentUser();
            createChildData(c, u);
            tempFB.delete();
        });

        tempAuth.createUserWithEmailAndPassword(c.getChildData().getEmail(), c.getChildData().getPassword()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("Debug", "Successfully created child account");
            } else {
                task.getException().printStackTrace();
                Log.d("Debug", "Failed to create child account");
            }
        });

    }

    private static void createChildData (Child c, FirebaseUser u) {

        DocumentReference childRef = FirebaseFirestore.getInstance().document("Users/"+FirebaseAuth.getInstance().getUid()+"/Adventurers/"+tempAuth.getUid());

        c.getChildData().setUid(tempAuth.getUid());
        c.getChildData().setPassword("");
        c.getChildData().setChildReference(childRef);

        Task<Void> createChildAuth = FirestoreManager.getFirestore()
                .collection("Users")
                .document(c.getChildData().getParentUID())
                .collection("Adventurers")
                .document(u.getUid())
                .set(c.getChildData())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Debug", "Successfully created child document");
                    } else {
                        task.getException().printStackTrace();
                        Log.d("Debug", "Failed to create child document");
                    }
                });

        Map<String, Object> m = new HashMap<>();
        m.put("role", "child");
        m.put("uid", c.getChildData().getUid());
        m.put("userReference", childRef);
        Task<Void> createChildReference = FirestoreManager.getFirestore()
                .collection("UserReferences")
                .document(c.getChildData().getUid())
                .set(m)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Debug", "Successfully created child document");
                    } else {
                        task.getException().printStackTrace();
                        Log.d("Debug", "Failed to create child document");
                    }
                });

        // Wait for them to complete
        Tasks.whenAllComplete(createChildAuth)
                .addOnCompleteListener(task -> {
                    Log.d("Debug", "Successfully completed child creation process");
                    tempFB.delete();
                });

    }
}











