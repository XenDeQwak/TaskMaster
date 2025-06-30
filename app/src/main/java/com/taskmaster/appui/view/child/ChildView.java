package com.taskmaster.appui.view.child;

import android.app.Activity;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.taskmaster.appui.R;
import com.taskmaster.appui.view.uimodule.TopBar;

public class ChildView extends AppCompatActivity {

    TopBar topBar;
    TextView goldAmount;

    void initNavigationMenu(Activity activity, Class c) {

        topBar = findViewById(R.id.topBarChild);
        topBar.init(false);
        topBar.initPageView(c);
        topBar.attachActivity(activity);

        goldAmount = topBar.getGoldAmount();
        if (c == CosmeticShop.class) {
            goldAmount.setVisibility(TextView.VISIBLE);
        } else {
            goldAmount.setVisibility(TextView.GONE);
        }

    }

}
