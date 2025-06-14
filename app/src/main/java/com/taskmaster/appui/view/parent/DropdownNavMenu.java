package com.taskmaster.appui.view.parent;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.taskmaster.appui.R;
import com.taskmaster.appui.util.NavUtil;
import com.taskmaster.appui.view.login.Splash;

public class DropdownNavMenu extends FrameLayout {

    ImageView NavBarButton;
    Button navQueueButton, navAdventurersButton, navLogOutButton;


    // Constructor used when inflating from XML
    public DropdownNavMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public DropdownNavMenu(Context context) {
        super(context);
        init();
    }

    private void init () {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.module_dropdown_nav_menu, this);
        // Initialize Navigation Buttons
        navQueueButton  = v.findViewById(R.id.navQueueButton);
        navAdventurersButton  = v.findViewById(R.id.navAdventurersButton);
        navLogOutButton = v.findViewById(R.id.navLogOutButton);
    }

    public void attachActivity (Activity activity) {
        NavUtil.setNavigation(activity, navQueueButton, ParentViewQuest.class);
        NavUtil.setNavigation(activity, navAdventurersButton, ManageChild.class);
        navLogOutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            NavUtil.instantNavigation(activity, Splash.class);
        });
    }
}
