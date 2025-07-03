package com.taskmaster.appui.view.uimodule;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.taskmaster.appui.R;
import com.taskmaster.appui.entity.Quest;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ViewQuest extends FrameLayout {
    Quest q;
    Boolean isParent;

    public ViewQuest(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ViewQuest(@NonNull Context context) {
        super(context);
        init();
    }

    public ViewQuest(@NonNull Context context, Quest q, boolean isParent) {
        super(context);
        setQuest(q);
        this.isParent = isParent;
        init();
    }

    private void setQuest (Quest q) {
        this.q = q;
    }

    ConstraintLayout viewQuestContainer;

    ImageView viewQuestAvatar;

    TextView viewQuestName,
            viewQuestDifficulty,
            viewQuestAdventurer,
            viewQuestDescription,
            viewQuestRewardStat,
            viewQuestRewardExtra,
            viewQuestDeadline,
            viewQuestTimeRemaining,
            viewQuestStatus;

    Button viewQuestButtonA, viewQuestButtonB, viewQuestButtonC;

    private void init () {
        LayoutInflater.from(getContext()).inflate(R.layout.module_quest_view, this);

        viewQuestContainer = findViewById(R.id.viewQuestContainer);

        viewQuestAvatar = findViewById(R.id.viewQuestAvatar);

        viewQuestName = findViewById(R.id.viewQuestName);
        viewQuestDifficulty = findViewById(R.id.viewQuestDifficulty);
        viewQuestAdventurer = findViewById(R.id.viewQuestAdventurer);
        viewQuestDescription = findViewById(R.id.viewQuestDescription);
        viewQuestRewardStat = findViewById(R.id.viewQuestRewardStat);
        viewQuestRewardExtra = findViewById(R.id.viewQuestRewardExtra);
        viewQuestDeadline = findViewById(R.id.viewQuestDeadline);
        viewQuestTimeRemaining = findViewById(R.id.viewQuestTimeRemaining);
        viewQuestStatus = findViewById(R.id.viewQuestStatus);

        viewQuestButtonA = findViewById(R.id.viewQuestButtonA);
        viewQuestButtonB = findViewById(R.id.viewQuestButtonB);
        viewQuestButtonC = findViewById(R.id.viewQuestButtonC);

        viewQuestContainer.getBackground().setAlpha(150);

        q.getAssignedReference().get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().get("Username") != null) {
                viewQuestAdventurer.setText(task.getResult().get("Username").toString());
            } else {
                viewQuestAdventurer.setText("None");
            }
        });

        Number endDate = q.getEndDate();
        Instant instant = Instant.ofEpochSecond(endDate.longValue());
        ZoneId zoneId = ZoneId.systemDefault();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String dueDate = instant.atZone(zoneId).format(formatter);

        Number diff = q.getDifficulty();

        viewQuestName.setText(q.getName());
        viewQuestDifficulty.setText("Difficulty: " + "★".repeat(diff.intValue()));
        viewQuestAdventurer.setText("Assigned: .....");
        viewQuestDescription.setText(q.getDescription());
        viewQuestRewardStat.setText("Reward: " + q.getRewardStat());
        viewQuestRewardExtra.setText("More Rewards:\n" + q.getRewardExtra());
        viewQuestDeadline.setText("Due: " + dueDate);
        viewQuestTimeRemaining.setText("00:00:00");
        viewQuestStatus.setText(q.getStatus());

        String rewardStat = q.getRewardStat();
        if (rewardStat.equalsIgnoreCase("strength")) {
            viewQuestAvatar.setImageResource(R.drawable.icon_str);
        } else if (rewardStat.equalsIgnoreCase("intelligence")) {
            viewQuestAvatar.setImageResource(R.drawable.icon_int);
        } else {
            viewQuestAvatar.setImageResource(R.drawable.coin_sprite);
        }

        if (isParent) {
            viewQuestButtonA.setText("Complete");
            viewQuestButtonB.setText("Delete");
            viewQuestButtonC.setText("Edit");
        } else {
            viewQuestButtonA.setText("Submit For Verification");
            viewQuestButtonB.setVisibility(GONE);
            viewQuestButtonC.setVisibility(GONE);
        }

    }

    public Button getViewQuestButtonA () {
        return viewQuestButtonA;
    }

    public Button getViewQuestButtonB () {
        return viewQuestButtonB;
    }

    public Button getViewQuestButtonC () {
        return viewQuestButtonC;
    }
}
