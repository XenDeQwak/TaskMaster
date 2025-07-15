package com.taskmaster.appui.view.parent;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.taskmaster.appui.R;
import com.taskmaster.appui.manager.entitymanager.QuestManager;

public class ParentPageQuestHistory extends ParentPage {

    QuestManager questManager;
    LinearLayout pvqh_scrollContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_parent_view_quest_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initNavigationMenu(this, ParentPageQuestHistory.class);

        pvqh_scrollContent = findViewById(R.id.pvqh_scrollContent);
        questManager = new QuestManager(pvqh_scrollContent);

        String[] status = {"Completed", "Failed", "Deleted", "Exempted"};
        questManager.fetchQuestsWhereStatus("parent", status);

    }
}