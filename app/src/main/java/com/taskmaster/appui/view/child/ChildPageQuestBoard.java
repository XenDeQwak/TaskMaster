package com.taskmaster.appui.view.child;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.taskmaster.appui.R;
import com.taskmaster.appui.data.ChildData;
import com.taskmaster.appui.entity.Child;
import com.taskmaster.appui.entity.CurrentUser;
import com.taskmaster.appui.manager.entitymanager.QuestManager;
import com.taskmaster.appui.view.uimodule.ChildStatsTab;

import org.checkerframework.checker.units.qual.C;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class ChildPageQuestBoard extends ChildPage {

    QuestManager questManager;
    LinearLayout questViewChild;
    ChildStatsTab childStatsTab;

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

        initNavigationMenu(this, ChildPageQuestBoard.class);

        setUpAvatar();
        CurrentUser user = CurrentUser.getInstance();
        DocumentSnapshot documentSnapshot = user.getUserData().getUserSnapshot();
        int avatarIndex = documentSnapshot.exists()? documentSnapshot.get("avatar", Integer.class) : 0;

        childStatsTab = findViewById(R.id.ChildStatsTab);
        childStatsTab.getChildStatsName().setText(user.getUserData().getUsername());
        childStatsTab.getChildStatsFloor().setText("Floor: " + user.getUserData().getUserSnapshot().get("floor"));
        childStatsTab.getChildStatsAvatarImage().setImageResource(avatarImages.get(avatarIndex));
        childStatsTab.setProgressionNav(this);

        questViewChild = findViewById(R.id.questViewChild);
        questManager = new QuestManager(questViewChild);

        String[] status = {"Ongoing", "Awaiting Reason For Failure"};
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