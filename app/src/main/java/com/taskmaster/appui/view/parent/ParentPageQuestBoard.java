package com.taskmaster.appui.view.parent;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.taskmaster.appui.R;
import com.taskmaster.appui.data.ChildData;
import com.taskmaster.appui.entity.Child;
import com.taskmaster.appui.entity.CurrentUser;
import com.taskmaster.appui.entity.Quest;
import com.taskmaster.appui.manager.entitymanager.ChildManager;
import com.taskmaster.appui.manager.entitymanager.QuestManager;
import com.taskmaster.appui.manager.firebasemanager.FirestoreManager;
import com.taskmaster.appui.view.uimodule.EditQuestTab;

public class ParentPageQuestBoard extends ParentPage {

    private LinearLayout questScrollContent;
    private EditQuestTab editQuest;
    private QuestManager questManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_parent_view_quest);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cvlb_container), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initNavigationMenu(this, ParentPageQuestBoard.class);

        questScrollContent = findViewById(R.id.pvq_scrollContent);
        questManager = new QuestManager(questScrollContent);

        // Initialize createQuestButton
        ImageView createQuestButton = topBar.getCreateObjectButton();
        createQuestButton.setOnClickListener(v -> {
            questManager.create();
        });

        String[] status = {"Ongoing", "Awaiting Configuration", "Awaiting Verification", "Awaiting Exemption"};
        questManager.fetchQuestsWhereStatus("parent", status);

        CurrentUser currentUser = CurrentUser.getInstance();
        currentUser.getUserData().getUserSnapshot().getReference().collection("Quests")
                .addSnapshotListener((qs, e) -> {
                    questManager.fetchQuestsWhereStatus("parent", status);
                });
    }
}