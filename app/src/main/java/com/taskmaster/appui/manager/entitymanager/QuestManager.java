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

    public static Quest parseQuestData (HashMap<String, Object> questData) {
        Quest q = new Quest();
        q.setName((String) questData.get("Name"));
        q.setDescription((String) questData.get("Description"));
        q.setCreatorUID((String) questData.get("CreatorUID"));
        q.setStartDate((Long) questData.get("StartDate"));
        q.setEndDate((Long) questData.get("EndDate"));
        q.setRewardStat((Stats) questData.get("RewardStat"));
        q.setRewardExtra((String) questData.get("RewardExtra"));

        return q;
    }

    public static Quest createTestQuest () {
        HashMap<String, Object> qd = new HashMap<>();
        qd.put("Name", "testquest");
        qd.put("Description", "I am a test quest");
        qd.put("CreatorUID", 2025000000000L);
        qd.put("StartDate", 202536586340L);
        qd.put("EndDate", Stats.STRENGTH);
        qd.put("RewardStat", FirestoreManager.getFirestore().collection("Users").document("test"));
        qd.put("RewardExtra", "test");
        Quest q = QuestManager.parseQuestData(qd);
        return q;
    }

    public static HashMap<String, Object> packQuestData (Quest quest) {
        HashMap<String, Object> qd = new HashMap<>();
        qd.put("Name", quest.getName());
        qd.put("Description", quest.getDescription());
        qd.put("CreatorUID", quest.getCreatorUID());
        qd.put("StartDate", quest.getStartDate());
        qd.put("EndDate", quest.getEndDate());
        qd.put("RewardStat", quest.getRewardStat());
        qd.put("RewardExtra", quest.getRewardExtra());

        return qd;
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
