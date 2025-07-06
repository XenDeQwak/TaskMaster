package com.taskmaster.appui.manager.entitymanager;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.taskmaster.appui.R;
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
        ((Activity)q.getQb().getContext()).runOnUiThread(() -> q.getQb().setVisibility(GONE));
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
                qvp.setTimer(DateTimeUtil.getDateTimeFromEpochSecond(q.getEndDate()));

//                if (editQuest != null) {
//                    qb.getViewQuestButtonC().setOnClickListener(v -> {
//                        editQuest.setVisibility(VISIBLE);
//                        editQuest.setQuest(q);
//                    });
//                }
//
//                qb.getViewQuestButtonB().setOnClickListener(v -> {
//                    qb.setVisibility(GONE);
//                    q.setStatus("Deleted");
//                    FirestoreManager.getFirestore().collection("Quests")
//                            .document(q.getQuestID())
//                            .update("Status", "Deleted")
//                            .addOnCompleteListener(task -> {
//                                if (task.isSuccessful()) {
//                                    Log.d("Debug", "Successfully deleted quest");
//                                } else {
//                                    Log.d("Debug", "Failed to deleted quest");
//                                }
//                            });
//                });
//
//                if (q.getStatus().equalsIgnoreCase("Awaiting Verification")) {
//                    qb.getViewQuestButtonA().setOnClickListener(v -> {
//                        qb.setVisibility(GONE);
//                        q.setStatus("Completed");
//                        FirestoreManager.getFirestore().collection("Quests")
//                                .document(q.getQuestID())
//                                .update("Status", "Completed")
//                                .addOnCompleteListener(task -> {
//                                    if (task.isSuccessful()) {
//                                        Log.d("Debug", "Successfully completed quest verification");
//                                        DocumentReference child = q.getAssignedReference();
//                                        if (q.getRewardStat().equalsIgnoreCase("strength")) {
//                                            child.update("Strength", FieldValue.increment(1));
//                                        } else if (q.getRewardStat().equalsIgnoreCase("intelligence")) {
//                                            child.update("Intelligence", FieldValue.increment(1));
//                                        }
//                                        child.update("Gold", FieldValue.increment(q.getDifficulty().intValue()));
//                                        child.update("QuestCompleted", FieldValue.increment(1));
//                                        Log.d("Debug", "Awareed " + q.getRewardStat() + " stat to child");
//                                        Log.d("Debug", "Awareed " + q.getDifficulty().intValue() + " gold to child");
//                                        HashMap<String, Object> map = new HashMap<>();
//                                        // Store a reference for all completed quests
//                                        map.put("QuestReference", FirestoreManager.getFirestore().document("Quests/"+q.getQuestID()));
//                                        q.getAssignedReference().collection("CompletedQuests").document(q.getQuestID()).set(map);
//                                    } else {
//                                        Log.d("Debug", "Failed to complete quest verification");
//                                    }
//                                });
//                    });
//                } else { // "#ADADADFF"
//                    qb.getViewQuestButtonA().setBackgroundTintList(scrollContent.getContext().getResources().getColorStateList(R.color.unavailable));
//                }

            }
        });
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    public void loadQuestsFromFirestoreChild (LinearLayout scrollContent) {
        questList.clear();
        scrollContent.removeAllViews();
        FirebaseUser child = AuthManager.getAuth().getCurrentUser();
        FirestoreManager.fetchQuestsAssignee(child.getUid(), dsl -> {
            for (DocumentSnapshot ds : dsl) {
                Quest q = QuestManager.parseQuestData((HashMap<String, Object>) ds.getData());
                questList.add(q);

                QuestViewPreview qvp = new QuestViewPreview(scrollContent.getContext());
                scrollContent.addView(qvp);
                qvp.setQuest(q, false);
                qvp.setTimer(DateTimeUtil.getDateTimeFromEpochSecond(q.getEndDate()));

//                if (q.getStatus().equalsIgnoreCase("Ongoing")) {
//                    qb.getViewQuestButtonA().setOnClickListener(v -> {
//                        q.setStatus("Awaiting Verification");
//                        q.setCompletedDate(DateTimeUtil.getDateTimeNow().toEpochSecond());
//                        FirestoreManager.getFirestore().collection("Quests")
//                                .document(q.getQuestID())
//                                .set(QuestManager.packQuestData(q))
//                                .addOnCompleteListener(task -> {
//                                    if (task.isSuccessful()) {
//                                        Log.d("Debug", "Successfully submitted quest for verification");
//                                    } else {
//                                        Log.d("Debug", "Failed to submit quest for verification");
//                                    }
//                                });
//                    });
//                } else { // "#ADADADFF"
//                    qb.getViewQuestButtonA().setText("Awaiting Verification...");
//                    qb.getViewQuestButtonA().setBackgroundTintList(scrollContent.getContext().getResources().getColorStateList(R.color.unavailable));
//                }
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
                //q.setQb(qb);

//                String questStatus = q.getStatus();
//                if (questStatus.equalsIgnoreCase("completed")) {
//                    qb.getViewQuestButtonA().setVisibility(GONE);
//                    qb.getViewQuestButtonB().setVisibility(GONE);
//                    qb.getViewQuestButtonC().setVisibility(GONE);
//                } else if (questStatus.equalsIgnoreCase("failed")) {
//                    qb.getViewQuestButtonA().setText("Request Exemption");
//                    qb.getViewQuestButtonB().setVisibility(GONE);
//                    qb.getViewQuestButtonC().setVisibility(GONE);
//
//                    qb.getViewQuestButtonA().setOnClickListener(v -> {
//                        childExemption.setVisibility(VISIBLE);
//                        childExemption.setQuest(q, e -> {
//                            loadAssignedQuestHistoryWhereStatus(scrollContent, childExemption, status);
//                        });
//                    });
//                } else if (questStatus.equalsIgnoreCase("awaiting exemption")) {
//                    qb.getViewQuestButtonA().setText("Awaiting Exemption");
//                    qb.getViewQuestButtonB().setVisibility(GONE);
//                    qb.getViewQuestButtonC().setVisibility(GONE);
//                }
            }
        });
    }

}
