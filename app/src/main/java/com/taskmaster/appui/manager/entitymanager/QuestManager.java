package com.taskmaster.appui.manager.entitymanager;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.taskmaster.appui.entity.Quest;
import com.taskmaster.appui.entity.enums.Stats;
import com.taskmaster.appui.manager.firebasemanager.AuthManager;
import com.taskmaster.appui.manager.firebasemanager.FirestoreManager;

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
                (String) qd.get("Name"),
                (String) qd.get("Description"),
                (String) qd.get("CreatorUID"),
                (DocumentReference) qd.get("CreatorRef"),
                (Long) qd.get("StartDate"),
                (Long) qd.get("EndDate"),
                (Stats) qd.get("RewardStat"),
                (String) qd.get("RewardExtra")
                );
    }

    public static HashMap<String, Object> packQuestData (Quest q) {
        HashMap<String, Object> qd = new HashMap<>();
        qd.put("Name", q.getName());
        qd.put("Description", q.getDescription());
        qd.put("CreatorUID", q.getCreatorUID());
        qd.put("CreatorRef", q.getCreatorReference());
        qd.put("StartDate", q.getStartDate());
        qd.put("EndDate", q.getEndDate());
        qd.put("RewardStat", q.getRewardStat());
        qd.put("RewardExtra", q.getRewardExtra());

        return qd;
    }

    public static Quest createTestQuest () {
        HashMap<String, Object> qd = new HashMap<>();
        qd.put("Name", "testquest");
        qd.put("Description", "I am a test quest");
        qd.put("CreatorUID", FirestoreManager.getFirestore().collection("Users").document("user").getId());
        qd.put("CreatorRef", FirestoreManager.getFirestore().collection("Users").document("user"));
        qd.put("StartDate", 2025000000000L);
        qd.put("EndDate", 202536586340L);
        qd.put("RewardStat", Stats.STRENGTH);
        qd.put("RewardExtra", "test");
        Quest q = QuestManager.parseQuestData(qd);
        return q;
    }

    public void loadQuestsFromFirestore () {
        questList.clear();
        FirebaseUser parent = AuthManager.getAuth().getCurrentUser();
        FirestoreManager.fetchQuests(parent.getUid(), dsl -> {
            for (DocumentSnapshot ds : dsl) {
                Quest q = QuestManager.parseQuestData((HashMap<String, Object>) ds.getData());
                questList.add(q);
            }
        });
    }

}
