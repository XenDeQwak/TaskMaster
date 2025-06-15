package com.taskmaster.appui.view.uimodule;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.taskmaster.appui.R;
import com.taskmaster.appui.util.NavUtil;
import com.taskmaster.appui.view.login.Splash;
import com.taskmaster.appui.view.parent.ParentViewManageChild;
import com.taskmaster.appui.view.parent.ParentViewQuest;

public class DropdownNavMenu extends FrameLayout {

    Button navQueueButton, navAdventurersButton, navLogOutButton;


    // Constructor used when inflating from XML
    public DropdownNavMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init () {
        LayoutInflater.from(getContext()).inflate(R.layout.module_dropdown_nav_menu, this);
        // Initialize Navigation Buttons

        navQueueButton  = findViewById(R.id.navQueueButton);
        navAdventurersButton = findViewById(R.id.navAdventurersButton);
        navLogOutButton = findViewById(R.id.navLogOutButton);

        LinearLayout container = findViewById(R.id.dropdownLinearView);
        container.getBackground().setAlpha(160);
    }

    public void attachActivity (Activity activity) {
        NavUtil.setNavigation(activity, navQueueButton, ParentViewQuest.class);
        NavUtil.setNavigation(activity, navAdventurersButton, ParentViewManageChild.class);
        navLogOutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            NavUtil.instantNavigation(activity, Splash.class);
        });


    }
}
