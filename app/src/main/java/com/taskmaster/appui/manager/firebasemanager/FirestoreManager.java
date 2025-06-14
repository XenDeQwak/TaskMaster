package com.taskmaster.appui.manager.firebasemanager;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.taskmaster.appui.util.GenericCallback;
import com.taskmaster.appui.entity.Quest;
import com.taskmaster.appui.manager.entitymanager.QuestManager;

import java.util.List;
import java.util.Map;

public class FirestoreManager {

    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private static FirebaseAuth auth = FirebaseAuth.getInstance();

    public static void checkIfEmailIsTaken (String email, GenericCallback<Boolean> callback) {
        firestore.collection("Users")
                .whereEqualTo("Role", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        callback.onCallback(task.isSuccessful() && task.getResult().isEmpty());
                    }
                });
    }

    public static void checkIfUsernameIsTaken (String username, GenericCallback<Boolean> callback) {
        firestore.collection("Users")
                .whereEqualTo("Username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        callback.onCallback(task.isSuccessful() && task.getResult().isEmpty());
                    }
                });
    }

    public static void getUserInformation (String UID, GenericCallback<DocumentSnapshot> callback) {
        firestore.collection("Users").document(UID).get().addOnCompleteListener(
                new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        callback.onCallback(task.getResult());
                    }
                }
        );
    }


    public static void createUser (FirebaseUser tempUser, AuthCredential credential, Map<String, Object> userData, GenericCallback callback) {
        tempUser.linkWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("Debug", "Successfully linked anon user to credential account");
                } else {
                    Log.d("Debug", "Failed to link anon user to credential account");
                }
            }
        });
        firestore.collection("Users").document(tempUser.getUid()).set(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("Debug", "Successfully created user document");
                    callback.onCallback(task);
                } else {
                    Log.d("Debug", "Failed to create user document");
                }
            }
        });
    }

    public static void uploadQuest (String creatorUID, Quest quest) {
        String questID = Integer.toString(quest.hashCode());
        firestore.collection("Quests").document(questID).set(QuestManager.packQuestData(quest))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Debug", "Successfully created quest document");
                    } else {
                        Log.d("Debug", "Failed to create quest document");
                    }
                });
    }

    public static void fetchQuests (String creatorUID, GenericCallback<List<DocumentSnapshot>> callback) {
        firestore.collection("Quests").whereEqualTo("CreatorUID", creatorUID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Debug", "Successfully fetched quests");
                        List<DocumentSnapshot> questDocs = task.getResult().getDocuments();
                        callback.onCallback(questDocs);
                    } else {
                        task.getException().printStackTrace();
                        Log.d("Debug", "Failed to fetch quests");
                    }
                });
    }

    public static FirebaseFirestore getFirestore () {
        return firestore;
    }
}
