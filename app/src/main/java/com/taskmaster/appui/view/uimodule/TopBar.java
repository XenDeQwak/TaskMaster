package com.taskmaster.appui.view.uimodule;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.taskmaster.appui.R;
import com.taskmaster.appui.view.child.ChildViewQuest;
import com.taskmaster.appui.view.child.ChildViewQuestHistory;
import com.taskmaster.appui.view.child.CosmeticShop;
import com.taskmaster.appui.view.child.ProgressionPage;
import com.taskmaster.appui.view.child.WeeklyBoss;
import com.taskmaster.appui.view.parent.ParentViewManageChild;
import com.taskmaster.appui.view.parent.ParentViewQuest;
import com.taskmaster.appui.view.parent.ParentViewQuestHistory;

public class TopBar extends FrameLayout {

    ImageView navBarButton;
    NavigationMenu navigationMenu;
    TextView pageTitleTextView;
    ImageView createObjectButton;
    TextView goldAmount;

    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init (boolean isParent) {
        LayoutInflater.from(getContext()).inflate(R.layout.module_top_bar, this);
        navBarButton = findViewById(R.id.navBarButton);
        navigationMenu = findViewById(R.id.dropdownNavMenu);
        pageTitleTextView = findViewById(R.id.pageTitleTextView);
        createObjectButton = findViewById(R.id.createObjectButton);
        goldAmount = findViewById(R.id.goldAmount);

        // Dropdown menu logic
        navBarButton.setOnClickListener(v -> {
            navigationMenu.bringToFront();
            if (navigationMenu.getVisibility() == GONE) {
                navigationMenu.setVisibility(VISIBLE);
            } else {
                navigationMenu.setVisibility(GONE);
            }
        });

        createObjectButton.setImageDrawable(null);
        goldAmount.setVisibility(View.GONE);

    }

    public void setTitle (String s) {
        pageTitleTextView.setText(s);
    }

    public <T extends AppCompatActivity> void setPageTitle(Class<T> view) {

        if (view == ParentViewQuest.class) {
            createObjectButton.setImageResource(R.drawable.add_quest);
            setTitle("Quest Board");
        } else if (view == ParentViewManageChild.class) {
            createObjectButton.setImageResource(R.drawable.add_adventurer);
            setTitle("Adventurers");
        } else if (view == ChildViewQuest.class) {
            setTitle("Quest Board");
            createObjectButton.setVisibility(View.GONE);
        } else if (view == CosmeticShop.class) {
            setTitle("Cosmetic Shop");
        } else if (view == WeeklyBoss.class) {
            setTitle("Weekly Boss");
            createObjectButton.setVisibility(View.GONE);
        } else if (view == ProgressionPage.class) {
            setTitle("Progression");
        } else if (view == ParentViewQuestHistory.class || view == ChildViewQuestHistory.class) {
            setTitle("Quest History");
        }

    }

    public TextView getGoldAmount () {
        return goldAmount;
    }

    public ImageView getCreateObjectButton () {
        return createObjectButton;
    }

    public NavigationMenu getDropdownNavMenu () {
        return navigationMenu;
    }

}
