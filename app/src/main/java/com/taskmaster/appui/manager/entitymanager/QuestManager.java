package com.taskmaster.appui.manager.entitymanager;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.taskmaster.appui.data.QuestData;
import com.taskmaster.appui.entity.Quest;
import com.taskmaster.appui.view.uimodule.QuestBoxPreview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestManager {

    private final CollectionReference Quests = FirebaseFirestore.getInstance().collection("Quests");


    private LinearLayout questContent;
    private ArrayList<Quest> questList;

    public QuestManager (LinearLayout questContent) {
        this.questContent = questContent;
        this.questList = new ArrayList<>();
    }

    @SuppressLint("NewApi")
    public void refresh () {
        questContent.removeAllViews();
         List<QuestBoxPreview> qbpList = questList
                .stream()
                .map(Quest::getQuestBoxPreview)
                .toList();
        for (QuestBoxPreview qbp : qbpList) {
            questContent.addView(qbp);
        }
    }

    public void create () {
        Quest q = new Quest(QuestData.newBlankQuestData());
        Quests.add(q.getQuestData()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("Debug", "Created Blank Quest");
                DocumentReference dr = task.getResult();
                q.getQuestData().setQuestReference(dr);
                q.getQuestData().setId(dr.getId());
                q.getQuestData().setCreatedBy(FirebaseAuth.getInstance().getUid());
                q.getQuestData().uploadData();
            } else {
                task.getException().printStackTrace();
            }
        });
    }

    /**
     * Fetches quests from the Firestore collection based on the given currentUser type and quest statuses.
     *
     * @param type   Specifies the currentUser type. If "parent", it queries quests where the current currentUser is the creator;
     *               otherwise, it queries quests assigned to the current currentUser.
     * @param status One or more status values (e.g., "ongoing", "completed", "failed") to filter quests by.
     */
    public void fetchQuestsWhereStatus (String type, String... status) {
        String field = type.equalsIgnoreCase("parent") ? "createdBy" : "assignedTo";
        Quests.whereEqualTo(field, FirebaseAuth.getInstance().getUid())
                .whereIn("status", Arrays.asList(status))
                .get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) task.getException().printStackTrace();
                    else {
                        task.getResult().getDocuments()
                                .stream()
                                .forEach(ds -> {
                                    Quest q = new Quest(ds.toObject(QuestData.class));
                                    questList.add(q);
                                });
                    }
                });
    }






}