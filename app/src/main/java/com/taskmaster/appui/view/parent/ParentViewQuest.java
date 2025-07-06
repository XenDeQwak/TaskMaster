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
import com.taskmaster.appui.entity.Quest;
import com.taskmaster.appui.manager.entitymanager.ChildManager;
import com.taskmaster.appui.manager.entitymanager.QuestManager;
import com.taskmaster.appui.manager.firebasemanager.FirestoreManager;
import com.taskmaster.appui.view.uimodule.EditQuestTab;

public class ParentViewQuest extends ParentView {

    QuestManager questManager;
    ChildManager childManager;
    ImageView createQuestButton;
    ScrollView questScrollView;
    LinearLayout questScrollContent;
    EditQuestTab editQuest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_parent_view_quest);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.statContainer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initNavigationMenu(this, ParentViewQuest.class);

        questScrollContent = findViewById(R.id.pvq_scrollContent);

        editQuest = findViewById(R.id.pvq_editTab);

        questManager = new QuestManager();
        questManager.loadQuestsFromFirestore(this, questScrollContent, editQuest);

        // Initialize createQuestButton
        createQuestButton = topBar.getCreateObjectButton();
        createQuestButton.setOnClickListener(v -> {
            //System.out.println("I AM PRESSED IN PARENTVIEWQUEST");
            Quest q = QuestManager.createBlankQuest();
            FirestoreManager.uploadQuest(q);
            questManager.loadQuestsFromFirestore(this, questScrollContent, editQuest);
        });

        findViewById(R.id.pvq_scrollView).getBackground().setAlpha(150);

    }
}