package com.taskmaster.appui.view.uimodule;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.taskmaster.appui.R;
import com.taskmaster.appui.entity.Quest;

public class ViewQuestTab extends FrameLayout {

    Quest q;

    public ViewQuestTab (@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init () {
        LayoutInflater.from(getContext()).inflate(R.layout.module_view_quest_tab, this);

        findViewById(R.id.editQuestCancel).setOnClickListener(e-> {
            this.setVisibility(View.GONE);
        });
    }

    public void setQuest (Quest q) {
        this.q = q;
        int img = (q.getRewardStat().equalsIgnoreCase("strength"))? R.drawable.icon_str : R.drawable.icon_int;
        ((ImageView)findViewById(R.id.editQuestImage)).setImageResource(img);
        ((EditText)findViewById(R.id.editQuestName)).setText(q.getName());
        String msg =
                q.getDescription()
                + "\n Rewards: " + q.getRewardStat()
                + "\n" + q.getRewardExtra();
        ((EditText)findViewById(R.id.editQuestDescription)).setText(msg);
    }

}
