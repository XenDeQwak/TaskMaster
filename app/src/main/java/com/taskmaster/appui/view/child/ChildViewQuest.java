package com.taskmaster.appui.view.child;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.taskmaster.appui.R;
import com.taskmaster.appui.manager.entitymanager.QuestManager;

public class ChildViewQuest extends ChildView {

    QuestManager questManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_child_view_quest);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.statContainer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initNavigationMenu(this, ChildViewQuest.class);

        questManager = new QuestManager();
        questManager.loadQuestsFromFirestore();

    }
}