package com.taskmaster.appui.view.child;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.taskmaster.appui.R;
import com.taskmaster.appui.entity.Child;
import com.taskmaster.appui.entity.User;
import com.taskmaster.appui.manager.entitymanager.ChildManager;
import com.taskmaster.appui.view.uimodule.ChildViewPreview;
import com.taskmaster.appui.view.uimodule.LeaderboardSortMenu;
import com.taskmaster.appui.view.uimodule.NavigationMenu;

import java.util.ArrayList;

public class ChildViewLeaderboard extends ChildView {

    ChildManager childManager;

    ScrollView cvlb_scrollView;
    LinearLayout cvlb_scrollContent;

    TextView goldUsername, silverUsername, bronzeUsername, goldAchievement, silverAchievement, bronzeAchievement;

    NavigationMenu dropdownNavMenu;
    LeaderboardSortMenu dropdownSortMenu;
    ImageView sortByButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_child_view_leaderboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cvlb_container), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        childManager = new ChildManager(this);

        initNavigationMenu(this, ChildViewLeaderboard.class);

        cvlb_scrollView = findViewById(R.id.cvlb_scrollView);
        cvlb_scrollContent = findViewById(R.id.cvlb_scrollContent);

        goldUsername = findViewById(R.id.goldUsername);
        silverUsername = findViewById(R.id.silverUsername);
        bronzeUsername = findViewById(R.id.bronzeUsername);
        goldAchievement = findViewById(R.id.goldAchievement);
        silverAchievement = findViewById(R.id.silverAchievement);
        bronzeAchievement = findViewById(R.id.bronzeAchievement);

        cvlb_scrollView.getBackground().setAlpha(150);

        sortLeaderboardByQuestCompleted();

        dropdownNavMenu = topBar.getDropdownNavMenu();
        dropdownSortMenu = topBar.getDropdownSortMenu();
        sortByButton = topBar.getCreateObjectButton();
        sortByButton.setImageResource(R.drawable.sortby_leaderboard);
        sortByButton.setOnClickListener(v -> {
            dropdownSortMenu.bringToFront();
            if (dropdownSortMenu.getVisibility() == GONE) {
                dropdownSortMenu.setVisibility(VISIBLE);
                dropdownNavMenu.setVisibility(GONE);
            } else {
                dropdownSortMenu.setVisibility(GONE);
            }
        });

        dropdownSortMenu.getSortMenuQuestCompleted().setOnClickListener(v-> {
            sortLeaderboardByQuestCompleted();
            dropdownSortMenu.setVisibility(GONE);
        });

        dropdownSortMenu.getSortMenuFloorReached().setOnClickListener(v-> {
            sortLeaderboardByFloorReached();
            dropdownSortMenu.setVisibility(GONE);
        });

        dropdownSortMenu.getSortMenuHighestStrength().setOnClickListener(v-> {
            sortLeaderboardByHighestStrength();
            dropdownSortMenu.setVisibility(GONE);
        });

        dropdownSortMenu.getSortMenuHighestIntelligence().setOnClickListener(v-> {
            sortLeaderboardByHighestIntelligence();
            dropdownSortMenu.setVisibility(GONE);
        });

    }

    @SuppressLint("NewApi")
    public void sortLeaderboardByQuestCompleted () {
        ArrayList<Child> childrenList = new ArrayList<>();
        User user = User.getInstance();
        String id = user.getDocumentSnapshot().get("ParentUID").toString();
        ChildManager.injectToList(id, childrenList, e -> {
            displayLeaderboard(
                    new ArrayList<>(
                            childrenList
                                    .stream()
                                    .sorted((child1, child2) -> child2.getQuestCompleted().intValue() - child1.getQuestCompleted().intValue())
                                    .toList()
                    ),
                    "Quests"
            );
        });
    }

    @SuppressLint("NewApi")
    public void sortLeaderboardByFloorReached () {
        ArrayList<Child> childrenList = new ArrayList<>();
        User user = User.getInstance();
        String id = user.getDocumentSnapshot().get("ParentUID").toString();
        ChildManager.injectToList(id, childrenList, e -> {
            displayLeaderboard(
                    new ArrayList<>(
                            childrenList
                                    .stream()
                                    .sorted((child1, child2) -> child2.getFloor().intValue() - child1.getFloor().intValue())
                                    .toList()
                    ),
                    "Floor"
            );
        });
    }

    @SuppressLint("NewApi")
    public void sortLeaderboardByHighestStrength () {
        ArrayList<Child> childrenList = new ArrayList<>();
        User user = User.getInstance();
        String id = user.getDocumentSnapshot().get("ParentUID").toString();
        ChildManager.injectToList(id, childrenList, e -> {
            displayLeaderboard(
                    new ArrayList<>(
                            childrenList
                                    .stream()
                                    .sorted((child1, child2) -> child2.getStrength() - child1.getStrength())
                                    .toList()
                    ),
                    "Strength"
            );
        });
    }

    @SuppressLint("NewApi")
    public void sortLeaderboardByHighestIntelligence () {
        ArrayList<Child> childrenList = new ArrayList<>();
        User user = User.getInstance();
        String id = user.getDocumentSnapshot().get("ParentUID").toString();
        ChildManager.injectToList(id, childrenList, e -> {
            displayLeaderboard(
                    new ArrayList<>(
                            childrenList
                                    .stream()
                                    .sorted((child1, child2) -> child2.getIntelligence() - child1.getIntelligence())
                                    .toList()
                    ),
                    "Intelligence"
            );
        });
    }

    public void displayLeaderboard(ArrayList<Child> cl, String s) {
        cvlb_scrollContent.removeAllViews();
        for (int i = 0; i < cl.size(); i++) {
            Child c = cl.get(i);
            int data = -999;
            switch (s.toLowerCase()) {
                case "quests": data = c.getQuestCompleted().intValue(); break;
                case "floor": data = c.getFloor().intValue(); break;
                case "strength": data = c.getStrength(); break;
                case "intelligence": data = c.getIntelligence(); break;
            }
            if (i == 0) {
                goldUsername.setText(c.getChildUsername());
                goldAchievement.setText(s + ": " + data);
            } else if (i == 1) {
                silverUsername.setText(c.getChildUsername());
                silverAchievement.setText(s + ": " + data);
            } else if (i == 2) {
                bronzeUsername.setText(c.getChildUsername());
                bronzeAchievement.setText(s + ": " + data);
            } else {
                ChildViewPreview cb = new ChildViewPreview(this, c);
                cvlb_scrollContent.addView(cb);
            }
        }
    }
}