package com.taskmaster.appui.manager.entitymanager;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.taskmaster.appui.entity.Quest;
import com.taskmaster.appui.entity.User;
import com.taskmaster.appui.manager.firebasemanager.AuthManager;
import com.taskmaster.appui.manager.firebasemanager.FirestoreManager;
import com.taskmaster.appui.view.uimodule.EditQuestTab;
import com.taskmaster.appui.view.uimodule.QuestBox;
import com.taskmaster.appui.view.uimodule.ViewQuestTab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuestManager {

    ArrayList<Quest> questList;

    public QuestManager () {
        this(new ArrayList<>());
    }

    public QuestManager (ArrayList<Quest> QuestList) {
        this.questList = QuestList;
    }

    public List<Quest> getQuestList() {
        return questList;
    }

    public void addQuest (Quest quest) {
        questList.add(quest);
    }

    public void removeQuest (Quest quest) {
        questList.remove(quest);
    }

    public static Quest parseQuestData (HashMap<String, Object> qd) {
        return new Quest(
                (String) qd.get("QuestID"),
                (String) qd.get("Name"),
                (String) qd.get("Description"),
                (String) qd.get("CreatorUID"),
                (DocumentReference) qd.get("CreatorRef"),
                (Long) qd.get("StartDate"),
                (Long) qd.get("EndDate"),
                (String) qd.get("RewardStat"),
                (String) qd.get("RewardExtra"),
                (String) qd.get("AssignedUID"),
                (DocumentReference) qd.get("AssignedRef"),
                (Long) qd.get("Difficulty")
                );
    }

    public static HashMap<String, Object> packQuestData (Quest q) {
        HashMap<String, Object> qd = new HashMap<>();
        qd.put("QuestID", q.getQuestID());
        qd.put("Name", q.getName());
        qd.put("Description", q.getDescription());
        qd.put("CreatorUID", q.getCreatorUID());
        qd.put("CreatorRef", q.getCreatorReference());
        qd.put("StartDate", q.getStartDate());
        qd.put("EndDate", q.getEndDate());
        qd.put("RewardStat", q.getRewardStat());
        qd.put("RewardExtra", q.getRewardExtra());
        qd.put("AssignedUID", q.getAssignedUID());
        qd.put("AssignedRef", q.getAssignedReference());
        qd.put("Difficulty", q.getDifficulty());

        return qd;
    }

    public static Quest createTestQuest () {
        HashMap<String, Object> qd = new HashMap<>();
        qd.put("Name", "testquest");
        qd.put("Description", "I am a test quest");
        qd.put("CreatorUID", FirestoreManager.getFirestore().collection("Users").document("user").getId());
        qd.put("CreatorRef", FirestoreManager.getFirestore().collection("Users").document("user"));
        qd.put("StartDate", 1735689601L);    // January 1, 2025, 00:00
        qd.put("EndDate", 1767225599L);       // December 31, 2025, 23:59
        qd.put("RewardStat", "DEFAULT");
        qd.put("RewardExtra", "test");
        qd.put("AssignedUID", "child");
        qd.put("AssignedRef", FirestoreManager.getFirestore().collection("Childs").document("child"));
        qd.put("Difficulty", 0);

        return QuestManager.parseQuestData(qd);
    }

    public static Quest createBlankQuest () {
        User user = User.getInstance();
        HashMap<String, Object> qd = new HashMap<>();
        qd.put("Name", "");
        qd.put("Description", "");
        qd.put("CreatorUID", user.getDocumentSnapshot().getId());
        qd.put("CreatorRef", user.getDocumentSnapshot().getReference());
        qd.put("StartDate", 1735689601L);    // January 1, 2025, 00:00
        qd.put("EndDate", 1767225599L);       // December 31, 2025, 23:59
        qd.put("RewardStat", "DEFAULT");
        qd.put("RewardExtra", "");
        qd.put("AssignedUID", "child");
        qd.put("AssignedRef", FirestoreManager.getFirestore().collection("Childs").document("child"));
        qd.put("Difficulty", 0L);

        return QuestManager.parseQuestData(qd);
    }

    public static void updateQuest () {

    }


    public void loadQuestsFromFirestore(Context context, LinearLayout scrollContent, EditQuestTab editQuest) {
        questList.clear();
        scrollContent.removeAllViews();
        FirebaseUser parent = AuthManager.getAuth().getCurrentUser();
        FirestoreManager.fetchQuests(parent.getUid(), dsl -> {
            for (DocumentSnapshot ds : dsl) {
                Quest q = QuestManager.parseQuestData((HashMap<String, Object>) ds.getData());
                questList.add(q);

                QuestBox qb = new QuestBox(context, q);
                //qb.setMinimumHeight(200);
                //qb.setMinimumWidth(200);
                scrollContent.addView(qb);

                if (editQuest != null) {
                    qb.getQuestContainer().setOnClickListener(v -> {
                        editQuest.setVisibility(View.VISIBLE);
                        editQuest.setQuest(q);
                    });
                }
            }
        });
    }

    public void loadQuestsFromFirestoreChild (Context context, LinearLayout scrollContent, ViewQuestTab editQuest) {
        questList.clear();
        scrollContent.removeAllViews();
        FirebaseUser child = AuthManager.getAuth().getCurrentUser();
        FirestoreManager.fetchQuestsAssignee(child.getUid(), dsl -> {
            for (DocumentSnapshot ds : dsl) {
                Quest q = QuestManager.parseQuestData((HashMap<String, Object>) ds.getData());
                questList.add(q);

                QuestBox qb = new QuestBox(context, q);
                //qb.setMinimumHeight(200);
                //qb.setMinimumWidth(200);
                scrollContent.addView(qb);

                if (editQuest != null) {
                    qb.getQuestContainer().setOnClickListener(v -> {
                        editQuest.setVisibility(View.VISIBLE);
                        editQuest.setQuest(q);
                    });
                }
            }
        });
    }

}
