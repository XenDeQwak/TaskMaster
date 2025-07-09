package com.taskmaster.appui.view.uimodule;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.taskmaster.appui.R;
import com.taskmaster.appui.util.NavUtil;
import com.taskmaster.appui.view.child.ProgressionPage;

public class ChildStatsTab extends FrameLayout {

    ConstraintLayout statsContainer;
    ImageView childStatsAvatarImage;
    AppCompatButton childStatsName, childStatsFloor, childStatsMoreStatsButton;


    public ChildStatsTab(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init () {
        LayoutInflater.from(getContext()).inflate(R.layout.module_child_stats_tab, this);

        childStatsAvatarImage = findViewById(R.id.childStatsAvatarImage);
        childStatsName = findViewById(R.id.childStatsName);
        childStatsFloor = findViewById(R.id.childStatsFloor);
        childStatsMoreStatsButton = findViewById(R.id.childStatsMoreStatsButton);

        statsContainer = findViewById(R.id.cvlb_container);
        statsContainer.getBackground().setAlpha(150);

    }

    public void setProgressionNav (Activity activity) {
        childStatsMoreStatsButton.setOnClickListener(e -> {
            NavUtil.instantNavigation(activity, ProgressionPage.class);
        });
    }

    public AppCompatButton getChildStatsName() {
        return childStatsName;
    }

    public AppCompatButton getChildStatsFloor() {
        return childStatsFloor;
    }

    public ImageView getChildStatsAvatarImage() {
        return childStatsAvatarImage;
    }
}
