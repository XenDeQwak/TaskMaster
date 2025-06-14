package com.taskmaster.appui.view.uimodule;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.taskmaster.appui.R;
import com.taskmaster.appui.view.parent.ParentView;
import com.taskmaster.appui.view.parent.ParentViewManageChild;
import com.taskmaster.appui.view.parent.ParentViewQuest;

public class TopBar extends FrameLayout {

    ImageView navBarButton;
    DropdownNavMenu dropdownNavMenu;
    TextView pageTitleTextView;
    ImageView createObjectButton;

    // Constructor used when inflating from XML
    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TopBar(Context context) {
        super(context);
        init();
    }

    private void init () {
        LayoutInflater.from(getContext()).inflate(R.layout.module_top_bar, this);
        navBarButton = findViewById(R.id.navBarButton);
        dropdownNavMenu = findViewById(R.id.dropdownNavMenu);
        pageTitleTextView = findViewById(R.id.pageTitleTextView);
        createObjectButton = findViewById(R.id.createObjectButton);

        ConstraintLayout container = findViewById(R.id.topBarContainer);
        container.getBackground().setAlpha(100);

        // Dropdown menu logic
        navBarButton.setOnClickListener(v -> {
            System.out.println("HELLO WORLD");
            FrameLayout navMenu = dropdownNavMenu.findViewById(R.id.dropdownContainer);
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

    public void attachActivity (Activity activity) {
        dropdownNavMenu.attachActivity(activity);
    }

    public void setTitle (String s) {
        pageTitleTextView.setText(s);
    }

    public <T extends ParentView> void setCreateButton (Class<T> parentView) {
        if (parentView == ParentViewQuest.class) {
            createObjectButton.setImageResource(R.drawable.add_quest);
            setTitle("Quest Board");
        } else if (parentView == ParentViewManageChild.class) {
            createObjectButton.setImageResource(R.drawable.add_adventurer);
            setTitle("Adventurers");
        }
    }

    public ImageView getCreateObjectButton () {
        return createObjectButton;
    }

}
