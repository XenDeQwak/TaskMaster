package com.taskmaster.appui.view.parent;

import android.app.Activity;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.taskmaster.appui.R;
import com.taskmaster.appui.view.uimodule.TopBar;

public class ChildView extends AppCompatActivity {

    TopBar topBar;
    TextView goldAmount;

    void initNavigationMenu(Activity activity, Class c) {

        topBar = findViewById(R.id.topBarParent);
        topBar.setCreateButton(c);
        topBar.attachActivity(activity);

        goldAmount = topBar.getGoldAmount();
        goldAmount.setVisibility(TextView.VISIBLE);

    }

}
