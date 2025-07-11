package com.taskmaster.appui.view.uimodule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.taskmaster.appui.R;
import com.taskmaster.appui.entity.Quest;
import com.taskmaster.appui.entity.RemainingTimer;
import com.taskmaster.appui.util.DateTimeUtil;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class QuestBoxPreview extends FrameLayout {

    Quest q;
    QuestBox qv;

    ConstraintLayout previewQuestContainer;
    ImageView previewQuestAvatar;
    TextView previewQuestName, previewQuestDeadline, previewQuestTimeRemaining, previewQuestAdventurer;

    public QuestBoxPreview(@NonNull Context context) {
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

    public void setQuest (Quest q, String state) {
        this.q = q;

        this.setOnClickListener(v -> {

            ViewGroup parent = (ViewGroup) this.getParent().getParent().getParent(); // Don't ask okay

            Overlay ov = new Overlay(getContext());
            ConstraintLayout.LayoutParams ovParams = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.MATCH_PARENT
            );
            ovParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            ovParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
            ovParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
            ovParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
            //ov.setFitsSystemWindows(true);
            ov.setLayoutParams(ovParams);
            ov.setClickable(true);
            parent.addView(ov);


            qv = new QuestBox(getContext(), state, ov);
            qv.setQuest(q, this);


            ConstraintLayout.LayoutParams qvParams = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            qvParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            qvParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
            qvParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
            qvParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
            qvParams.setMargins(16,16,16,16);
            qv.setLayoutParams(qvParams);
            qv.setClickable(true);

            parent.addView(qv);
        });

        //previewQuestContainer.getBackground().setAlpha(150);

        String rewardStat = q.getRewardStat();
        if (rewardStat.equalsIgnoreCase("strength")) {
            previewQuestAvatar.setImageResource(R.drawable.icon_str);
            if (q.getStatus().equalsIgnoreCase("awaiting verification")) {
                previewQuestAvatar.setImageResource(R.drawable.icon_str_pending);
            }
        } else if (rewardStat.equalsIgnoreCase("intelligence")) {
            previewQuestAvatar.setImageResource(R.drawable.icon_int);
            if (q.getStatus().equalsIgnoreCase("awaiting verification")) {
                previewQuestAvatar.setImageResource(R.drawable.icon_int_pending);
            }
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
            if (state.equalsIgnoreCase("child")) {
                previewQuestAdventurer.setVisibility(GONE);
            } else if (task.isSuccessful() && task.getResult().get("Username") != null) {
                previewQuestAdventurer.setText("Assigned: " + task.getResult().get("Username").toString());
            } else {
                previewQuestAdventurer.setText("Assigned: None");
            }
        });

        if (q.getStatus().equalsIgnoreCase("ongoing")) {
            setTimer(DateTimeUtil.getDateTimeFromEpochSecond(q.getEndDate()));
        } else {
            Duration d = Duration.between(DateTimeUtil.getDateTimeNow(), DateTimeUtil.getDateTimeFromEpochSecond(q.getEndDate()));
            if (d.isNegative()) {
                previewQuestTimeRemaining.setText(q.getStatus().toUpperCase());
            } else {
                previewQuestTimeRemaining.setText(RemainingTimer.toRemainingDuration(d));
            }
        }
    }

    public void setTimer (ZonedDateTime deadline) {
        RemainingTimer rTimer = new RemainingTimer(previewQuestTimeRemaining, deadline, q, this);
        DateTimeUtil.addTimer(rTimer);
    }

}
