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
import com.taskmaster.appui.view.child.ChildPageLeaderboard;
import com.taskmaster.appui.view.child.ChildPageQuestBoard;
import com.taskmaster.appui.view.child.ChildPageQuestHistory;
import com.taskmaster.appui.view.child.CosmeticShop;
import com.taskmaster.appui.view.child.ProgressionPage;
import com.taskmaster.appui.view.child.ChildPageWeeklyBoss;
import com.taskmaster.appui.view.parent.ParentPageManageChild;
import com.taskmaster.appui.view.parent.ParentPageQuestBoard;
import com.taskmaster.appui.view.parent.ParentPageQuestHistory;

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

        if (view == ParentPageQuestBoard.class) {
            setTitle("Quest Board");
        } else if (view == ParentPageManageChild.class) {
            setTitle("Adventurers");
        } else if (view == ChildPageQuestBoard.class) {
            setTitle("Quest Board");
        } else if (view == CosmeticShop.class) {
            setTitle("Cosmetic Shop");
        } else if (view == ChildPageWeeklyBoss.class) {
            setTitle("Weekly BossAvatar");
        } else if (view == ProgressionPage.class) {
            setTitle("Progression");
        }  else if (view == ChildPageLeaderboard.class) {
            setTitle("Leaderboard");
        } else if (view == ParentPageQuestHistory.class || view == ChildPageQuestHistory.class) {
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
