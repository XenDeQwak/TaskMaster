package com.taskmaster.appui.view.child;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.taskmaster.appui.R;
import com.taskmaster.appui.entity.Child;
import com.taskmaster.appui.entity.User;
import com.taskmaster.appui.manager.entitymanager.ChildManager;
import com.taskmaster.appui.manager.firebasemanager.AuthManager;
import com.taskmaster.appui.view.uimodule.ChildViewPreview;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Comparator;

public class ChildViewLeaderboard extends ChildView {

    ChildManager childManager;

    ScrollView cvlb_scrollView;
    LinearLayout cvlb_scrollContent;

    TextView goldUsername, silverUsername, bronzeUsername, goldAchievement, silverAchievement, bronzeAchievement;

    @SuppressLint("NewApi")
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

        ArrayList<Child> childrenList = new ArrayList<Child>();
        User user = User.getInstance();
        String id = user.getDocumentSnapshot().get("ParentUID").toString();
        ChildManager.injectToList(id, childrenList, e -> {
             ArrayList<Child> cl = new ArrayList<>(
                     childrenList
                             .stream()
                             .sorted((child1, child2) -> child2.getQuestCompleted().intValue() - child1.getQuestCompleted().intValue())
                             .toList()
             );

                for (int i = 0; i < cl.size(); i++) {
                    Child c = cl.get(i);
                    if (i == 0) {
                        goldUsername.setText(c.getChildUsername());
                        goldAchievement.setText("Quests: " + c.getQuestCompleted().intValue());
                    } else if (i == 1) {
                        silverUsername.setText(c.getChildUsername());
                        silverAchievement.setText("Quests: " + c.getQuestCompleted().intValue());
                    } else if (i == 2) {
                        bronzeUsername.setText(c.getChildUsername());
                        bronzeAchievement.setText("Quests: " + c.getQuestCompleted().intValue());
                    } else {
                        ChildViewPreview cb = new ChildViewPreview(this, c);
                        cvlb_scrollContent.addView(cb);
                    }
                }
        });
    }
}