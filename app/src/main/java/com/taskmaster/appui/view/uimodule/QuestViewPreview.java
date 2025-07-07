package com.taskmaster.appui.view.uimodule;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.taskmaster.appui.R;
import com.taskmaster.appui.entity.Quest;
import com.taskmaster.appui.entity.RemainingTimer;
import com.taskmaster.appui.util.DateTimeUtil;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class QuestViewPreview extends FrameLayout {

    Quest q;
    QuestView qv;

    ConstraintLayout previewQuestContainer;
    ImageView previewQuestAvatar;
    TextView previewQuestName, previewQuestDeadline, previewQuestTimeRemaining, previewQuestAdventurer;

    public QuestViewPreview(@NonNull Context context) {
        super(context);
        init();
    }

    private void init () {
        LayoutInflater.from(getContext()).inflate(R.layout.module_quest_view_preview, this);

        previewQuestContainer = findViewById(R.id.previewQuestContainer);
        previewQuestAvatar = findViewById(R.id.previewQuestAvatar);
        previewQuestName = findViewById(R.id.previewQuestName);
        previewQuestDeadline = findViewById(R.id.previewQuestDeadline);
        previewQuestTimeRemaining = findViewById(R.id.previewQuestTimeRemaining);
        previewQuestAdventurer = findViewById(R.id.previewQuestAdventurer);

    }

    public void setQuest (Quest q, Boolean isParent) {
        this.q = q;

        this.setOnClickListener(v -> {
            qv = new QuestView(getContext(), isParent);
            qv.setQuest(q, this);
            if (q.getStatus().equalsIgnoreCase("ongoing")) {
                qv.setTimer(DateTimeUtil.getDateTimeFromEpochSecond(q.getEndDate()));
            }

            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            params.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
            params.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
            params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
            params.setMargins(16,16,16,16);
            qv.setLayoutParams(params);
            qv.setClickable(true);

            ViewGroup parent = (ViewGroup) this.getParent().getParent().getParent(); // Don't ask okay
            parent.addView(qv);
        });

        previewQuestContainer.getBackground().setAlpha(150);

        String rewardStat = q.getRewardStat();
        if (rewardStat.equalsIgnoreCase("strength")) {
            previewQuestAvatar.setImageResource(R.drawable.icon_str);
        } else if (rewardStat.equalsIgnoreCase("intelligence")) {
            previewQuestAvatar.setImageResource(R.drawable.icon_int);
        } else {
            previewQuestAvatar.setImageResource(R.drawable.coin_sprite);
        }

        previewQuestName.setText(q.getName());

        Number endDate = q.getEndDate();
        ZonedDateTime deadline = DateTimeUtil.getDateTimeFromEpochSecond(endDate.longValue());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mma");
        String dueDate = deadline.format(formatter);
        previewQuestDeadline.setText("Due: " + dueDate);

        q.getAssignedReference().get().addOnCompleteListener(task -> {
            if (!isParent) {
                previewQuestAdventurer.setVisibility(GONE);
            } else if (task.isSuccessful() && task.getResult().get("Username") != null) {
                previewQuestAdventurer.setText("Assigned: " + task.getResult().get("Username").toString());
            } else {
                previewQuestAdventurer.setText("Assigned: None");
            }
        });

        if (!q.getStatus().equalsIgnoreCase("failed")) {
            setTimer(deadline);
        }
    }

    public void setTimer (ZonedDateTime deadline) {
        RemainingTimer rTimer = new RemainingTimer(previewQuestTimeRemaining, deadline, q, this);
        DateTimeUtil.addTimer(rTimer);
    }

}
