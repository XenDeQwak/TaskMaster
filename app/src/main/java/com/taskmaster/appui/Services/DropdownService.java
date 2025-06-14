package com.taskmaster.appui.Services;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.Group;

import com.google.firebase.auth.FirebaseAuth;
import com.taskmaster.appui.Page.Main.QuestManagement;
import com.taskmaster.appui.Page.Login.Splash;
import com.taskmaster.appui.Page.Main.User;
import com.taskmaster.appui.Page.ManageChild;
import com.taskmaster.appui.Page.WeeklyBoss;
import com.taskmaster.appui.R;

public class DropdownService {

    public static void dropdownSetup(Activity activity, View root){
        ImageView dropdownNavButton = activity.findViewById(R.id.NavBarButton);
        Group dropDownGroup = activity.findViewById(R.id.dropdownGroup);
        AppCompatButton navQuestPage = activity.findViewById(R.id.navQuestPage);
        AppCompatButton navManageAdv = activity.findViewById(R.id.navManageAdv);
        AppCompatButton navLogOut = activity.findViewById(R.id.navLogOut);
        NavUtil.setNavigation(activity, navLogOut, Splash.class);
        NavUtil.setNavigation(activity,navQuestPage, QuestManagement.class);
        // exclude elems within dropdown
        View[] dropDownElements = {activity.findViewById(R.id.navFrame)};

        //NAV
        navLogOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            NavUtil.instantNavigation(activity, Splash.class);
        });
        NavUtil.setNavigation(activity,navQuestPage, QuestManagement.class);

        dropDownGroup.setVisibility(View.GONE);

        User user = User.getInstance();

        if ("parent".equals(user.getDocumentSnapshot().get("Role"))) {
            NavUtil.setNavigation(activity, navManageAdv, ManageChild.class);
            navManageAdv.setText("Manage Adventurers");
        } else {
            NavUtil.setNavigation(activity, navManageAdv, WeeklyBoss.class);
            navManageAdv.setText("Weekly Boss");
        }

        //Btn Fx
        dropdownNavButton.setOnClickListener(v -> {
            if (dropDownGroup.getVisibility() == View.VISIBLE) {
                dropDownGroup.setVisibility(View.GONE);
            } else {
                dropDownGroup.setVisibility(View.VISIBLE);
            }
        });

        // exit dropdown & popup group
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    boolean isInsideDropdown = false;
                    for (View element : dropDownElements) {
                        if (isViewTouched(element, event)) {
                            isInsideDropdown = true;
                            break;
                        }
                    }
                    if (!isInsideDropdown) {
                        dropDownGroup.setVisibility(View.GONE);
                    }
                }
                return false;
            }
        });

    }
    private static boolean isViewTouched(View view, MotionEvent event) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];

        return event.getRawX() >= x && event.getRawX() <= x + view.getWidth()
                && event.getRawY() >= y && event.getRawY() <= y + view.getHeight();
    }
}
