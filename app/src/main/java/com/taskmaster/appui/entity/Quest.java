package com.taskmaster.appui.entity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.taskmaster.appui.R;
import com.taskmaster.appui.data.QuestData;
import com.taskmaster.appui.manager.entitymanager.QuestManager;
import com.taskmaster.appui.util.DateTimeUtil;
import com.taskmaster.appui.view.uimodule.QuestBox;
import com.taskmaster.appui.view.uimodule.QuestBoxPreview;

import java.time.Duration;

public class Quest {

    private QuestManager questManager;
    private Context context;
    private QuestData questData;
    private QuestBox questBox;
    private QuestBoxPreview questBoxPreview;
    private RemainingTimer remainingTimer;

    public Quest (QuestData questData, Context context, QuestManager questManager) {
        this.questManager = questManager;
        this.questData = questData;
        this.context = context;

        // Create QuestBox
        this.questBox = new QuestBox(context);
        this.questBox.setQuest(this);
        this.questBox.getViewQuestButtonC().setOnClickListener(v -> {
            this.questBox.setVisibility(GONE);
        });
        this.questBox.getViewQuestButtonD().setOnClickListener(v -> {
            this.questBox.setVisibility(GONE);
            this.questBoxPreview.setVisibility(GONE);
            if (getQuestData().getStatus().equalsIgnoreCase("awaiting configuration")) {
                getQuestData().getQuestReference().delete();
            } else {
                getQuestData().setStatus("Deleted");
                getQuestData().uploadData();
            }
        });
        String role = CurrentUser.getInstance().getUserData().getUserSnapshot().getString("role");
        if (role.equalsIgnoreCase("child")) {
            this.questBox.getViewQuestButtonD().setVisibility(GONE);
        }


        // Create QuestBoxPreview
        this.questBoxPreview = new QuestBoxPreview(context);
        this.questBoxPreview.setQuest(this);
        this.questBoxPreview.setOnClickListener(v -> {
            this.questBox.setVisibility(VISIBLE);
            System.out.println("I've been clicked");
        });

        if (getQuestData().getStatus().equalsIgnoreCase("ongoing")) {
            remainingTimer = new RemainingTimer(DateTimeUtil.getDateTimeFromEpochSecond(getQuestData().getEndDate()), "DD:HH:MM:SS");
            remainingTimer.setOnTick(timer -> {
                Duration d = Duration.between(DateTimeUtil.getDateTimeNow(), DateTimeUtil.getDateTimeFromEpochSecond(questData.getEndDate()));
                Activity activity = (Activity) getContext();
                activity.runOnUiThread(() -> {
                    questBox.getViewQuestTimeRemaining().setText(DateTimeUtil.formatDuration(d, "DD:HHH:MMM:SSS"));
                    questBoxPreview.getPreviewQuestTimeRemaining().setText(DateTimeUtil.formatDuration(d, "DD:HHH:MMM:SSS"));
                    if (d.isNegative() || d.isZero()) {
                        timer.setPastDue(true);
                        questBox.getViewQuestTimeRemaining().setText("00:00:00:00");
                        questBoxPreview.getPreviewQuestTimeRemaining().setText("00:00:00:00");
                    }
                });
            });
            remainingTimer.setOnFinish(timer -> {
                questData.setStatus("Awaiting Reason For Failure");
                questData.uploadData();
            });
            DateTimeUtil.addTimer(remainingTimer);
        }
    }

    public void updateQuestBox () {
        this.questBox.setQuest(this);
        this.questBoxPreview.setQuest(this);
    }

    public QuestManager getQuestManager() {
        return questManager;
    }

    public void setQuestManager(QuestManager questManager) {
        this.questManager = questManager;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public QuestData getQuestData() {
        return questData;
    }

    public void setQuestData(QuestData questData) {
        this.questData = questData;
    }

    public QuestBox getQuestBox() {
        return questBox;
    }

    public void setQuestBox(QuestBox questBox) {
        this.questBox = questBox;
    }

    public QuestBoxPreview getQuestBoxPreview() {
        return questBoxPreview;
    }

    public void setQuestBoxPreview(QuestBoxPreview questBoxPreview) {
        this.questBoxPreview = questBoxPreview;
    }


}
