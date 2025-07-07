package com.taskmaster.appui.entity;

import static android.view.View.GONE;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taskmaster.appui.manager.entitymanager.QuestManager;
import com.taskmaster.appui.util.DateTimeUtil;

import java.time.Duration;
import java.time.ZonedDateTime;

public class RemainingTimer {

    View container;
    Quest q;
    long hour, minute, second;
    TextView timeTextView;
    ZonedDateTime due;

    Boolean pastDue = false;

    public RemainingTimer(TextView timeTextView, ZonedDateTime due, Quest q, View container) {
        this.timeTextView = timeTextView;
        this.due = due;
        this.q = q;
        this.container = container;
    }

    public String getRemainingTime () {
        ZonedDateTime now = DateTimeUtil.getDateTimeNow();
        Duration remaining = Duration.between(now, due);
        long rem = remaining.toSeconds();

        if (rem < 0) {
            rem = 0;
            pastDue = true;
            QuestManager.failQuest(q);
            ViewGroup parent = (ViewGroup) container.getParent();
            ((Activity)parent.getContext()).runOnUiThread(() -> {
                container.setVisibility(GONE);
                parent.removeView(container);
            });
        }

        this.second = rem % 60;
        this.minute = (rem/60)%60;
        this.hour = rem/3600;

        return hour + ":" + ((minute>9)?minute:"0"+minute) + ":" + ((second>9)?second:"0"+second);
    }

    public void setText (String s) {
        Activity act = (Activity) timeTextView.getContext();
        act.runOnUiThread(() -> {
            timeTextView.setText(s);
        });
    }


}
