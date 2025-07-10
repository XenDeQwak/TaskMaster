package com.taskmaster.appui.view.parent;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.taskmaster.appui.R;
import com.taskmaster.appui.view.child.CosmeticShop;
import com.taskmaster.appui.view.uimodule.TopBar;

public class ParentView extends AppCompatActivity {

    TopBar topBar;

    void initNavigationMenu(Activity activity, Class c) {

        topBar = findViewById(R.id.glb_topbar);
        topBar.init(true);
        topBar.setPageTitle(c);
        topBar.getDropdownNavMenu().initNavButtonsParent(activity);
        topBar.getDropdownNavMenu().initLogoutButton(activity);

        topBar.getCreateObjectButton().setVisibility(VISIBLE);
        if (c == ParentViewQuest.class) {
            topBar.getCreateObjectButton().setImageResource(R.drawable.add_quest);
        } else if (c == ParentViewManageChild.class) {
            topBar.getCreateObjectButton().setImageResource(R.drawable.add_adventurer);
        } else if (c == ParentViewQuestHistory.class) {
            topBar.getCreateObjectButton().setVisibility(GONE);
        }

    }

}
