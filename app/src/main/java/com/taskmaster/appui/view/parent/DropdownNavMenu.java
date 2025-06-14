package com.taskmaster.appui.view.parent;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.taskmaster.appui.R;
import com.taskmaster.appui.util.NavUtil;
import com.taskmaster.appui.view.login.Splash;

public class DropdownNavMenu extends RelativeLayout {

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
        LayoutInflater.from(getContext()).inflate(R.layout.module_dropdown_nav_menu, this);
        // Initialize Navigation Buttons
        NavBarButton = findViewById(R.id.NavBarButton);
        navQueueButton  = findViewById(R.id.navQueueButton);
        navAdventurersButton = findViewById(R.id.navAdventurersButton);
        navLogOutButton = findViewById(R.id.navLogOutButton);

        LinearLayout ll = findViewById(R.id.dropdownLinearView);
        ll.getBackground().setAlpha(160);
    }

    public void attachActivity (Activity activity) {
        NavUtil.setNavigation(activity, navQueueButton, ParentViewQuest.class);
        NavUtil.setNavigation(activity, navAdventurersButton, ParentViewManageChild.class);
        navLogOutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            NavUtil.instantNavigation(activity, Splash.class);
        });

        // Dropdown menu logic
        NavBarButton.setOnClickListener(v -> {
            System.out.println("HELLO WORLD");
            FrameLayout navMenu = findViewById(R.id.dropdownContainer);
            if (navMenu.getVisibility() == GONE) {
                //System.out.println("Now you see me");
                navMenu.bringToFront();
                navMenu.setVisibility(VISIBLE);
            } else {
                //System.out.println("Now you don't");
                navMenu.setVisibility(GONE);
            }
        });
    }
}
