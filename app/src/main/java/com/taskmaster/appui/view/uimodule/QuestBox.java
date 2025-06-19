package com.taskmaster.appui.view.uimodule;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.taskmaster.appui.R;
import com.taskmaster.appui.entity.Quest;

public class QuestBox extends FrameLayout {

    ConstraintLayout questContainer;
    ImageView questImage;
    TextView questNameText;
    Quest q;

    public QuestBox(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QuestBox(@NonNull Context context) {
        super(context);
        init();
    }

    public QuestBox(@NonNull Context context, Quest q) {
        super(context);
        this.q = q;
        init();
    }

    private void init () {
        LayoutInflater.from(getContext()).inflate(R.layout.module_quest, this);

        questContainer = findViewById(R.id.questContainer);
        questImage = findViewById(R.id.questImage);
        questNameText = findViewById(R.id.questNameText);

        //questImage.setImageResource(R.drawable.blank_icon);
        questNameText.setText(q.getName().equals("")? "Configure Quest.." : q.getName());
    }

    public ConstraintLayout getQuestContainer () {
        return questContainer;
    }
}
