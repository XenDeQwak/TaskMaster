package com.taskmaster.appui.manager.firebasemanager;

import static com.taskmaster.appui.manager.entitymanager.ChildManager.packChildData;

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
        tempFB = FirebaseApp.initializeApp(context, options, "Temp");
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

        tempAuth.createUserWithEmailAndPassword(c.getChildEmail(), c.getChildPassword()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("Debug", "Successfully created child account");
            } else {
                task.getException().printStackTrace();
                Log.d("Debug", "Failed to create child account");
            }
        });

    }

    private static void createChildData (Child c, FirebaseUser u) {

        //System.out.println(c.getParentRef());
        //System.out.println(c.getParentRef().getPath());
        c.setChildPassword("");
        HashMap<String, Object> cd = (HashMap<String, Object>) ChildManager.packChildData(c);
        cd.put("Role", "child");
        Task<Void> createChildAuth = FirestoreManager.getFirestore()
                .collection("Childs")
                .document(u.getUid()).set(cd)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Debug", "Successfully created child document");
                    } else {
                        task.getException().printStackTrace();
                        Log.d("Debug", "Failed to create child document");
                    }
                });

        HashMap<String, DocumentReference> ref = new HashMap<>();
        ref.put("ChildRef", FirestoreManager.getFirestore().collection("Childs").document(u.getUid()));
        Task<Void> createChildData = FirestoreManager.getFirestore()
                .collection("Users")
                .document(c.getParentUID())
                .collection("adventurers")
                .document(u.getUid())
                .set(ref)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Debug", "Successfully created child reference");
                    } else {
                        task.getException().printStackTrace();
                        Log.d("Debug", "Failed to create child reference");
                    }
                });

        // Wait for both to complete
        Tasks.whenAllComplete(createChildAuth, createChildData)
                .addOnCompleteListener(task -> {
                    Log.d("Debug", "Successfully completed child creation process");
                    tempFB.delete();
                });

    }
}











