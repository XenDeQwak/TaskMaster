package com.taskmaster.appui.view.parent;

import android.app.Activity;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.taskmaster.appui.R;
import com.taskmaster.appui.view.uimodule.DropdownNavMenu;
import com.taskmaster.appui.view.uimodule.TopBar;

public class ParentView extends AppCompatActivity {

    TopBar topBar;

    void initNavigationMenu(Activity activity, Class c) {

        topBar = findViewById(R.id.topBarParent);
        topBar.setCreateButton(c);
        topBar.attachActivity(activity);

    }

}
