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
import com.taskmaster.appui.data.ChildData;
import com.taskmaster.appui.entity.Child;
import com.taskmaster.appui.entity.CurrentUser;
import com.taskmaster.appui.manager.entitymanager.QuestManager;
import com.taskmaster.appui.view.uimodule.ChildExemptionTab;

public class ChildPageQuestHistory extends ChildPage {

    QuestManager questManager;
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



        initNavigationMenu(this, ChildPageQuestHistory.class);

        cvqh_scrollContent = findViewById(R.id.cvqh_scrollContent);
        questManager = new QuestManager(cvqh_scrollContent);

        String[] status = {"Completed", "Failed", "Exempted"};
        questManager.fetchQuestsWhereStatus("child", status);

        CurrentUser currentUser = CurrentUser.getInstance();
        currentUser.getUserData().getUserSnapshot().getReference().get()
                .addOnCompleteListener(ds -> {
                    Child c = new Child(ds.getResult().toObject(ChildData.class));
                    c.getChildData().getParentReference().collection("Quests")
                            .addSnapshotListener((qs, e) -> {
                                questManager.fetchQuestsWhereStatus("child", status);
                            });
                });
    }
}