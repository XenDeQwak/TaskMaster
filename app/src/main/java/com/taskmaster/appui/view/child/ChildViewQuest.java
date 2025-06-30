package com.taskmaster.appui.view.child;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.taskmaster.appui.R;
import com.taskmaster.appui.manager.entitymanager.QuestManager;
import com.taskmaster.appui.view.uimodule.ChildStatsTab;

public class ChildViewQuest extends ChildView {

    QuestManager questManager;
    LinearLayout questView;

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

        ChildStatsTab stats = findViewById(R.id.ChildStatsTab);
        stats.setProgressionNav(this);

        findViewById(R.id.questScrollViewChild).getBackground().setAlpha(150);

        questView = findViewById(R.id.questViewChild);
        questManager = new QuestManager();
        questManager.loadQuestsFromFirestoreParent(this, questView, null);

    }
}