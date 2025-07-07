package com.taskmaster.appui.manager.entitymanager;

import static android.view.View.GONE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.taskmaster.appui.entity.Quest;
import com.taskmaster.appui.entity.User;
import com.taskmaster.appui.manager.firebasemanager.AuthManager;
import com.taskmaster.appui.manager.firebasemanager.FirestoreManager;
import com.taskmaster.appui.util.DateTimeUtil;
import com.taskmaster.appui.view.uimodule.ChildExemptionTab;
import com.taskmaster.appui.view.uimodule.EditQuestTab;
import com.taskmaster.appui.view.uimodule.QuestView;
import com.taskmaster.appui.view.uimodule.QuestViewPreview;

import java.util.ArrayList;
import java.util.Arrays;
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
                (String) qd.get("AssignedUID"),
                (Long) qd.get("StartDate"),
                (Long) qd.get("EndDate"),
                (Long) qd.get("CompletedDate"),
                (String) qd.get("RewardStat"),
                (String) qd.get("RewardExtra"),
                (DocumentReference) qd.get("CreatorRef"),
                (DocumentReference) qd.get("AssignedRef"),
                (Number) qd.get("Difficulty"),
                (String) qd.get("Status"),
                (String) qd.get("Reason")
                );
    }

    public static HashMap<String, Object> packQuestData (Quest q) {
        HashMap<String, Object> qd = new HashMap<>();
        qd.put("QuestID", q.getQuestID());
        qd.put("Name", q.getName());
        qd.put("Description", q.getDescription());
        qd.put("CreatorUID", q.getCreatorUID());
        qd.put("AssignedUID", q.getAssignedUID());
        qd.put("StartDate", q.getStartDate());
        qd.put("EndDate", q.getEndDate());
        qd.put("CompletedDate", q.getCompletedDate());
        qd.put("RewardStat", q.getRewardStat());
        qd.put("RewardExtra", q.getRewardExtra());
        qd.put("CreatorRef", q.getCreatorReference());
        qd.put("AssignedRef", q.getAssignedReference());
        qd.put("Difficulty", q.getDifficulty());
        qd.put("Status", q.getStatus());
        qd.put("Reason", q.getReason());

        return qd;
    }

    public static Quest createTestQuest () {
        HashMap<String, Object> qd = new HashMap<>();
        qd.put("Name", "testquest");
        qd.put("Description", "I am a test quest.");
        qd.put("CreatorUID", FirestoreManager.getFirestore().collection("Users").document("user").getId());
        qd.put("CreatorRef", FirestoreManager.getFirestore().collection("Users").document("user"));
        qd.put("StartDate", 1735689601L);
        qd.put("EndDate", 1767225599L);
        qd.put("CompletedDate", 1735689601L);
        qd.put("RewardStat", "DEFAULT");
        qd.put("RewardExtra", "test");
        qd.put("AssignedUID", "child");
        qd.put("AssignedRef", FirestoreManager.getFirestore().collection("Childs").document("child"));
        qd.put("Difficulty", 0);
        qd.put("Status", "Ongoing");
        qd.put("Reason", "This is a reason.");

        return QuestManager.parseQuestData(qd);
    }

    public static Quest createBlankQuest () {
        User user = User.getInstance();
        HashMap<String, Object> qd = new HashMap<>();
        qd.put("Name", "Blank Quest");
        qd.put("Description", "No Description");
        qd.put("CreatorUID", user.getDocumentSnapshot().getId());
        qd.put("CreatorRef", user.getDocumentSnapshot().getReference());
        qd.put("StartDate", 1735689601L);
        qd.put("EndDate", 1767225599L);
        qd.put("CompletedDate", 1735689601L);
        qd.put("RewardStat", "None");
        qd.put("RewardExtra", "None");
        qd.put("AssignedUID", "child");
        qd.put("AssignedRef", FirestoreManager.getFirestore().collection("Childs").document("child"));
        qd.put("Difficulty", 0L);
        qd.put("Status", "Awaiting Configuration");
        qd.put("Reason", "None");

        return QuestManager.parseQuestData(qd);
    }

    @SuppressLint("RestrictedApi")
    public static void failQuest (Quest q) {
        q.setStatus("Failed");
        FirestoreManager.getFirestore().document("Quests/"+q.getQuestID())
                .set(QuestManager.packQuestData(q));

    }


    public void loadCreatedQuestWhereStatus(LinearLayout scrollContent, EditQuestTab editQuest, String... status) {
        questList.clear();
        scrollContent.removeAllViews();
        FirebaseUser parent = AuthManager.getAuth().getCurrentUser();
        FirestoreManager.fetchCreatedQuestsWhereStatus(parent.getUid(), Arrays.asList(status), dsl -> {
            for (DocumentSnapshot ds : dsl) {
                Quest q = QuestManager.parseQuestData((HashMap<String, Object>) ds.getData());
                questList.add(q);

                QuestViewPreview qvp = new QuestViewPreview(scrollContent.getContext());
                scrollContent.addView(qvp);
                qvp.setQuest(q, true);
                if (q.getStatus().equalsIgnoreCase("ongoing")) {
                    qvp.setTimer(DateTimeUtil.getDateTimeFromEpochSecond(q.getEndDate()));
                }

            }
        });
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    public void loadAssignedQuestsWhereStatus(LinearLayout scrollContent, String... status) {
        questList.clear();
        scrollContent.removeAllViews();
        FirebaseUser child = AuthManager.getAuth().getCurrentUser();
        FirestoreManager.fetchAssignedQuestsWhereStatus(child.getUid(), Arrays.asList(status), dsl -> {
            for (DocumentSnapshot ds : dsl) {
                Quest q = QuestManager.parseQuestData((HashMap<String, Object>) ds.getData());
                questList.add(q);

                QuestViewPreview qvp = new QuestViewPreview(scrollContent.getContext());
                scrollContent.addView(qvp);
                qvp.setQuest(q, false);
                if (q.getStatus().equalsIgnoreCase("ongoing")) {
                    qvp.setTimer(DateTimeUtil.getDateTimeFromEpochSecond(q.getEndDate()));
                }

            }
        });
    }

    public void loadAssignedQuestHistoryWhereStatus(LinearLayout scrollContent, ChildExemptionTab childExemption, String... status) {
        questList.clear();
        scrollContent.removeAllViews();
        FirebaseUser child = AuthManager.getAuth().getCurrentUser();
        FirestoreManager.fetchAssignedQuestsWhereStatus(child.getUid(), Arrays.asList(status),dsl -> {
            for (DocumentSnapshot ds : dsl) {
                Quest q = QuestManager.parseQuestData((HashMap<String, Object>) ds.getData());
                questList.add(q);

                QuestView qb = new QuestView(scrollContent.getContext(), false);
                scrollContent.addView(qb);

            }
        });
    }

}
