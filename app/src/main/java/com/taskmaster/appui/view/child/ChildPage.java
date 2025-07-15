package com.taskmaster.appui.view.child;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Activity;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.taskmaster.appui.R;
import com.taskmaster.appui.view.uimodule.TopBar;

public class ChildPage extends AppCompatActivity {

    TopBar topBar;
    TextView goldAmount;

    void initNavigationMenu(Activity activity, Class c) {

        topBar = findViewById(R.id.glb_topbar);
        topBar.init(false);
        topBar.setPageTitle(c);
        topBar.getDropdownNavMenu().initNavButtonsChild(activity);
        topBar.getDropdownNavMenu().initLogoutButton(activity);

        goldAmount = topBar.getGoldAmount();
        goldAmount.setVisibility(GONE);
        topBar.getCreateObjectButton().setVisibility(GONE);
        if (c == CosmeticShop.class) {
            goldAmount.setVisibility(VISIBLE);
            topBar.getCreateObjectButton().setVisibility(VISIBLE);
        } else if (c == ChildPageLeaderboard.class) {
            topBar.getCreateObjectButton().setVisibility(VISIBLE);
            topBar.getCreateObjectButton().setImageResource(R.drawable.sortby_leaderboard);
        }

    }

}
