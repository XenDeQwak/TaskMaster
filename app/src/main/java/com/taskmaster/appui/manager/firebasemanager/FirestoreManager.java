package com.taskmaster.appui.manager.firebasemanager;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.taskmaster.appui.util.GenericCallback;
import com.taskmaster.appui.entity.Quest;
import com.taskmaster.appui.manager.entitymanager.QuestManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirestoreManager {

    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public static void checkIfEmailIsTaken (String email, GenericCallback<Boolean> callback) {
        firestore.collection("Users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> dsl = task.getResult().getDocuments();
                        if (dsl.isEmpty()) {
                            callback.onCallback(true);
                        } else {
                            callback.onCallback(false);
                        }
                    } else {
                        task.getException().printStackTrace();
                    }
                });
    }

    public static void checkIfUsernameIsTaken (String username, GenericCallback<Boolean> callback) {
        firestore.collection("Users")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> dsl = task.getResult().getDocuments();
                        if (dsl.isEmpty()) {
                            callback.onCallback(true);
                        } else {
                            callback.onCallback(false);
                        }
                    } else {
                        task.getException().printStackTrace();
                    }
                });
    }

    public static void getUserInformation (String UID, GenericCallback<DocumentSnapshot> callback) {
        firestore.collection("UserReferences").document(UID).get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot ds = task.getResult();
                    if (ds.exists()) {
                        ds.get("userReference", DocumentReference.class)
                                .get()
                                .addOnCompleteListener(task1 -> callback.onCallback(task1.getResult()));
                    } else {
                        callback.onCallback(null);
                    }
                });
    }


    public static void createUser (FirebaseUser tempUser, AuthCredential credential, Map<String, Object> userData, GenericCallback callback) {
        tempUser.linkWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("Debug", "Successfully linked anon currentUser to credential account");
                } else {
                    Log.d("Debug", "Failed to link anon currentUser to credential account");
                }
            }
        });
        firestore.collection("Users").document(tempUser.getUid()).set(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("Debug", "Successfully created currentUser document");
                    callback.onCallback(task);
                } else {
                    Log.d("Debug", "Failed to create currentUser document");
                }
            }
        });
    }

    public static void uploadQuest (Quest q) {

        //System.out.println(q.getCreatorReference());
        //System.out.println(q.getCreatorReference().getPath());
        String questID = Integer.toString(q.hashCode());
        //q.setQuestID(questID);
        //System.out.println(QuestManager.packQuestData(q));
//        firestore.collection("Quests").document(questID).set(QuestManager.packQuestData(q))
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Log.d("Debug", "Successfully created quest document");
//                    } else {
//                        Log.d("Debug", "Failed to create quest document");
//                    }
//                });

        HashMap<String, Object> ref = new HashMap<>();
        ref.put("QuestRef", firestore.collection("Quests").document(questID));
//        firestore.collection("Users").document(q.getCreatorUID()).collection("quests").document(questID).set(ref)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Log.d("Debug", "Successfully created quest reference");
//                    } else {
//                        task.getException().printStackTrace();
//                        Log.d("Debug", "Failed to create quest reference");
//                    }
//                });
    }

    public static void updateQuest (Quest q) {

        //String questID = q.getQuestID();
        //System.out.println(questID);
//        firestore.collection("Quests").document(questID).set(QuestManager.packQuestData(q))
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Log.d("Debug", "Successfully updated quest document");
//                    } else {
//                        Log.d("Debug", "Failed to update quest document");
//                    }
//                });

    }


    public static void fetchQuests (String creatorUID, GenericCallback<List<DocumentSnapshot>> callback) {
        firestore.collection("Quests")
                .whereEqualTo("CreatorUID", creatorUID)
                //.whereNotEqualTo("Status", "Completed")
                //.whereNotEqualTo("Status", "Failed")
                //.whereNotEqualTo("Status", "Deleted")
                .whereIn("Status", Arrays.asList("Ongoing", "Awaiting Configuration", "Awaiting Verification"))
                .get()
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

    public static void fetchQuestsAssignee (String creatorUID, GenericCallback<List<DocumentSnapshot>> callback) {
        firestore.collection("Quests")
                .whereEqualTo("AssignedUID", creatorUID)
                .whereIn("Status", Arrays.asList("Ongoing","Awaiting Verification"))
                .get()
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

    public static void fetchCreatedQuestsWhereStatus (String id, List<String> statusList, GenericCallback<List<DocumentSnapshot>> callback) {
        firestore.collection("Quests")
                .whereEqualTo("CreatorUID", id)
                .whereIn("Status", statusList)
                .get()
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

    public static void fetchAssignedQuestsWhereStatus (String id, List<String> statusList, GenericCallback<List<DocumentSnapshot>> callback) {
        firestore.collection("Quests")
                .whereEqualTo("AssignedUID", id)
                .whereIn("Status", statusList)
                .get()
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


    public static void fetchAdventurers (String parentUID, GenericCallback<List<DocumentSnapshot>> callback) {
        firestore.collection("Users/"+parentUID+"/Adventurers").whereEqualTo("parentUID", parentUID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Debug", "Successfully fetched adventurers");
                        List<DocumentSnapshot> childDocs = task.getResult().getDocuments();
                        callback.onCallback(childDocs);
                    } else {
                        task.getException().printStackTrace();
                        Log.d("Debug", "Failed to fetch adventurers");
                    }
                });
    }

    public static FirebaseFirestore getFirestore () {
        return firestore;
    }
}
