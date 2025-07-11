package com.taskmaster.appui.manager.entitymanager;

import android.annotation.SuppressLint;
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
import com.taskmaster.appui.view.uimodule.QuestBoxPreview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class QuestManager {

    LinearLayout questContent;

    public QuestManager (LinearLayout questContent) {
        this.questContent = questContent;
    }


}