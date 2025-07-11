package com.taskmaster.appui.view.uimodule;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.taskmaster.appui.R;
import com.taskmaster.appui.manager.firebasemanager.AuthManager;
import com.taskmaster.appui.util.NavUtil;
import com.taskmaster.appui.view.child.ChildPageQuestBoard;
import com.taskmaster.appui.view.child.ChildPageQuestHistory;
import com.taskmaster.appui.view.child.ChildPageLeaderboard;
import com.taskmaster.appui.view.child.CosmeticShop;
import com.taskmaster.appui.view.child.WeeklyBoss;
import com.taskmaster.appui.view.login.Splash;
import com.taskmaster.appui.view.parent.ParentPageManageChild;
import com.taskmaster.appui.view.parent.ParentPageQuestBoard;
import com.taskmaster.appui.view.parent.ParentPageQuestHistory;

public class NavigationMenu extends FrameLayout {

    ConstraintLayout navMenuContainer;

    Button navMenuQuestButton, navMenuAdventurerButton, navMenuShopButton, navMenuBossButton, navMenuLeaderboardButton, navMenuQuestHistoryButton, navMenuLogoutButton;

    public NavigationMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init () {
        LayoutInflater.from(getContext()).inflate(R.layout.module_nav_menu, this);

        navMenuContainer = findViewById(R.id.navMenuContainer);

        navMenuQuestButton = findViewById(R.id.navMenuQuestButton);
        navMenuAdventurerButton = findViewById(R.id.navMenuAdventurerButton);
        navMenuShopButton = findViewById(R.id.navMenuShopButton);
        navMenuBossButton = findViewById(R.id.navMenuBossButton);
        navMenuLeaderboardButton = findViewById(R.id.navMenuLeaderboardButton);
        navMenuQuestHistoryButton = findViewById(R.id.navMenuQuestHistoryButton);
        navMenuLogoutButton = findViewById(R.id.navMenuLogoutButton);

        navMenuContainer.getBackground().setAlpha(150);
    }

    public void initNavButtonsParent (Activity activity) {
        NavUtil.setNavigation(activity, navMenuQuestButton, ParentPageQuestBoard.class);
        NavUtil.setNavigation(activity, navMenuAdventurerButton, ParentPageManageChild.class);
        NavUtil.setNavigation(activity, navMenuQuestHistoryButton, ParentPageQuestHistory.class);

        navMenuShopButton.setVisibility(GONE);
        navMenuBossButton.setVisibility(GONE);
        navMenuLeaderboardButton.setVisibility(GONE);
    }

    public void initNavButtonsChild (Activity activity) {
        NavUtil.setNavigation(activity, navMenuQuestButton, ChildPageQuestBoard.class);
        NavUtil.setNavigation(activity, navMenuShopButton, CosmeticShop.class);
        NavUtil.setNavigation(activity, navMenuBossButton, WeeklyBoss.class);
        NavUtil.setNavigation(activity, navMenuLeaderboardButton, ChildPageLeaderboard.class);
        NavUtil.setNavigation(activity, navMenuQuestHistoryButton, ChildPageQuestHistory.class);

        navMenuAdventurerButton.setVisibility(GONE);
    }

    public void initLogoutButton (Activity activity) {
        navMenuLogoutButton.setOnClickListener(v -> {
            AuthManager.getAuth().signOut();
            NavUtil.instantNavigation(activity, Splash.class);
        });
    }
}
