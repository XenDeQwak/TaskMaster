package com.taskmaster.appui.view.child;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.taskmaster.appui.R;
import com.taskmaster.appui.entity.User;
import com.taskmaster.appui.manager.entitymanager.QuestManager;
import com.taskmaster.appui.view.uimodule.ChildStatsTab;
import com.taskmaster.appui.view.uimodule.ViewQuestTab;

import java.util.ArrayList;
import java.util.List;

public class ChildViewQuest extends ChildView {

    QuestManager questManager;
    LinearLayout questViewChild;
    ViewQuestTab editQuestTab;

    List<Integer> avatarImages = new ArrayList<>();

    private void setUpAvatar(){
        avatarImages.add(R.drawable.placeholderavatar5_framed_round);
        avatarImages.add(R.drawable.placeholderavatar1_framed_round);
        avatarImages.add(R.drawable.placeholderavatar2_framed_round);
        avatarImages.add(R.drawable.placeholderavatar3_framed_round);
        avatarImages.add(R.drawable.placeholderavatar4_framed_round);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_child_view_quest);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cvlb_container), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initNavigationMenu(this, ChildViewQuest.class);

        setUpAvatar();
        User u = User.getInstance();
        ((ChildStatsTab)findViewById(R.id.ChildStatsTab)).getChildStatsName().setText((String)u.getDocumentSnapshot().get("Username"));
        ((ChildStatsTab)findViewById(R.id.ChildStatsTab)).getChildStatsFloor().setText("Floor: " + u.getDocumentSnapshot().get("Floor").toString());
        ((ChildStatsTab)findViewById(R.id.ChildStatsTab)).getChildStatsAvatarImage().setImageResource(avatarImages.get(u.getDocumentSnapshot().getDouble("Avatar").intValue()));

        ChildStatsTab stats = findViewById(R.id.ChildStatsTab);
        stats.setProgressionNav(this);

        findViewById(R.id.questScrollViewChild).getBackground().setAlpha(150);
        editQuestTab = findViewById(R.id.pvq_editTab);

        questViewChild = findViewById(R.id.questViewChild);
        questManager = new QuestManager();
        String[] status = {"Ongoing", "Awaiting Reason", "Awaiting Exemption"};
        questManager.loadAssignedQuestsWhereStatus(questViewChild, status);

        findViewById(R.id.questScrollViewChild).getBackground().setAlpha(160);

    }
}