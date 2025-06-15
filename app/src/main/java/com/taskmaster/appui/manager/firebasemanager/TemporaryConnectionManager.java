package com.taskmaster.appui.manager.firebasemanager;

import static com.taskmaster.appui.manager.entitymanager.ChildManager.packChildData;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.taskmaster.appui.entity.Child;

import java.util.HashMap;
import java.util.Map;

public class TemporaryConnectionManager {

    private FirebaseOptions options = new FirebaseOptions.Builder()
            .setApiKey("AIzaSyAkhsreVJJf0Hs_-wQ1SuAkXAp_J4tMihs")
                .setApplicationId("1:120375756304:android:fdb00f240e692a8f5b2c95")
                .setProjectId("taskmasterv3")
                .build();;
    private FirebaseApp tempFB;
    FirebaseAuth tempAuth = FirebaseAuth.getInstance(tempFB);
    FirebaseFirestore tempFirestore = FirebaseFirestore.getInstance(tempFB);
    Task<Void> lastTask;

    public void start (Context context) {
        Log.d("Debug", "Starting Temporary Connection");
        tempFB = FirebaseApp.initializeApp(context, options, "Temp");
    }

    public void end() {
        lastTask.addOnCompleteListener(task -> {
            Log.d("Debug", "Closing Temporary Connection");
            tempFB.delete();
        });
    }

    public void uploadChild (Child c) {
        createChildAuth(c);
    }

    private void createChildAuth (Child c) {
        tempAuth.createUserWithEmailAndPassword(c.getChildEmail(), c.getChildPassword()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("Debug", "Successfully created child account");
            } else {
                task.getException().printStackTrace();
                Log.d("Debug", "Failed to create child account");
            }
        });

        tempAuth.addAuthStateListener(auth -> {
            createChildData(c);
        });
    }

    private void createChildData (Child c) {
        FirebaseUser parent = AuthManager.getAuth().getCurrentUser();
        DocumentReference parentRef = FirestoreManager.getFirestore().collection("Users").document(parent.getUid());

        Map<String, Object> childData = packChildData(c);
        FirebaseUser child = FirebaseAuth.getInstance(tempFB).getCurrentUser();


        Map<String, Object> childParentReference = new HashMap<>();
        childParentReference.put("Parent", parent.getUid());
        lastTask = tempFirestore.collection("ChildParentLookUp").document(child.getUid()).set(childParentReference)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Debug", "Successfully created child-parent reference document");
                    } else {
                        Log.d("Debug", "Failed to create child-parent reference document");
                    }
                });
    }
}











