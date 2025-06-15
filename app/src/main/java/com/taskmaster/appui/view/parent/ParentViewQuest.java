package com.taskmaster.appui.view.parent;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.taskmaster.appui.R;
import com.taskmaster.appui.entity.Quest;
import com.taskmaster.appui.manager.firebasemanager.AuthManager;
import com.taskmaster.appui.entity.enums.Stats;
import com.taskmaster.appui.manager.entitymanager.QuestManager;
import com.taskmaster.appui.manager.firebasemanager.FirestoreManager;
import com.taskmaster.appui.view.uimodule.TopBar;

import java.util.HashMap;

public class ParentViewQuest extends ParentView {

    QuestManager questManager;
    ImageView createQuestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_parent_quest_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initNavigationMenu(this, ParentViewQuest.class);

        // Initialize createQuestButton
        createQuestButton = topBar.getCreateObjectButton();
        createQuestButton.setOnClickListener(v -> {
            //System.out.println("I AM PRESSED");
            Quest q = QuestManager.createTestQuest();
            questManager.addQuest(q);
            FirestoreManager.uploadQuest(q);
        });

        questManager = new QuestManager();
        questManager.loadQuestsFromFirestore();
    }
}