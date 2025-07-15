package com.taskmaster.appui.manager.entitymanager;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.taskmaster.appui.data.ChildData;
import com.taskmaster.appui.data.QuestData;
import com.taskmaster.appui.entity.CurrentUser;
import com.taskmaster.appui.entity.Quest;
import com.taskmaster.appui.view.uimodule.QuestBox;
import com.taskmaster.appui.view.uimodule.QuestBoxPreview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestManager {

    private final DocumentReference Parent =
            (CurrentUser.getInstance().getUserData().getRole().equalsIgnoreCase("parent"))
                    ?
                    FirebaseFirestore.getInstance().document("Users/"+FirebaseAuth.getInstance().getUid())
                    :
                    CurrentUser.getInstance().getUserData().getUserSnapshot().toObject(ChildData.class).getParentReference()
            ;
    private final CollectionReference Quests = Parent.collection("Quests");
    private final LinearLayout questContent;
    private final ArrayList<Quest> questList;

    public QuestManager (LinearLayout questContent) {
        this.questContent = questContent;
        this.questList = new ArrayList<>();
    }

    @SuppressLint("NewApi")
    public void refresh () {
        questContent.removeAllViews();
        questList.stream()
                .map(Quest::getQuestBoxPreview)
                .forEach(questContent::addView);
        questList.stream()
                .map(Quest::getQuestBox)
                .forEach(qb -> {
                    ViewGroup parent = (ViewGroup) questContent.getParent().getParent();
                    parent.removeView(qb);
                    ConstraintLayout.LayoutParams qvParams = new ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.MATCH_PARENT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT
                    );
                    qvParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
                    qvParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
                    qvParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
                    qvParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
                    qvParams.setMargins(16,16,16,16);
                    qb.setLayoutParams(qvParams);
                    qb.setClickable(true);
                    qb.setVisibility(GONE);
                    parent.addView(qb);
                });
        System.out.println("Refreshed");
    }

    public void create () {
        Quest q = new Quest(QuestData.newBlankQuestData(), questContent.getContext(), this);
        Quests.add(q.getQuestData()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("Debug", "Created Blank Quest");
                DocumentReference dr = task.getResult();
                q.getQuestData().setQuestReference(dr);
                q.getQuestData().setId(dr.getId());
                q.getQuestData().setCreatedBy(FirebaseAuth.getInstance().getUid());
                q.getQuestData().setCreatorReference(Parent);
                q.getQuestData().uploadData();
                questList.add(q);
            } else {
                task.getException().printStackTrace();
            }
        });
    }

    public void remove (Quest q) {
        if (questList.contains(q)) {
            questList.remove(q);
        } else {
            //throw new IllegalArgumentException("Quest is not part of QuestList");
        }
    }

    /**
     * Fetches quests from the Firestore collection based on the given currentUser type and quest statuses.
     *
     * @param type   Specifies the currentUser type. If "parent", it queries quests where the current currentUser is the creator;
     *               otherwise, it queries quests assigned to the current currentUser.
     * @param status One or more status values (e.g., "ongoing", "completed", "failed") to filter quests by.
     */
    public void fetchQuestsWhereStatus (String type, String... status) {
        questList.forEach(quest -> {
            quest.getQuestBox().setVisibility(GONE);
            quest.updateQuestBox();
        });
        String field = type.equalsIgnoreCase("parent") ? "createdBy" : "assignedTo";
        Quests
                .whereEqualTo(field, FirebaseAuth.getInstance().getUid())
                .whereIn("status", Arrays.asList(status))
                .get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) task.getException().printStackTrace();
                    else {
                        questList.clear();
                        //System.out.println(task.getResult().getDocuments());;
                        task.getResult().getDocuments()
                                .stream()
                                .forEach(ds -> {
                                    //System.out.println("Name: " + ds.get("name"));
                                    Quest q = new Quest(ds.toObject(QuestData.class), questContent.getContext(), this);
                                    questList.add(q);
                                });
                        //System.out.println(field + ": " + FirebaseAuth.getInstance().getUid());
                        //System.out.println(questList);
                        refresh();
                    }
                });
    }






}