package com.taskmaster.appui.view.parent;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.taskmaster.appui.R;
import com.taskmaster.appui.view.uimodule.TopBar;

public class ParentView extends AppCompatActivity {

    TopBar topBar;

    void initNavigationMenu(Activity activity, Class c) {

        topBar = findViewById(R.id.topBarParent);
        topBar.initPageView(c);
        topBar.attachActivity(activity);

    }

}
