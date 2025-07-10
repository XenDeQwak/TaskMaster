package com.taskmaster.appui.view.uimodule;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.taskmaster.appui.R;
import com.taskmaster.appui.view.child.ChildViewLeaderboard;
import com.taskmaster.appui.view.child.ChildViewQuest;
import com.taskmaster.appui.view.child.ChildViewQuestHistory;
import com.taskmaster.appui.view.child.CosmeticShop;
import com.taskmaster.appui.view.child.ProgressionPage;
import com.taskmaster.appui.view.child.WeeklyBoss;
import com.taskmaster.appui.view.parent.ParentViewManageChild;
import com.taskmaster.appui.view.parent.ParentViewQuest;
import com.taskmaster.appui.view.parent.ParentViewQuestHistory;

public class TopBar extends FrameLayout {

    ImageView navBarButton;
    NavigationMenu dropdownNavMenu;
    LeaderboardSortMenu dropdownSortMenu;
    TextView pageTitleTextView;
    ImageView createObjectButton;
    TextView goldAmount;

    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init (boolean isParent) {
        LayoutInflater.from(getContext()).inflate(R.layout.module_top_bar, this);

        navBarButton = findViewById(R.id.navBarButton);
        dropdownNavMenu = findViewById(R.id.dropdownNavMenu);
        dropdownSortMenu = findViewById(R.id.dropdownSortMenu);
        pageTitleTextView = findViewById(R.id.pageTitleTextView);
        createObjectButton = findViewById(R.id.createObjectButton);
        goldAmount = findViewById(R.id.goldAmount);

        // Dropdown menu logic
        navBarButton.setOnClickListener(v -> {
            dropdownNavMenu.bringToFront();
            if (dropdownNavMenu.getVisibility() == GONE) {
                dropdownNavMenu.setVisibility(VISIBLE);
                dropdownSortMenu.setVisibility(GONE);
            } else {
                dropdownNavMenu.setVisibility(GONE);
            }
        });

        //createObjectButton.setImageDrawable(null);
        goldAmount.setVisibility(View.GONE);

    }

    public void setTitle (String s) {
        pageTitleTextView.setText(s);
    }

    public <T extends AppCompatActivity> void setPageTitle(Class<T> view) {

        if (view == ParentViewQuest.class) {
            setTitle("Quest Board");
        } else if (view == ParentViewManageChild.class) {
            setTitle("Adventurers");
        } else if (view == ChildViewQuest.class) {
            setTitle("Quest Board");
        } else if (view == CosmeticShop.class) {
            setTitle("Cosmetic Shop");
        } else if (view == WeeklyBoss.class) {
            setTitle("Weekly Boss");
        } else if (view == ProgressionPage.class) {
            setTitle("Progression");
        }  else if (view == ChildViewLeaderboard.class) {
            setTitle("Leaderboard");
        } else if (view == ParentViewQuestHistory.class || view == ChildViewQuestHistory.class) {
            setTitle("Quest History");
        }

    }

    public TextView getGoldAmount () {
        return goldAmount;
    }

    public ImageView getCreateObjectButton () {
        return createObjectButton;
    }

    public NavigationMenu getDropdownNavMenu () {
        return dropdownNavMenu;
    }

    public LeaderboardSortMenu getDropdownSortMenu () {
        return dropdownSortMenu;
    }

}
