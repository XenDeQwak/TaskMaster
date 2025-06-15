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

        createQuestButton = topBar.getCreateObjectButton();

        // Create Quest
        createQuestButton.setOnClickListener(v -> {
            HashMap<String, Object> questData = new HashMap<>();
            questData.put("Name", "testquest");
            questData.put("Description", "I am a test quest");
            questData.put("StartDate", 2025000000000L);
            questData.put("DeadlineDate", 202536586340L);
            questData.put("RewardStat", Stats.STRENGTH);
            questData.put("CreatorReference", FirestoreManager.getFirestore().collection("Users").document("test"));
            questData.put("CreatorUID", "test");
            Quest q = QuestManager.parseQuestData(questData);
            questManager.addQuest(q);
            FirestoreManager.uploadQuest(AuthManager.getAuth().getUid(), q);
        });

        questManager = new QuestManager();
        questManager.loadQuestsFromFirestore();
    }
}