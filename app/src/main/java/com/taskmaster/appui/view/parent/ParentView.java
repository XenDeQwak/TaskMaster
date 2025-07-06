package com.taskmaster.appui.view.parent;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.taskmaster.appui.R;
import com.taskmaster.appui.view.uimodule.TopBar;

public class ParentView extends AppCompatActivity {

    TopBar topBar;

    void initNavigationMenu(Activity activity, Class c) {

        topBar = findViewById(R.id.topBarChild);
        topBar.init(true);
        topBar.setPageTitle(c);
        topBar.getDropdownNavMenu().initNavButtonsParent(activity);
        topBar.getDropdownNavMenu().initLogoutButton(activity);

    }

}
