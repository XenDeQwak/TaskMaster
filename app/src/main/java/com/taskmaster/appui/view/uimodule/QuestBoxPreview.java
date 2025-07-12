package com.taskmaster.appui.view.uimodule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.taskmaster.appui.R;
import com.taskmaster.appui.entity.Child;
import com.taskmaster.appui.entity.Quest;
import com.taskmaster.appui.entity.RemainingTimer;
import com.taskmaster.appui.util.DateTimeUtil;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class QuestBoxPreview extends FrameLayout {

    Quest q;

    ImageView previewQuestAvatar;
    TextView previewQuestName, previewQuestDeadline, previewQuestTimeRemaining, previewQuestAdventurer;

    public QuestBoxPreview(@NonNull Context context) {
        super(context);
        init();
    }

    private void init () {
        LayoutInflater.from(getContext()).inflate(R.layout.module_quest_view_preview, this);

        previewQuestAvatar = findViewById(R.id.previewQuestAvatar);
        previewQuestName = findViewById(R.id.previewQuestName);
        previewQuestDeadline = findViewById(R.id.previewQuestDeadline);
        previewQuestTimeRemaining = findViewById(R.id.previewQuestTimeRemaining);
        previewQuestAdventurer = findViewById(R.id.previewQuestAdventurer);

    }

    @SuppressLint("SetTextI18n")
    public void setQuest (Quest q) {
        this.q = q;

        previewQuestName.setText(q.getQuestData().getName());

        ZonedDateTime deadline = DateTimeUtil.getDateTimeFromEpochSecond(q.getQuestData().getEndDate());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mma");
        String dueDate = deadline.format(formatter);
        previewQuestDeadline.setText("Due: " + dueDate);

        String status = q.getQuestData().getStatus();
        if (status.equalsIgnoreCase("ongoing")) {
            RemainingTimer rTimer = new RemainingTimer(previewQuestTimeRemaining, deadline, q, this);
            DateTimeUtil.addTimer(rTimer);
        } else {
            previewQuestTimeRemaining.setText(status.toUpperCase());
        }

        Boolean rewardStat = q.getQuestData().getRewardStat().equalsIgnoreCase("strength");
        Boolean questStatus = q.getQuestData().getStatus().equalsIgnoreCase("awaiting verification");
        int icon = rewardStat?
                questStatus? R.drawable.icon_str_pending : R.drawable.icon_str  :
                questStatus? R.drawable.icon_int_pending : R.drawable.icon_int
                ;
        previewQuestAvatar.setImageResource(icon);

        if (q.getQuestData().getAdventurerReference() != null) {
            q.getQuestData().getAdventurerReference().get()
                    .addOnCompleteListener(task -> {
                        Child c = task.getResult().toObject(Child.class);
                        previewQuestAdventurer.setText("Assigned to: " + c.getChildUsername());
                    });
        } else {
            previewQuestAdventurer.setText("Assigned to: None");
        }

    }


}
