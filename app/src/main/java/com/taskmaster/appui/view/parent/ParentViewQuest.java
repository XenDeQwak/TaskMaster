package com.taskmaster.appui.view.parent;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.taskmaster.appui.R;
import com.taskmaster.appui.entity.Quest;
import com.taskmaster.appui.manager.entitymanager.QuestManager;
import com.taskmaster.appui.manager.firebasemanager.FirestoreManager;

public class ParentViewQuest extends ParentView {

    QuestManager questManager;
    ImageView createQuestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_parent_view_quest);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initNavigationMenu(this, ParentViewQuest.class);

        questManager = new QuestManager();
        questManager.loadQuestsFromFirestore();

        // Initialize createQuestButton
        createQuestButton = topBar.getCreateObjectButton();
        createQuestButton.setOnClickListener(v -> {
            //System.out.println("I AM PRESSED IN PARENTVIEWQUEST");
            Quest q = QuestManager.createTestQuest();
            questManager.addQuest(q);
            FirestoreManager.uploadQuest(q);
        });

    }
}