package com.taskmaster.appui.view.uimodule;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.taskmaster.appui.R;

public class LeaderboardSortMenu extends FrameLayout {

    ConstraintLayout sortMenuContainer;

    Button sortMenuQuestCompleted, sortMenuFloorReached, sortMenuHighestStrength, sortMenuHighestIntelligence;

    public LeaderboardSortMenu(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init () {
        LayoutInflater.from(getContext()).inflate(R.layout.module_leaderboard_sort_menu, this);

        sortMenuContainer = findViewById(R.id.sortMenuContainer);

        sortMenuQuestCompleted = findViewById(R.id.sortMenuQuestCompleted);
        sortMenuFloorReached = findViewById(R.id.sortMenuFloorReached);
        sortMenuHighestStrength = findViewById(R.id.sortMenuHighestStrength);
        sortMenuHighestIntelligence = findViewById(R.id.sortMenuHighestIntelligence);

        sortMenuContainer.getBackground().setAlpha(150);
    }

    public Button getSortMenuQuestCompleted() {
        return sortMenuQuestCompleted;
    }

    public Button getSortMenuFloorReached() {
        return sortMenuFloorReached;
    }

    public Button getSortMenuHighestStrength() {
        return sortMenuHighestStrength;
    }

    public Button getSortMenuHighestIntelligence() {
        return sortMenuHighestIntelligence;
    }
}
