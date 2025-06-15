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
        q.setCreatorUID((String) questData.get("Name"));
        q.setStartDate((Long) questData.get("Name"));
        q.setEndDate((Long) questData.get("Name"));
        q.setRewardStat((Stats) questData.get("Name"));
        q.setRewardExtra((String) questData.get("Name"));
        q.setName((String) questData.get("Name"));

        return q;
    }

    public static HashMap<String, Object> packQuestData (Quest quest) {
        HashMap<String, Object> qd = new HashMap<>();


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
