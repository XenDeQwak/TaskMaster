package com.taskmaster.appui.view.uimodule;

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

        statsContainer = findViewById(R.id.statContainer);
        statsContainer.getBackground().setAlpha(150);

    }



}
