package com.taskmaster.appui.view.child;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.taskmaster.appui.R;
import com.taskmaster.appui.manager.entitymanager.QuestManager;
import com.taskmaster.appui.view.uimodule.ChildExemptionTab;

public class ChildPageQuestHistory extends ChildPage {

    QuestManager questManager;
    ScrollView cvqh_scrollView;
    LinearLayout cvqh_scrollContent;
    ChildExemptionTab cvqh_childExemptionTab;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_child_view_quest_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //questManager = new QuestManager();

        initNavigationMenu(this, ChildPageQuestHistory.class);

        cvqh_scrollView = findViewById(R.id.cvqh_scrollView);
        //cvqh_scrollView.getBackground().setAlpha(150);

        cvqh_scrollContent = findViewById(R.id.cvqh_scrollContent);
        cvqh_childExemptionTab = findViewById(R.id.cvqh_childExemptionTab);
        String[] status = {"Completed", "Failed", "Exempted"};
        //questManager.loadAssignedQuestHistoryWhereStatus(cvqh_scrollContent, cvqh_childExemptionTab, status);
    }
}