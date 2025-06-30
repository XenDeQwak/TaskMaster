package com.taskmaster.appui.view.uimodule;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.taskmaster.appui.R;
import com.taskmaster.appui.util.NavUtil;
import com.taskmaster.appui.view.child.ChildViewQuest;
import com.taskmaster.appui.view.child.CosmeticShop;
import com.taskmaster.appui.view.child.WeeklyBoss;
import com.taskmaster.appui.view.login.Splash;
import com.taskmaster.appui.view.parent.ParentViewManageChild;
import com.taskmaster.appui.view.parent.ParentViewQuest;

public class DropdownNavMenu extends FrameLayout {

    Button navQueueButton, navAdventurersButton, navLogOutButton;
    Button navQueueButton2, navShopButton, navBossButton, navLogoutButton2;
    boolean isParent;

    // Constructor used when inflating from XML
    public DropdownNavMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void init (boolean isParent) {
        this.isParent = isParent;
        if (isParent) {
            LayoutInflater.from(getContext()).inflate(R.layout.module_nav_menu_parent, this);

            navQueueButton  = findViewById(R.id.navQueueButton2);
            navAdventurersButton = findViewById(R.id.navAdventurersButton);
            navLogOutButton = findViewById(R.id.navLogOutButton);
        } else {
            LayoutInflater.from(getContext()).inflate(R.layout.module_nav_menu_child, this);

            navQueueButton2  = findViewById(R.id.navQueueButton2);
            navShopButton = findViewById(R.id.navShopButton);
            navBossButton = findViewById(R.id.navBossButton);
            navLogoutButton2 = findViewById(R.id.navLogoutButton2);
        }


        LinearLayout container = findViewById(R.id.dropdownLinearView);
        container.getBackground().setAlpha(160);
    }

    public void attachActivity (Activity activity) {
        if (isParent) {
            NavUtil.setNavigation(activity, navQueueButton, ParentViewQuest.class);
            NavUtil.setNavigation(activity, navAdventurersButton, ParentViewManageChild.class);
            navLogOutButton.setOnClickListener(v -> {
                FirebaseAuth.getInstance().signOut();
                NavUtil.instantNavigation(activity, Splash.class);
            });
        } else {
            NavUtil.setNavigation(activity, navQueueButton2, ChildViewQuest.class);
            NavUtil.setNavigation(activity, navShopButton, CosmeticShop.class);
            NavUtil.setNavigation(activity, navBossButton, WeeklyBoss.class);
            navLogoutButton2.setOnClickListener(v -> {
                FirebaseAuth.getInstance().signOut();
                NavUtil.instantNavigation(activity, Splash.class);
            });
        }
    }
}
