package com.taskmaster.appui.view.uimodule;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.taskmaster.appui.R;

public class EditQuestTab extends FrameLayout {

    EditText editQuestName, editQuestHour, editQuestMinute, editQuestSecond, editQuestDescription;
    Button setRewardsButton, editQuestSave, editQuestCancel, editQuestAssignNext, editQuestAssignPrev;
    ImageView editQuestChildAvatar;
    ConstraintLayout editQuestContainer;


    public EditQuestTab(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init () {
        LayoutInflater.from(getContext()).inflate(R.layout.module_edit_quest_tab, this);

        editQuestName = findViewById(R.id.editQuestName);
        editQuestHour = findViewById(R.id.editQuestHour);
        editQuestMinute = findViewById(R.id.editQuestMinute);
        editQuestSecond = findViewById(R.id.editQuestSecond);
        editQuestDescription = findViewById(R.id.editQuestDescription);
        setRewardsButton = findViewById(R.id.setRewardsButton);
        editQuestSave = findViewById(R.id.editQuestSave);
        editQuestCancel = findViewById(R.id.editQuestCancel);
        editQuestAssignNext = findViewById(R.id.editQuestAssignNext);
        editQuestAssignPrev = findViewById(R.id.editQuestAssignPrev);
        editQuestChildAvatar = findViewById(R.id.editQuestChildAvatar);
        editQuestContainer = findViewById(R.id.editQuestContainer);

        editQuestCancel.getBackground().setAlpha(150);
    }
}
