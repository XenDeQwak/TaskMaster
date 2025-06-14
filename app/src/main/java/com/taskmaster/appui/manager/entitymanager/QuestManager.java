package com.taskmaster.appui.manager.entitymanager;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.taskmaster.appui.entity.Quest;
import com.taskmaster.appui.entity.enums.Stats;
import com.taskmaster.appui.manager.firebasemanager.AuthManager;
import com.taskmaster.appui.manager.firebasemanager.FirestoreManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class QuestManager {

    List<Quest> QuestList;

    public QuestManager (List<Quest> QuestList) {
        this.QuestList = QuestList;
    }

    public QuestManager () {
        this.QuestList = new ArrayList<>();
    }

    public List<Quest> getQuestList() {
        return QuestList;
    }

    public void addQuest (Quest quest) {
        QuestList.add(quest);
    }

    public void removeQuest (Quest quest) {
        QuestList.remove(quest);
    }

    public static Quest parseQuestData (HashMap<String, Object> questData) {
        Quest q = new Quest();
        q.setName((String) questData.get("Name"));
        q.setDescription((String) questData.get("Description"));
        q.setStartDate((long) questData.get("StartDate"));
        q.setDeadlineDate((long) questData.get("DeadlineDate"));
        q.setRewardStat((Stats) questData.get("RewardStat"));
        q.setCreatorReference((DocumentReference) questData.get("CreatorReference"));
        q.setCreatorUID((String) questData.get("CreatorUID"));

        return q;
    }

    public static HashMap<String, Object> packQuestData (Quest quest) {
        HashMap<String, Object> qd = new HashMap<>();
        qd.put("Name", quest.getName());
        qd.put("Description", quest.getDescription());
        qd.put("StartDate", quest.getStartDate());
        qd.put("DeadlineDate", quest.getDeadlineDate());
        qd.put("RewardStat", quest.getRewardStat());
        qd.put("CreatorReference", quest.getCreatorReference());
        qd.put("CreatorUID", quest.getCreatorUID());

        return qd;
    }

    public void loadQuestsFromFirestore () {
        QuestList.clear();
        FirebaseUser parent = AuthManager.getAuth().getCurrentUser();
        FirestoreManager.fetchQuests(parent.getUid(), dsl -> {
            for (DocumentSnapshot ds : dsl) {
                Quest q = QuestManager.parseQuestData((HashMap<String, Object>) ds.getData());
                QuestList.add(q);
            }
        });
    }

}
